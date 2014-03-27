package com.strapin.bean;

public class CustomeInfoWindowBean {
	public String name;
	public String location;
	public String description;
	public String date1;
	public String time;
	
	public CustomeInfoWindowBean(String name,String location,String description,String date1,String time){
		this.name = name;
		this.location = location;
		this.description = description;
		this.date1 = date1;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	

}
