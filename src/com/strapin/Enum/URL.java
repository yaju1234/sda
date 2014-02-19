package com.strapin.Enum;

public enum URL {
	SET_USER_LOCATION("http://clickfordevelopers.com/demo/snowmada/setlocation.php"),
	LOGIN("http://clickfordevelopers.com/demo/snowmada/login.php");	
	
	public String url;
	
	URL(String ob) {
		this.url = ob;
	}
	public String getURL() {
		return url;
	}
}
