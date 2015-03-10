package com.example.campusdetectionv2.support;

import java.io.Serializable;

public class BrowsingData implements Serializable{
	private int id;
	private String username;
	private String type;
	private String remark;
	private String time;
	private String submitTime;
	private double lat;
	private double lng;
	private double userLat;
	private double userLng;
	private boolean flag;
	private String picpath;
	
	public void BrowsingData(){
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTypeString()
	{
		String str="";
		switch(type){
		case "Garbage" : 
			str="垃圾污染";
			break;
		case "Discharge" : 
			str="污染排放";
			break;
		case "Damage" : 
			str="设施损坏";
		}
		return str;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(String submiTime) {
		this.submitTime = submiTime;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getUserLat() {
		return userLat;
	}
	public void setUserLat(double userLat) {
		this.userLat = userLat;
	}
	public double getUserLng() {
		return userLng;
	}
	public void setUserLng(double userLng) {
		this.userLng = userLng;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getPicpath() {
		return picpath;
	}
	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}

}
