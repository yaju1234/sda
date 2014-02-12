package com.strapin.Interface;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;

public interface ISplash {
	interface Presenter{
		public void handleSplash();
	}
	public Activity getActivity();
	public Context getContext();
	//public AnimationDrawable getAnimationDrawable();

}
