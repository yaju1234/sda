package snowmada.main.view;

import static snowmada.main.view.CommonUtilities.SENDER_ID;
import static snowmada.main.view.CommonUtilities.displayMessage;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;
import com.strapin.application.SnomadaApp;
import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.global.Global;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	private static LocationManager locationManager;
	private static Handler handler = new Handler();
	private static Runnable runnable;
	private static long lastUsed;
	private static long idle=0;
	private SnowmadaDbAdapter mDbAdapter;
	public static SnomadaApp app = null;

    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
    	mDbAdapter = SnowmadaDbAdapter.databaseHelperInstance(context);
    	app = (SnomadaApp) getApplication();
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM");
        ServerUtilities.register(context, registrationId,mDbAdapter.getUserFbID());
    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("price");
        
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
	private static void generateNotification(final Context context,String message) {
		
		Log.e("Push notification", message);
		//Toast.makeText(context, "Your Emergency Becon has been Activated Ski Patrol Has Been Notified", Toast.LENGTH_LONG).show();
		try {
			JSONObject json = new JSONObject(message);
			Global.isZoomAtUSerLocationFirstTime = false;
			if (json.getString("status").equalsIgnoreCase("1")) {
				TrackLocation.databaseHelperInstance(context);
				//TrackLocation.databaseHelperInstance(context).requestUpdate();
				String noti_msg = json.getString("message");

				locationManager = (LocationManager) context
						.getSystemService(Context.LOCATION_SERVICE);
						handler.removeCallbacks(runnable);

					lastUsed = System.currentTimeMillis();
					idle = 0;
					runnable = new Runnable() {
						public void run() {
							handler.postDelayed(runnable, 3000);

							idle = System.currentTimeMillis() - lastUsed;
							Log.i("idle", "" + idle);

							if (idle >= 5*60*1000) {
								//TrackLocation.databaseHelperInstance(context).removeLocationUpdate();
								handler.removeCallbacks(runnable);
								}
						}
					};
					handler.postDelayed(runnable, 1000);

					int icon = R.drawable.app_logo;
					long when = System.currentTimeMillis();
					NotificationManager notificationManager = (NotificationManager) context
							.getSystemService(Context.NOTIFICATION_SERVICE);
					Notification notification = new Notification(icon,
							noti_msg, when);

					String title = context.getString(R.string.app_name);

					Intent notificationIntent = new Intent(context,
							HomeView.class);
					// set intent so it does not start a new activity
					notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_SINGLE_TOP);
					PendingIntent intent = PendingIntent.getActivity(context,
							0, new Intent()/* notificationIntent */, 0);
					notification.setLatestEventInfo(context, title, noti_msg,
							intent);
					notification.flags |= Notification.FLAG_AUTO_CANCEL;

					// Play default notification sound
					notification.defaults |= Notification.DEFAULT_SOUND;

					// notification.sound = Uri.parse("android.resource://" +
					// context.getPackageName() + "your_sound_file_name.mp3");

					// Vibrate if vibrate is enabled
					notification.defaults |= Notification.DEFAULT_VIBRATE;
					notificationManager.notify(0, notification);

				//}
			}else if (json.getString("status").equalsIgnoreCase("2")) {
				Log.e("json.getStringgfg", "json.getStringgfg");

				String latitude = json.getString("lat");
				String longitude = json.getString("lng");
				String Fname = json.getString("fname");
				String Lname = json.getString("lname");

				SnowmadaDbAdapter mDbAdapter = SnowmadaDbAdapter
						.databaseHelperInstance(context);
				if (mDbAdapter.getSkiPetrolRowCount() > 0) {
					mDbAdapter.updateSkiPetrolInfo(Fname, Lname, latitude,
							longitude);
				} else {
					mDbAdapter.insertSkiPetrolInfo(Fname, Lname, latitude,
							longitude);
				}
				Intent intent = new Intent(
						"SNOWMADA_GET_CURRENT_LOCATION_INTENT");
				Log.e("json.getStringgfg1111", "json.getStringgfg111");
			
				context.sendBroadcast(intent);
				
			}else  if(json.getString("status").equalsIgnoreCase("4")) {
				 String msg = json.getString("chatmessage");
				 String name = json.getString("name");
				 String sender_fb_id = json.getString("sender_fb_id");
				 
				if(/*Global.isApplicationForeground*/app.getAppInfo().isAppForeground){
					
					if(Global.isChatActive){
						if(!name.equalsIgnoreCase(Global.mChatUserName)){// Third person ping
							
							int icon = R.drawable.app_logo;
							long when = System.currentTimeMillis();
							NotificationManager notificationManager = (NotificationManager) context
									.getSystemService(Context.NOTIFICATION_SERVICE);
							Notification notification = new Notification(icon,
									name+":"+msg, when);

							String title = context.getString(R.string.app_name);

							Intent notificationIntent = new Intent(context,
									HomeView.class);
							notificationIntent.putExtra("sender_fb_id", sender_fb_id);
							notificationIntent.putExtra("sender_name", name);
							notificationIntent.putExtra("event", "chat");
							// set intent so it does not start a new activity
							notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_SINGLE_TOP);
							PendingIntent intent = PendingIntent.getActivity(context,
									0, /*new Intent()*/ notificationIntent , 0);
							notification.setLatestEventInfo(context, title, name+":"+msg,
									intent);
							notification.flags |= Notification.FLAG_AUTO_CANCEL;

							// Play default notification sound
							notification.defaults |= Notification.DEFAULT_SOUND;

							// notification.sound = Uri.parse("android.resource://" +
							// context.getPackageName() + "your_sound_file_name.mp3");

							// Vibrate if vibrate is enabled
							notification.defaults |= Notification.DEFAULT_VIBRATE;
							notificationManager.notify(0, notification);
						}
						
					}else{
						// When chat is not active but app is foreground
						 
						//if(!SnowmadaDbAdapter.databaseHelperInstance(context).isUserMessageExist(sender_fb_id)){
							SnowmadaDbAdapter.databaseHelperInstance(context).insertChatMessage(sender_fb_id, name,"1", msg);
						/*}else{
							SnowmadaDbAdapter.databaseHelperInstance(context).updateChatMessageInfo(sender_fb_id, msg);
						}
						*/
						int icon = R.drawable.app_logo;
							long when = System.currentTimeMillis();											
							NotificationManager notificationManager = (NotificationManager) context
									.getSystemService(Context.NOTIFICATION_SERVICE);
							Notification notification = new Notification(icon,
									name+":"+msg, when);

							String title = context.getString(R.string.app_name);

							Intent notificationIntent = new Intent(context,
									HomeView.class);
							notificationIntent.putExtra("sender_fb_id", sender_fb_id);
							notificationIntent.putExtra("sender_name", name);
							// set intent so it does not start a new activity
							notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_SINGLE_TOP);
							PendingIntent intent = PendingIntent.getActivity(context,
									0, /*new Intent()*/ notificationIntent , 0);
							notification.setLatestEventInfo(context, title, name+":"+msg,
									intent);
							notification.flags |= Notification.FLAG_AUTO_CANCEL;

							// Play default notification sound
							notification.defaults |= Notification.DEFAULT_SOUND;

							// notification.sound = Uri.parse("android.resource://" +
							// context.getPackageName() + "your_sound_file_name.mp3");

							// Vibrate if vibrate is enabled
							notification.defaults |= Notification.DEFAULT_VIBRATE;
							notificationManager.notify(0, notification);
					}
				}else{//  app is background

					
					 int icon = R.drawable.app_logo;
						long when = System.currentTimeMillis();
						NotificationManager notificationManager = (NotificationManager) context
								.getSystemService(Context.NOTIFICATION_SERVICE);
						Notification notification = new Notification(icon,
								name+":"+msg, when);

						String title = context.getString(R.string.app_name);

						Intent notificationIntent = new Intent(context,
								HomeView.class);
						notificationIntent.putExtra("sender_fb_id", sender_fb_id);
						notificationIntent.putExtra("sender_name", name);
						notificationIntent.putExtra("event", "chat");
						// set intent so it does not start a new activity
						notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_SINGLE_TOP);
						PendingIntent intent = PendingIntent.getActivity(context,
								0, /*new Intent()*/ notificationIntent , 0);
						notification.setLatestEventInfo(context, title, name+":"+msg,
								intent);
						notification.flags |= Notification.FLAG_AUTO_CANCEL;

						// Play default notification sound
						notification.defaults |= Notification.DEFAULT_SOUND;

						// notification.sound = Uri.parse("android.resource://" +
						// context.getPackageName() + "your_sound_file_name.mp3");

						// Vibrate if vibrate is enabled
						notification.defaults |= Notification.DEFAULT_VIBRATE;
						notificationManager.notify(0, notification);
				
				}
			}else if(json.getString("status").equalsIgnoreCase("5")){//Status five for current friend request receive
				 String sender_id = json.getString("senderid");
				 String sender_name = json.getString("sendername");
				 String msg = "send friend request";
				 
				 
				 Log.e("pending request", "pending request");
				 int icon = R.drawable.app_logo;
					long when = System.currentTimeMillis();
					NotificationManager notificationManager = (NotificationManager) context
							.getSystemService(Context.NOTIFICATION_SERVICE);
					Notification notification = new Notification(icon,
							sender_name+" "+msg, when);

					String title = context.getString(R.string.app_name);

					/*Intent notificationIntent = new Intent(context,
							HomeView.class);
					notificationIntent.putExtra("sender_fb_id", sender_fb_id);
					notificationIntent.putExtra("sender_name", name);
					// set intent so it does not start a new activity
					notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
					PendingIntent intent = PendingIntent.getActivity(context,
							0, new Intent() /*notificationIntent*/ , 0);
					notification.setLatestEventInfo(context, title, sender_name+" "+msg,
							intent);
					notification.flags |= Notification.FLAG_AUTO_CANCEL;

					// Play default notification sound
					notification.defaults |= Notification.DEFAULT_SOUND;

					// notification.sound = Uri.parse("android.resource://" +
					// context.getPackageName() + "your_sound_file_name.mp3");

					// Vibrate if vibrate is enabled
					notification.defaults |= Notification.DEFAULT_VIBRATE;
					notificationManager.notify(0, notification);
				 
			 }else if(json.getString("status").equalsIgnoreCase("6")){//Request acknowledgment
				
				 Global.isAddSnowmadaFriend = true;
				 String friend_name = json.getString("friend_name");
				 String msg = "has accept your friend request";
				 
				 Log.e("acknowledgment", "acknowledgment");
				 
				 int icon = R.drawable.app_logo;
					long when = System.currentTimeMillis();
					NotificationManager notificationManager = (NotificationManager) context
							.getSystemService(Context.NOTIFICATION_SERVICE);
					Notification notification = new Notification(icon,
							friend_name+" "+msg, when);

					String title = context.getString(R.string.app_name);

					/*Intent notificationIntent = new Intent(context,
							HomeView.class);
					notificationIntent.putExtra("sender_fb_id", sender_fb_id);
					notificationIntent.putExtra("sender_name", name);
					// set intent so it does not start a new activity
					notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
					PendingIntent intent = PendingIntent.getActivity(context,
							0, new Intent() /*notificationIntent*/ , 0);
					notification.setLatestEventInfo(context, title, friend_name+":"+msg,
							intent);
					notification.flags |= Notification.FLAG_AUTO_CANCEL;

					// Play default notification sound
					notification.defaults |= Notification.DEFAULT_SOUND;

					// notification.sound = Uri.parse("android.resource://" +
					// context.getPackageName() + "your_sound_file_name.mp3");

					// Vibrate if vibrate is enabled
					notification.defaults |= Notification.DEFAULT_VIBRATE;
					notificationManager.notify(0, notification);
			 }else if(json.getString("status").equalsIgnoreCase("7")){//Request acknowledgment
				
				 String msg = json.getString("meetup_noti_msg");
				 String name = json.getString("name");
				 
				 int icon = R.drawable.app_logo;
					long when = System.currentTimeMillis();
					NotificationManager notificationManager = (NotificationManager) context
							.getSystemService(Context.NOTIFICATION_SERVICE);
					Notification notification = new Notification(icon,
							name+" "+msg, when);

					String title = context.getString(R.string.app_name);

					Intent notificationIntent = new Intent(context,
							HomeView.class);
					notificationIntent.putExtra("event", "meetup");
					// set intent so it does not start a new activity
					notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_SINGLE_TOP);
					PendingIntent intent = PendingIntent.getActivity(context,
							0, /*new Intent()*/ notificationIntent , 0);
					notification.setLatestEventInfo(context, title, name+":"+msg,
							intent);
					notification.flags |= Notification.FLAG_AUTO_CANCEL;

					// Play default notification sound
					notification.defaults |= Notification.DEFAULT_SOUND;

					// notification.sound = Uri.parse("android.resource://" +
					// context.getPackageName() + "your_sound_file_name.mp3");

					// Vibrate if vibrate is enabled
					notification.defaults |= Notification.DEFAULT_VIBRATE;
					notificationManager.notify(0, notification);
			 }
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
    
    public static boolean isGPSenabled(){
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}