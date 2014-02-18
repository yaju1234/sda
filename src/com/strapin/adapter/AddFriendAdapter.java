package com.strapin.adapter;

import java.util.ArrayList;

import org.json.JSONObject;

import snowmada.main.view.HomeView;
import snowmada.main.view.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.android.gms.maps.model.Marker;
import com.strapin.Util.ImageLoader;
import com.strapin.bean.AddFriendBean;
import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.global.Global;
import com.strapin.network.KlHttpClient;

public class AddFriendAdapter extends ArrayAdapter<AddFriendBean>{
	
	private ArrayList<AddFriendBean> mItems = new ArrayList<AddFriendBean>();
	private ViewHolder mHolder;
	int size = 0;
	public ImageLoader imageLoader;
	private HomeView activity;
	private Marker marker;
	private ProgressDialog mDialog;
	private Double mLat;
	private Double mLng;
	private String mName;
	private SnowmadaDbAdapter mSdb;
	
	public AddFriendAdapter(SnowmadaDbAdapter sdb,HomeView activity, int textViewResourceId,	ArrayList<AddFriendBean> items) {
		super(activity, textViewResourceId, items);
		mSdb = sdb;
		this.mItems = items;
		size = mItems.size();
		this.activity =activity;
		
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
	public View getView( final int position,  View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.add_friend_row, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);	
			mHolder.mImage = (ImageView)v.findViewById(R.id.user_friend_image);
			mHolder.mName = (TextView)v.findViewById(R.id.facebook_friend_name);
			mHolder.mMain = (RelativeLayout)v.findViewById(R.id.main);
				
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}	
		
		mHolder.mMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(Global.mSelectedTab == 1){


					//Log.e("Reach here", "reach here");
					final Dialog dialog = new Dialog(activity);				
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
					dialog.setContentView(R.layout.friend_add_dialog);
					//dialog.set
					TextView name = (TextView)dialog.findViewById(R.id.friend_name);
					ImageView image = (ImageView)dialog.findViewById(R.id.image_user_friend_dialog);
					final CheckBox isTrack = (CheckBox)dialog.findViewById(R.id.check_track);
					Button yes = (Button)dialog.findViewById(R.id.btn_yes);
					yes.setText(Html.fromHtml("<font color=\"#ffffff\">YE</font><font color=\"#28b6ff\">S</font>"));
					Button no = (Button)dialog.findViewById(R.id.btn_no);
					no.setText(Html.fromHtml("<font color=\"#ffffff\">N</font><font color=\"#28b6ff\">O</font>"));
					
					name.setText(mItems.get(position).getName());
					isTrack.setChecked(true);
					/*if(mItems.get(position).isStatus()){
						isTrack.setChecked(true);
					}else{
						isTrack.setChecked(false);
					}*/
					imageLoader.DisplayImage("https://graph.facebook.com/"+mItems.get(position).getFacebookId()+"/picture",image);
					
					/*isTrack.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if(mItems.get(position).isStatus()){
								
							}else{
								isTrack.setChecked(false);
								Toast.makeText(mCtx, "This Friend does not use this application. You can't access his location", Toast.LENGTH_LONG).show();
							}						
						}
					});*/
					
					yes.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							int status = 0;
							if(isTrack.isChecked()){
								status = 1;
							}else{
								status = 0;
							}
							new AddSnowmadaFriend().execute(mItems.get(position).getFacebookId(),mItems.get(position).getName(),""+status);
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
					//Global.getFacebookStrapIn().sendChallenge(activity,mItems.get(position).getFacebookId());
				
				}
			}
		});
			
		final AddFriendBean bean = mItems.get(position);
		if(bean != null){
			//mHolder.mImage.setBackgroundResource(bean.getImage());
			imageLoader.DisplayImage("https://graph.facebook.com/"+bean.getFacebookId()+"/picture",mHolder.mImage);
			mHolder.mName.setText(bean.getName());
			
		}		
		return v;
	}
	class ViewHolder {	
		public ImageView mImage;
		public TextView mName;
		public TextView mGnar;	
		public RelativeLayout mMain;			
	}
	 public class AddSnowmadaFriend extends AsyncTask<String, Void, Boolean>{		
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
			  		JSONObject jsonObject = new JSONObject();
			  		jsonObject.put("fbid", mSdb.getUserFbID());
			  		jsonObject.put("friend_fb_id", params[0]);
			  		jsonObject.put("friend_name", params[1]);
			  		jsonObject.put("track_status", params[2]);
			  		jsonObject.put("sendername",mSdb.getUserFirstName()+" "+mSdb.getUserLastName() );
			  		Log.e("JSON", jsonObject.toString());
			  		JSONObject json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/add_friend_request.php", jsonObject);
			        return json.getBoolean("status");
			        	
					
				} catch (Exception e) {
					mDialog.dismiss();
					e.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(Boolean status) {
				mDialog.dismiss();
				if(status){
					Toast.makeText(activity, "Friend added successfully", Toast.LENGTH_LONG).show();
					//Global.isAddSnowmadaFriend = true;
				}
				
			}	
		}
	
}
