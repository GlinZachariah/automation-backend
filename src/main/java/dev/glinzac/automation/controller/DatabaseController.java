package dev.glinzac.automation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dev.glinzac.automation.MainService;
import dev.glinzac.automation.entities.DatabaseEntity;
import dev.glinzac.automation.models.Result;

@RestController
public class DatabaseController {
	
	@Autowired
	MainService mainService;
	
	boolean res;
	
	@RequestMapping(method = RequestMethod.GET,path = "/test")
	public String testConnection() {
		return "Connection Successful!";
	}
	
	@RequestMapping(method = RequestMethod.POST,path = "/validate")
	public Result validateDatabase(@RequestBody DatabaseEntity db) {
		//TODO 

		if(db.getDb_dtls().equals("MySQL")) {
			res= mainService.testMySQLConnection(db.getDb_hostname(), db.getDb_port(), db.getDb_name(), db.getDb_user_name(), db.getDb_pwd());
			if(res == true) {
				mainService.saveData(db);
				return new Result("Working");
			}
		}else if(db.getDb_dtls().equals("Oracle")) {
			res= mainService.testOracleConnection(db.getDb_hostname(), db.getDb_port(), db.getDb_sid(), db.getDb_user_name(), db.getDb_pwd(),db.getDb_ser());
			if(res == true) {
				mainService.saveData(db);
				return new Result("Working");
			}
		}
		return new Result("Not Working");
	}
	
	@RequestMapping(method=RequestMethod.GET,path="/search/{dbName}")
	public List<DatabaseEntity> searchDatabase(@PathVariable String dbName) {
		System.out.println("Database Search : "+dbName);
		return mainService.getDatabases(dbName);
	}
	
	

}
