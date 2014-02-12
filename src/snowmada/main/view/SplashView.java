package snowmada.main.view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;
import com.strapin.Interface.ISplash;
import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.global.Global;
import com.strapin.presenter.SplashPresenter;

public class SplashView extends BaseView implements ISplash{
	
	private ImageView mImg;
	private SplashPresenter mPresenter;
	private SnowmadaDbAdapter mDbAdapter;
	

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		mDbAdapter = SnowmadaDbAdapter.databaseHelperInstance(getApplicationContext());
		Global.isApplicationForeground = true;
		Global.mChatSenderID = "";
		mImg = (ImageView) findViewById(R.id.imageView1);
		if(!mDbAdapter.isSessionvalid()){
			mPresenter = new SplashPresenter(this);
			mPresenter.handleSplash();
		}
		
	}
	/*@Override
	public void init(){
		
	}*/

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public Context getContext() {
		return this;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if(mDbAdapter.isSessionvalid()){
			Intent i = new Intent(SplashView.this,HomeView.class);
			startActivity(i);
			finish();
		}
	}

	

}
