package com.strapin.application;

import com.strapin.Enum.Constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppInfo {
	public String userFirstName=null;
	public String userLastName = null;
	public String userId = null;
	public boolean session = false;
	public String senderIdChat="";
	public boolean isAppForeground = false;
	public boolean isAlertForSKIPatrol = false;
	public SharedPreferences sharedPreferences;
	
	public AppInfo(Context ctx){
		
		sharedPreferences = ctx.getSharedPreferences(Constant.Settings.GLOBAL_SETTINGS.name(), Context.MODE_PRIVATE);
		userFirstName = sharedPreferences.getString(Constant.Settings.FACEBOOK_FIRST_NAME.name(), userFirstName);
		userLastName = sharedPreferences.getString(Constant.Settings.FACEBOOK_LAST_NAME.name(), userLastName);
		userId = sharedPreferences.getString(Constant.Settings.FACEBOOK_ID.name(), userId);
		session = sharedPreferences.getBoolean(Constant.Settings.IS_SESSION_AVAILABLE.name(), session);
		senderIdChat = sharedPreferences.getString(Constant.Settings.SENDER_ID_CHAT.name(), senderIdChat);
		isAppForeground = sharedPreferences.getBoolean(Constant.Settings.APP_FOREGROUND.name(), isAppForeground);
		isAlertForSKIPatrol = sharedPreferences.getBoolean(Constant.Settings.ALERT_SKI_PATROL.name(), isAlertForSKIPatrol);
	}
	
	public void setUserInfo(String fname, String lname, String fid){
		this.userFirstName = fname;
		this.userLastName = lname;
		this.userId = fid;
		Editor edit = sharedPreferences.edit();
		edit.putString(Constant.Settings.FACEBOOK_FIRST_NAME.name(), userFirstName);
		edit.putString(Constant.Settings.FACEBOOK_LAST_NAME.name(), userLastName);
		edit.putString(Constant.Settings.FACEBOOK_ID.name(), userId);
		edit.commit();
	}
	
	public void setSession(boolean flg){
		session = flg;
		Editor edit = sharedPreferences.edit();
		edit.putBoolean(Constant.Settings.IS_SESSION_AVAILABLE.name(), session);
		edit.commit();
	}
	
	public void setSenderIDChat(String  id){
		senderIdChat = id;
		Editor edit = sharedPreferences.edit();
		edit.putString(Constant.Settings.SENDER_ID_CHAT.name(), senderIdChat);
		edit.commit();
	}
	
	public void setIsAppForgroung(boolean flg){
		isAppForeground = flg;
		Editor edit = sharedPreferences.edit();
		edit.putBoolean(Constant.Settings.APP_FOREGROUND.name(), isAppForeground);
		edit.commit();
	}
	
	public void setIsAlertForSKIPatrol(boolean flg){
		isAlertForSKIPatrol = flg;
		Editor edit = sharedPreferences.edit();
		edit.putBoolean(Constant.Settings.ALERT_SKI_PATROL.name(), isAlertForSKIPatrol);
		edit.commit();
	}

}
