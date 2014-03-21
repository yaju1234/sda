package com.strapin.bean;

public class AppusersBean {
	
	private String mName;
	private String mId;
	
	public AppusersBean(String mId,String mName){
		this.mName = mName;
		this.mId = mId;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}
    /**
     * Facebok id for Facebook friend
     * Phone number for contact list
     * @return
     */
	public String getId() {
		return mId;
	}
	/**
	 * 
	 * @param mId   Facebook if for facebook friends
	 *              Phone number for Contact list
	 */
	public void setId(String mId) {
		this.mId = mId;
	}
	
	
	

}
