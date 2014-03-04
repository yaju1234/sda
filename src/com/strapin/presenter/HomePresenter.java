package com.strapin.presenter;

import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import snowmada.main.view.HomeView;
import snowmada.main.view.R;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.strapin.Util.ImageLoader;
import com.strapin.Util.Utility;
import com.strapin.adapter.ChatAdapter;
import com.strapin.adapter.FriendAdapter;
import com.strapin.adapter.FriendRequestAdapter;
import com.strapin.bean.ChatBean;
import com.strapin.bean.FriendListBean;
import com.strapin.bean.FriendRequestBean;
import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.global.Global;
import com.strapin.network.KlHttpClient;

public class HomePresenter implements IHome.Presenter{
	private HomeView mHomeView;
	private ArrayList<FriendListBean> mFriendArr = new ArrayList<FriendListBean>();
	private FriendAdapter mAdapter;
	private ProgressDialog mDialog;
	private Double mLat;
	private Double mLng;
	public  String trackingpersionname;
	private Marker marker;
	private boolean TrackDurationControllFlag = false;
	private  Handler handler = new Handler();
	private  Runnable runnable;
	private  long lastUsed;
	private  long idle=0;
	private ChatAdapter mChatAdapter;
	private FriendRequestAdapter mRequestAdapter;
	private int COUNT = 0;
	public int deletedPos = -1;
	public boolean isTracking = true;
	
	private ArrayList<FriendRequestBean> mRequestArr = new ArrayList<FriendRequestBean>();
	
	
	public HomePresenter(HomeView mHomeView){
		this.mHomeView = mHomeView;
		callAdapter();
		
	}

	@Override
	public void callAdapter() {
		if(Utility.isNetworkConnected(mHomeView)){
			new GetFriendListWeb().execute();
		}
	}
	
	public void trackSKIPatrol(String id,String fname, String lname){	
			
				TrackDurationControllFlag = true;
				mHomeView.hideSlide().setVisibility(View.GONE);
				mHomeView.myApp.friendId = id;
				trackingpersionname = fname+" "+lname;
				Global.isZoom = true;
				COUNT = 0;
				isTracking = false;
				if(marker!=null){
					marker.remove();
				}
				mHomeView.getProgressBarLayout().setVisibility(View.VISIBLE);
				handler.removeCallbacks(runnable);
				mHomeView.myApp.doTrackFriendLocation = true;
				
		
	}

