package com.strapin.global;

public class Constant {
	public enum Settings{
		GLOBAL_SETTINGS,
		FACEBOOK_FIRST_NAME,
		FACEBOOK_LAST_NAME,
		USER_ID,
		USER_IMAGE,
		IS_SESSION_AVAILABLE,
		SENDER_ID_CHAT,
		APP_FOREGROUND,
		ALERT_SKI_PATROL,
		LOGIN_TYPE,
		MEET_UP_INSTRUCTION,
		TRACK_INSTRUCTION,
		CHAT_INSTRUCTION;
	}
	
	public static final int ACTIVE_TRACK_STATUS = 1;
	public static final int DEACTIVE_TRACK_STATUS = 0;
	
	public static final int TRACKING_PUSH_NOTIFICATION = 1;
	public static final int SKI_PATROL_PUSH_NOTIFICATION = 2;
	public static final int ONLINE_STATUS_PUSH_NOTIFICATION = 3;
	public static final int CHAT_PUSH_NOTIFICATION = 4;
	public static final int FRIEND_REQUEST_COME_PUSH_NOTIFICATION = 5;
	public static final int FRIEND_REQUEST_ACCEPT_PUSH_NOTIFICATION = 6;
	public static final int CREATE_MEETUP_PUSH_NOTIFICATION = 7;
	
	public static final String NORMAL_LOGIN = "N";
	public static final String FACEBOOK_LOGIN = "F";
	

}
