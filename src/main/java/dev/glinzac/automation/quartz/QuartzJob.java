package dev.glinzac.automation.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.glinzac.automation.CustomJobBean;

public class QuartzJob implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(QuartzJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		logger.info("Executing Job with key ==> {}", context.getJobDetail().getKey());
		
		JobDataMap jobDataMap = context.getMergedJobDataMap();
        String subject = jobDataMap.getString("subject");
        String body = jobDataMap.getString("body");
        String recipientEmail = jobDataMap.getString("email");
        System.out.println("The subject is "+subject+" : "+body+" : "+recipientEmail);
	}

}
