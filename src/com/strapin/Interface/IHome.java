package com.strapin.Interface;

import snowmada.main.view.Sliding;

import com.google.android.gms.maps.GoogleMap;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public interface IHome {
	public interface Presenter{
		public void callAdapter();
		public void setOnFriendClick(int pos);
		public void getFriendCurrentLocation();
		public void findListPosition(String sataus, String fbid);
		public void doSkiPatrolFunction();
		public void functionChat(String facebookid,String name);
		public void CallChatWindow(String friendName,String fbid);
		
	}
	public Activity getActivity();
	public Context getContext();
	public ListView getList();
	public GoogleMap getMap();
	public Sliding hideSlide();
	public TextView getChatFriend();
	public ListView getChatListView();
	public ListView getRequestList();
	public void init();
	public void defaultChatWindoOpenFromNotificationList();
	public void createMenuDialog();
	public void isSkyPetrolShow();
	public void getDeviceId();
	public void createRunnableThread();
	public void callService();
	public RelativeLayout getProgressBarLayout();

}
