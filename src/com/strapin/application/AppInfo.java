package com.strapin.application;

import com.strapin.Enum.Constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppInfo {
	public String mFbFirstName;
	public String mFbLastName;
	public String mFbID;
	public boolean session = false;
	public String senderidchat="";
	public boolean isappforeground = false;
	public SharedPreferences sharedPreferences;
	
	public AppInfo(Context ctx){
		
		sharedPreferences = ctx.getSharedPreferences(Constant.Settings.GLOBAL_SETTINGS.name(), Context.MODE_PRIVATE);
		mFbFirstName = sharedPreferences.getString(Constant.Settings.FACEBOOK_FIRST_NAME.name(), null);
		mFbLastName = sharedPreferences.getString(Constant.Settings.FACEBOOK_LAST_NAME.name(), null);
		mFbID = sharedPreferences.getString(Constant.Settings.FACEBOOK_ID.name(), null);
		session = sharedPreferences.getBoolean(Constant.Settings.IS_SESSION_AVAILABLE.name(), session);
		senderidchat = sharedPreferences.getString(Constant.Settings.SENDER_ID_CHAT.name(), senderidchat);
		isappforeground = sharedPreferences.getBoolean(Constant.Settings.APP_FOREGROUND.name(), isappforeground);
	}
	
	public void setUserInfo(String fname, String lname, String fid){
		this.mFbFirstName = fname;
		this.mFbLastName = lname;
		this.mFbID = fid;
		Editor edit = sharedPreferences.edit();
		edit.putString(Constant.Settings.FACEBOOK_FIRST_NAME.name(), mFbFirstName);
		edit.putString(Constant.Settings.FACEBOOK_LAST_NAME.name(), mFbLastName);
		edit.putString(Constant.Settings.FACEBOOK_ID.name(), mFbID);
		edit.commit();
	}
	
	public void setSession(boolean flg){
		session = flg;
		Editor edit = sharedPreferences.edit();
		edit.putBoolean(Constant.Settings.IS_SESSION_AVAILABLE.name(), session);
	}
	
	public void setSenderIDChat(String  id){
		senderidchat = id;
		Editor edit = sharedPreferences.edit();
		edit.putString(Constant.Settings.SENDER_ID_CHAT.name(), senderidchat);
	}
	
	public void setIsAppForgroung(boolean flg){
		isappforeground = flg;
		Editor edit = sharedPreferences.edit();
		edit.putBoolean(Constant.Settings.APP_FOREGROUND.name(), isappforeground);
	}

}
