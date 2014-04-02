package com.strapin.application;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.strapin.global.Constants;

public class AppInfo {
	public String userFirstName          =null;
	public String userLastName           = null;
	public String userId                 = null;
	public String image                  = null;
	public boolean session               = false;
	public String senderIdChat           ="";
	//public boolean isAppForeground       = false;
	public boolean isAlertForSKIPatrol   = false;
	public String loginType              = "";
	public SharedPreferences sharedPreferences;
	
	public AppInfo(Context ctx){
		
		sharedPreferences        = ctx.getSharedPreferences(Constants.Settings.GLOBAL_SETTINGS.name(), Context.MODE_PRIVATE);
		userFirstName            = sharedPreferences.getString(Constants.Settings.FACEBOOK_FIRST_NAME.name(), userFirstName);
		userLastName             = sharedPreferences.getString(Constants.Settings.FACEBOOK_LAST_NAME.name(), userLastName);
		userId                   = sharedPreferences.getString(Constants.Settings.USER_ID.name(), userId);
		image                    = sharedPreferences.getString(Constants.Settings.USER_IMAGE.name(), image);
		session                  = sharedPreferences.getBoolean(Constants.Settings.IS_SESSION_AVAILABLE.name(), session);
		senderIdChat             = sharedPreferences.getString(Constants.Settings.SENDER_ID_CHAT.name(), senderIdChat);
		//isAppForeground          = sharedPreferences.getBoolean(Constants.Settings.APP_FOREGROUND.name(), isAppForeground);
		isAlertForSKIPatrol      = sharedPreferences.getBoolean(Constants.Settings.ALERT_SKI_PATROL.name(), isAlertForSKIPatrol);
		loginType                = sharedPreferences.getString(Constants.Settings.LOGIN_TYPE.name(), loginType);
	}
	
	public void setUserInfo(String fname, String lname, String id, String img){
		this.userFirstName       = fname;
		this.userLastName        = lname;
		this.userId              = id;
		this.image               = img;
		Editor edit              = sharedPreferences.edit();
		edit.putString(Constants.Settings.FACEBOOK_FIRST_NAME.name(), userFirstName);
		edit.putString(Constants.Settings.FACEBOOK_LAST_NAME.name(), userLastName);
		edit.putString(Constants.Settings.USER_ID.name(), userId);
		edit.putString(Constants.Settings.USER_IMAGE.name(), image);
		edit.commit();
	}
	
	public void setImage(String img){
		image                    = img;
		Editor edit              = sharedPreferences.edit();
		edit.putString(Constants.Settings.USER_IMAGE.name(), image);
		edit.commit();
	}
	
	public void setSession(boolean flg){
		session                  = flg;
		Editor edit              = sharedPreferences.edit();
		edit.putBoolean(Constants.Settings.IS_SESSION_AVAILABLE.name(), session);
		edit.commit();
	}
	
	public void setSenderIDChat(String  id){
		senderIdChat             = id;
		Editor edit              = sharedPreferences.edit();
		edit.putString(Constants.Settings.SENDER_ID_CHAT.name(), senderIdChat);
		edit.commit();
	}
	
	/*public void setIsAppForgroung(boolean flg){
		isAppForeground          = flg;
		Editor edit              = sharedPreferences.edit();
		edit.putBoolean(Constants.Settings.APP_FOREGROUND.name(), isAppForeground);
		edit.commit();
	}*/
	
	public void setIsAlertForSKIPatrol(boolean flg){
		isAlertForSKIPatrol      = flg;
		Editor edit              = sharedPreferences.edit();
		edit.putBoolean(Constants.Settings.ALERT_SKI_PATROL.name(), isAlertForSKIPatrol);
		edit.commit();
	}
	
	public void setLoginType(String type){
		loginType                = type;
		Editor edit              = sharedPreferences.edit();
		edit.putString(Constants.Settings.LOGIN_TYPE.name(), loginType);
		edit.commit();
	}
	
	
}
