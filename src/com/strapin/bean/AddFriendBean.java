package com.strapin.bean;

public class AddFriendBean {
	public String friendId;
	public String name;
	
	public AddFriendBean(String friendid,String name){
		this.friendId = friendid;
		this.name = name;	
		}

	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendid) {
		this.friendId = friendid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
