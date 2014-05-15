package com.strapin.adapter;

import java.util.ArrayList;

import snowmada.main.view.HomeView;
import snowmada.main.view.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.strapin.bean.ChatBean;
import com.strapin.global.Global;

public class ChatAdapter extends ArrayAdapter<ChatBean>{
	
	private HomeView activity;
	private ArrayList<ChatBean> mItems = new ArrayList<ChatBean>();
	private ViewHolder mHolder;
	
	
	public ChatAdapter(HomeView activity, int textViewResourceId,	ArrayList<ChatBean> mChat) {
		super(activity, textViewResourceId, mChat);
		this.activity = activity;
		mItems = Global.mChatArr;
		
		for(int i=0; i<mItems.size(); i++){
		    System.out.println("!-- Chat history message "+mItems.get(i).getMessage());
		}
		
		
		
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
			v = vi.inflate(R.layout.chat_row, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);	
			mHolder.mSender          = (RelativeLayout)v.findViewById(R.id.sender);
			mHolder.mReceiver        = (RelativeLayout)v.findViewById(R.id.receiver);
			mHolder.mSenderMsg       = (TextView)v.findViewById(R.id.sender_msg);
			mHolder.mReceiverMsg     = (TextView)v.findViewById(R.id.receiver_msg);
			mHolder.mSenderName      = (TextView)v.findViewById(R.id.sender_name);
			mHolder.mReceiverName    = (TextView)v.findViewById(R.id.receiver_name);
			mHolder.iv_avatar_rcver  = (ImageView)v.findViewById(R.id.iv_avatar_rcver);
			mHolder.iv_user_img      = (ImageView)v.findViewById(R.id.iv_user_img);
			mHolder.mMain            = (LinearLayout)v.findViewById(R.id.main);
				
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}	
		
		
		final ChatBean bean = mItems.get(position);
		System.out.println("!-- Chat mEssage123");
		System.out.println("!-- Chat mEssage "+bean.getMessage());
		if(bean != null){
			 if(bean.getSender().equalsIgnoreCase(activity.myApp.getAppInfo().userFirstName)){
				mHolder.mSender.setVisibility(View.VISIBLE);
				mHolder.mReceiver.setVisibility(View.GONE);
				mHolder.mSenderName.setText("Me");
				mHolder.mSenderMsg.setText(bean.getMessage());
				System.out.println("!---activity.myApp.getAppInfo().image  "+activity.myApp.getAppInfo().image);
				activity.imageLoader.DisplayImage(activity.myApp.getAppInfo().image,mHolder.iv_user_img);
				
			}else{
				mHolder.mSender.setVisibility(View.GONE);
				mHolder.mReceiver.setVisibility(View.VISIBLE);
				mHolder.mReceiverName.setText(bean.getSender());
				mHolder.mReceiverMsg.setText(bean.getMessage());
				Log.e("snomada", "AVATAR "+Global.iv_chat_avatar_img);
				System.out.println("!---Global.iv_chat_avatar_img  "+Global.iv_chat_avatar_img);
				activity.imageLoader.DisplayImage(Global.iv_chat_avatar_img,mHolder.iv_avatar_rcver);
			}
			
		}		
		return v;
	}
	class ViewHolder {	
		public RelativeLayout mSender;
		public RelativeLayout mReceiver;
		public TextView mSenderMsg;
		public TextView mReceiverMsg;	
		public TextView mSenderName;
		public TextView mReceiverName;
		public ImageView iv_avatar_rcver;
		public ImageView iv_user_img;
		public LinearLayout mMain;
	}
	
	
}
