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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
	private String mName;
	public ImageLoader imageLoader;
	private Marker marker;
	private SnowmadaDbAdapter mDbAdapter;
	private boolean TrackDurationControllFlag = false;
	private static Handler handler = new Handler();
	private static Runnable runnable;
	private static long lastUsed;
	private static long idle=0;
	private ChatAdapter mChatAdapter;
	private FriendRequestAdapter mRequestAdapter;
	private int COUNT = 0;
	
	private ArrayList<FriendRequestBean> mRequestArr = new ArrayList<FriendRequestBean>();
	
	
	public HomePresenter(HomeView mHomeView){
		this.mHomeView = mHomeView;
		mDbAdapter = SnowmadaDbAdapter.databaseHelperInstance(mHomeView.getActivity());
		imageLoader=new ImageLoader(mHomeView.getActivity());
		callAdapter();
		
	}

	@Override
	public void callAdapter() {
		if(Utility.isNetworkConnected(mHomeView.getContext())){
			new GetFriendListWeb().execute();
		}
	}
	
	

	@Override
	public void setOnFriendClick(final int pos) {
		final Dialog dialog = new Dialog(mHomeView.getActivity());				
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.track_dialog);
		
		TextView username = (TextView)dialog.findViewById(R.id.user_name);
		ImageView image = (ImageView)dialog.findViewById(R.id.img_friend);
		LinearLayout track = (LinearLayout)dialog.findViewById(R.id.track);
		LinearLayout chat = (LinearLayout)dialog.findViewById(R.id.chat_layout);
		LinearLayout profile = (LinearLayout)dialog.findViewById(R.id.profile);
		ImageView cancel = (ImageView)dialog.findViewById(R.id.cross);
		imageLoader.DisplayImage("https://graph.facebook.com/"+mFriendArr.get(pos).getFbId()+"/picture",image);
		dialog.show();
		username.setText(mFriendArr.get(pos).getName());
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();				
			}
		});
		
		chat.setOnClickListener(new OnClickListener() {
			
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
		
		track.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(mFriendArr.get(pos).getOnlineStatus().equalsIgnoreCase("1")){
					
					mHomeView.hideSlide().setVisibility(View.GONE);
					mName = mFriendArr.get(pos).getName();
					Global.sFriendName = mName;
					Global.sFriendId = mFriendArr.get(pos).getFbId();
					Global.isZoom = true;
					if(Utility.isNetworkConnected(mHomeView.getContext())){
						new PushNotificationWeb().execute(mDbAdapter.getUserFbID(),Global.sFriendId);
						//new getFriendLocation().execute(Global.sFriendId);
						mHomeView.app.doTrackFriendLocation = true;
						mHomeView.getProgressBarLayout().setVisibility(View.VISIBLE);
						COUNT = 0;
					}
					
				}else{
					
					//Toast.makeText(mHomeView.getContext(), "You can't Track the location of that friend", Toast.LENGTH_LONG).show();
					final Dialog dialog1 = new Dialog(mHomeView.getActivity());				
					dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
					dialog1.setContentView(R.layout.track_fail_dialog);
					dialog1.setCancelable(false);
					Button ok = (Button)dialog1.findViewById(R.id.iv_ok);
					ok.setText(Html.fromHtml("<font color=\"#ffffff\">O</font><font color=\"#28b6ff\">K</font>"));
					TextView tv_alert_txt = (TextView)dialog1.findViewById(R.id.tv_alert_text);
					tv_alert_txt.setText(Html.fromHtml("<font color=\"#ffffff\">ALERT</font><font color=\"#28b6ff\">DIALOG</font>"));
					ok.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialog1.dismiss();							
						}
					});
					dialog1.show();
				}	
				dialog.cancel();
			}
		});
		}
	
	
	@Override
	public void getFriendCurrentLocation() {
		if(Utility.isNetworkConnected(mHomeView.getContext())){
			new getFriendLocation1().execute(Global.sFriendId);
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

				if (idle >= /*30000*/5*60*1000) {
					mHomeView.app.doTrackFriendLocation = false;
					mHomeView.getMap().clear();	
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
		mAdapter = new FriendAdapter(HomePresenter.this,mHomeView, R.layout.friend_row1, mFriendArr);
		mHomeView.getList().setAdapter(mAdapter);
		//mHomeView.getList().setOnTouchListener(mTouchListener);
		}
	
	
public class SKIEmergencyButtonPressWeb extends AsyncTask<String, Void, Boolean>{

		
		protected void onPreExecute() {
			mDialog = new ProgressDialog(mHomeView.getActivity());
			mDialog.setMessage("Please wait...");
			mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDialog.setIndeterminate(true);
			mDialog.setCancelable(false);
			mDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			
		  	try {
				JSONObject mJsonObject = new JSONObject();
				mJsonObject.put("fbid", params[0]);
				
				Log.e("JSON", mJsonObject.toString());
				JSONObject json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/ski_patrol.php", mJsonObject);
				return json.getBoolean("status");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			mDialog.dismiss();
			if(result){
				
				final Dialog dialog1 = new Dialog(mHomeView.getActivity());				
				dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog1.setContentView(R.layout.skipetrol_btn_press_dialog);
				dialog1.setCancelable(false);
				
				Button ok = (Button)dialog1.findViewById(R.id.iv_dlg_ok);
				ok.setText(Html.fromHtml("<font color=\"#ffffff\">O</font><font color=\"#28b6ff\">K</font>"));
				TextView tv_dialog = (TextView)dialog1.findViewById(R.id.tv_alert_dialog_text);
				tv_dialog.setText(Html.fromHtml("<font color=\"#ffffff\">ALERT</font><font color=\"#28b6ff\">DIALOG</font>"));
				
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
		mDialog = new ProgressDialog(mHomeView.getContext());
		mDialog.setMessage("Please wait...");
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(false);
		mDialog.show();		
		Log.e("GetFriendListWeb", "GetFriendListWeb");
		
	}
	@Override
	protected Boolean doInBackground(String... params) {
		
	  	try {
	  		boolean flag = false;
	  		JSONObject jsonObject = new JSONObject();
	  		jsonObject.put("fbid", mDbAdapter.getUserFbID());
	  		
	  		Log.e("JSON", jsonObject.toString());
	  		JSONObject json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/snowmada_friend.php", jsonObject);
	  		Log.e("Friend list Response JSON", json.toString());
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
		Log.e("GetFriendListWeb onPostExecute", "GetFriendListWeb onPostExecute");
		if(status){
			mAdapter = new FriendAdapter(HomePresenter.this,mHomeView, R.layout.friend_row1, mFriendArr);
			
			mHomeView.getList().setAdapter(mAdapter);
			
		}				
	}	
}



public class getFriendLocation1 extends AsyncTask<String, Void, Boolean>{		
	protected void onPreExecute() {
	
	}
	@Override
	protected Boolean doInBackground(String... params) {			
	  	try {
	  		JSONObject jsonObject = new JSONObject();
	  		jsonObject.put("fbid", params[0]);
	  		
	  		JSONObject json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/getlocation.php", jsonObject);
	  		Log.e("Received friend location JSOn", json.toString());
	  		Double lat =  Double.valueOf(json.getString("lat"));
	  		mLat = lat;
	  		Double lng =  Double.valueOf(json.getString("lng"));
	  		mLng = lng;
			
		} catch (Exception e) {
			//mDialog.dismiss();
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Boolean status) {
		//Toast.makeText(mHomeView, ""+COUNT, Toast.LENGTH_LONG).show();
		
		COUNT++;
		if(COUNT>3){
			mHomeView.getProgressBarLayout().setVisibility(View.GONE);
			mHomeView.getMap().clear();				
		marker = mHomeView.getMap().addMarker(new MarkerOptions().position(new LatLng(mLat, mLng)).title("Name:"+mName/*+" "+new Date().getHours()+":"+new Date().getMinutes()+":"+new Date().getSeconds()+"Distance:"+distance+" meter"*/).snippet("Time:"+new Date().getHours()+":"+new Date().getMinutes()+":"+new Date().getSeconds()).snippet("Time:"+new Date().getHours()+":"+new Date().getMinutes()+":"+new Date().getSeconds())  .icon(BitmapDescriptorFactory.fromResource(R.drawable.friend)));
		if(Global.isZoom){
			Global.isZoom = false;
			mHomeView.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLat, mLng), 16));
		}
		if(Global.isInfoWindow){
			marker.showInfoWindow();	
		}
		
		if(TrackDurationControllFlag){
			TrackDurationControllFlag = false;
			HandleTrackPeriod();
		}
		}
		
			
	}	
}

public class PushNotificationWeb extends AsyncTask<String, Void, Boolean>{		
	protected void onPreExecute() {
	
	}
	@Override
	protected Boolean doInBackground(String... params) {			
	  	try {
	  		JSONObject jsonObject = new JSONObject();
	  		jsonObject.put("fbid", params[0]);
	  		jsonObject.put("friend_fb_id", params[1]);	
	  		Log.e("PushNotificationWeb", jsonObject.toString());
	  		JSONObject json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/send_message.php", jsonObject);
			
		} catch (Exception e) {
			//mDialog.dismiss();
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Boolean status) {}	
}


@Override
public void doSkiPatrolFunction() {
	new SKIEmergencyButtonPressWeb().execute(mDbAdapter.getUserFbID());
	
}

@Override
public void functionChat(String facebookid, String name) {
	mHomeView.getChatFriend().setText(name);
	mHomeView.hideSlide().setVisibility(View.GONE);
	String fname = name;
	String[] splitStr = fname.split("\\s+");
	Global.mChatSenderID = facebookid;
	Global.mChatUserName = splitStr[0];
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
	  		jsonObject.put("sender_fb_id", mDbAdapter.getUserFbID());
	  		jsonObject.put("receiver_fb_id",params[0]);
	  		jsonObject.put("sender_name", mDbAdapter.getUserFirstName());
	  		jsonObject.put("receiver_name", params[1]);
	  		
	  		JSONObject json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/chat_history.php", jsonObject);
	  		Log.e("Received friend location JSOn", json.toString());
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
		//mDialog.dismiss();
		if(result != null){
			if(result.size()>0){
				mChatAdapter = new ChatAdapter(mHomeView.getContext(), R.layout.chat_row, result);
				mHomeView.getChatListView().setAdapter(mChatAdapter);
				mHomeView.getChatListView().setSelection(Global.mChatArr.size()-1);
			}
		}		
	}	
}

public void ackAfterFriendRequest(int pos){
	mRequestArr.remove(pos);
	mRequestAdapter = new FriendRequestAdapter(HomePresenter.this,mHomeView.getActivity(),mHomeView.getActivity(),R.layout.request_notification_row, mRequestArr);
	mHomeView.getRequestList().setAdapter(mRequestAdapter);	
}

public void getFriendRequest(){
	new GetAllFriendRequest().execute();
}

public class GetAllFriendRequest extends AsyncTask<String, Void, Boolean>{		
	protected void onPreExecute() {
		mDialog = new ProgressDialog(mHomeView.getActivity());
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
			mJsonObject.put("fbid", mDbAdapter.getUserFbID());
			json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/pending_friend.php", mJsonObject);
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
						String trackstatus = obj.getString("trackstatus");
						String sender_name = sender_fname +" "+sender_lname;
						Log.e("name", sender_name);
					
						mRequestArr.add(new FriendRequestBean(sender_fbid, mDbAdapter.getUserFbID(), sender_name, record_id,trackstatus));
						
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
				mRequestAdapter = new FriendRequestAdapter(HomePresenter.this,mHomeView.getActivity(),mHomeView.getActivity(),R.layout.request_notification_row, mRequestArr);
				mHomeView.getRequestList().setAdapter(mRequestAdapter);	
			}							
		}
	}

public void updatePendingFriendList(String sender_id, String sender_name,
		String receiver_fbid, String record_id, String track_status) {
		mRequestArr.add(new FriendRequestBean(sender_id, mDbAdapter.getUserFbID(), sender_name, record_id,track_status));
		mRequestAdapter = new FriendRequestAdapter(HomePresenter.this,mHomeView.getActivity(),mHomeView.getActivity(),R.layout.request_notification_row, mRequestArr);
		mHomeView.getRequestList().setAdapter(mRequestAdapter);	
	}

@Override
public void CallChatWindow(String friendName, String fbid) {
	mHomeView.getChatWindowActive(friendName,fbid);
	
}
}