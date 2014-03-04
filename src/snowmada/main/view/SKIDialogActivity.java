package snowmada.main.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class SKIDialogActivity extends BaseView{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final Vibrator vibrator;
		long pattern[]={0,200,100,300,400};
	    vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 0);           
        
        
        final Dialog skiPatrolAlertdialog = new Dialog(SKIDialogActivity.this);				
		skiPatrolAlertdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		skiPatrolAlertdialog.setCancelable(false);
		skiPatrolAlertdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		skiPatrolAlertdialog.setContentView(R.layout.emergency_dialog);
		
		Button ok = (Button)skiPatrolAlertdialog.findViewById(R.id.iv_ok);
		ok.setText(Html.fromHtml("<font color=\"#ffffff\">O</font><font color=\"#28b6ff\">K</font>"));
		TextView tv_alert = (TextView)skiPatrolAlertdialog.findViewById(R.id.tv_alert_text);
		tv_alert.setText(Html.fromHtml("<font color=\"#ffffff\">ALERT</font>&nbsp;&nbsp;<font color=\"#28b6ff\">DIALOG</font>"));
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vibrator.cancel();
				skiPatrolAlertdialog.cancel();
	        	myApp.getAppInfo().setIsAlertForSKIPatrol(true);
	        	if(myApp.getAppInfo().isAppForeground){	        		
	        		Intent i = new Intent(getApplicationContext(),HomeView.class);
	        		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        		startActivity(i);
	        		SKIDialogActivity.this.finish(); 
	        	
	        		
	        	}else{
	        		Intent i = new Intent(getApplicationContext(),SplashView.class);
	        		startActivity(i);
	        		SKIDialogActivity.this.finish();
	        	}
			}
		});
		skiPatrolAlertdialog.show();
	
	}

}