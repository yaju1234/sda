package com.strapin.facebook;

public enum FacebookValue {
	TOKEN("access_token"), EXPIRE("expires_in"), KEY("facebook-credentials");
	
	public String mFacebookValue;
	
	FacebookValue(String mFacebookValue){
		this.mFacebookValue = mFacebookValue;
	}
	
	public String getFacebookValue(){
		return mFacebookValue;
	}
}
