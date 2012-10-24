package com.milos.restosys.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Report {
	
	private String waiterID;
	private String waiterName;
	private String date;
	private String time;
	private double total;
	private List<Article> articles;
	
	public Report() {}
	
	public Report(JSONObject json) {
		try {
			waiterID = json.getString("waiter-id");
			waiterName = json.getString("waiter-name");
			total = json.getDouble("total");
			date = json.getString("date");
			time = json.getString("time");
			
			JSONArray arts = json.getJSONArray("articles");
			articles = new ArrayList<Article>();
			for (int i = 0; i < arts.length(); i++) {
				articles.add(new Article(arts.getJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String getWaiterID() {
		return waiterID;
	}
	public void setWaiterID(String waiterID) {
		this.waiterID = waiterID;
	}
	public String getWaiterName() {
		return waiterName;
	}
	public void setWaiterName(String waiterName) {
		this.waiterName = waiterName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public List<Article> getArticles() {
		return articles;
	}
	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	

}
