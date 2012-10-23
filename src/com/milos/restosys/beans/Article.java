package com.milos.restosys.beans;

import org.json.JSONException;
import org.json.JSONObject;

public class Article {
	
	private String code;
	private String label;
	private String category;
	private String subCategory;
	private String quantity;
	private double price;
	private int visible;
	private int instock;
	
	public Article() {}
	
	public Article(JSONObject json) {
		try {
			code = json.getString("code");
			label = json.getString("label");
			category = json.getString("category");
			subCategory = json.getString("sub-category");
			quantity = json.getString("quantity");
			price = json.getDouble("price");
			visible = json.getInt("visible");
			instock = json.getInt("instock");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getVisible() {
		return visible;
	}
	public void setVisible(int visible) {
		this.visible = visible;
	}
	public int getInstock() {
		return instock;
	}
	public void setInstock(int instock) {
		this.instock = instock;
	}
	
}
