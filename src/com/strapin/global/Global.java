package com.strapin.global;

import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.strapin.bean.ChatBean;

public class Global {

	// public static String mDob = "";
	public static boolean isLoginFirstTime = false;
	public static boolean isMyLocationCurrentUploadIntoServer = true;

	// public static String sFriendName = "";
	public static boolean isZoom = false;

	/* public static String mChatUserName = ""; */

	public static GoogleMap map;

	public static GoogleMap getMap() {
		return map;
	}

	public static void setMap(GoogleMap map) {
		Global.map = map;
	}

	/* public static int selectedTab = 1; */
	/* public static boolean isWebServiceCallForRefreshFriendList = false; */

	
	
	public static boolean isServiceContinue = true;

	public static boolean isZoomAtUSerLocationFirstTime = true;

	public static ArrayList<ChatBean> mChatArr = new ArrayList<ChatBean>();

}
