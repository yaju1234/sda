package com.strapin.bean;

public class ContactListBean {
	public String name;
	public String ph;
	
	public ContactListBean (String name, String ph){
		this.name = name;
		this.ph   = ph;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the ph
	 */
	public String getPh() {
		return ph;
	}
	/**
	 * @param ph the ph to set
	 */
	public void setPh(String ph) {
		this.ph = ph;
	}
	
	

}
