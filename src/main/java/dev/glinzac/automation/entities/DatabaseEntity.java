package dev.glinzac.automation.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="db_entity")
public class DatabaseEntity {
	
	@Id
	@Column
	private String db_name;
	
	@Column
	private String db_user_name;
	
	@Column
	private String db_pwd;
	
	@Column
	private String db_dtls;
	
	@Column
	private String db_hostname;
	
	@Column
	private int db_port;
	
	@Column
	private String db_ser;
	
	@Column
	private String db_sid;


	public String getDb_name() {
		return db_name;
	}

	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}

	public String getDb_user_name() {
		return db_user_name;
	}

	public void setDb_user_name(String db_user_name) {
		this.db_user_name = db_user_name;
	}

	public String getDb_pwd() {
		return db_pwd;
	}

	public void setDb_pwd(String db_pwd) {
		this.db_pwd = db_pwd;
	}

	public String getDb_dtls() {
		return db_dtls;
	}

	public void setDb_dtls(String db_dtls) {
		this.db_dtls = db_dtls;
	}

	public String getDb_hostname() {
		return db_hostname;
	}

	public void setDb_hostname(String db_hostname) {
		this.db_hostname = db_hostname;
	}

	public int getDb_port() {
		return db_port;
	}

	public void setDb_port(int db_port) {
		this.db_port = db_port;
	}

	public String getDb_sid() {
		return db_sid;
	}

	public void setDb_sid(String db_sid) {
		this.db_sid = db_sid;
	}

	public String getDb_ser() {
		return db_ser;
	}

	public void setDb_ser(String db_ser) {
		this.db_ser = db_ser;
	}

	

	
}

