package com.strapin.presenter;

import snowmada.main.view.SigninView;
import snowmada.main.view.SplashView;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.strapin.Interface.ISplash;
import com.strapin.Util.Utility;
import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.global.Global;

public class SplashPresenter implements ISplash.Presenter{
	
	private SplashView mSplashView;
	private static final int WAIT_TIME = 3000;
	private static final int VALUE = 1; 
	private SnowmadaDbAdapter mDBAdapter;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case VALUE:
				
				/*Intent i1 = new Intent(mSplashView.getContext(),Server.class);
				mSplashView.getActivity().startService(i1);*/
				//mSplashView.getAnimationDrawable().stop();
				if(Global.isSessionValid){
					if(Utility.isNetworkConnected(mSplashView.getContext())){
						Intent i = new Intent(mSplashView.getContext(),SigninView.class);
						mSplashView.getActivity().startActivity(i);
						mSplashView.getActivity().finish();
					}else{
						Toast.makeText(mSplashView.getContext(), "You have no internet connection", Toast.LENGTH_LONG).show();
						mSplashView.getActivity().finish();
					}
					
				}else{
					Intent i = new Intent(mSplashView.getContext(),SigninView.class);
					mSplashView.getActivity().startActivity(i);
					mSplashView.getActivity().finish();
				}
				
				break;

			default:
				break;
			}
		}
		
	};
	
	public SplashPresenter(SplashView mSplashView){
		this.mSplashView = mSplashView;
		
		mDBAdapter = SnowmadaDbAdapter.databaseHelperInstance(mSplashView);
		if(mDBAdapter.getSKIRowCount()<1){
			mDBAdapter.insertSKI("0");
		}
	}

	@Override
	public void handleSplash() {
		handler.sendEmptyMessageDelayed(VALUE, WAIT_TIME);
	}

}
