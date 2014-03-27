package com.strapin.adapter;

import java.util.ArrayList;

import snowmada.main.view.HomeView;
import snowmada.main.view.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.strapin.Util.ImageLoader;
import com.strapin.bean.AppUserInfoBean;

public class InviteFriendAdapter extends ArrayAdapter<AppUserInfoBean>{
	
	private ArrayList<AppUserInfoBean> mItems = new ArrayList<AppUserInfoBean>();
	private ViewHolder mHolder;
	public ImageLoader imageLoader;
	private HomeView activity;
	
	public InviteFriendAdapter(HomeView activity, int textViewResourceId,	ArrayList<AppUserInfoBean> items) {
		super(activity, textViewResourceId, items);
		this.mItems = items;
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
			mHolder.image = (ImageView)v.findViewById(R.id.iv_img_friend);
			mHolder.name = (TextView)v.findViewById(R.id.tv_friend_name);
			mHolder.mMain = (LinearLayout)v.findViewById(R.id.ll_main_layout);
				
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}	
		
		mHolder.mMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mItems.get(position).getUserType().equalsIgnoreCase("F")){
					activity.sendRequestDialog(mItems.get(position).getId());
				}else{
					Uri smsUri = Uri.parse("sms:"+mItems.get(position).getId());
					Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
					intent.putExtra("sms_body", "Hi  I'm using Snomada https://play.google.com/store/apps/details?id=snowmada.main.view&hl=en");
					activity.startActivity(intent);
				}				
			}
		});
	
		final AppUserInfoBean bean = mItems.get(position);
		if(bean != null){
			Log.e("Image", bean.getImage());
			imageLoader.DisplayImage(bean.getImage(),mHolder.image);
			mHolder.name.setText(bean.getFirstName());
			
		}		
		return v;
	}
	class ViewHolder {	
		public ImageView image;
		public TextView name;
		public LinearLayout mMain;			
	}
	
}
