package com.strapin.bean;

public class FriendRequestBean {
	
	private String senderfbId;
	private String receiverfbId;
	private String senderName;
	private String recordId;
	private String mTrackStatus;
	
	public FriendRequestBean(String senderfbId, String receiverfbId,String senderName, String recordId,String mTrackStatus) {
		super();
		this.senderfbId = senderfbId;
		this.receiverfbId = receiverfbId;
		this.senderName = senderName;
		this.recordId = recordId;
		this.mTrackStatus = mTrackStatus;
	}

	public String getSenderfbId() {
		return senderfbId;
	}

	public void setSenderfbId(String senderfbId) {
		this.senderfbId = senderfbId;
	}

	public String getReceiverfbId() {
		return receiverfbId;
	}

	public void setReceiverfbId(String receiverfbId) {
		this.receiverfbId = receiverfbId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getTrackStatus() {
		return mTrackStatus;
	}

	public void setTrackStatus(String mTrackStatus) {
		this.mTrackStatus = mTrackStatus;
	}
	
	
}
