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
import com.strapin.bean.NewMessage;
import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.presenter.HomePresenter;

@SuppressWarnings("unused")
public class OfflineMessageAdapter extends ArrayAdapter<NewMessage>{
	
	private ArrayList<NewMessage> mItems = new ArrayList<NewMessage>();
	private ViewHolder mHolder;
	private ImageLoader imageLoader;
	private Activity activity;
	private HomePresenter mPresenter;
	private LinearLayout mLayoutMessageNotificationList; 
	public OfflineMessageAdapter(HomePresenter mPresenter,Activity activity, int textViewResourceId,	ArrayList<NewMessage> mChat) {
		super(activity, textViewResourceId);
		this.activity = activity;
		this.mItems = mChat;
		this.mPresenter = mPresenter;
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
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}	
			
		final NewMessage bean = mItems.get(position);
		if(bean!= null){
			mHolder.name.setText(bean.getName());
			mHolder.message.setText(bean.getMessage());
			imageLoader.DisplayImage("https://graph.facebook.com/"+bean.getId()+"/picture",mHolder.image);				
		}		
		return v;
	}
	class ViewHolder {	
		public TextView name;
		public TextView message;	
		public ImageView image;
		public RelativeLayout main;
	}
}
