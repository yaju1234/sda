package snowmada.main.view;

import org.json.JSONException;
import org.json.JSONObject;

import com.strapin.Enum.URL;
import com.strapin.Util.Utility;
import com.strapin.global.Constant;
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
		sharedPreferences = getApplicationContext().getSharedPreferences(Constant.Settings.GLOBAL_SETTINGS.name(), Context.MODE_PRIVATE);
		userId = sharedPreferences.getString(Constant.Settings.USER_ID.name(), userId);
		Log.e("Service created", "Service created");
		System.out.println("!-- running");
		Log.e("s1","started");
		doUpdateStatus_bkp();
		
	}
	 
	 private void doUpdateStatus_bkp(){
	     Thread t = new Thread(
		 new Runnable(){
		     	public void run() {
		     	    try{
		     		while(true){
        		     		System.out.println("!-- running");
        		     		Log.e("s1","started");
        		     		new PingServer().execute();
        		     		Thread.sleep(TIME_SPAN);
		     		}
		     	    }catch(Exception e){}
			}
	     });
	     t.start();
	 }

//	 @Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		 doUpdateStatus();
//		return START_STICKY;
//	}

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
				JSONObject json = KlHttpClient.SendHttpPost(URL.SIGNAL_STATUS.getUrl(), jsonObject);
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
