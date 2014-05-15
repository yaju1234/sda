package com.strapin.presenter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import snowmada.main.view.HomeView;
import snowmada.main.view.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.strapin.Enum.URL;
import com.strapin.Interface.IHome;
import com.strapin.Util.Utility;
import com.strapin.adapter.ChatAdapter;
import com.strapin.adapter.FriendAdapter;
import com.strapin.adapter.FriendRequestAdapter;
import com.strapin.adapter.GalleryAdapter;
import com.strapin.adapter.ProfileDealsAdapter;
import com.strapin.bean.ChatBean;
import com.strapin.bean.CommentBean;
import com.strapin.bean.FriendListBean;
import com.strapin.bean.FriendRequestBean;
import com.strapin.bean.GoodDeals;
import com.strapin.bean.ImageBean;
import com.strapin.bean.ProfileDealsBean;
import com.strapin.global.Global;
import com.strapin.network.KlHttpClient;

public class HomePresenter implements IHome.Presenter {
	private HomeView mHomeView;
	public ArrayList<FriendListBean> friendlistArr   = new ArrayList<FriendListBean>();
	private FriendAdapter friendlistAdapter;
	private Double mLat;
	private Double mLng;
	public  String trackingpersionname;
	private Marker marker;
	public  boolean TrackDurationControllFlag        = false;
	private Handler handler                          = new Handler();
	private Runnable runnable;
	private long lastUsed;
	private long idle                                = 0;
	private ChatAdapter mChatAdapter;
	private FriendRequestAdapter mRequestAdapter;
	private ProfileDealsAdapter mProfileDealsAdapter;
	private GalleryAdapter mGalleryAdapter;
	public  static int COUNT                          = 0;
	public  int deletedPos                            = -1;
	public  boolean isTracking                        = true;
	public  boolean isFriendListFetched               = false;
	private String TAG                                = "snomada";
	public boolean isException                        = false;
	public boolean isEditButtonEnable                  = false;

	private ArrayList<FriendRequestBean> mRequestArr   = new ArrayList<FriendRequestBean>();
	private ArrayList<ProfileDealsBean> profileDealsArr= new ArrayList<ProfileDealsBean>();
	private ArrayList<ImageBean>        imageArr       = new ArrayList<ImageBean>();
	public ArrayList<GoodDeals> mDealsArr = new ArrayList<GoodDeals>();
	
	public String name;
	public String age;
	public String loc;
	public String image;
	public String fav_mountain;
	public String shred_style;
	public String about_me;

	public HomePresenter(HomeView mHomeView) {
		this.mHomeView = mHomeView;

	}

	@Override
	public void getFriendList() {
		if (Utility.isNetworkConnected(mHomeView)) {
			isFriendListFetched = false;
			new GetFriendListWeb().execute();
		}
	}

	/**
	 * Select a Friend from sliding friend list (Chat is not doing on that
	 * time). A dialog with open with 4 buttons 1. Track 2. View profile 3. Chat
	 * 4. Delete Friend
	 * 
	 */
	@Override
	public void setOnFriendClick(final int pos) {
	        mHomeView.slidingmenu.toggle();
		final Dialog dialog = new Dialog(mHomeView);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.track_dialog);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		TextView friend_name = (TextView) dialog.findViewById(R.id.tv_friend_full_name);
		ImageView friend_image = (ImageView) dialog.findViewById(R.id.iv_friend_profile_img);
		Button chat_with = (Button) dialog.findViewById(R.id.btn_chat_with_friend);
		chat_with.setText(Html.fromHtml("<font color=\"#ffffff\">CH</font><font color=\"#28b6ff\">AT</font>"));
		Button profile = (Button) dialog.findViewById(R.id.btn_view_friend_frofile);
		profile.setText(Html.fromHtml("<font color=\"#ffffff\">PROF</font><font color=\"#28b6ff\">ILE</font>"));
		Button track_friend = (Button) dialog.findViewById(R.id.btn_track_friend_location);
		track_friend.setText(Html.fromHtml("<font color=\"#ffffff\">TRA</font><font color=\"#28b6ff\">CK</font>"));
		Button delete = (Button) dialog.findViewById(R.id.btn_delete_friend);
		delete.setText(Html	.fromHtml("<font color=\"#ffffff\">DEL</font><font color=\"#28b6ff\">ETE</font>"));
		Log.d(TAG, TAG + "" + friendlistArr.size());
		mHomeView.imageLoader.DisplayImage(friendlistArr.get(pos).getImage()/*"https://graph.facebook.com/"+ friendlistArr.get(pos).getFbId()+ "/picture"*/, friend_image);
		TextView online_status = (TextView) dialog	.findViewById(R.id.tv_friend_online_status);
		if (friendlistArr.get(pos).isOnline()) {
			online_status.setText("Online");
			online_status.setTextColor(Color.parseColor("#0be423"));
		} else {
			online_status.setText("Offline");
			online_status.setTextColor(Color.parseColor("#FF0000"));
		}

