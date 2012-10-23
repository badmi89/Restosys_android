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
	private Date date;
	private Date time;
	
	public Bill() {}
	
	public Bill(JSONObject json) {
		try {
			id = json.getString("id");
			userId = json.getString("user-id");
			printed = json.getInt("printed");
			total = json.getDouble("total");
			date = new SimpleDateFormat("dd.MM.yyyy").parse(json.getString("date"));
			time = new SimpleDateFormat("HH.mm").parse(json.getString("time"));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	

}
