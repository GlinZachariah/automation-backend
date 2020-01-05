package dev.glinzac.automation.models;

import java.sql.Date;

public class JobDetailModel {
	private String jobName;
	private String Status;
	private String jobKey;
	private Date lastExecuted;
	private Date nextExecuted;
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getJobKey() {
		return jobKey;
	}
	public void setJobKey(String jobKey) {
		this.jobKey = jobKey;
	}
	public Date getLastExecuted() {
		return lastExecuted;
	}
	public void setLastExecuted(Date lastExecuted) {
		this.lastExecuted = lastExecuted;
	}
	public Date getNextExecuted() {
		return nextExecuted;
	}
	public void setNextExecuted(Date nextExecuted) {
		this.nextExecuted = nextExecuted;
	}
	
	
}
