package com.strapin.global;

import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.strapin.bean.ChatBean;
import com.strapin.bean.ImageBean;

public class Global {

	// public static String mDob = "";
	public static boolean isLoginFirstTime = false;
	public static boolean isMyLocationCurrentUploadIntoServer = true;

	// public static String sFriendName = "";
	public static boolean isZoom = false;
	public static boolean isTrackedLocationZoomed = false;
	public static boolean isAppForeground       = false;
	
	public static ArrayList<ImageBean>       imageArr       = new ArrayList<ImageBean>();

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
	public static String iv_chat_avatar_img = "";
	
	public static ArrayList<ImageBean> galleryItem;
	public static int selectedPos = 0;
	
	public static boolean meetupzoomFlag = false;
	
	
	public static boolean profileeditflag = false;
	public static String user_location = "";
	public static String user_fav_mountain = "";
	public static String user_shred_style = "";
	public static String user_about_me = "";
	public static String user_age = "";
	
	
	public static boolean profileImageeditflag = false;
	//public static boolean meetupinstructionflag = false;
	
}
