package dev.glinzac.automation;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.glinzac.automation.entities.DatabaseEntity;
import dev.glinzac.automation.quartz.QuartzJob;
import dev.glinzac.automation.repository.DatabaseRepository;

@Service
public class MainService {
	
	@Autowired
	DatabaseRepository dbRepo;
	

	
	
	
	static ResultSet resultSet = null;
	static List<String> columnNames = null;

	public boolean testMySQLConnection(String ip,int port, String db,String username,String pwd) {
        String databaseURL = "jdbc:mysql://"+ip+":"+port+"/"+db;
        System.out.println(databaseURL);
        String user = username;
        String password = pwd;
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(databaseURL, user, password);
            if (conn != null) {
                System.out.println("Connected to the database");
            }
            return true;
        } catch (ClassNotFoundException ex) {
            System.out.println("Could not find database driver class");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    return true;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return false;
	}
	
	public boolean testOracleConnection(String ip,int port, String sid,String username,String pwd,String ser) {
		String databaseURL;
		if(ser.equals("Service")) {
			databaseURL = "jdbc:oracle:thin:@"+ip+":"+port+"/"+sid;
		}else {
			databaseURL = "jdbc:oracle:thin:@"+ip+":"+port+":"+sid;
		}
         
        System.out.println(databaseURL);
        String user = username;
        String password = pwd;
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(databaseURL, user, password);
            if (conn != null) {
                System.out.println("Connected to the database");
            }
            return true;
        } catch (ClassNotFoundException ex) {
            System.out.println("Could not find database driver class");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    return true;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return false;
	}

	public void saveData(DatabaseEntity db) {
		dbRepo.save(db);
	}

	public List<DatabaseEntity> getDatabases(String dbName) {
		return dbRepo.getDatabase(dbName);
		
	}

	public List<DatabaseEntity> getDatabaseList() {
		return dbRepo.getDatabaseList();
	}
	
	
	public Connection makeMySQLConnection(DatabaseEntity dbEntity) {
		String databaseURL = "jdbc:mysql://"+dbEntity.getDb_hostname()+":"+dbEntity.getDb_port()+"/"+dbEntity.getDb_name();
        System.out.println(databaseURL);
        String user = dbEntity.getDb_user_name();
        String password = dbEntity.getDb_pwd();
        Connection conn = null;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(databaseURL, user, password);
            if (conn != null) {
                System.out.println("Connected to the database");
                return conn;
            }   
        } catch (ClassNotFoundException ex) {
            System.out.println("Could not find database driver class");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        } 
		return conn;
	}
	
	public Connection makeOracleConnection(DatabaseEntity dbEntity) {
		String databaseURL;
		if(dbEntity.getDb_ser().equals("Service")) {
			databaseURL = "jdbc:oracle:thin:@"+dbEntity.getDb_hostname()+":"+dbEntity.getDb_port()+"/"+dbEntity.getDb_sid();
		}else {
			databaseURL = "jdbc:oracle:thin:@"+dbEntity.getDb_hostname()+":"+dbEntity.getDb_port()+":"+dbEntity.getDb_sid();
		}
         
        System.out.println(databaseURL);
        String user = dbEntity.getDb_user_name();
        String password = dbEntity.getDb_pwd();
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(databaseURL, user, password);
            if (conn != null) {
                System.out.println("Connected to the database");
                return conn;
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Could not find database driver class");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return conn;
	}
	
	
	public List<String> getColumnNames(String dbName,String queryString){
		DatabaseEntity dbEntity = getDatabases(dbName).get(0);
		Statement statement;
		if(dbEntity.getDb_dtls().equals("MySQL")) {
			Connection conn = makeMySQLConnection(dbEntity);
			if(conn != null) {
				try {
					statement = conn.createStatement();
					resultSet = statement.executeQuery(queryString);
					ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
					columnNames = new ArrayList<String>();
					for(int i=1;i<=resultSetMetaData.getColumnCount();i++) {
						columnNames.add(resultSetMetaData.getColumnName(i));
					}
					statement.close();
//					createJobInstance();
					return columnNames;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}else if (dbEntity.getDb_dtls().equals("Oracle")) {
			Connection conn = makeOracleConnection(dbEntity);
			if(conn != null) {
				try {
					statement = conn.createStatement();
					resultSet = statement.executeQuery(queryString);
					ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
					columnNames = new ArrayList<String>();
					for(int i=1;i<=resultSetMetaData.getColumnCount();i++) {
						columnNames.add(resultSetMetaData.getColumnName(i));
					}
					statement.close();
					return columnNames;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return new ArrayList<String>();
	}
	
	
	public String createJobInstance(String sec) {
		System.out.println("Create Job Instance called");
		Scheduler scheduler;
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			System.out.println("The scheduler context is : "+scheduler.getSchedulerInstanceId());
			JobDetail jobDetails=createJobDetails();
			Trigger trigger  = buildJobTrigger(jobDetails, "	1/"+sec+" * * * * ? * ");
			scheduler.scheduleJob(jobDetails, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return "Working";
	}
	
	public JobDetail createJobDetails() {
		JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("email", "glinzac@gmail.com");
        jobDataMap.put("subject", "SET");
        jobDataMap.put("body", "Working");
        System.out.println("Job Details Working");
        return JobBuilder.newJob(QuartzJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
	}
	
	
	public Trigger buildJobTrigger(JobDetail jobDetail,String cronSchedule) {
		System.out.println("Trigger working");
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "job-triggers")
                .withDescription("Job Trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule))
                .build();
    }

	public List<String> getJobInstance() {
		Scheduler scheduler;
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			System.out.println("The scheduler context is : "+scheduler.getSchedulerInstanceId());
			scheduler.getCurrentlyExecutingJobs().forEach((val)->{
				System.out.println("val : "+val);
			});
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList();
	}
	
}
