package com.strapin.Enum;

public enum URL {
	SEND_CURRENT_LOCATION("http://clickfordevelopers.com/demo/snowmada/send_current_location.php"),
	LOGIN("http://clickfordevelopers.com/demo/snowmada/login.php"),
	COUNT_PENDING_FRIEND("http://clickfordevelopers.com/demo/snowmada/count_pending_friend.php"),
	ADD_FRIEND("http://clickfordevelopers.com/demo/snowmada/add_friend_request.php"),
	STATUS_TRACK_TOOGLE("http://clickfordevelopers.com/demo/snowmada/change_track.php"),
	ACCEPT_FRIEND_REQ("http://clickfordevelopers.com/demo/snowmada/change_track.php"),
	MESSAGE("http://clickfordevelopers.com/demo/snowmada/new_chat_history.php"),
	ACTIVE_APP_USERS("http://clickfordevelopers.com/demo/snowmada/app_user.php"),
	ADD_MEET_UP_LOCATION("http://clickfordevelopers.com/demo/snowmada/add_meet_up.php"),
	MEET_UP_MERKER_LIST("http://clickfordevelopers.com/demo/snowmada/list_meetup.php"),
	CHAT("http://clickfordevelopers.com/demo/snowmada/chat.php"),
	MEET_UP_DETELE("http://clickfordevelopers.com/demo/snowmada/del_meet_up.php"),
	FRIEND_DELETE("http://clickfordevelopers.com/demo/snowmada/delete_friend.php"),
	PENDING_FRIEND_REQUEST("http://clickfordevelopers.com/demo/snowmada/pending_friend.php"),
	CHAT_HISTORY("http://clickfordevelopers.com/demo/snowmada/chat_history.php"),
	SEND_MESSAGE("http://clickfordevelopers.com/demo/snowmada/send_message.php"),
	GET_LOCATION("http://clickfordevelopers.com/demo/snowmada/track.php"),
	FRIEND_LIST("http://clickfordevelopers.com/demo/snowmada/snowmada_friend.php"),
	SKI_PATROL("http://clickfordevelopers.com/demo/snowmada/ski_patrol.php");
	
	URL(String ob) {
		this.url = ob;
	}
	public String getUrl() {
		return url;
	}
	public String url;
}
