package com.strapin.bean;

public class FacebookFriendBean {
	
	private String mName;
	private String mId;
	
	public FacebookFriendBean(String mName,String mId){
		this.mName = mName;
		this.mId = mId;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getId() {
		return mId;
	}

	public void setId(String mId) {
		this.mId = mId;
	}
	
	
	

}
