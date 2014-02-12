package snowmada.main.view;

import org.json.JSONException;
import org.json.JSONObject;

import com.strapin.Util.Utility;
import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.network.KlHttpClient;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class ServerStatus extends Service{

	  private static final int TIME_SPAN = 15000;
	 private static final int START_TIME_DELAY = 0000;
	 private SnowmadaDbAdapter mDbAdapter;
	 private Handler handler = new Handler();
	 private Runnable runnable;
	 

	 @Override
	 public IBinder onBind(Intent intent) {
		 return null;
	 } 	 

	 @Override
	public void onCreate() {
		super.onCreate();
		mDbAdapter = SnowmadaDbAdapter.databaseHelperInstance(getApplicationContext());		
		Log.e("Service created", "Service created");
		 doUpdateStatus();
	}


	 public void doUpdateStatus() {
		 	runnable = new Runnable(){
			   public void run() {
				    handler.postDelayed(runnable, TIME_SPAN);
				 
				    if(Utility.isNetworkConnected(getApplicationContext())){
				    	new getAppUsers().execute();
				    }
				
			   } 
			 };
			 handler.postDelayed(runnable,START_TIME_DELAY);		
	 
	 }
	/*@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("onStartCommand", "onStartCommand");
		 doUpdateStatus();
		return START_STICKY;
	}*/
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("Service destroyed", "Service destroyed");
	}
	
	public class getAppUsers extends AsyncTask<String, Void, Boolean> {
		

		@Override
		protected Boolean doInBackground(String... params) {
			JSONObject jsonObject = new JSONObject();
	  		try {
				jsonObject.put("fbid", mDbAdapter.getUserFbID());
				jsonObject.put("signal_status", 1);
				Log.e("Online status", jsonObject.toString());
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
