package snowmada.main.view;

import org.json.JSONException;
import org.json.JSONObject;

import com.strapin.Util.Utility;
import com.strapin.global.Constants;
import com.strapin.network.KlHttpClient;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class ServerStatus extends Service{

	private static final int TIME_SPAN = 30000;
	private static final int START_TIME_DELAY = 0000;
	private Handler handler = new Handler();
	private Runnable runnable;
	 public String userId = null;
	 public SharedPreferences sharedPreferences;
	 

	 @Override
	 public IBinder onBind(Intent intent) {
		 return null;
	 } 	 

	 @Override
	public void onCreate() {
		super.onCreate();
		sharedPreferences = getApplicationContext().getSharedPreferences(Constants.Settings.GLOBAL_SETTINGS.name(), Context.MODE_PRIVATE);
		userId = sharedPreferences.getString(Constants.Settings.USER_ID.name(), userId);
		Log.e("Service created", "Service created");
		doUpdateStatus();
	}


	 public void doUpdateStatus() {
		 	runnable = new Runnable(){
			   public void run() {
				    handler.postDelayed(runnable, TIME_SPAN);
				 
				    if(Utility.isNetworkConnected(getApplicationContext())){
				    	new PingServer().execute();
				    }
				
			   } 
			 };
			 handler.postDelayed(runnable,START_TIME_DELAY);		
	 
	 }
	 
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("Service destroyed", "Service destroyed");
	}
	
	public class PingServer extends AsyncTask<String, Void, Boolean> {
		

		@Override
		protected Boolean doInBackground(String... params) {
			JSONObject jsonObject = new JSONObject();
	  		try {
				jsonObject.put("fbid", userId);
				jsonObject.put("signal_status", 1);
				Log.e("online offline status====>>>", jsonObject.toString());
				JSONObject json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/device_signal_status.php", jsonObject);
				if(json!=null){
					return json.getBoolean("status");
				}
	  		} catch (JSONException e) {
				e.printStackTrace();
			}
	  		
			return null;
		}

	}
	 
	 
	}
