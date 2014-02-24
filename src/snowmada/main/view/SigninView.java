package snowmada.main.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.facebook.*;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.*;
import com.strapin.Enum.URL;
import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.global.Global;
import com.strapin.network.KlHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SigninView extends BaseView {

   private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";
    private LoginButton loginButton;
    private PendingAction pendingAction = PendingAction.NONE;
    private GraphUser user;
    private boolean isUiUpdateCall = false; 
   
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
        
       
        
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
      

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
       

       
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();

        // Call the 'activateApp' method to log an myApp event for use in analytics and advertising reporting.  Do so in
        // the onResume methods of the primary Activities that an myApp may be launched into.
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
    	        	showProgressDailog();
    	        	if(db.getRowCount()>0){
    					db.updateUserInfo(user.getId(),user.getFirstName(),user.getLastName());
    				}else{
    					db.insertUserInfo(user.getId(),user.getFirstName(),user.getLastName());
    				}
    	        	myApp.getAppInfo().setSession(true);
    	        	Global.mDob = user.getBirthday();
    				myApp.getAppInfo().setSession(true);
    				getFriendList();
    				new SignInWeb().execute();
    	        	
    	        } else {
    	         
    	        }
    	}
       
    }

    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case POST_PHOTO:
                 break;
            case POST_STATUS_UPDATE:
                break;
        }
    }


     public class SignInWeb extends AsyncTask<String, Void, Boolean>{		
		@Override
		protected Boolean doInBackground(String... params) {			
		  	try {
		  		JSONObject jsonObject = new JSONObject();
		  		jsonObject.put("fbid", db.getUserFbID());
		  		jsonObject.put("fname", db.getUserFirstName());
		  		jsonObject.put("lname", db.getUserLastName());
		  		JSONObject json = KlHttpClient.SendHttpPost(URL.LOGIN.getUrl(), jsonObject);
		    return json.getBoolean("status");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Boolean status) {
			if(status){
			dismissProgressDialog();
			startActivity(new Intent(SigninView.this, HomeView.class));
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

    	Request request = new Request(session, "/fql", params,                   
    	        HttpMethod.GET,                 
    	        new Request.Callback(){       
    	    public void onCompleted(Response response) {
    	        

    	        try{
    	        	Log.i("TAG", "Result: " + response.toString());
        	        GraphObject graphObject = response.getGraphObject();

                    JSONObject jsonObject = graphObject.getInnerJSONObject();
                    JSONArray array = jsonObject.getJSONArray("data");
                    if(db.getFbFriendCount()>0){
                    	db.emptyFriendTable();
                    }
                    
                    for(int i1=0; i1<array.length(); i1++){
						JSONObject c = array.getJSONObject(i1);
						String id = c.getString("uid");							
						String name = c.getString("name");
						db.insertfacebookFriends(id, name);
						
						
					}
					
    	        }catch(JSONException e){
    	            e.printStackTrace();
    	        }
    	    }                  
    	}); 
    	Request.executeBatchAsync(request); 
    }
}
