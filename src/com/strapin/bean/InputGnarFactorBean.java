package com.strapin.bean;

public class InputGnarFactorBean {
	private String mDescription;
	private String mPoints;
	
	public InputGnarFactorBean(String mDescription, String mPoints) {
		super();
		this.mDescription = mDescription;
		this.mPoints = mPoints;
	}
	public String getmDescription() {
		return mDescription;
	}
	public void setmDescription(String mDescription) {
		this.mDescription = mDescription;
	}
	public String getmPoints() {
		return mPoints;
	}
	public void setmPoints(String mPoints) {
		this.mPoints = mPoints;
	}
	
}
