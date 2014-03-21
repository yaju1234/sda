package com.strapin.adapter;

import java.util.ArrayList;

import org.json.JSONObject;

import snowmada.main.view.HomeView;
import snowmada.main.view.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.strapin.Enum.URL;
import com.strapin.bean.AppUserInfoBean;
import com.strapin.network.KlHttpClient;

public class AddFriendAdapter extends ArrayAdapter<AppUserInfoBean>{
	
	private ArrayList<AppUserInfoBean> mItems = new ArrayList<AppUserInfoBean>();
	private ViewHolder mHolder;
	private HomeView activity;
	public String responseMsg;
	
	public AddFriendAdapter(HomeView activity, int textViewResourceId,	ArrayList<AppUserInfoBean> items) {
		super(activity, textViewResourceId, items);
		this.mItems = items;
		this.activity =activity;		
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
	public View getView( final int position,  View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.add_friend_row, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);	
			mHolder.image = (ImageView)v.findViewById(R.id.iv_img_friend);
			mHolder.name = (TextView)v.findViewById(R.id.tv_friend_name);
			mHolder.main = (LinearLayout)v.findViewById(R.id.ll_main_layout);
				
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}	
		
		mHolder.main.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(activity.myApp.selectedTab == 1){
					final Dialog dialog = new Dialog(activity);				
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
					dialog.setContentView(R.layout.dialog_add_friend);
					//dialog.set
					TextView name = (TextView)dialog.findViewById(R.id.tv_req_friend_name);
					ImageView image = (ImageView)dialog.findViewById(R.id.iv_req_friend_img);
					final CheckBox isTrack = (CheckBox)dialog.findViewById(R.id.check_track);
					Button yes = (Button)dialog.findViewById(R.id.btn_yes);
					yes.setText(Html.fromHtml("<font color=\"#ffffff\">YE</font><font color=\"#28b6ff\">S</font>"));
					Button no = (Button)dialog.findViewById(R.id.btn_no);
					no.setText(Html.fromHtml("<font color=\"#ffffff\">N</font><font color=\"#28b6ff\">O</font>"));
					
					name.setText(mItems.get(position).getFirstName()+" "+mItems.get(position).getLastName());
					isTrack.setChecked(true);
					activity.imageLoader.DisplayImage(mItems.get(position).getImage(),image);
						
					yes.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int status = 0;
							if(isTrack.isChecked()){
								status = 1;
							}else{
								status = 0;
							}
							new AddSnowmadaFriend().execute(mItems.get(position).getId(),mItems.get(position).getFirstName()+" "+mItems.get(position).getLastName(),""+status);
							dialog.dismiss();
						}
					});
					
					no.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							dialog.dismiss();
						}
					});
					dialog.show();				
				
				}else{
					
				
				}
			}
		});
			
		final AppUserInfoBean bean = mItems.get(position);
		if(bean != null){
			activity.imageLoader.DisplayImage(bean.getImage(),mHolder.image);
			mHolder.name.setText(bean.getFirstName());
			
		}		
		return v;
	}
	class ViewHolder {	
		public ImageView image;
		public TextView name;
		public TextView mGnar;	
		public LinearLayout main;			
	}
	 public class AddSnowmadaFriend extends AsyncTask<String, Void, Boolean>{		
			protected void onPreExecute() {
				activity.showProgressDailog();
				
			}
			@Override
			protected Boolean doInBackground(String... params) {	
				boolean flg = false;
			  	try {
			  		JSONObject request = new JSONObject();
			  		request.put("fbid", activity.myApp.getAppInfo().userId);
			  		request.put("friend_fb_id", params[0]);
			  		request.put("friend_name", params[1]);
			  		request.put("track_status", params[2]);
			  		request.put("image", activity.myApp.getAppInfo().image);
			  		request.put("sendername",activity.myApp.getAppInfo().userFirstName+" "+activity.myApp.getAppInfo().userLastName);
			  		JSONObject response = KlHttpClient.SendHttpPost(URL.ADD_FRIEND.getUrl(), request);
			       if(response!=null){
			    	   flg = response.getBoolean("status");
			    	   responseMsg = response.getString("message");
			    	   return flg;
			       }
			  	   	
					
				} catch (Exception e) {
					activity.dismissProgressDialog();
					e.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(Boolean status) {
				activity.dismissProgressDialog();
				if(status!=null){
					Log.e("TAG", "=========================================");
					if(status){					
						Toast.makeText(activity, responseMsg, Toast.LENGTH_LONG).show();				
					}else{
						Toast.makeText(activity, responseMsg, Toast.LENGTH_LONG).show();
					}
				}
				
				
			}	
		}
	
}
