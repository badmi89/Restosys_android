package com.milos.restosys.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	
	private String id;
	private String firstname;
	private String lastname;
	private String passcode;
	private String role;
	private int inshift;
	private int visible;
	private String dateStart;
	private String dateEnd;
	
	public User() {}
	
	public User(JSONObject json) {
		try {
			id = json.getString("id");
			firstname = json.getString("fname");
			lastname = json.getString("lname");
			passcode = json.getString("passcode");
			role = json.getString("role");
			inshift = json.getInt("inshift");
			visible = json.getInt("visible");
			dateStart = json.getString("date-start");
			dateEnd = json.getString("date-end");
		} catch (JSONException e) {
			e.printStackTrace();
		} 
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getPasscode() {
		return passcode;
	}
	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getInshift() {
		return inshift;
	}
	public void setInshift(int inshift) {
		this.inshift = inshift;
	}
	public int getVisible() {
		return visible;
	}
	public void setVisible(int visible) {
		this.visible = visible;
	}
	public String getDateStart() {
		return dateStart;
	}
	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	
	

}
