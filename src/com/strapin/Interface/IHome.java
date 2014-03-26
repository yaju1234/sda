package com.strapin.Interface;

import snowmada.main.view.Sliding;

import com.google.android.gms.maps.GoogleMap;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public interface IHome {
	public interface Presenter{
		public void getFriendList();
		public void setOnFriendClick(int pos);
		public void getFriendCurrentLocation();
		public void findListPosition(boolean sataus, String fbid);
		public void doSkiPatrolFunction();
		public void functionChat(String facebookid,String name,boolean status, String image);
		public void CallChatWindow(String friendName,String fbid,boolean status,String image);
		
	}
	public GoogleMap getMap();
	public Sliding hideSlide();
	public TextView getChatFriend();
	public ListView getChatListView();
	public ListView getRequestList();
	public void init();
	public void defaultChatWindoOpenFromNotificationList();
	public void createMenuDialog();
	public void getPushNotificationDeviceID();
	public RelativeLayout getProgressBarLayout();

}