	@Override
	public void setOnFriendClick(final int pos) {
		mHomeView.hideSlide().setVisibility(View.GONE);
		final Dialog dialog = new Dialog(mHomeView);				
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.track_dialog);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);		
		TextView friend_name = (TextView)dialog.findViewById(R.id.tv_friend_full_name);
		ImageView friend_image = (ImageView)dialog.findViewById(R.id.iv_friend_profile_img);		
		Button chat_with = (Button)dialog.findViewById(R.id.btn_chat_with_friend);
		chat_with.setText(Html.fromHtml("<font color=\"#ffffff\">CH</font><font color=\"#28b6ff\">AT</font>"));
		Button profile = (Button)dialog.findViewById(R.id.btn_view_friend_frofile);
		profile.setText(Html.fromHtml("<font color=\"#ffffff\">PROF</font><font color=\"#28b6ff\">ILE</font>"));
		Button track_friend = (Button)dialog.findViewById(R.id.btn_track_friend_location);
		track_friend.setText(Html.fromHtml("<font color=\"#ffffff\">TRA</font><font color=\"#28b6ff\">CK</font>"));
		Button delete = (Button)dialog.findViewById(R.id.btn_delete_friend);
		delete.setText(Html.fromHtml("<font color=\"#ffffff\">DEL</font><font color=\"#28b6ff\">ETE</font>"));
		mHomeView.imageLoader.DisplayImage("https://graph.facebook.com/"+mFriendArr.get(pos).getFbId()+"/picture",friend_image);
		TextView online_status = (TextView)dialog.findViewById(R.id.tv_friend_online_status);
		if(mFriendArr.get(pos).getOnlineStatus().equalsIgnoreCase("1")){
			online_status.setText("Online");
			online_status.setTextColor(Color.parseColor("#0be423"));
		}else{
			online_status.setText("Offline");
			online_status.setTextColor(Color.parseColor("#FF0000"));
		}
		
		friend_name.setText(mFriendArr.get(pos).getName());
		dialog.show();		
		chat_with.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
				String friendName = mFriendArr.get(pos).getName();
				String fbid = mFriendArr.get(pos).getFbId();
				mHomeView.getChatWindowActive(friendName,fbid);
			}
		});		
		profile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();				
			}
		});		
		track_friend.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(isTracking){
					isTracking = false;
					if(mFriendArr.get(pos).getOnlineStatus().equalsIgnoreCase("1")){	
						TrackDurationControllFlag = true;
						mHomeView.hideSlide().setVisibility(View.GONE);
						trackingpersionname = mFriendArr.get(pos).getName();
						mHomeView.myApp.friendId = mFriendArr.get(pos).getFbId();
						Global.isZoom = true;
						if(Utility.isNetworkConnected(mHomeView)){
							new SendTrackNotification().execute();
							mHomeView.myApp.doTrackFriendLocation = true;
							mHomeView.getProgressBarLayout().setVisibility(View.VISIBLE);
							COUNT = 0;
						}
						
					}else{
						final Dialog dialog1 = new Dialog(mHomeView);				
						dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
						dialog1.setContentView(R.layout.track_fail_dialog);
						dialog1.setCancelable(false);
						Button ok = (Button)dialog1.findViewById(R.id.iv_ok);
						ok.setText(Html.fromHtml("<font color=\"#ffffff\">O</font><font color=\"#28b6ff\">K</font>"));
						TextView tv_alert_txt = (TextView)dialog1.findViewById(R.id.tv_alert_text);
						tv_alert_txt.setText(Html.fromHtml("<font color=\"#ffffff\">ALERT</font>&nbsp;&nbsp;<font color=\"#28b6ff\">DIALOG</font>"));
						ok.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								isTracking = true;
								dialog1.dismiss();							
							}
						});
						dialog1.show();
					}	
					dialog.cancel();
				}else{
					Toast.makeText(mHomeView, "Please wait... tracing continue", Toast.LENGTH_LONG).show();
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
		if(mHomeView.myApp.isNetworkConnected(mHomeView)){
			new LocationTrack().execute();
		}		
	}
	
	public void HandleTrackPeriod() {
		lastUsed = System.currentTimeMillis();
		idle = 0;
		runnable = new Runnable() {
			public void run() {
				handler.postDelayed(runnable, 3000);
				idle = System.currentTimeMillis() - lastUsed;
				Log.i("idle", "" + idle);
				if (idle >= 60000) {
					mHomeView.myApp.doTrackFriendLocation = false;
					COUNT = 0;
					isTracking = true;
					if(marker!=null){
						marker.remove();
					}					
					handler.removeCallbacks(runnable);					
					}
			}
		};
		handler.postDelayed(runnable, 1000);
		
	}

	

	@Override
	public void findListPosition(String sataus, String fbid) {
		int pos = -1;		
		for(int i=0; i<mFriendArr.size(); i++){
			if(mFriendArr.get(i).getFbId().equalsIgnoreCase(fbid)){
				pos = i;
				break;
			}
		}
		Log.e("position", ""+pos);		
		updateItemAtPosition(pos, sataus);
	}
	
	private void updateItemAtPosition(int position, String status) {
		mFriendArr.get(position).setOnlineStatus(status);
		mAdapter = new FriendAdapter(mHomeView, R.layout.friend_row1, mFriendArr);
		mHomeView.getList().setAdapter(mAdapter);
		//mHomeView.getList().setOnTouchListener(mTouchListener);
		}
	
	
