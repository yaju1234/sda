package com.strapin.adapter;

import java.util.ArrayList;

import snowmada.main.view.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import com.strapin.bean.DealsBean;

public class DealsAdapter extends ArrayAdapter<DealsBean>{
	private Context mCtx;
	private ArrayList<DealsBean> mItems = new ArrayList<DealsBean>();
	private ViewHolder mHolder;
	int size = 0;
	
	public DealsAdapter(Context context, int textViewResourceId,	ArrayList<DealsBean> items) {
		super(context, textViewResourceId, items);
		this.mCtx = context;
		this.mItems = items;
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
			v = vi.inflate(R.layout.deals_row, null);
			mHolder = new ViewHolder();
			mHolder.mMainBg = (LinearLayout)v.findViewById(R.id.main_bg);
			v.setTag(mHolder);	
			
				
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}	
		
		if(position%2 ==0){
			mHolder.mMainBg.setBackgroundResource(R.drawable.black_1);
			
		}else{
			mHolder.mMainBg.setBackgroundResource(R.drawable.black_2);
		}
			
		final DealsBean bean = mItems.get(position);
		if(bean != null){
			Log.e("", ""+bean.getmName());
		}		
		return v;
	}
	class ViewHolder {	
			
		public LinearLayout mMainBg;		
	}
}
