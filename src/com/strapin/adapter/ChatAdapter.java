package com.strapin.adapter;

import java.util.ArrayList;

import snowmada.main.view.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.strapin.bean.ChatBean;
import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.global.Global;

public class ChatAdapter extends ArrayAdapter<ChatBean>{
	
	private Context mCtx;
	private ArrayList<ChatBean> mItems = new ArrayList<ChatBean>();
	private ViewHolder mHolder;
	int size = 0;
	private SnowmadaDbAdapter mDbAdapter;
	public ChatAdapter(Context context, int textViewResourceId,	ArrayList<ChatBean> mChat) {
		super(context, textViewResourceId, mChat);
		this.mCtx = context;
		mItems = Global.mChatArr;
		mDbAdapter = SnowmadaDbAdapter.databaseHelperInstance(mCtx);
		/*for(int i=mChat.size()-1; i>=0; i--){
			this.mItems.add(new ChatBean(mChat.get(i).getSender(), mChat.get(i).getMessage()));
		}*/
		//this.mItems = items;
		size = mItems.size();
		
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
			LayoutInflater vi = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.chat_row, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);	
			mHolder.mSender = (RelativeLayout)v.findViewById(R.id.sender);
			mHolder.mReceiver = (RelativeLayout)v.findViewById(R.id.receiver);
			mHolder.mSenderMsg = (TextView)v.findViewById(R.id.sender_msg);
			mHolder.mReceiverMsg = (TextView)v.findViewById(R.id.receiver_msg);
			mHolder.mSenderName = (TextView)v.findViewById(R.id.sender_name);
			mHolder.mReceiverName = (TextView)v.findViewById(R.id.receiver_name);
			mHolder.mMain = (LinearLayout)v.findViewById(R.id.main);
				
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}	
		
		
		final ChatBean bean = mItems.get(position);
		if(bean != null){
			/*if(bean.getSender().equalsIgnoreCase("me")){
				mHolder.mSender.setVisibility(View.VISIBLE);
				mHolder.mReceiver.setVisibility(View.GONE);
				mHolder.mSenderName.setText(Global.mFName);
				mHolder.mSenderMsg.setText(bean.getMessage());
			}else*/ if(bean.getSender().equalsIgnoreCase(mDbAdapter.getUserFirstName())){
				mHolder.mSender.setVisibility(View.VISIBLE);
				mHolder.mReceiver.setVisibility(View.GONE);
				mHolder.mSenderName.setText("Me");
				mHolder.mSenderMsg.setText(bean.getMessage());
				//mHolder.mMain.setBackgroundResource(R.drawable.chat_bg1);
			}else{
				mHolder.mSender.setVisibility(View.GONE);
				mHolder.mReceiver.setVisibility(View.VISIBLE);
				mHolder.mReceiverName.setText(bean.getSender());
				mHolder.mReceiverMsg.setText(bean.getMessage());
				//mHolder.mMain.setBackgroundResource(R.drawable.chat_bg2);
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
		public LinearLayout mMain;
	}
	
	
}
