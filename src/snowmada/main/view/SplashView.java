package snowmada.main.view;

import android.content.Intent;
import android.os.Bundle;
import com.strapin.Interface.ISplash;
import com.strapin.global.Global;
import com.strapin.presenter.SplashPresenter;

public class SplashView extends BaseView implements ISplash{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
	}
	
	@Override
	public void init(){
		myApp.getAppInfo().setSenderIDChat("");
		myApp.getAppInfo().setIsAppForgroung(true);
		if(!myApp.getAppInfo().session){
			new SplashPresenter(this);			
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		myApp.getAppInfo().setIsAppForgroung(true);
		if(myApp.getAppInfo().session){
			startActivity(new Intent(SplashView.this,HomeView.class));
			SplashView.this.finish();
		}
	}

}
