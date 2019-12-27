package dev.glinzac.automation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dev.glinzac.automation.MainService;
import dev.glinzac.automation.entities.DatabaseEntity;
import dev.glinzac.automation.models.JobTemplateModel;

@RestController
public class JobController {
	
	@Autowired
	MainService mainService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/jobs/getDatabases")
	public List<DatabaseEntity> getDatabases(){
		return mainService.getDatabaseList();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/jobs/getColumnNames")
	public List<String> getColumnDetails(@RequestBody JobTemplateModel job ){
		return mainService.getColumnNames(job.getDbName(), job.getQueryString());
	}
	

}
