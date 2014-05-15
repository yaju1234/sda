package snowmada.main.view;

import static com.strapin.common.CommonUtilities.SENDER_ID;
import static com.strapin.common.CommonUtilities.displayMessage;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;
import com.strapin.application.SnomadaApp;
import com.strapin.common.ServerUtilities;
import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.global.Constant;
import com.strapin.global.Global;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	private static LocationManager locationManager;
	public static SnomadaApp app = null;
	 public static String weiw_message = null;
	 public static SharedPreferences sharedPreferences;
	 public GCMIntentService() {
        super(SENDER_ID);
    }
    
  @Override
    protected void onRegistered(Context context, String registrationId) {
    	app = (SnomadaApp) getApplication();
    	sharedPreferences = context.getSharedPreferences(Constant.Settings.GLOBAL_SETTINGS.name(), Context.MODE_PRIVATE);
    	Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM");
        ServerUtilities.register(context, registrationId,app.getAppInfo().userId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        ServerUtilities.unregister(context, registrationId);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("price");        
        displayMessage(context, message);
        generateNotification(context, message);
    }

     @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        generateNotification(context, message);
    }

     @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,   errorId));
        return super.onRecoverableError(context, errorId);
    }
	@SuppressWarnings("deprecation")
	private static void generateNotification(final Context context,String message) {
		
		Log.e("snomada", message);
		try {
			JSONObject json = new JSONObject(message);
			int status = Integer.parseInt(json.getString("status"));
			/*app.isZoom = false;*/
			
			if (status==Constant.TRACKING_PUSH_NOTIFICATION) {
				TrackLocation.createInstance(context,app);
				String noti_msg                         = json.getString("message");
				locationManager                         = (LocationManager) context	.getSystemService(Context.LOCATION_SERVICE);
					Intent notificationIntent           = new Intent(context,	HomeView.class);
					notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
					PendingIntent intent                = PendingIntent.getActivity(context,0, new Intent(), 0);
					showNotification(context, noti_msg,intent);
				
			}else if (status==Constant.SKI_PATROL_PUSH_NOTIFICATION) {
				if(!Global.isAppForeground){
					String patroler_id                  = json.getString("patroler_id");
					String latitude                     = json.getString("lat");
					String longitude                    = json.getString("lng");
					String fname                        = json.getString("fname");
					String lname                        = json.getString("lname"); 					
					
					SnowmadaDbAdapter mDbAdapter        = SnowmadaDbAdapter.databaseHelperInstance(context);
						if (mDbAdapter.getSkiPetrolRowCount() > 0) {
							    mDbAdapter.updateSkiPetrolInfo(patroler_id,fname, lname, latitude,	longitude);
						} else {
							   mDbAdapter.insertSkiPetrolInfo(patroler_id,fname, lname, latitude,longitude);
						}
						Intent intent = new Intent(	"SKI_PATROL_INTENT");				
						context.sendBroadcast(intent);
				}
				
				

				
				
			}else  if(status==Constant.CHAT_PUSH_NOTIFICATION) {
				 String msg          = json.getString("chatmessage");
				 String name         = json.getString("name");
				 String sender_fb_id = json.getString("sender_fb_id");				 
				 weiw_message        = name+":"+msg;				 
				if(Global.isAppForeground){					
					if(app.isChatActive){
						if(!name.equalsIgnoreCase(app.IMname)){// Third person ping							
							Intent notificationIntent = new Intent(context,	HomeView.class);
							notificationIntent.putExtra("sender_fb_id", sender_fb_id);
							notificationIntent.putExtra("sender_name", name);
							notificationIntent.putExtra("event", "chat");
							notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_SINGLE_TOP);
							PendingIntent intent = PendingIntent.getActivity(context,0,notificationIntent , 0);
							showNotification(context, weiw_message,intent);							
						}
						
					}else{
							SnowmadaDbAdapter.databaseHelperInstance(context).insertChatMessage(sender_fb_id, name,"1", msg);
							Intent notificationIntent = new Intent(context,	HomeView.class);
							notificationIntent.putExtra("sender_fb_id", sender_fb_id);
							notificationIntent.putExtra("sender_name", name);
							notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
							PendingIntent intent = PendingIntent.getActivity(context,0,  notificationIntent , 0);
							showNotification(context, weiw_message,intent);
							
					}
				}else{//  myApp is background
					
						Intent notificationIntent = new Intent(context,	HomeView.class);
						notificationIntent.putExtra("sender_fb_id", sender_fb_id);
						notificationIntent.putExtra("sender_name", name);
						notificationIntent.putExtra("event", "chat");
						notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
						PendingIntent intent = PendingIntent.getActivity(context,0,notificationIntent , 0);
						showNotification(context, weiw_message,intent);
						
				}
			}else if(status==Constant.FRIEND_REQUEST_COME_PUSH_NOTIFICATION){//Status five for current friend request receive
				 String sender_id          = json.getString("senderid");
				 String sender_name        = json.getString("sendername");
				 String msg                = "send friend request";				 
				 weiw_message              = sender_name+" "+msg;				 
				 PendingIntent intent      = PendingIntent.getActivity(context,0, new Intent() , 0);					
				    showNotification(context, weiw_message,intent);				 
			 }else if(status==Constant.FRIEND_REQUEST_ACCEPT_PUSH_NOTIFICATION){//Request acknowledgment				
				 app.isWebServiceCallForRefreshFriendList = true;
				 String friend_name = json.getString("friend_name");
				 String msg = "has accept your friend request";				 
				 weiw_message = friend_name+" "+msg;				 
					PendingIntent intent = PendingIntent.getActivity(context,0, new Intent(), 0);
					showNotification(context, weiw_message,intent);
					
			 }else if(status==Constant.CREATE_MEETUP_PUSH_NOTIFICATION){//Request acknowledgment
				
				 String msg = json.getString("meetup_noti_msg");
				 String name = json.getString("name");
				 String lat = json.getString("lat");
				 String lng = json.getString("lng");
				 weiw_message = msg+" "+name;
				 if(!Global.isAppForeground){
				     Global.meetupzoomFlag = true;
					 SnowmadaDbAdapter mDbAdapter = SnowmadaDbAdapter.databaseHelperInstance(context);
					 mDbAdapter.insertMeetUpInfo(lat, lng, "1");
				 }else{
				     Global.meetupzoomFlag = true;
				     SnowmadaDbAdapter mDbAdapter = SnowmadaDbAdapter.databaseHelperInstance(context);
					 mDbAdapter.insertMeetUpInfo(lat, lng, "1");
				 }
				 	Intent notificationIntent = new Intent(context,	HomeView.class);
					notificationIntent.putExtra("event", "meetup");
					notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_SINGLE_TOP);
					PendingIntent intent = PendingIntent.getActivity(context,0,notificationIntent , 0);					
					showNotification(context, weiw_message,intent);
					
			 }
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
    
    public static boolean isGPSenabled(){
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    
    public static void showNotification(Context context,String noti_msg,PendingIntent intent){    	
    	int icon = R.drawable.app_logo;
		long when = System.currentTimeMillis();
		String title = context.getString(R.string.app_name);
		Notification notification = new Notification(icon,noti_msg, when);
		NotificationManager notificationManager = (NotificationManager) context	.getSystemService(Context.NOTIFICATION_SERVICE);
		notification.setLatestEventInfo(context, title, noti_msg,intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);
    }
   
}