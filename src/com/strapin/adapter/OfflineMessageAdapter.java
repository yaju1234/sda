package com.strapin.adapter;

import java.util.ArrayList;

import snowmada.main.view.R;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.strapin.Util.ImageLoader;
import com.strapin.bean.MessageBean;
import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.presenter.HomePresenter;

@SuppressWarnings("unused")
public class OfflineMessageAdapter extends ArrayAdapter<MessageBean>{
	
	private Context mCtx;
	private ArrayList<MessageBean> mItems = new ArrayList<MessageBean>();
	private ViewHolder mHolder;
	private ImageLoader imageLoader;
	private Activity activity;
	private HomePresenter mPresenter;
	private SnowmadaDbAdapter mSdb;
	private LinearLayout mLayoutMessageNotificationList; 
	public OfflineMessageAdapter( LinearLayout mLayoutMessageNotificationList ,HomePresenter mPresenter,Activity activity,Context context, int textViewResourceId,	ArrayList<MessageBean> mChat) {
		super(context, textViewResourceId);
		this.mCtx = context;
		this.activity = activity;
		this.mItems = mChat;
		this.mPresenter = mPresenter;
		imageLoader=new ImageLoader(activity);
		this.mLayoutMessageNotificationList =mLayoutMessageNotificationList ; 
		mSdb = SnowmadaDbAdapter.databaseHelperInstance(mCtx);
		Log.e("SIze", ""+mItems.size());
		
	}		  
	@Override
	public int getCount() {
		return mItems.size();
	}

	
	
	@Override
	public View getView( final int position,  View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.message_notification_row, null);
			mHolder = new ViewHolder();
			v.setTag(mHolder);	
			mHolder.mSenderMsg = (TextView)v.findViewById(R.id.friend_message);
			mHolder.mSenderName = (TextView)v.findViewById(R.id.facebook_friend_name);
			mHolder.mFriendImage = (ImageView)v.findViewById(R.id.user_friend_image);
			mHolder.mMain = (RelativeLayout)v.findViewById(R.id.main);
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}	
		
		mHolder.mMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mLayoutMessageNotificationList.setVisibility(View.GONE);
				//mSdb.deleteChatMessage( mItems.get(position).getSenderFbId());
				mSdb.UpdateMessageStatus(""+mItems.get(position).getId());
				mPresenter.CallChatWindow(mItems.get(position).getSenderName(), mItems.get(position).getSenderFbId());	
				
			}
		});
		
		final MessageBean bean = mItems.get(position);
		if(bean!= null){
			//Log.e("Adapter", ""+bean.getSenderName());
			mHolder.mSenderName.setText(bean.getSenderName());
			mHolder.mSenderMsg.setText(bean.getTextMessage());
			imageLoader.DisplayImage("https://graph.facebook.com/"+bean.getSenderFbId()+"/picture",mHolder.mFriendImage);				
		}		
		return v;
	}
	class ViewHolder {	
		public TextView mSenderName;
		public TextView mSenderMsg;	
		public ImageView mFriendImage;
		public RelativeLayout mMain;
	}
}
