package com.strapin.bean;

public class NewMessage {
	public String senderId;
	public String receiverId;
	public String senderFirstName;
	public String receiverFirstName;
	public String senderLastName;
	public String receiverLastName;
	public String date1;
	public String message;
	public String senderImage;
	public String receiverImage;
	
	public NewMessage(String senderId,String receiverId, String senderFirstName,String receiverFirstName,String senderLastName,String receiverLastName,String date1,String message,String senderImage,String receiverImage){
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.senderFirstName = senderFirstName;
		this.receiverFirstName = receiverFirstName;
		this.senderLastName = senderLastName;
		this.receiverLastName = receiverLastName;
		this.date1 = date1;
		this.message = message;
		this.senderImage = senderImage;
		this.receiverImage = receiverImage;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getSenderFirstName() {
		return senderFirstName;
	}

	public void setSenderFirstName(String senderFirstName) {
		this.senderFirstName = senderFirstName;
	}

	public String getReceiverFirstName() {
		return receiverFirstName;
	}

	public void setReceiverFirstName(String receiverFirstName) {
		this.receiverFirstName = receiverFirstName;
	}

	public String getSenderLastName() {
		return senderLastName;
	}

	public void setSenderLastName(String senderLastName) {
		this.senderLastName = senderLastName;
	}

	public String getReceiverLastName() {
		return receiverLastName;
	}

	public void setReceiverLastName(String receiverLastName) {
		this.receiverLastName = receiverLastName;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSenderImage() {
		return senderImage;
	}

	public void setSenderImage(String senderImage) {
		this.senderImage = senderImage;
	}

	public String getReceiverImage() {
		return receiverImage;
	}

	public void setReceiverImage(String receiverImage) {
		this.receiverImage = receiverImage;
	}
	
	

}
