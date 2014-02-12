package com.strapin.bean;

public class ChatBean {
	private String mSender;
	private String mMessage;
	
	public ChatBean(String sender,String message){
		this.mSender = sender;
		this.mMessage = message;
	}

	public String getSender() {
		return mSender;
	}

	public void setSender(String mSender) {
		this.mSender = mSender;
	}

	public String getMessage() {
		return mMessage;
	}

	public void setMessage(String mMessage) {
		this.mMessage = mMessage;
	}
}
