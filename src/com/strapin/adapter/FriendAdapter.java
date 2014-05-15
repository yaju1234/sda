package com.strapin.adapter;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

import snowmada.main.view.HomeView;
import snowmada.main.view.R;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strapin.Enum.URL;
import com.strapin.bean.FriendListBean;
import com.strapin.global.Global;
import com.strapin.network.KlHttpClient;

public class FriendAdapter extends ArrayAdapter<FriendListBean>{
	
	private ArrayList<FriendListBean> mItems = new ArrayList<FriendListBean>();
	private ViewHolder mHolder;
	private HomeView activity;
	private String changeTrackStatus;
	private int pos;
	public String TAG = "snomada";
	
	
	public FriendAdapter(HomeView activity, int textViewResourceId,	ArrayList<FriendListBean> items) {
		super(activity, textViewResourceId, items);
		this.mItems = items;
		this.activity =activity;		
	}		  
	
	@Override
	public View getView( final int position,  View convertView, ViewGroup parent) {
		View v = convertView;		  
		if (v == null) {			
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.friend_row1, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);	
			mHolder.mImage = (ImageView)v.findViewById(R.id.user_image);
			mHolder.mName = (TextView)v.findViewById(R.id.user_name);
			mHolder.ll_main_row = (LinearLayout)v.findViewById(R.id.ll_main_layout);
			mHolder.lbl_online_status = (TextView)v.findViewById(R.id.on_line_status);
			mHolder.mTrackStatus = (CheckBox)v.findViewById(R.id.checkBox1);
			mHolder.mAllowText = (TextView)v.findViewById(R.id.allow_text);
				
		}
		else {
			
			mHolder =  (ViewHolder) v.getTag();
		}
	
		mHolder.ll_main_row.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {					
					String fname = mItems.get(position).getName();
					// When chating 
					if(activity.myApp.isChatActive){
						activity.myApp.getAppInfo().setSenderIDChat( mItems.get(position).getFbId());
							String[] splitStr = fname.split("\\s+");
							activity.myApp.IMname = splitStr[0];
							Global.mChatArr.clear();
							boolean status = mItems.get(position).isOnline();
							activity.presenter.functionChat(mItems.get(position).getFbId(), mItems.get(position).getName(),status,mItems.get(position).getImage());
						
					}else{// When Tracking  
						activity.myApp.isTrackingSKIPatrol = false;
						activity.presenter.setOnFriendClick(position);
					}				
			}
		});
		
		mHolder.mTrackStatus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mItems.get(position).getStatus().equalsIgnoreCase("1")){
				pos = position;
					new UpdateTrackStatus().execute(activity.myApp.getAppInfo().userId/*""+mDbAdapter.getUserFbID()*/,mItems.get(position).getFbId(),"0");
				}else{
					pos = position;
					new UpdateTrackStatus().execute(activity.myApp.getAppInfo().userId,mItems.get(position).getFbId(),"1");
					
				}
				
			}
			
		});
		
		if(position%2 == 0){
		    mHolder.ll_main_row.setBackgroundColor(Color.parseColor("#16181a"));
		}else{
		    mHolder.ll_main_row.setBackgroundColor(Color.parseColor("#1a1c1f"));
		}
	
			
		final FriendListBean bean = mItems.get(position);
		if(bean != null){
			
			if(bean.getISUserExist().equalsIgnoreCase("1")){
				mHolder.mAllowText.setVisibility(View.VISIBLE);
				mHolder.mTrackStatus.setVisibility(View.VISIBLE);
			}else{
				mHolder.mAllowText.setVisibility(View.INVISIBLE);
				mHolder.mTrackStatus.setVisibility(View.INVISIBLE);
			}
			Log.e(TAG, "Image===>>>"+bean.getImage());
			activity.imageLoader.DisplayImage(bean.getImage(),mHolder.mImage);
			mHolder.mName.setText(bean.getName());
			if(bean.isOnline()){
				mHolder.lbl_online_status.setText("Online");
				mHolder.lbl_online_status.setTextColor(Color.parseColor("#0be423"));
			}else{
				mHolder.lbl_online_status.setText("Offline");
				mHolder.lbl_online_status.setTextColor(Color.parseColor("#ff0000"));				
			}			
			if(bean.getStatus().equalsIgnoreCase("1")){
				mHolder.mTrackStatus.setChecked(true);
				activity.myApp.getAppInfo().setSenderIDChat(bean.getFbId());
				
			}else{
				mHolder.mTrackStatus.setChecked(false);
			}
		}	
		return v;
	}
	class ViewHolder {	
		public ImageView mImage;
		public TextView mName;
		public LinearLayout ll_main_row;
		public TextView lbl_online_status;
		public CheckBox mTrackStatus;
		public TextView mAllowText;		
	}
public class UpdateTrackStatus extends AsyncTask<String, Void, Boolean>{
		
		protected void onPreExecute() {
			activity.showProgressDailog();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			
		  	try {
				JSONObject mJsonObject = new JSONObject();
				mJsonObject.put("fbid", params[0]);
				mJsonObject.put("friend_fb_id", params[1]);
				mJsonObject.put("track_status", params[2]);
				changeTrackStatus = params[2];
				JSONObject json = KlHttpClient.SendHttpPost(URL.STATUS_TRACK_TOOGLE.getUrl(), mJsonObject);
				if(json!=null){
					return json.getBoolean("status");
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				activity.dismissProgressDialog();
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			activity.dismissProgressDialog();
			if(result){
				mItems.get(pos).setStatus(changeTrackStatus);
				notifyDataSetChanged();
			}
		}
	}
}
