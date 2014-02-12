package com.strapin.application;

import android.app.Application;

public class SnomadaApp extends Application {
	public AppInfo mAppUserInfo;
	public boolean inIt = false;
	

	public AppInfo getAppInfo() {
		return mAppUserInfo;
	}

	public void setAppInfo(AppInfo appInfo) {
		this.mAppUserInfo = appInfo;
	}

	
	
}
