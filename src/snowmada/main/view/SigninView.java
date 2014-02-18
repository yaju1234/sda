package snowmada.main.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ViewGroup;
import com.facebook.*;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.widget.*;
import com.strapin.application.SnomadaApp;
import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.global.Global;
import com.strapin.network.KlHttpClient;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SigninView extends FragmentActivity {

    private static final String PERMISSION = "publish_actions";
    private static final Location SEATTLE_LOCATION = new Location("") {
        {
            setLatitude(47.6097);
            setLongitude(-122.3331);
        }
    };
    private SnomadaApp app = null;
    private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";
    private boolean isFetching = false;
    private LoginButton loginButton;
     private PendingAction pendingAction = PendingAction.NONE;
    private ViewGroup controlsContainer;
    private GraphUser user;
    private GraphPlace place;
    private List<GraphUser> tags;
    private boolean canPresentShareDialog;
    private SnowmadaDbAdapter mDbAdapter;
    private boolean isUiUpdateCall = false; 
    private ProgressDialog mDialog;
  
    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }
    private UiLifecycleHelper uiHelper;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d("HelloFacebook", "Success!");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        app = (SnomadaApp) getApplication();
        
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        mDbAdapter = SnowmadaDbAdapter.databaseHelperInstance(getApplicationContext());

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }

        setContentView(R.layout.signin);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        

    	Session session = Session.getActiveSession();
    	if (session != null){
    	     session.closeAndClearTokenInformation();
    	     session = null;
    	     Log.i("TAG", "cleared session");
    	}

     
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
             	
            	SigninView.this.user = user;
            	isUiUpdateCall = true;
                updateUI();
                handlePendingAction();
            }
        });
       

        canPresentShareDialog = FacebookDialog.canPresentShareDialog(this,
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising reporting.  Do so in
        // the onResume methods of the primary Activities that an app may be launched into.
        AppEventsLogger.activateApp(this);

       // updateUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (pendingAction != PendingAction.NONE &&
                (exception instanceof FacebookOperationCanceledException ||
                exception instanceof FacebookAuthorizationException)) {
                new AlertDialog.Builder(SigninView.this)
                    .setTitle(R.string.cancelled)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.ok, null)
                    .show();
            pendingAction = PendingAction.NONE;
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
            handlePendingAction();
        }
       // updateUI();
    }

    private void updateUI() {
    	if(isUiUpdateCall){
    		 Session session = Session.getActiveSession();
    	        Log.e("Session", ""+session.isOpened());
    	        boolean enableButtons = (session != null && session.isOpened());

    	     
    	        if (enableButtons && user != null) {
    	        	mDialog = new ProgressDialog(SigninView.this);
    				mDialog.setMessage("Please wait...");
    				mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    				mDialog.setIndeterminate(true);
    				mDialog.setCancelable(false);
    				mDialog.show();
    	        	if(mDbAdapter.getRowCount()>0){
    					mDbAdapter.updateUserInfo(user.getId(),user.getFirstName(),user.getLastName());
    				}else{
    					mDbAdapter.insertUserInfo(user.getId(),user.getFirstName(),user.getLastName());
    				}
    	        	app.getAppInfo().setSession(true);
    	        	/*if(mDbAdapter.getSessionValueRowCount()>0){
    	        		mDbAdapter.updateSession(1);
    	        	}else{
    	        		mDbAdapter.insertSessionvalue(1);
    	        	}*/
    				Global.mDob = user.getBirthday();
    				Global.isSessionValid = true;
    				getFriendList();
    				new SavefacebookCredentials().execute();
    	        	
    	        } else {
    	         
    	        }
    	}
       
    }

    @SuppressWarnings("incomplete-switch")
    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case POST_PHOTO:
             //   postPhoto();
                break;
            case POST_STATUS_UPDATE:
                //postStatusUpdate();
                break;
        }
    }

    private interface GraphObjectWithId extends GraphObject {
        String getId();
    }

    private void showPublishResult(String message, GraphObject result, FacebookRequestError error) {
        String title = null;
        String alertMessage = null;
        if (error == null) {
            title = getString(R.string.success);
            String id = result.cast(GraphObjectWithId.class).getId();
            alertMessage = getString(R.string.successfully_posted_post, message, id);
        } else {
            title = getString(R.string.error);
            alertMessage = error.getErrorMessage();
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(alertMessage)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void onClickPostStatusUpdate() {
        performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
    }

    private FacebookDialog.ShareDialogBuilder createShareDialogBuilder() {
        return new FacebookDialog.ShareDialogBuilder(this)
                .setName("Hello Facebook")
                .setDescription("The 'Hello Facebook' sample application showcases simple Facebook integration")
                .setLink("http://developers.facebook.com/android");
    }

    private void postStatusUpdate() {
        if (canPresentShareDialog) {
            FacebookDialog shareDialog = createShareDialogBuilder().build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
        } else if (user != null && hasPublishPermission()) {
            final String message = getString(R.string.status_update, user.getFirstName(), (new Date().toString()));
            Request request = Request
                    .newStatusUpdateRequest(Session.getActiveSession(), message, place, tags, new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {
                            showPublishResult(message, response.getGraphObject(), response.getError());
                        }
                    });
            request.executeAsync();
        } else {
            pendingAction = PendingAction.POST_STATUS_UPDATE;
        }
    }




    private boolean hasPublishPermission() {
        Session session = Session.getActiveSession();
        return session != null && session.getPermissions().contains("publish_actions");
    }

    private void performPublish(PendingAction action, boolean allowNoSession) {
        Session session = Session.getActiveSession();
        if (session != null) {
            pendingAction = action;
            if (hasPublishPermission()) {
                // We can do the action right away.
                handlePendingAction();
                return;
            } else if (session.isOpened()) {
                // We need to get new permissions, then complete the action when we get called back.
                session.requestNewPublishPermissions(new Session.NewPermissionsRequest(this, PERMISSION));
                return;
            }
        }

        if (allowNoSession) {
            pendingAction = action;
            handlePendingAction();
        }
    }
    
    public class SavefacebookCredentials extends AsyncTask<String, Void, Boolean>{		
		protected void onPreExecute() {
			
			
		}
		@Override
		protected Boolean doInBackground(String... params) {			
		  	try {
		  		JSONObject jsonObject = new JSONObject();
		  		jsonObject.put("fbid", mDbAdapter.getUserFbID());
		  		jsonObject.put("fname", mDbAdapter.getUserFirstName());
		  		jsonObject.put("lname", mDbAdapter.getUserLastName());
		  		Log.e("JSON", jsonObject.toString());
		  		JSONObject json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/login.php", jsonObject);
		    return json.getBoolean("status");
				
			} catch (Exception e) {
				//mDialog.dismiss();
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Boolean status) {
			if(status){
				if(mDialog.isShowing()){
					mDialog.dismiss();
				}
				Intent i = new Intent(SigninView.this, HomeView.class);
				Global.isSessionValid = true;
				startActivity(i);
				SigninView.this.finish();
			}
			
		}	
		
		 

	}
    public void getFriendList(){
    	String fqlQuery = "SELECT uid,name,pic_square FROM user WHERE uid IN " +
    	        "(SELECT uid2 FROM friend WHERE uid1 = me())";

    	Bundle params = new Bundle();
    	params.putString("q", fqlQuery);
    	Session session = Session.getActiveSession();

    	Request request = new Request(session,
    	        "/fql",                         
    	        params,                         
    	        HttpMethod.GET,                 
    	        new Request.Callback(){       
    	    public void onCompleted(Response response) {
    	        

    	        try{
    	        	Log.i("TAG", "Result: " + response.toString());
        	        GraphObject graphObject = response.getGraphObject();

                    JSONObject jsonObject = graphObject.getInnerJSONObject();
                    JSONArray array = jsonObject.getJSONArray("data");
                    /*Log.e("Friend Array", array.toString());
    	        		Global.setFriendJSOn(array);*/
                    if(mDbAdapter.getFbFriendCount()>0){
                    	mDbAdapter.emptyFriendTable();
                    }
                    
                    for(int i1=0; i1<array.length(); i1++){
						JSONObject c = array.getJSONObject(i1);
						String id = c.getString("uid");							
						String name = c.getString("name");
						mDbAdapter.insertfacebookFriends(id, name);
						//mAddFriendArr.add(new AddFriendBean(id, name));
						
					}
					
    	        }catch(JSONException e){
    	            e.printStackTrace();
    	        }
    	    }                  
    	}); 
    	Request.executeBatchAsync(request); 
    }
}
