package com.strapin.bean;

public class FriendListBean {
	private String Name;
	private String FbId;
	private String Status;
	private boolean isOnline;
	private String mISUserExist;
	private String image;
	
	public FriendListBean( String Name,String FbId,String Status,boolean mOnlineStatus,String mISUserExist,String image){
		this.Name = Name;
		this.FbId = FbId;
		this.Status = Status;
		this.isOnline = mOnlineStatus;
		this.mISUserExist = mISUserExist;
		this.image = image;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getFbId() {
		return FbId;
	}

	public void setFbId(String fbId) {
		FbId = fbId;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public String getISUserExist() {
		return mISUserExist;
	}

	public void setISUserExist(String mISUserExist) {
		this.mISUserExist = mISUserExist;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
}
