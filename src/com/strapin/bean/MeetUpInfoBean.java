package com.strapin.bean;

public class MeetUpInfoBean {

	private String mFacebookId;
	private String mName;
	private String mLocation;
	private String mLatitude;
	private String mLongitude;
	private String mTime;
	private String mAbout;
	private String mStatus;
	private String mCreater;
	
	public MeetUpInfoBean(String mFacebookId, String mName,
			String mLocation, String mLatitude, String mLongitude,
			String mTime, String mAbout, String mStatus, String mCreater) {
		super();
		this.mFacebookId = mFacebookId;
		this.mName = mName;
		this.mLocation = mLocation;
		this.mLatitude = mLatitude;
		this.mLongitude = mLongitude;
		this.mTime = mTime;
		this.mAbout = mAbout;
		this.mStatus = mStatus;
		this.mCreater = mCreater;
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

	public String getLocation() {
		return mLocation;
	}

	public void setLocation(String mLocation) {
		this.mLocation = mLocation;
	}

	public String getLatitude() {
		return mLatitude;
	}

	public void setLatitude(String mLatitude) {
		this.mLatitude = mLatitude;
	}

	public String getLongitude() {
		return mLongitude;
	}

	public void setLongitude(String mLongitude) {
		this.mLongitude = mLongitude;
	}

	public String getTime() {
		return mTime;
	}

	public void setTime(String mTime) {
		this.mTime = mTime;
	}

	public String getAbout() {
		return mAbout;
	}

	public void setAbout(String mAbout) {
		this.mAbout = mAbout;
	}

	public String getStatus() {
		return mStatus;
	}

	public void setStatus(String mStatus) {
		this.mStatus = mStatus;
	}

	public String getCreater() {
		return mCreater;
	}

	public void setCreater(String mCreater) {
		this.mCreater = mCreater;
	}
	
}
