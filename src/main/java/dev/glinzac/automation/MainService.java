package dev.glinzac.automation;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.glinzac.automation.entities.DatabaseEntity;
import dev.glinzac.automation.quartz.CronExpressionGenerator;
import dev.glinzac.automation.quartz.CustomQuartzJob;
import dev.glinzac.automation.quartz.CustomQuartzJobBean;
import dev.glinzac.automation.repository.DatabaseRepository;

@Service
public class MainService {
	
	@Autowired
	DatabaseRepository dbRepo;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	CustomQuartzJobBean customJobBean;
	
	@Autowired
	CronExpressionGenerator cronGenerator;
	
	
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
	
//	Accept JobDetail and Save it=> convert into cron expression
	public String createJobInstance(String sec) {
		System.out.println("Create Job Instance called");
//		customJobBean = applicationContext.getBean(CustomQuartzJobBean.class);
		JobDetail jobDetail = customJobBean.createJobDetails();
		Trigger jobTrigger = customJobBean.buildJobTrigger(jobDetail, "	1/"+sec+" * * * * ? * ");
		try {
			customJobBean.scheduler(jobTrigger, jobDetail, customJobBean.scheduleJob());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return "Working";
	}
	
	
	

	public List<String> getJobInstance() {
//		customJobBean =customJobBean = applicationContext.getBean(CustomQuartzJobBean.class);
		try {
			Scheduler scheduler = customJobBean.scheduleJob().getScheduler();
			System.out.println("Scheduler Name is : " +scheduler.getSchedulerName());
			for (String groupName : scheduler.getJobGroupNames()) {

			     for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
							
				  String jobName = jobKey.getName();
				  String jobGroup = jobKey.getGroup();
							
				  //get job's trigger
				  List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
				  Date nextFireTime = triggers.get(0).getNextFireTime(); 

					System.out.println("[jobName] : " + jobName + " [groupName] : "
						+ jobGroup + " - " + nextFireTime);

				  }

			    }
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList();
	}
	
	public void stopAllJobs() {
//		customJobBean = customJobBean = applicationContext.getBean(CustomQuartzJobBean.class);
		Scheduler scheduler = customJobBean.scheduleJob().getScheduler();
		System.out.println("Job Deletion process");
		try {
			for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals("job-details"))) {
				System.out.println("Job Deleted : "+jobKey.getName());
//				scheduler.interrupt(jobKey);
				scheduler.deleteJob(jobKey);
			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//  TODO Perform Job action run query, send mail from details in JobDataMap
	
	@Async
	public void executeJob(JobExecutionContext context) {
		JobDataMap jobDataMap = context.getMergedJobDataMap();
        String subject = jobDataMap.getString("subject");
        String body = jobDataMap.getString("body");
        String recipientEmail = jobDataMap.getString("email");
        System.out.println("The subject is "+subject+" : "+body+" : "+recipientEmail);	
	}
	
}
