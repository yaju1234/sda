package snowmada.main.view;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.global.Global;
import com.strapin.network.KlHttpClient;

public class DialogActivity extends BaseView{
	private SnowmadaDbAdapter mDb;
@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDb = SnowmadaDbAdapter.databaseHelperInstance(DialogActivity.this);
		
		Log.e("Emergency press dialog", "Emergency press dialog");
		final Vibrator vibrator;
		long pattern[]={0,200,100,300,400};
	    vibrator = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 0);           
        
        
        final Dialog dialog = new Dialog(DialogActivity.this);				
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.emergency_dialog);
		
		Button ok = (Button)dialog.findViewById(R.id.iv_ok);
		ok.setText(Html.fromHtml("<font color=\"#ffffff\">O</font><font color=\"#28b6ff\">K</font>"));
		TextView tv_alert = (TextView)dialog.findViewById(R.id.tv_alert_text);
		tv_alert.setText(Html.fromHtml("<font color=\"#ffffff\">ALERT</font><font color=\"#28b6ff\">DIALOG</font>"));
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vibrator.cancel();
				dialog.cancel();
	        	//mDb.updateSKI("1");
				app.getAppInfo().setIsAlertForSKIPatrol(true);
	        	String st[] = mDb.getSkiPetrolInfo();
	        	new GetSkiAcknowledgement().execute(st[0],st[1]);
	        	
	        	//Global.isSkiPatrolPressed = true;
	        	if(app.getAppInfo().isAppForeground/*Global.isApplicationForeground*/){
	        		
	        		Intent i = new Intent(getApplicationContext(),HomeView.class);
	        		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        		startActivity(i);
	        		DialogActivity.this.finish();
	        		
	        		//alogActivity.this.finish();
	        		
	        	}else{
	        		Intent i = new Intent(getApplicationContext(),SplashView.class);
	        		startActivity(i);
	        		DialogActivity.this.finish();
	        	}
			}
		});
		dialog.show();
	
	}
	public class GetSkiAcknowledgement extends AsyncTask<String, Void, Boolean>{		
		protected void onPreExecute() {
			
		}
		@Override
		protected Boolean doInBackground(String... params) {			
		  	try {
				JSONObject mJsonObject = new JSONObject();
				mJsonObject.put("fname", params[0]);
				mJsonObject.put("lname", params[1]);	
				Log.e("JSON", mJsonObject.toString());
				JSONObject obj = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/ack_ski.php", mJsonObject);	
				return obj.getBoolean("status");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Boolean Status) {		
			Log.e("message", ""+Status);
			
		}			
	}
}