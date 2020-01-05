package dev.glinzac.automation.quartz;

import java.util.Properties;
import java.util.UUID;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
public class CustomQuartzJobBean{
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Bean
	public SpringBeanJobFactory springBeanJobFactory() {
	    AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
	    jobFactory.setApplicationContext(applicationContext);
	    return jobFactory;
	}
	
	
	@Bean
    public SchedulerFactoryBean scheduleJob() {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		
		Properties properties = new Properties();
        properties.setProperty("org.quartz.scheduler.instanceName", "MyQuartzInstance");
        properties.setProperty("org.quartz.scheduler.instanceId", "CUSTOM_INSTANCE");
        properties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        properties.setProperty("org.quartz.threadPool.threadCount", "20");
        properties.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        properties.setProperty("org.quartz.jobStore.driverDelegateClass", " org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
//        properties.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        properties.setProperty("org.quartz.jobStore.dataSource", "automation");
        properties.setProperty("org.quartz.jobStore.misfireThreshold", "25000");
        
        properties.setProperty("org.quartz.dataSource.automation.driver", "com.mysql.jdbc.Driver");
        properties.setProperty("org.quartz.dataSource.automation.URL", "jdbc:mysql://localhost:3306/automation");
        properties.setProperty("org.quartz.dataSource.automation.user", "root");
        properties.setProperty("org.quartz.dataSource.automation.password", "root");
//        properties.setProperty("org.quartz.dataSource.automation.maxConnections ", "20");
        
        
        schedulerFactory.setQuartzProperties(properties);
        schedulerFactory.setJobFactory(springBeanJobFactory());
        return schedulerFactory;

        
	}
	

	public Scheduler scheduler(Trigger trigger, JobDetail job, SchedulerFactoryBean factory) 
	  throws SchedulerException {
	    Scheduler scheduler = factory.getScheduler();
	    scheduler.scheduleJob(job, trigger);
	    scheduler.start();
	    return scheduler;
	}
	
	

	public JobDetail createJobDetails() {
		JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("email", "glinzac@gmail.com");
        jobDataMap.put("subject", "SET");
        jobDataMap.put("body", "Working");
        System.out.println("Job Details Working");
        return JobBuilder.newJob(CustomQuartzJob.class)
                .withIdentity(UUID.randomUUID().toString(), "job-details")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably(true)
                .requestRecovery(true)
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


}
