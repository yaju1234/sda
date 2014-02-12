package com.strapin.Enum;

public enum URL {
	SET_USER_LOCATION("http://clickfordevelopers.com/demo/snowmada/setlocation.php");
	
	public String mURL;
	
	URL(String mURL) {
		this.mURL = mURL;
	}
	public String getURL() {
		return mURL;
	}
}