public class SKIEmergencyButtonPressWeb extends AsyncTask<String, Void, Boolean>{

		
		protected void onPreExecute() {
			mDialog = new ProgressDialog(mHomeView);
			mDialog.setMessage("Please wait...");
			mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDialog.setIndeterminate(true);
			mDialog.setCancelable(false);
			mDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			
		  	try {
				JSONObject request = new JSONObject();
				request.put("fbid", params[0]);
				JSONObject response = KlHttpClient.SendHttpPost(URL.SKI_PATROL.getUrl(), request);
				return response.getBoolean("status");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			mDialog.dismiss();
			if(result){
				
				final Dialog dialog1 = new Dialog(mHomeView);				
				dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog1.setContentView(R.layout.skipetrol_btn_press_dialog);
				dialog1.setCancelable(false);
				
				Button ok = (Button)dialog1.findViewById(R.id.iv_dlg_ok);
				ok.setText(Html.fromHtml("<font color=\"#ffffff\">O</font><font color=\"#28b6ff\">K</font>"));
				TextView tv_dialog = (TextView)dialog1.findViewById(R.id.tv_alert_dialog_text);
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


public class GetFriendListWeb extends AsyncTask<String, Void, Boolean>{		
	protected void onPreExecute() {
		mDialog = new ProgressDialog(mHomeView);
		mDialog.setMessage("Please wait...");
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(false);
		mDialog.show();	
		
	}
	@Override
	protected Boolean doInBackground(String... params) {
		
	  	try {
	  		boolean flag = false;
	  		JSONObject jsonObject = new JSONObject();
	  		jsonObject.put("fbid", mHomeView.myApp.getAppInfo().userId);
	  		JSONObject json = KlHttpClient.SendHttpPost(URL.FRIEND_LIST.getUrl(), jsonObject);
	  		flag = json.getBoolean("status");
	  		if(flag){
	  			mFriendArr.clear();
	  			JSONArray arr = json.getJSONArray("friends");
	  			for(int j=0; j< arr.length(); j++){
	  				JSONObject c = arr.getJSONObject(j);
	  				String name = c.getString("name");
	  				String fbid = c.getString("friend_facebook_id");
	  				String track = c.getString("track");
	  				String online = c.getString("online_status");
	  				String isuserexists = c.getString("isuserexists");
	  				mFriendArr.add(new FriendListBean(name, fbid, track,online,isuserexists));
	  				//
	  			} 
	  			return flag;
	   }else{
		   return flag;
	   }					
		} catch (Exception e) {
			mDialog.dismiss();
			e.printStackTrace();
			return false;
		}				
	}
	@Override
	protected void onPostExecute(Boolean status) {
		mDialog.dismiss();
		if(status){
			mAdapter = new FriendAdapter(mHomeView, R.layout.friend_row1, mFriendArr);			
			mHomeView.getList().setAdapter(mAdapter);
			
		}
	}	
}



public class LocationTrack extends AsyncTask<String, Void, Boolean>{		

	@Override
	protected Boolean doInBackground(String... params) {			
	  	try {
	  		JSONObject jsonObject = new JSONObject();
	  		jsonObject.put("user_id", mHomeView.myApp.friendId);
	  		
	  		JSONObject json = KlHttpClient.SendHttpPost(URL.GET_LOCATION.getUrl(), jsonObject);
	  		Log.e("Received friend location JSOn", json.toString());
	  		Double lat =  Double.valueOf(json.getString("lat"));
	  		mLat = lat;
	  		Double lng =  Double.valueOf(json.getString("lng"));
	  		mLng = lng;
			
		} catch (Exception e) {
			//prsDlg.dismiss();
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Boolean status) {
		
		COUNT++;
		if(COUNT>3){
			mHomeView.getProgressBarLayout().setVisibility(View.GONE);
		if(marker != null){
			marker.remove();
		}
		if(mHomeView.myApp.doTrackFriendLocation){
			marker = mHomeView.getMap().addMarker(new MarkerOptions().position(new LatLng(mLat, mLng)).title("Name:"+trackingpersionname/*+" "+new Date().getHours()+":"+new Date().getMinutes()+":"+new Date().getSeconds()+"Distance:"+distance+" meter"*/).snippet("Time:"+new Date().getHours()+":"+new Date().getMinutes()+":"+new Date().getSeconds()).snippet("Time:"+new Date().getHours()+":"+new Date().getMinutes()+":"+new Date().getSeconds())  .icon(BitmapDescriptorFactory.fromResource(R.drawable.friend)));
			marker.showInfoWindow();
			if(Global.isZoom){
				Global.isZoom = false;
				mHomeView.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLat, mLng), 16));
				
			}
		}
		
			
		if(TrackDurationControllFlag){
			TrackDurationControllFlag = false;
			HandleTrackPeriod();
		}
		}
		
			
	}	
}

public class SendTrackNotification extends AsyncTask<String, Void, Boolean>{		
	@Override
	protected Boolean doInBackground(String... params) {			
	  	try {
	  		JSONObject jsonObject = new JSONObject();
	  		jsonObject.put("fbid", mHomeView.myApp.getAppInfo().userId);
	  		jsonObject.put("friend_fb_id", mHomeView.myApp.friendId);	
	  		Log.e("SendTrackNotification", jsonObject.toString());
	  		JSONObject json = KlHttpClient.SendHttpPost(URL.SEND_MESSAGE.getUrl(), jsonObject);
			
		} catch (Exception e) {
			//prsDlg.dismiss();
			e.printStackTrace();
		}
		return null;
	}
	
}


@Override
public void doSkiPatrolFunction() {
	new SKIEmergencyButtonPressWeb().execute(mHomeView.myApp.getAppInfo().userId);
	
}

@Override
public void functionChat(String facebookid, String name) {
	mHomeView.getChatFriend().setText(name);
	mHomeView.hideSlide().setVisibility(View.GONE);
	String fname = name;
	String[] splitStr = fname.split("\\s+");
	mHomeView.myApp.getAppInfo().setSenderIDChat(facebookid);
	mHomeView.myApp.IMname = splitStr[0];
	new getChatHistory().execute(facebookid,splitStr[0]);
}
public void closeSlider(){
	mHomeView.hideSlide().setVisibility(View.GONE);
}
public class getChatHistory extends AsyncTask<String, Void, ArrayList<ChatBean>>{		
	protected void onPreExecute() {
	
	}
	@Override
	protected ArrayList<ChatBean> doInBackground(String... params) {			
	  	try {
	  		JSONObject jsonObject = new JSONObject();
	  		jsonObject.put("sender_fb_id", mHomeView.myApp.getAppInfo().userId);
	  		jsonObject.put("receiver_fb_id",params[0]);
	  		jsonObject.put("sender_name", mHomeView.myApp.getAppInfo().userFirstName);
	  		jsonObject.put("receiver_name", params[1]);
	  		
	  		JSONObject json = KlHttpClient.SendHttpPost(URL.CHAT_HISTORY.getUrl(), jsonObject);
	  		Log.e("Received Chat history", json.toString());
	  		if(json != null){
				Global.mChatArr.clear();
					JSONArray array = json.getJSONArray("history");
					for(int i = 0;i<array.length();i++){
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
		//prsDlg.dismiss();
		if(result != null){
			if(result.size()>0){
				mChatAdapter = new ChatAdapter(mHomeView, R.layout.chat_row, result);
				mHomeView.getChatListView().setAdapter(mChatAdapter);
				mHomeView.getChatListView().setSelection(Global.mChatArr.size()-1);
			}else{
				mChatAdapter = new ChatAdapter(mHomeView, R.layout.chat_row, result);
				mHomeView.getChatListView().setAdapter(mChatAdapter);
			}
		}		
	}	
}

public void ackAfterFriendRequest(int pos){
	mRequestArr.remove(pos);
	mRequestAdapter = new FriendRequestAdapter(mHomeView,R.layout.request_notification_row, mRequestArr);
	mHomeView.getRequestList().setAdapter(mRequestAdapter);	
}

public void getFriendRequest(){
	new GetAllFriendRequest().execute();
}

public class GetAllFriendRequest extends AsyncTask<String, Void, Boolean>{		
	protected void onPreExecute() {
		mDialog = new ProgressDialog(mHomeView);
		mDialog.setMessage("Please wait...");
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(false);
		mDialog.show();
	}		
	@Override
	protected Boolean doInBackground(String... params) {
		JSONObject json;
		boolean flag = false;
	  	try {
			JSONObject mJsonObject = new JSONObject();
			mJsonObject.put("fbid", mHomeView.myApp.getAppInfo().userId);
			json = KlHttpClient.SendHttpPost(URL.PENDING_FRIEND_REQUEST.getUrl(), mJsonObject);
			if(json!=null){
				flag = json.getBoolean("status");
				if(flag){
					JSONArray jarray = json.getJSONArray("friends");
					mRequestArr.clear();
					for(int i= 0; i<jarray.length(); i++){
						JSONObject obj = jarray.getJSONObject(i);
						String record_id = obj.getString("recordid");
						String sender_fbid = obj.getString("sender_facebook_profile_id");
						String sender_fname = obj.getString("first_name");
						String sender_lname = obj.getString("last_name");
						int trackstatus = Integer.parseInt(obj.getString("trackstatus"));
						String sender_name = sender_fname +" "+sender_lname;
						Log.e("name", sender_name);
					
						mRequestArr.add(new FriendRequestBean(sender_fbid, mHomeView.myApp.getAppInfo().userId, sender_name, record_id,trackstatus));
						
					}
				}else{
					return false;
				}
			}
	  	}catch (Exception e) {
			return false;
		}
		return flag;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {							
			mDialog.dismiss();		
			if(result){
				mRequestAdapter = new FriendRequestAdapter(mHomeView,R.layout.request_notification_row, mRequestArr);
				mHomeView.getRequestList().setAdapter(mRequestAdapter);	
			}							
		}
	}

public void updatePendingFriendList(String sender_id, String sender_name,
		String receiver_fbid, String record_id, int track_status) {
		mRequestArr.add(new FriendRequestBean(sender_id, mHomeView.myApp.getAppInfo().userId, sender_name, record_id,track_status));
		mRequestAdapter = new FriendRequestAdapter(mHomeView,R.layout.request_notification_row, mRequestArr);
		mHomeView.getRequestList().setAdapter(mRequestAdapter);	
	}

@Override
public void CallChatWindow(String friendName, String fbid) {
	mHomeView.getChatWindowActive(friendName,fbid);
	
}

public class DeleteFriendWeb extends AsyncTask<String, Void, Boolean>{		
	protected void onPreExecute() {
		mDialog = new ProgressDialog(mHomeView);
		mDialog.setMessage("Please wait...");
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(false);
		mDialog.show();
	}		
	@Override
	protected Boolean doInBackground(String... params) {
		JSONObject json;
		boolean flag = false;
	  	try {
			JSONObject mJsonObject = new JSONObject();
			mJsonObject.put("fbid", mHomeView.myApp.getAppInfo().userId);
			mJsonObject.put("friend_id", params[0]);
			json = KlHttpClient.SendHttpPost(URL.FRIEND_DELETE.getUrl(), mJsonObject);
			if(json!=null){
				flag = json.getBoolean("del_status");
				
			}
	  	}catch (Exception e) {
			return flag;
		}
		return flag;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {							
			mDialog.dismiss();		
			if(result){
				mFriendArr.remove(deletedPos);
				
				mAdapter = new FriendAdapter(mHomeView, R.layout.friend_row1, mFriendArr);				
				mHomeView.getList().setAdapter(mAdapter);
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
	Button btn_yes = (Button)deleteDlg.findViewById(R.id.btn_confirm_delete_yes);
	btn_yes.setText(Html.fromHtml("<font color=\"#ffffff\">YE</font><font color=\"#28b6ff\">S</font>"));
	Button btn_no = (Button)deleteDlg.findViewById(R.id.btn_confirm_delete_no);
	btn_no.setText(Html.fromHtml("<font color=\"#ffffff\">N</font><font color=\"#28b6ff\">O</font>"));
	btn_yes.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			deleteDlg.dismiss();
			String friend_id = mFriendArr.get(pos).getFbId();
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

}