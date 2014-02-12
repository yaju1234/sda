package com.strapin.bean;

public class FriendListBean {
	private String Name;
	private String FbId;
	private String Status;
	private String mOnlineStatus;
	private String mISUserExist;
	
	public FriendListBean( String Name,String FbId,String Status,String mOnlineStatus,String mISUserExist){
		this.Name = Name;
		this.FbId = FbId;
		this.Status = Status;
		this.mOnlineStatus = mOnlineStatus;
		this.mISUserExist = mISUserExist;
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

	public String getOnlineStatus() {
		return mOnlineStatus;
	}

	public void setOnlineStatus(String mOnlineStatus) {
		this.mOnlineStatus = mOnlineStatus;
	}

	public String getISUserExist() {
		return mISUserExist;
	}

	public void setISUserExist(String mISUserExist) {
		this.mISUserExist = mISUserExist;
	}
}
