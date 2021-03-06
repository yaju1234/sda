package snowmada.main.view;

import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.strapin.Enum.URL;
import com.strapin.Util.Utility;
import com.strapin.application.SnomadaApp;
import com.strapin.global.Global;
import com.strapin.network.KlHttpClient;


public class TrackLocation implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener{
	private static TrackLocation sInstance;
	private static Context sContext;
	private static LocationClient mLocationClient;
	private static LocationRequest mLocationRequest;
	private boolean isUpload = true;
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	public static final int MILLISECONDS_PER_SECOND = 1000;
	public static final int UPDATE_INTERVAL_IN_SECONDS = 2;
	public static final int FAST_CEILING_IN_SECONDS = 1;
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND	* UPDATE_INTERVAL_IN_SECONDS;
	public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND* FAST_CEILING_IN_SECONDS;
	private static  SnomadaApp app;
	
	private TrackLocation(final Context context) {
		sContext = context;
			
	}
	
	public static TrackLocation createInstance(final Context context,SnomadaApp App) {
		
		if (sInstance == null) {
			sInstance = new TrackLocation(context);
			app = App;
			
			open();
		}

		return sInstance;

	}

	private static void open() {
		
		mLocationRequest = LocationRequest.create();
	    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
		mLocationClient = new LocationClient(sContext, sInstance, sInstance);
		mLocationClient.connect();
		if (mLocationClient.isConnected()) {
			mLocationClient.removeLocationUpdates(sInstance);
			mLocationClient.requestLocationUpdates(mLocationRequest, sInstance);

		}
	}
	  
	public void removeLocationUpdate(){
		mLocationClient.removeLocationUpdates(this);		
	}
	
	public void requestUpdate(){
		mLocationClient.requestLocationUpdates(mLocationRequest, sInstance);		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {}

	@Override
	public void onConnected(Bundle connectionHint) {		
		mLocationClient.requestLocationUpdates(mLocationRequest,this);
	}

	@Override
	public void onDisconnected() {	}

	@Override
	public void onLocationChanged(Location location) {
			if(Global.isZoom){
				Global.isZoom = false;
			   Global.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
			
		}
		if(isUpload){
			if(Utility.isNetworkConnected(sContext)){
				isUpload = false;
				new SetLocationInWeb().execute(""+location.getLatitude(),""+location.getLongitude());
			}	
		}
			
	
	}
	
	 public class SetLocationInWeb extends AsyncTask<String, Void, Boolean>{		
			protected void onPreExecute() {			
			}
			@Override
			protected Boolean doInBackground(String... params) {			
			  	try {
			  		JSONObject jsonObject = new JSONObject();
			  		jsonObject.put("user_id", app.getAppInfo().userId);
			  		jsonObject.put("lat", params[0]);
			  		jsonObject.put("lng", params[1]);
			  		Log.i("SEND USER LOCATION =====>", jsonObject.toString());
			  		JSONObject json = KlHttpClient.SendHttpPost(URL.SEND_CURRENT_LOCATION.getUrl(), jsonObject);
			   if(json != null){
				   return json.getBoolean("status");
			   }
			  		
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(Boolean status) {
				isUpload = true;
				if(status != null){					
					if(status){
					
					}
				}
						
			}	
		}
}
