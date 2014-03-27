package com.strapin.bean;

public class InviteFriendBean {
	private String mFacebookId;
	private String mName;
	
	public InviteFriendBean(String mFacebookId,String mName){
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
