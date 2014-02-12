package com.strapin.adapter;

import java.util.ArrayList;

import org.json.JSONObject;

import snowmada.main.view.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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

import com.strapin.Util.ImageLoader;
import com.strapin.bean.FriendRequestBean;
import com.strapin.global.Global;
import com.strapin.network.KlHttpClient;
import com.strapin.presenter.HomePresenter;

public class FriendRequestAdapter extends ArrayAdapter<FriendRequestBean>{
	private Context mCtx;
	private ViewHolder mHolder;
	private Activity activity;
	private ProgressDialog mDialog;
	private ImageLoader imageLoader;
	public ArrayList<FriendRequestBean> item = new ArrayList<FriendRequestBean>();
	private HomePresenter mPresenter;
	private int pos;
	  
	public FriendRequestAdapter(HomePresenter mPresenter,Activity activity,Context context, int textViewResourceId,	ArrayList<FriendRequestBean> items) {
		super(context, textViewResourceId);
		this.mCtx = context;
		this.item = items;	
		this.activity = activity;
		this.mPresenter = mPresenter;
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
			LayoutInflater vi = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
					Log.e("isChecked", ""+isChecked);
					
					if(isChecked){
						item.get(position).setTrackStatus("1");
						mHolder.mTrackStatus.setChecked(true);
						notifyDataSetChanged();
					}else{
						item.get(position).setTrackStatus("0");
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
					String track_status= item.get(position).getTrackStatus();
					Log.e("item.get(position).getTrackStatus()", item.get(position).getTrackStatus());
					new AddFriendStatus().execute(action,record_id,track_status);
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
			if(item.get(position).getTrackStatus().equalsIgnoreCase("0")){
				mHolder.mTrackStatus.setChecked(false);
			}else{
				mHolder.mTrackStatus.setChecked(true);
			}
			imageLoader.DisplayImage("https://graph.facebook.com/"+mVendor.getSenderfbId()+"/picture",mHolder.mFriendImage);				
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
			mDialog = new ProgressDialog(activity);
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
				mJsonObject.put("actn", params[0]);
				mJsonObject.put("recordid", params[1]);
				mJsonObject.put("track_status", params[2]);
				Log.e("ACk mJSON REQ", mJsonObject.toString());
				json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/accept_friend.php", mJsonObject);
				if(json!=null){
					flag = json.getBoolean("status");
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
					Global.isAddSnowmadaFriend = true;
					mPresenter.ackAfterFriendRequest(pos);
				}							
			}
		}
}
