package dev.glinzac.automation.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import dev.glinzac.automation.MainService;

@Component
public class CustomQuartzJob implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomQuartzJob.class);
	
	@Autowired
	MainService mainService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Executing Job with key ==> {}", context.getJobDetail().getKey());
		mainService.executeJob(context);
	}

}
