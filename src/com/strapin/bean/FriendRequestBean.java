package com.strapin.bean;

public class FriendRequestBean {
	
	private String senderfbId;
	private String receiverfbId;
	private String senderName;
	private String recordId;
	private int trackStatus;
	
	public FriendRequestBean(String senderfbId, String receiverfbId,String senderName, String recordId,int trackStatus) {
		super();
		this.senderfbId = senderfbId;
		this.receiverfbId = receiverfbId;
		this.senderName = senderName;
		this.recordId = recordId;
		this.trackStatus = trackStatus;
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

	public int getTrackStatus() {
		return trackStatus;
	}

	public void setTrackStatus(int trackStatus) {
		this.trackStatus = trackStatus;
	}
	
	
}
