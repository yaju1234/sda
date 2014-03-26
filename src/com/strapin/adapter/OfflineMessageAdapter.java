package com.strapin.adapter;

import java.util.ArrayList;

import snowmada.main.view.HomeView;
import snowmada.main.view.R;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.strapin.Util.ImageLoader;
import com.strapin.bean.NewMessage;
import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.presenter.HomePresenter;

@SuppressWarnings("unused")
public class OfflineMessageAdapter extends ArrayAdapter<NewMessage>{
	
	private ArrayList<NewMessage> mItems = new ArrayList<NewMessage>();
	private ViewHolder mHolder;
	private ImageLoader imageLoader;
	private HomeView activity;
	private HomePresenter mPresenter;
	private LinearLayout mLayoutMessageNotificationList; 
	private  LinearLayout mLayoutMsgNotiList;
	public OfflineMessageAdapter(HomeView activity, int textViewResourceId,	ArrayList<NewMessage> mChat , LinearLayout mLayoutMsgNotiList) {
		super(activity, textViewResourceId);
		this.activity = activity;
		this.mItems = mChat;
		this.mLayoutMsgNotiList = mLayoutMsgNotiList;
		imageLoader=new ImageLoader(activity);		
	}		  
	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public View getView( final int position,  View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.message_notification_row, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);	
			mHolder.message = (TextView)v.findViewById(R.id.tv_reply_message);
			mHolder.name = (TextView)v.findViewById(R.id.tv_name);
			mHolder.image = (ImageView)v.findViewById(R.id.iv_profile_image);
			mHolder.main = (RelativeLayout)v.findViewById(R.id.rl_main);
			mHolder.reply_icon = (ImageView)v.findViewById(R.id.iv_reply_icon);
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}	
		
		mHolder.main.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mLayoutMsgNotiList.getVisibility() == View.VISIBLE) {
					mLayoutMsgNotiList.setVisibility(View.GONE);
				} else {
					mLayoutMsgNotiList.setVisibility(View.VISIBLE);		
					
				}
				if(activity.myApp.getAppInfo().userId.equalsIgnoreCase(mItems.get(position).getSenderId())){
					boolean status= activity.presenter.getFriendStatus(mItems.get(position).getReceiverId());
					activity.presenter.CallChatWindow(mItems.get(position).getReceiverFirstName(), mItems.get(position).getReceiverId(),status,mItems.get(position).getReceiverImage());
				}else{
					boolean status= activity.presenter.getFriendStatus(mItems.get(position).getSenderId());
					activity.presenter.CallChatWindow(mItems.get(position).getSenderFirstName(), mItems.get(position).getSenderId(),status,mItems.get(position).getSenderImage());
				}
				
				
			}
		});
			
		final NewMessage bean = mItems.get(position);
		if(bean!= null){
			if(activity.myApp.getAppInfo().userId.equalsIgnoreCase(bean.getSenderId())){
				mHolder.reply_icon.setVisibility(View.VISIBLE);
				mHolder.name.setText(bean.getSenderFirstName()+" "+bean.getSenderLastName());
				mHolder.message.setText(bean.getMessage());
				imageLoader.DisplayImage(bean.getSenderImage(),mHolder.image);	
				Log.e("snomada", "ImageURL111=====>>>>"+bean.getSenderImage());
			}else{
				mHolder.reply_icon.setVisibility(View.GONE);
				mHolder.name.setText(bean.getSenderFirstName()+" "+bean.getSenderLastName());
				mHolder.message.setText(bean.getMessage());
				imageLoader.DisplayImage(bean.getSenderImage(),mHolder.image);
				Log.e("snomada", "ImageURL222=====>>>>"+bean.getSenderImage());
			}
						
		}		
		return v;
	}
	class ViewHolder {	
		public TextView name;
		public TextView message;	
		public ImageView image;
		public ImageView reply_icon;
		public RelativeLayout main;
	}
}