		friend_name.setText(friendlistArr.get(pos).getName());
		dialog.show();
		// Chat with button for chat with the selected friend
		chat_with.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
				String friendName = friendlistArr.get(pos).getName();
				String fbid = friendlistArr.get(pos).getFbId();
				boolean status = Integer.parseInt(friendlistArr.get(pos).getStatus())==1?true:false;
				String image  = friendlistArr.get(pos).getImage();
				mHomeView.getChatWindowActive(friendName, fbid,status,image);
			}
		});
		// View profile button for view the friend profile
		profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
				mHomeView.setLayoutViewProfile();
				handleProfileView(friendlistArr.get(pos).getFbId(),false);
			}
		});
		// Track button for tracking the friend location . (Track will working
		// only when user/friend is online)
		track_friend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// First Check any tracing is continue or not
				if (isTracking) {
					isTracking = false;
					// Check the friend/user is online/offline
					if (friendlistArr.get(pos).isOnline()) {
						TrackDurationControllFlag = true;
						trackingpersionname = friendlistArr.get(pos).getName();
						mHomeView.myApp.friendId = friendlistArr.get(pos).getFbId();
						Global.isTrackedLocationZoomed = true;
						if (Utility.isNetworkConnected(mHomeView)) {
							new SendTrackNotification().execute();
							mHomeView.myApp.doTrackFriendLocation = true;
							mHomeView.getProgressBarLayout().setVisibility(	View.VISIBLE);
							COUNT = 0;
						}

					} else {
						final Dialog user_offline_dialog = new Dialog(mHomeView);
						user_offline_dialog	.requestWindowFeature(Window.FEATURE_NO_TITLE);
						user_offline_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
						user_offline_dialog	.setContentView(R.layout.track_fail_dialog);
						user_offline_dialog.setCancelable(false);
						Button ok = (Button) user_offline_dialog.findViewById(R.id.iv_ok);
						ok.setText(Html	.fromHtml("<font color=\"#ffffff\">O</font><font color=\"#28b6ff\">K</font>"));
						TextView tv_alert_txt = (TextView) user_offline_dialog.findViewById(R.id.tv_alert_text);
						tv_alert_txt.setText(Html.fromHtml("<font color=\"#ffffff\">ALERT</font>&nbsp;&nbsp;<font color=\"#28b6ff\">DIALOG</font>"));
						ok.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								isTracking = true;
								user_offline_dialog.dismiss();
							}
						});
						user_offline_dialog.show();
					}
					dialog.cancel();
				} else {
					Toast.makeText(mHomeView,	"Please wait..one tracking continue",Toast.LENGTH_LONG).show();
				}
			}

		});

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
				showConfirmDeleteDialog(pos);

			}
		});
	}

	@Override
	public void getFriendCurrentLocation() {

		//Toast.makeText(mHomeView, "COUNT == " + COUNT, 1000).show();
		if (mHomeView.myApp.isNetworkConnected(mHomeView)) {
			if (COUNT > 1) {
				new LocationTrack().execute(true);

			} else {
				
				mHomeView.getProgressBarLayout().setVisibility(View.VISIBLE);
				new LocationTrack().execute(false);
				COUNT++;
			}

		}
	}

	public void HandleTrackPeriod() {
		lastUsed = System.currentTimeMillis();
		idle = 0;
		runnable = new Runnable() {
			public void run() {

				handler.postDelayed(runnable, 100);
				idle = System.currentTimeMillis() - lastUsed;
				Log.i("idle", "" + idle);
				if (idle >= 30000) {
					mHomeView.myApp.doTrackFriendLocation = false;
					Log.e(TAG, TAG + "staus"+ mHomeView.myApp.doTrackFriendLocation);
					COUNT = 0;
					isTracking = true;
					if (marker != null) {
						marker.remove();
					}
					handler.removeCallbacks(runnable);
				}

			}
		};
		handler.postDelayed(runnable, 1000);

	}

	@Override
	public void findListPosition(boolean sataus, String fbid) {
		int pos = -1;
		for (int i = 0; i < friendlistArr.size(); i++) {
			if (friendlistArr.get(i).getFbId().equalsIgnoreCase(fbid)) {
				pos = i;
				break;
			}
		}
		updateItemAtPosition(pos, sataus);
	}

	private void updateItemAtPosition(int position, boolean isonline) {
		friendlistArr.get(position).setOnline(isonline);
		friendlistAdapter = new FriendAdapter(mHomeView, R.layout.friend_row1,	friendlistArr);
		mHomeView.lv_friend_list.setAdapter(friendlistAdapter);
	}

	public class SKIEmergencyButtonPressWeb extends	AsyncTask<String, Void, Boolean> {

		protected void onPreExecute() {
			mHomeView.showProgressDailog();
		}

		@Override
		protected Boolean doInBackground(String... params) {

			try {
				JSONObject request = new JSONObject();
				request.put("fbid", params[0]);
				JSONObject response = KlHttpClient.SendHttpPost(URL.SKI_PATROL.getUrl(), request);
				if(response!=null){
					return response.getBoolean("status");
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mHomeView.dismissProgressDialog();
			if (result) {

				final Dialog dialog1 = new Dialog(mHomeView);
				dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog1.getWindow().setBackgroundDrawable(	new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog1.setContentView(R.layout.skipetrol_btn_press_dialog);
				dialog1.setCancelable(false);

				Button ok = (Button) dialog1.findViewById(R.id.iv_dlg_ok);
				ok.setText(Html	.fromHtml("<font color=\"#ffffff\">O</font><font color=\"#28b6ff\">K</font>"));
				TextView tv_dialog = (TextView) dialog1	.findViewById(R.id.tv_alert_dialog_text);
				tv_dialog.setText(Html.fromHtml("<font color=\"#ffffff\">ALERT</font>&nbsp;&nbsp;<font color=\"#28b6ff\">DIALOG</font>"));

				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialog1.dismiss();
					}
				});

				dialog1.show();
			}
		}
	}

	public class GetFriendListWeb extends AsyncTask<String, Void, Boolean> {
		protected void onPreExecute() {
			 mHomeView.showProgressDailog();
			
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				boolean flag = false;
				String image;
				JSONObject request = new JSONObject();
				request.put("fbid", mHomeView.myApp.getAppInfo().userId);
				JSONObject response = KlHttpClient.SendHttpPost(URL.FRIEND_LIST.getUrl(), request);
				flag = response.getBoolean("status");
				if (flag) {
					friendlistArr.clear();
					JSONArray arr = response.getJSONArray("friends");
					for (int j = 0; j < arr.length(); j++) {
						JSONObject c = arr.getJSONObject(j);
						String name = c.getString("name");
						String fbid = c.getString("friend_facebook_id");
						String track = c.getString("track");
						boolean online = Integer.parseInt(c.getString("online_status")) == 1 ? true: false;
						String isuserexists = c.getString("isuserexists");
						if(!c.isNull("image")){
							image = URL.IMAGE_PATH.getUrl()+c.getString("image");		  					
		  				}else{
		  					image = "https://graph.facebook.com/" +fbid+ "/picture";
		  				}
						friendlistArr.add(new FriendListBean(name, fbid, track,	online, isuserexists,image));
						//
					}
					return flag;
				} else {
					return flag;
				}
			} catch (Exception e) {
				 mHomeView.dismissProgressDialog();
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean status) {
			 mHomeView.dismissProgressDialog();
			if (status) {
				isFriendListFetched = true;
				friendlistAdapter = new FriendAdapter(mHomeView,R.layout.friend_row1, friendlistArr);
				mHomeView.lv_friend_list.setAdapter(friendlistAdapter);
			}
		}
	}

	public class LocationTrack extends AsyncTask<Boolean, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Boolean... params) {
			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("user_id", mHomeView.myApp.friendId);
				JSONObject json = KlHttpClient.SendHttpPost(URL.GET_LOCATION.getUrl(), jsonObject);
				Log.e("Received friend location JSOn", json.toString());
				Double lat = Double.valueOf(json.getString("lat"));
				mLat = lat;
				Double lng = Double.valueOf(json.getString("lng"));
				mLng = lng;

			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}catch(NumberFormatException e){
				e.printStackTrace();
				isException = true;
				return false;
			}
			return params[0];
		}

		@Override
		protected void onPostExecute(Boolean status) {
			Log.e("SSSS","Zoom: "+Global.isTrackedLocationZoomed+ " Count "+ COUNT+  "  Status "+status);
			if (status) {
				mHomeView.getProgressBarLayout().setVisibility(View.GONE);
				if (marker != null) {
					marker.remove();
				}
				if (mHomeView.myApp.doTrackFriendLocation) {
					Log.d(TAG, TAG + "lat=" + mLat);
					Log.d(TAG, TAG + "lat=" + mLng);
					marker = mHomeView.getMap().addMarker(
							new MarkerOptions()
									.position(new LatLng(mLat, mLng))
									.title("Name:" + trackingpersionname)
									.snippet("Time:" + new Date().getHours()
													+ ":"
													+ new Date().getMinutes()
													+ ":"
													+ new Date().getSeconds())
									.snippet("Time:" + new Date().getHours()
													+ ":"
													+ new Date().getMinutes()
													+ ":"
													+ new Date().getSeconds())
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.friend)));
					marker.showInfoWindow();
					//Toast.makeText(mHomeView, ""+Global.isTrackedLocationZoomed, Toast.LENGTH_LONG).show();
					if (Global.isTrackedLocationZoomed) {
						mHomeView.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLat, mLng), 16));
						Global.isTrackedLocationZoomed = false;
					}
				}
				/**
				 * Call Timer for 1 min
				 */
				if (TrackDurationControllFlag) {
					Log.e("timer Contol flag=======>>>>>>>>>>>>>>>>>>>>>>>>",	"" + TrackDurationControllFlag);
					TrackDurationControllFlag = false;
					HandleTrackPeriod(); // Track 1 min duration control function
				}

			}else{
				if(COUNT > 3 && isException){
					isException = false;
					mHomeView.getProgressBarLayout().setVisibility(View.GONE);
					Toast.makeText(mHomeView, "Error locating friend", Toast.LENGTH_LONG).show();
					mHomeView.myApp.doTrackFriendLocation = false;
					Log.e(TAG, TAG + "staus"+ mHomeView.myApp.doTrackFriendLocation);
					COUNT = 0;
					isTracking = true;
				}
				
			}
		}
	}

	public class SendTrackNotification extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("fbid", mHomeView.myApp.getAppInfo().userId);
				jsonObject.put("friend_fb_id", mHomeView.myApp.friendId);
				Log.e("SendTrackNotification", jsonObject.toString());
				JSONObject json = KlHttpClient.SendHttpPost(URL.SEND_MESSAGE.getUrl(), jsonObject);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	@Override
	public void doSkiPatrolFunction() {
		if(mHomeView.myApp.isNetworkConnected(mHomeView)){
			new SKIEmergencyButtonPressWeb().execute(mHomeView.myApp.getAppInfo().userId);
		}
		

	}

	@Override
	public void functionChat(String facebookid, String name, boolean status, String image) {
	    System.out.println("!--- Image"+ image);
	    mHomeView.slidingmenu.toggle();
		mHomeView.getChatFriend().setText(name);
		if(status){
			mHomeView.getChatFriend().setCompoundDrawablesWithIntrinsicBounds(null, null, mHomeView.getResources().getDrawable(R.drawable.green_ball), null);
		}else{
			mHomeView.getChatFriend().setCompoundDrawablesWithIntrinsicBounds(null, null, mHomeView.getResources().getDrawable(R.drawable.red_ball), null);
		}
		String fname = name;
		String[] splitStr = fname.split("\\s+");
		mHomeView.myApp.getAppInfo().setSenderIDChat(facebookid);
		Global.iv_chat_avatar_img = image;
		mHomeView.myApp.IMname = splitStr[0];
		new getChatHistory().execute(facebookid, splitStr[0]);
	}

	

	public class getChatHistory extends	AsyncTask<String, Void, ArrayList<ChatBean>> {
		protected void onPreExecute() {

		}

		@Override
		protected ArrayList<ChatBean> doInBackground(String... params) {
			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("sender_fb_id",	 mHomeView.myApp.getAppInfo().userId);
				jsonObject.put("receiver_fb_id", params[0]);
				jsonObject.put("sender_name",    mHomeView.myApp.getAppInfo().userFirstName);
				jsonObject.put("receiver_name",  params[1]);

				JSONObject json = KlHttpClient.SendHttpPost(URL.CHAT_HISTORY.getUrl(), jsonObject);
				System.out.println("!-- Chat history "+json.toString());
				if (json != null) {
					Global.mChatArr.clear();
					JSONArray array = json.getJSONArray("history");
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						String sender_name = obj.getString("sender");
						String message = obj.getString("message");
						Global.mChatArr.add(new ChatBean(sender_name, message));
					}
					return Global.mChatArr;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<ChatBean> result) {
			// prsDlg.dismiss();
			if (result != null) {
				if (result.size() > 0) {
				    System.out.println("!-- Chat history if ");
					mChatAdapter = new ChatAdapter(mHomeView,R.layout.chat_row, result);
					mHomeView.getChatListView().setAdapter(mChatAdapter);
					mHomeView.getChatListView().setSelection(Global.mChatArr.size() - 1);
				} else {
				    System.out.println("!-- Chat history else");
					mChatAdapter = new ChatAdapter(mHomeView,R.layout.chat_row, result);
					mHomeView.getChatListView().setAdapter(mChatAdapter);
				}
			}
		}
	}

	public void ackAfterFriendRequest(int pos) {
		mRequestArr.remove(pos);
		mRequestAdapter = new FriendRequestAdapter(mHomeView,R.layout.request_notification_row, mRequestArr);
		mHomeView.getRequestList().setAdapter(mRequestAdapter);
	}

	public void getFriendRequest() {
		new GetAllFriendRequest().execute();
	}

	public class GetAllFriendRequest extends AsyncTask<String, Void, Boolean> {
		protected void onPreExecute() {
			mHomeView.showProgressDailog();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			JSONObject json;
			boolean flag = false;
			String image;
			try {
				JSONObject mJsonObject = new JSONObject();
				mJsonObject.put("fbid", mHomeView.myApp.getAppInfo().userId);
				json = KlHttpClient.SendHttpPost(URL.PENDING_FRIEND_REQUEST.getUrl(), mJsonObject);
				if (json != null) {
					flag = json.getBoolean("status");
					if (flag) {
						JSONArray jarray = json.getJSONArray("friends");
						mRequestArr.clear();
						for (int i = 0; i < jarray.length(); i++) {
							JSONObject obj      = jarray.getJSONObject(i);
							String record_id    = obj.getString("recordid");
							String sender_fbid  = obj.getString("sender_facebook_profile_id");
							String sender_fname = obj.getString("first_name");
							String sender_lname = obj.getString("last_name");
							int trackstatus     = Integer.parseInt(obj.getString("trackstatus"));
							String sender_name  = sender_fname + " "+ sender_lname;
							Log.e("name", sender_name);
							if(!obj.isNull("image")){
								image = URL.IMAGE_PATH.getUrl()+obj.getString("image");		  					
			  				}else{
			  					image = "https://graph.facebook.com/" +sender_fbid+ "/picture";
			  				}
							mRequestArr.add(new FriendRequestBean(sender_fbid,mHomeView.myApp.getAppInfo().userId,sender_name, record_id, trackstatus,image));

						}
					} else {
						return false;
					}
				}
			} catch (Exception e) {
				return false;
			}
			return flag;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mHomeView.dismissProgressDialog();
			if (result) {
				mRequestAdapter = new FriendRequestAdapter(mHomeView,R.layout.request_notification_row, mRequestArr);
				mHomeView.getRequestList().setAdapter(mRequestAdapter);
			}
		}
	}

	public void updatePendingFriendList(String sender_id, String sender_name,String receiver_fbid, String record_id, int track_status,String image) {
		mRequestArr.add(new FriendRequestBean(sender_id, mHomeView.myApp.getAppInfo().userId, sender_name, record_id, track_status,image));
		mRequestAdapter = new FriendRequestAdapter(mHomeView,R.layout.request_notification_row, mRequestArr);
		mHomeView.getRequestList().setAdapter(mRequestAdapter);
	}

	@Override
	public void CallChatWindow(String friendName, String fbid,boolean status, String image) {
		mHomeView.getChatWindowActive(friendName, fbid,status,image);

	}

	public class DeleteFriendWeb extends AsyncTask<String, Void, Boolean> {
		protected void onPreExecute() {
			mHomeView.showProgressDailog();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			JSONObject json;
			boolean flag = false;
			try {
				JSONObject mJsonObject = new JSONObject();
				mJsonObject.put("fbid", mHomeView.myApp.getAppInfo().userId);
				mJsonObject.put("friend_id", params[0]);
				json = KlHttpClient.SendHttpPost(URL.FRIEND_DELETE.getUrl(),mJsonObject);
				if (json != null) {
					flag = json.getBoolean("del_status");

				}
			} catch (Exception e) {
				mHomeView.dismissProgressDialog();
				return flag;
			}
			return flag;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mHomeView.dismissProgressDialog();
			if (result) {
				friendlistArr.remove(deletedPos);
				friendlistAdapter = new FriendAdapter(mHomeView,R.layout.friend_row1, friendlistArr);
				mHomeView.lv_friend_list.setAdapter(friendlistAdapter);
			}
		}
	}

	public void showConfirmDeleteDialog(final int pos) {
		final Dialog deleteDlg = new Dialog(mHomeView);
		deleteDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		deleteDlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		deleteDlg.setContentView(R.layout.confirm_delete_dlg);
		deleteDlg.setCancelable(true);
		deleteDlg.setCanceledOnTouchOutside(true);
		Button btn_yes = (Button) deleteDlg	.findViewById(R.id.btn_confirm_delete_yes);
		btn_yes.setText(Html.fromHtml("<font color=\"#ffffff\">YE</font><font color=\"#28b6ff\">S</font>"));
		Button btn_no = (Button) deleteDlg.findViewById(R.id.btn_confirm_delete_no);
		btn_no.setText(Html	.fromHtml("<font color=\"#ffffff\">N</font><font color=\"#28b6ff\">O</font>"));
		btn_yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteDlg.dismiss();
				String friend_id = friendlistArr.get(pos).getFbId();
				deletedPos = pos;
				new DeleteFriendWeb().execute(friend_id);
			}
		});

		btn_no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				deleteDlg.dismiss();
			}
		});
		deleteDlg.show();
	}
	
	public boolean getFriendStatus(String id){
		boolean status  = false;
		for(int i=0; i<friendlistArr.size(); i++){
			if(friendlistArr.get(i).getFbId().equalsIgnoreCase(id)){
				status = friendlistArr.get(i).isOnline();
				break;
			}
		}
		return status;
		
	}
	
	public String getFriendImage(String id){
		String image  = "";
		System.out.println("!-- id ");
		System.out.println("!-- id "+ id);
		System.out.println("!-- Size "+ friendlistArr.size());
		for(int i=0; i<friendlistArr.size(); i++){
			if(friendlistArr.get(i).getFbId().equalsIgnoreCase(id)){
				image = friendlistArr.get(i).getImage();
				break;
			}
		}
		return image;
		
	}
	
	public void handleProfileView(String userID , boolean isEnable){		
		isEditButtonEnable  = isEnable;	
		new ProfileWeb().execute(userID);
	}
	
	public class ProfileWeb extends AsyncTask<String, Void, Boolean> {
		/*String name;
		String age;
		String loc;
		String image;
		String fav_mountain;
		String shred_style;
		String about_me;*/
		protected void onPreExecute() {
			mHomeView.showProgressDailog();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			JSONObject json;
			boolean flag = true;
			try {
				JSONObject mJsonObject = new JSONObject();
				mJsonObject.put("fbid", params[0]);
				json = KlHttpClient.SendHttpPost(URL.PROFILE_WITH_COMMENTS.getUrl(),mJsonObject);
				Log.e("TAG13", json.toString());
				//json = new JSONObject(readXMLinString(mHomeView));
				if (json != null) {
					name     = json.getString("first_name")+" "+json.getString("last_name");
					age      = json.getString("age");
					image    = json.getString("image");
					loc      = json.getString("city");
					fav_mountain  = json.getString("favorite_mountain");
					shred_style   = json.getString("shred_style");
					about_me      = json.getString("about_me");
					JSONArray jArrDeals = json.getJSONArray("purchased_deals");
					profileDealsArr.clear();
					for(int i=0; i<jArrDeals.length(); i++){
						JSONObject c = jArrDeals.getJSONObject(i);
						String deals_name = c.getString("deals_name");
						String deals_id = c.getString("deals_id");
						String deals_image_link = c.getString("image");
						profileDealsArr.add(new ProfileDealsBean(deals_id, deals_name, deals_image_link));
					}
					
					JSONArray jArrImage = json.getJSONArray("gallery");
					imageArr.clear();
					
					Log.e(TAG,"Count =========>>>"+ jArrImage.length());
					for(int i=0; i<jArrImage.length(); i++){
					    ImageBean bean = new ImageBean();
						JSONObject c = jArrImage.getJSONObject(i);
						String image_id = c.getString("id");
						bean.setImageId(image_id);
						String image_link = URL.GALLERY_IMG_PATH.getUrl()+c.getString("image");
						Log.e("TAG2","image_link =========>>>"+ image_link);
						bean.setImageLink(image_link);
						JSONArray jArray = c.getJSONArray("comments");
						 ArrayList<CommentBean> commentArr = new ArrayList<CommentBean>();
						for (int i1 = 0; i1 < jArray.length(); i1++) {
						    JSONObject c1 = jArray.getJSONObject(i1);
						    String fname = c1.getString("first_name");
						    String lname = c1.getString("last_name");
						    String profile_pic = c1.getString("profile_picture");
						    String txt_commets = c1.getString("comment");
						    commentArr.add(new CommentBean(fname, lname,   profile_pic, txt_commets));
						    bean.setCommentArr(commentArr);
						}
						/*bean.setCommentArr(commentArr);*/
						imageArr.add(bean);
						
						
						
					}

				}
			} catch (Exception e) {
				mHomeView.dismissProgressDialog();
				return flag;
			}
			
			for(int j=0; j<imageArr.size(); j++){
			    Log.e("TAG1", "Image ==========>>+imageArr.size()  "+imageArr.get(j).getImageLink()+" "+imageArr.size());
			    /*for(int j1=0; j1<imageArr.get(j).getCommentArr().size(); j1++){
				 Log.e("TAG1", "Comments ===========>>"+imageArr.get(j).getCommentArr().get(j1).getComments());
			    }*/
			}
			//imageArr.add(new ImageBean(image_id, image_link));
			
			Global.imageArr = imageArr;
			return flag;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mHomeView.dismissProgressDialog();
			if (result) {
				mHomeView.tv_prof_name.setText(name);
				mHomeView.tv_prof_age.setText(age);
				mHomeView.tv_prof_loc.setText(loc);
				mHomeView.imageLoader.DisplayImage(URL.IMAGE_PATH.getUrl()+image, mHomeView.mProfileImage);
				
				mHomeView.tv_prof_fev_mountain.setText(fav_mountain);
				mHomeView.tv_prof_shred_mountain.setText(shred_style);
				mHomeView.tv_prof_about_me.setText(about_me);
				
				
				if(isEditButtonEnable){
					mHomeView.mProfileImage.setClickable(true);
					mHomeView.btnGalleryImageUpload.setVisibility(View.VISIBLE);
					mHomeView.ll_edit_button_layout.setVisibility(View.VISIBLE);
				}else{
					mHomeView.mProfileImage.setClickable(false);
					
					mHomeView.ll_icon_prof_loc_edit_save.setVisibility(View.GONE);
					mHomeView.ll_icon_prof_fav_mountain_edit_save.setVisibility(View.GONE);
					mHomeView.ll_icon_prof_about_me_edit_save.setVisibility(View.GONE);
					mHomeView.btnGalleryImageUpload.setVisibility(View.GONE);
					mHomeView.ll_edit_button_layout.setVisibility(View.GONE);
				}
				
			    mProfileDealsAdapter = new ProfileDealsAdapter(mHomeView, R.layout.row_deals, profileDealsArr);
			    mHomeView.fgv_prof_deals_gallery.setAdapter(mProfileDealsAdapter);
			    mHomeView.fgv_prof_deals_gallery.setSelection(profileDealsArr.size()/2);
			    mGalleryAdapter = new GalleryAdapter(mHomeView, R.layout.row_image_gallery, imageArr);
			    mHomeView.gv_image_gallery.setAdapter(mGalleryAdapter);	
			    
			}
		}
	}
	
	public void resetImageAdapter(ArrayList<ImageBean> images){
		  Log.e(TAG, "Gallery Size "+images.size());
		  mGalleryAdapter = new GalleryAdapter(mHomeView, R.layout.row_image_gallery, images);
		  mHomeView.gv_image_gallery.setAdapter(mGalleryAdapter);	
	}
	public static String readXMLinString(Context c) {
		try {
			InputStream is = c.getAssets().open("log1.txt");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String text = new String(buffer);

			return text;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	
	
	public class GoodDealsWeb extends AsyncTask<Boolean, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Boolean... params) {
		    boolean flag = false;
			try {			        
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("user_id", mHomeView.myApp.friendId);
				JSONObject json = KlHttpClient.SendHttpPost(URL.GOOD_DEALS.getUrl(), jsonObject);
				
				if(json!=null){
				    flag = json.getBoolean("status");
				    if(flag){
					JSONArray arr = json.getJSONArray("data");
					for(int i=0; i<arr.length(); i++){
					    JSONObject c = arr.getJSONObject(i);
					    long markerid = System.currentTimeMillis()+ new Random().nextInt(1000);
					    	String id = c.getString("id");
						String name = c.getString("name");
						String advt_name = c.getString("advt_name");
						String address = c.getString("address");
						Double lat = Double.valueOf(c.getString("lat"));					
						Double lng = Double.valueOf(c.getString("lng"));
						
						
						String url = "http://23.239.206.137/uploads/advertisements/banners/"+c.getString("advt_image");
						Bitmap bitmap; 
						    bitmap = getBitmapFromURL(url);
						
						String desc = c.getString("description");
						mDealsArr.add(new GoodDeals(""+markerid,id, name, advt_name, address, lat, lng, bitmap,desc)); 
					}
					
				    }
				}			
				

			} catch (JSONException e) {
				e.printStackTrace();
				return flag;
			}catch(NumberFormatException e){
				e.printStackTrace();
				return flag;
			}
			return flag;
		}

		@Override
		protected void onPostExecute(Boolean status) {
		    if(status){
			
			for(int i=0 ; i<mDealsArr.size(); i++){/*
			    marker =  mHomeView.getMap().addMarker(new MarkerOptions()
				.position(new LatLng(mDealsArr.get(i).getLat(),mDealsArr.get(i).getLng()))
				.title("Name:" + mDealsArr.get(i).getAdvtName())
				.snippet("Location:"+ mDealsArr.get(i).getAddress())
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
			    	mHomeView.markerIdHasMap.put(marker, mDealsArr.get(i).getMarkerId());
			        // marker.showInfoWindow();
			*/
			    /*Bitmap bitmap; 
			    bitmap = getBitmapFromURL(mDealsArr.get(i).getImage());*/
			    
			 
			      marker =  mHomeView.getMap().addMarker(new MarkerOptions()
				.position(new LatLng(mDealsArr.get(i).getLat(),mDealsArr.get(i).getLng()))
				.title("Name:" + mDealsArr.get(i).getAdvtName())
				.snippet("Price :"+ "100$\n"+ "Description : "+mDealsArr.get(i).getDescription()+"\n"+"BUY it now?")
				.icon(BitmapDescriptorFactory.fromBitmap(mDealsArr.get(i).getImage())));
			    	mHomeView.markerIdHasMap.put(marker, mDealsArr.get(i).getMarkerId()); 
			  
			
			}
			
		    }
		}
	}
	
	public static Bitmap getBitmapFromURL(String src) {
	    try {
	     //create instance of InputStream and pass URL
	     InputStream in = new java.net.URL(src).openStream();
	     //decode stream and initialize bitmap
	    return BitmapFactory.decodeStream(in);
	    } catch (Exception e) {
	      Log.e("Error", e.getMessage());
	     e.printStackTrace();
	     return null;
	    }
	}
	
}