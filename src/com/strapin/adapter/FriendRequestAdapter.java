package com.strapin.adapter;

import java.util.ArrayList;

import org.json.JSONObject;

import snowmada.main.view.HomeView;
import snowmada.main.view.R;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strapin.Enum.URL;
import com.strapin.Util.ImageLoader;
import com.strapin.bean.FriendRequestBean;
import com.strapin.global.Constant;
import com.strapin.network.KlHttpClient;

public class FriendRequestAdapter extends ArrayAdapter<FriendRequestBean>{
	private ViewHolder mHolder;
	private HomeView activity;
	private ImageLoader imageLoader;
	public ArrayList<FriendRequestBean> item = new ArrayList<FriendRequestBean>();
	private int pos;
	  
	public FriendRequestAdapter(HomeView activity, int textViewResourceId,	ArrayList<FriendRequestBean> items) {
		super(activity, textViewResourceId);
		this.item = items;	
		this.activity = activity;
		imageLoader=new ImageLoader(activity);
	}	

	@Override
	public int getCount() {
		return item.size();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView( final int position,  View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.request_notification_row, null);

			mHolder = new ViewHolder();
			v.setTag(mHolder);
			
			mHolder.mName = (TextView)v.findViewById(R.id.facebook_friend_name);
			mHolder.mYesButton = (LinearLayout)v.findViewById(R.id.yes_button);
			mHolder.mNoButton = (LinearLayout)v.findViewById(R.id.no_button);
			mHolder.mFriendImage = (ImageView)v.findViewById(R.id.user_friend_image);
			mHolder.mTrackStatus = (CheckBox)v.findViewById(R.id.track_checked_status);
			
			mHolder.mTrackStatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						item.get(position).setTrackStatus(Constant.ACTIVE_TRACK_STATUS);
						mHolder.mTrackStatus.setChecked(true);
						notifyDataSetChanged();
					}else{
						item.get(position).setTrackStatus(Constant.DEACTIVE_TRACK_STATUS);
						mHolder.mTrackStatus.setChecked(false);
						notifyDataSetChanged();
					}
					
				}
			});
			
			mHolder.mYesButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {					
					String action = "accept";
					pos = position;					
					String record_id = item.get(position).getRecordId();
					int track_status= item.get(position).getTrackStatus();
					new AddFriendStatus().execute(action,record_id,""+track_status);
				}
			});
			mHolder.mNoButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {					
					String action = "deny";
					pos = position;
					String record_id = item.get(position).getRecordId();
					new AddFriendStatus().execute(action,record_id,"0");
				}
			});
			
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}			
		
		final FriendRequestBean mVendor = item.get(position);
		
		if(mVendor != null){
			
			mHolder.mName.setText(mVendor.getSenderName());
			if(item.get(position).getTrackStatus()==Constant.DEACTIVE_TRACK_STATUS){
				mHolder.mTrackStatus.setChecked(false);
			}else{
				mHolder.mTrackStatus.setChecked(true);
			}
			imageLoader.DisplayImage(mVendor.getImage(),mHolder.mFriendImage);				
		}		
		return v;
	}

	class ViewHolder {
		public TextView mName;
		public LinearLayout mYesButton;
		public LinearLayout mNoButton;
		private ImageView mFriendImage;
		private CheckBox mTrackStatus;
	}
	public class AddFriendStatus extends AsyncTask<String, Void, Boolean>{		
		protected void onPreExecute() {
			activity.showProgressDailog();
		}		
		@Override
		protected Boolean doInBackground(String... params) {
			JSONObject response;
			boolean flag = false;
		  	try {
				JSONObject request = new JSONObject();
				request.put("actn", params[0]);
				request.put("recordid", params[1]);
				request.put("track_status", params[2]);
				response = KlHttpClient.SendHttpPost(URL.ACCEPT_FRIEND_REQ.getUrl(), request);
				if(response!=null){
					flag = response.getBoolean("status");
				}
				
		  	}catch (Exception e) {
		  		activity.dismissProgressDialog();
				return false;
			}
			return flag;
		  	
		}
		
		@Override
		protected void onPostExecute(Boolean result) {							
			activity.dismissProgressDialog();		
				if(result){
					activity.myApp.isWebServiceCallForRefreshFriendList = true;
					activity.presenter.ackAfterFriendRequest(pos);
				}							
			}
		}
}
