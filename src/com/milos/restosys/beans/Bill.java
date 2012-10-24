package com.milos.restosys.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Bill {
	
	private String id;
	private String userId;
	private int printed;
	private double total;
	private String date;
	private String time;
	
	public Bill() {}
	
	public Bill(JSONObject json) {
		try {
			id = json.getString("id");
			userId = json.getString("user-id");
			printed = json.getInt("printed");
			total = json.getDouble("total");
			date = json.getString("date");
			time = json.getString("time");
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getPrinted() {
		return printed;
	}
	public void setPrinted(int printed) {
		this.printed = printed;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	

}
