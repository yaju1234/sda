package com.strapin.global;

import java.util.ArrayList;

import org.json.JSONArray;
import com.google.android.gms.maps.GoogleMap;
import com.strapin.bean.ChatBean;

public class Global {
	
public static String mDob = "";
    public static boolean isSessionValid = false;
    public static boolean isLoginFirstTime = false;
    public static boolean isMyLocationCurrentUploadIntoServer = true;
    public static JSONArray mFriendJSOn = new JSONArray();
    
    public static String sFriendName = "";
    public static String sFriendId = "";
   // public static  boolean flag = true;
    public static  boolean isZoom = false;
    
    
    public static String mChatUserName = "";
     
   // public static boolean isRefresh = false;
    
    
   // public static boolean isTrackFriendLocation = false;
   // public static boolean isMeetUploaction = false;
    
    public static boolean isInfoWindow = false;
    
	public static JSONArray getFriendJSOn() {
		return mFriendJSOn;
	}
	public static void setFriendJSOn(JSONArray mFriendJSOn) {
		Global.mFriendJSOn = mFriendJSOn;
	}
	
	
	//public static  FacebookStrapIn mFacebook;
    
	public static GoogleMap map;

	public static GoogleMap getMap() {
		return map;
	}
	public static void setMap(GoogleMap map) {
		Global.map = map;
	}
	
	public static int mSelectedTab = 1;
	public static boolean isAddSnowmadaFriend = false;
	
	
	public static boolean isServiceContinue = true;
	
	public static boolean isZoomAtUSerLocationFirstTime = true;
	
	
	/*public static boolean isApplicationForeground = false;*/
	
	public static boolean isChatActive  = false;
	public static ArrayList<ChatBean> mChatArr = new ArrayList<ChatBean>();
	public static String mChatSenderID="";
	
	

}
