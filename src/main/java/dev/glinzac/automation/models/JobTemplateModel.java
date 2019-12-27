package dev.glinzac.automation.models;

public class JobTemplateModel {
	
	String dbName;
	String queryString;
	
	
	public JobTemplateModel() {
		super();
	}
	public JobTemplateModel(String dbName, String queryString) {
		super();
		this.dbName = dbName;
		this.queryString = queryString;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	
}
