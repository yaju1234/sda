package com.strapin.application;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.strapin.global.Constant;

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
	public boolean isMeetUpInstruction   = false;
	public boolean isTrackInstruction   = false;
	public boolean isChatInstruction   = false;
	public SharedPreferences sharedPreferences;
	
	public AppInfo(Context ctx){
		
		sharedPreferences        = ctx.getSharedPreferences(Constant.Settings.GLOBAL_SETTINGS.name(), Context.MODE_PRIVATE);
		userFirstName            = sharedPreferences.getString(Constant.Settings.FACEBOOK_FIRST_NAME.name(), userFirstName);
		userLastName             = sharedPreferences.getString(Constant.Settings.FACEBOOK_LAST_NAME.name(), userLastName);
		userId                   = sharedPreferences.getString(Constant.Settings.USER_ID.name(), userId);
		image                    = sharedPreferences.getString(Constant.Settings.USER_IMAGE.name(), image);
		session                  = sharedPreferences.getBoolean(Constant.Settings.IS_SESSION_AVAILABLE.name(), session);
		senderIdChat             = sharedPreferences.getString(Constant.Settings.SENDER_ID_CHAT.name(), senderIdChat);
		//isAppForeground          = sharedPreferences.getBoolean(Constant.Settings.APP_FOREGROUND.name(), isAppForeground);
		isAlertForSKIPatrol      = sharedPreferences.getBoolean(Constant.Settings.ALERT_SKI_PATROL.name(), isAlertForSKIPatrol);
		loginType                = sharedPreferences.getString(Constant.Settings.LOGIN_TYPE.name(), loginType);
		isMeetUpInstruction      = sharedPreferences.getBoolean(Constant.Settings.MEET_UP_INSTRUCTION.name(), isMeetUpInstruction);
		isTrackInstruction       = sharedPreferences.getBoolean(Constant.Settings.TRACK_INSTRUCTION.name(), isTrackInstruction);
		isChatInstruction        = sharedPreferences.getBoolean(Constant.Settings.CHAT_INSTRUCTION.name(), isChatInstruction);
	}
	
	public void setUserInfo(String fname, String lname, String id, String img){
		this.userFirstName       = fname;
		this.userLastName        = lname;
		this.userId              = id;
		this.image               = img;
		Editor edit              = sharedPreferences.edit();
		edit.putString(Constant.Settings.FACEBOOK_FIRST_NAME.name(), userFirstName);
		edit.putString(Constant.Settings.FACEBOOK_LAST_NAME.name(), userLastName);
		edit.putString(Constant.Settings.USER_ID.name(), userId);
		edit.putString(Constant.Settings.USER_IMAGE.name(), image);
		edit.commit();
	}
	
	public void setFirstname(String str){
	        userFirstName                    = str;
		Editor edit              = sharedPreferences.edit();
		edit.putString(Constant.Settings.FACEBOOK_FIRST_NAME.name(), userFirstName);
		edit.commit();
	}
	
	public void setLasttname(String str){
	        userLastName                    = str;
		Editor edit              = sharedPreferences.edit();
		edit.putString(Constant.Settings.FACEBOOK_LAST_NAME.name(), userLastName);
		edit.commit();
	}
	
	
	
	public void setImage(String img){
		image                    = img;
		Editor edit              = sharedPreferences.edit();
		edit.putString(Constant.Settings.USER_IMAGE.name(), image);
		edit.commit();
	}
	
	public void setSession(boolean flg){
		session                  = flg;
		Editor edit              = sharedPreferences.edit();
		edit.putBoolean(Constant.Settings.IS_SESSION_AVAILABLE.name(), session);
		edit.commit();
	}
	
	public void setSenderIDChat(String  id){
		senderIdChat             = id;
		Editor edit              = sharedPreferences.edit();
		edit.putString(Constant.Settings.SENDER_ID_CHAT.name(), senderIdChat);
		edit.commit();
	}
	
	/*public void setIsAppForgroung(boolean flg){
		isAppForeground          = flg;
		Editor edit              = sharedPreferences.edit();
		edit.putBoolean(Constant.Settings.APP_FOREGROUND.name(), isAppForeground);
		edit.commit();
	}*/
	
	public void setIsAlertForSKIPatrol(boolean flg){
		isAlertForSKIPatrol      = flg;
		Editor edit              = sharedPreferences.edit();
		edit.putBoolean(Constant.Settings.ALERT_SKI_PATROL.name(), isAlertForSKIPatrol);
		edit.commit();
	}
	
	public void setLoginType(String type){
		loginType                = type;
		Editor edit              = sharedPreferences.edit();
		edit.putString(Constant.Settings.LOGIN_TYPE.name(), loginType);
		edit.commit();
	}
	
	public void setMeetUpInstructionStatus(Boolean status){
	        isMeetUpInstruction      = status;
		Editor edit              = sharedPreferences.edit();
		edit.putBoolean(Constant.Settings.MEET_UP_INSTRUCTION.name(), isMeetUpInstruction);
		edit.commit();
	}
	public void setTrackInstructionStatus(Boolean status){
	        isTrackInstruction     = status;
		Editor edit              = sharedPreferences.edit();
		edit.putBoolean(Constant.Settings.TRACK_INSTRUCTION.name(), isTrackInstruction);
		edit.commit();
	}
	
	public void setChatInstructionStatus(Boolean status){
	        isChatInstruction     = status;
		Editor edit              = sharedPreferences.edit();
		edit.putBoolean(Constant.Settings.CHAT_INSTRUCTION.name(), isChatInstruction);
		edit.commit();
	}
	
	
}
