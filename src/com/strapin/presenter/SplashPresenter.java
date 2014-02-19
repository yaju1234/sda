package com.strapin.presenter;

import snowmada.main.view.SigninView;
import snowmada.main.view.SplashView;
import android.content.Intent;

import com.strapin.Interface.ISplash;

public class SplashPresenter extends BasePresenter implements ISplash.Presenter{
	
	private SplashView mSplashView;
	public static final int SPLASH_ACREEN_WAIT_TIME = 3000;
	
	
	public SplashPresenter(SplashView splashView){
		this.mSplashView = splashView;	
	}

	@Override
	public void doWait() {
		Thread t = new Thread(){
			public void run(){
				try {
					Thread.sleep(SPLASH_ACREEN_WAIT_TIME);
					callNextScreen();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
		}

	@Override
	public void callNextScreen() {
		mSplashView.startActivity(new Intent(mSplashView,SigninView.class));
		mSplashView.finish();
		
	}

	
}
