package com.strapin.bean;

public class AppUserBean {
	private String mFacebookId;
	private String mName;
	
	public AppUserBean(String mFacebookId,String mName){
		this.mFacebookId = mFacebookId;
		this.mName = mName;
		
		
	}

	public String getFacebookId() {
		return mFacebookId;
	}

	public void setFacebookId(String mFacebookId) {
		this.mFacebookId = mFacebookId;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

}
