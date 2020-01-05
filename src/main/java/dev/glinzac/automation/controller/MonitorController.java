package dev.glinzac.automation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dev.glinzac.automation.MainService;

@RestController
public class MonitorController {
	
	@Autowired
	MainService mainService;

	@RequestMapping(method = RequestMethod.GET,value= "/monitor/Jobs")
	public List<String> createJobInstance() {
		return mainService.getJobInstance();
	}
	
	@RequestMapping(method = RequestMethod.GET,value= "/monitor/stopAllJobs")
	public void stopAllJobInstance() {
		mainService.stopAllJobs();
	}
	
	
}
