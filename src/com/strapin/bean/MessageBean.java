package com.strapin.bean;

public class MessageBean {
	private int id;
	private String mSenderName;
	private String mSenderFbId;
	private String mStatus;
	private String mTextMessage;
	
	public MessageBean(int id, String senderfbid,String sendername,String status,String textmessage) {
		super();
		this.id = id;
		this.mSenderName = sendername;
		this.mSenderFbId = senderfbid;
		this.mStatus = status;
		this.mTextMessage = textmessage;
	}

	public String getSenderName() {
		return mSenderName;
	}

	public void setSenderName(String mSenderName) {
		this.mSenderName = mSenderName;
	}

	public String getSenderFbId() {
		return mSenderFbId;
	}

	public void setSenderFbId(String mSenderFbId) {
		this.mSenderFbId = mSenderFbId;
	}

	public String getTextMessage() {
		return mTextMessage;
	}

	public void setTextMessage(String mTextMessage) {
		this.mTextMessage = mTextMessage;
	}

	public String getStatus() {
		return mStatus;
	}

	public void setStatus(String status) {
		this.mStatus = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
