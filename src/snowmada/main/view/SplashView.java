package snowmada.main.view;

import android.content.Intent;
import android.os.Bundle;
import com.strapin.Interface.ISplash;
import com.strapin.presenter.SplashPresenter;

public class SplashView extends BaseView implements ISplash{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
	}
	
	@Override
	public void init(){
		app.getAppInfo().setSenderIDChat("");
		if(!app.getAppInfo().session){
			new SplashPresenter(this);			
		}
	}

	@Override
	protected void onResume() {
		super.onResume();		
		if(app.getAppInfo().session){
			startActivity(new Intent(SplashView.this,HomeView.class));
			SplashView.this.finish();
		}
	}

}
