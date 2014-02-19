package com.strapin.adapter;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

import snowmada.main.view.HomeView;
import snowmada.main.view.R;
import android.app.ProgressDialog;
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
import com.strapin.Util.ImageLoader;
import com.strapin.bean.FriendListBean;
import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.global.Global;
import com.strapin.network.KlHttpClient;
import com.strapin.presenter.HomePresenter;

public class FriendAdapter extends ArrayAdapter<FriendListBean>{
	
	private ArrayList<FriendListBean> mItems = new ArrayList<FriendListBean>();
	private ViewHolder mHolder;
	int size = 0;
	public ImageLoader imageLoader;
	private HomeView activity;
	private ProgressDialog mDialog;
	private HomePresenter mPresenter;
	private SnowmadaDbAdapter mDbAdapter;
	private String changeTrackStatus;
	private int pos;
	
	
	public FriendAdapter(HomePresenter mPresenter,HomeView activity, int textViewResourceId,	ArrayList<FriendListBean> items) {
		super(activity, textViewResourceId, items);
		this.mItems = items;
		size = mItems.size();
		this.activity =activity;
		this.mPresenter = mPresenter;
		mDbAdapter = SnowmadaDbAdapter.databaseHelperInstance(activity);
		imageLoader=new ImageLoader(activity);
		
	}		  
	@Override
	public int getCount() {
		return mItems.size();
	}

	 @Override
	    public long getItemId(int position) {
	        return 0;
	    	
	    }
	
	  @Override
	    public boolean hasStableIds() {
	        return true;
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
			mHolder.mMain = (LinearLayout)v.findViewById(R.id.main);
			mHolder.mOnlineStatus = (TextView)v.findViewById(R.id.on_line_status);
			mHolder.mTrackStatus = (CheckBox)v.findViewById(R.id.checkBox1);
			mHolder.mAllowText = (TextView)v.findViewById(R.id.allow_text);
				
		}
		else {
			
			mHolder =  (ViewHolder) v.getTag();
		}
	
		mHolder.mMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {					
					String fname = mItems.get(position).getName();
					if(Global.isChatActive){
						activity.app.getAppInfo().setSenderIDChat( mItems.get(position).getFbId());
							String[] splitStr = fname.split("\\s+");
							Global.mChatUserName = splitStr[0];
							Global.mChatArr.clear();
							mPresenter.functionChat(mItems.get(position).getFbId(), mItems.get(position).getName());
						
					}else{
					mPresenter.setOnFriendClick(position);
					}				
			}
		});
		
		mHolder.mTrackStatus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mItems.get(position).getStatus().equalsIgnoreCase("1")){
				pos = position;
					new UpdateTrackStatus().execute(""+mDbAdapter.getUserFbID(),mItems.get(position).getFbId(),"0");
				}else{
					pos = position;
					new UpdateTrackStatus().execute(""+mDbAdapter.getUserFbID(),mItems.get(position).getFbId(),"1");
					
				}
				
			}
			
		});
		
		//mHolder.mMain.setOn
		/*mHolder.mMain.setOnTouchListener(new OnSwipeTouchListener() {

		    public void onSwipeTop() {
		        Toast.makeText(mCtx, "top", Toast.LENGTH_SHORT).show();
		    }
		    public void onSwipeRight() {
		        Toast.makeText(mCtx, "right", Toast.LENGTH_SHORT).show();
		    }
		    public void onSwipeLeft() {
		        Toast.makeText(mCtx, "left", Toast.LENGTH_SHORT).show();
		    }
		    public void onSwipeBottom() {
		        Toast.makeText(mCtx, "bottom", Toast.LENGTH_SHORT).show();
		    }

		});
		*/
			
		final FriendListBean bean = mItems.get(position);
		if(bean != null){
			//mHolder.mImage.setBackgroundResource(bean.getImage());
			
			if(bean.getISUserExist().equalsIgnoreCase("1")){
				mHolder.mAllowText.setVisibility(View.VISIBLE);
				mHolder.mTrackStatus.setVisibility(View.VISIBLE);
			}else{
				mHolder.mAllowText.setVisibility(View.INVISIBLE);
				mHolder.mTrackStatus.setVisibility(View.INVISIBLE);
			}
			imageLoader.DisplayImage("https://graph.facebook.com/"+bean.getFbId()+"/picture",mHolder.mImage);
			mHolder.mName.setText(bean.getName());
			//mHolder.mGnar.setText("GNR Factor:"+"000000");
			if(bean.getOnlineStatus().equalsIgnoreCase("1")){
				mHolder.mOnlineStatus.setText("Online");
				mHolder.mOnlineStatus.setTextColor(Color.parseColor("#0be423"));
			}else{
				mHolder.mOnlineStatus.setText("Offline");
				mHolder.mOnlineStatus.setTextColor(Color.parseColor("#ff0000"));
				//mHolder.mOnlineStatus.setBackgroundResource(R.drawable.status);
			}
			
			if(bean.getStatus().equalsIgnoreCase("1")){
				mHolder.mTrackStatus.setChecked(true);
				activity.app.getAppInfo().setSenderIDChat(bean.getFbId());
				
			}else{
				mHolder.mTrackStatus.setChecked(false);
			}
		}	
		
		return v;
	}
	class ViewHolder {	
		public ImageView mImage;
		public TextView mName;
		public LinearLayout mMain;
		public TextView mOnlineStatus;
		public CheckBox mTrackStatus;
		public TextView mAllowText;
		
				
	}
	
	
public class UpdateTrackStatus extends AsyncTask<String, Void, Boolean>{

		
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
			
		  	try {
				JSONObject mJsonObject = new JSONObject();
				mJsonObject.put("fbid", params[0]);
				mJsonObject.put("friend_fb_id", params[1]);
				mJsonObject.put("track_status", params[2]);
				changeTrackStatus = params[2];
				Log.e("JSON", mJsonObject.toString());
				JSONObject json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/change_track.php", mJsonObject);
				if(json!=null){
					return json.getBoolean("status");
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				mDialog.dismiss();
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			mDialog.dismiss();
			if(result){
				mItems.get(pos).setStatus(changeTrackStatus);
				notifyDataSetChanged();
			}
		}
	}
}
