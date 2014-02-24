package com.strapin.Enum;

public enum URL {
	SET_USER_LOCATION("http://clickfordevelopers.com/demo/snowmada/setlocation.php"),
	LOGIN("http://clickfordevelopers.com/demo/snowmada/login.php"),
	COUNT_PENDING_FRIEND("http://clickfordevelopers.com/demo/snowmada/count_pending_friend.php");
	
	URL(String ob) {
		this.url = ob;
	}
	public String getUrl() {
		return url;
	}
	public String url;
}
