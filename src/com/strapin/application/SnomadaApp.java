package com.strapin.application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class SnomadaApp extends Application {
	public AppInfo mAppUserInfo;
	public boolean inIt = false;
	public boolean isMeetuplocationEditTextEditable = false;
	public boolean isMeetuplocationWindoEnable = false;
	public boolean doTrackFriendLocation = false;
	

	public AppInfo getAppInfo() {
		return mAppUserInfo;
	}

	public void setAppInfo(AppInfo appInfo) {
		this.mAppUserInfo = appInfo;
	}

	 public  boolean isNetworkConnected(Context ctx){
		    ConnectivityManager connectivityManager 
		            = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    return activeNetworkInfo != null && activeNetworkInfo.isConnected();    
		}
	
}
