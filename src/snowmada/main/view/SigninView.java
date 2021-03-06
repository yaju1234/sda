package snowmada.main.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.facebook.*;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.*;
import com.strapin.Enum.URL;
import com.strapin.global.Constant;
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
    private Button mSignup;
    private EditText et_username,et_password;
    private ProgressBar signin_progress;
    private Button btn_login;
    private String logintype;
    private String fb_fname;
    private String fb_lname;
    private String birthday;
    private String fb_id;
    private String TAG = "snomada";
   
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
            String name   = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }
        setContentView(R.layout.signin);  

        loginButton = (LoginButton) findViewById(R.id.login_button);
        mSignup     = (Button)findViewById(R.id.btn_sign_up);
        btn_login   = (Button)findViewById(R.id.btn_login);
        et_username = (EditText)findViewById(R.id.input_user_name);
        et_password = (EditText)findViewById(R.id.input_password);  
        signin_progress = (ProgressBar)findViewById(R.id.signin_progress);
        
        btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(fieldValidation()){
					logintype = Constant.NORMAL_LOGIN;
					myApp.getAppInfo().setLoginType("N"); //  Set Login type for normal login   NORMAIL LOGIN: N
					showProgressDailog();
					new SignInWeb().execute();
				}
				
			}
		});
        
        mSignup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				Intent i = new Intent(SigninView.this, SignUpView.class);
				startActivity(i);
			}
		});
        

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
            	myApp.getAppInfo().setLoginType("F");//  Set Login type for facebook login   FACEBOOK LOGIN: F
                updateUI();
                handlePendingAction();
            }
        });
       

       
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
        AppEventsLogger.activateApp(this);
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
     
    }

    private void updateUI() {
    	if(isUiUpdateCall){
    		isUiUpdateCall = false;
    		 Session session = Session.getActiveSession();
    	        Log.e("Session", ""+session.isOpened());
    	        boolean enableButtons = (session != null && session.isOpened());    	     
    	        if (enableButtons && user != null) {
    	        	//showProgressDailog();
    	            	signin_progress.setVisibility(View.VISIBLE);
    	        	Log.e("user", user.toString());
    	        	fb_fname = user.getFirstName();
    	        	fb_lname = user.getLastName();
    	        	birthday = user.getBirthday();
    	        	fb_id = user.getId();
    	        	//getFriendList();
    	        	logintype = Constant.FACEBOOK_LOGIN;
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
			boolean flg = false;
		  	try {
		  		JSONObject request = new JSONObject();
		  	if(logintype.equalsIgnoreCase("F")){		  		
		  		request.put("fbid", fb_id);
		  		request.put("fname", fb_fname);
		  		request.put("lname", fb_lname);
		  		request.put("birthday", birthday);
		  		request.put("usertype", "F");
		  	}else{
		  		request.put("user_id", et_username.getText().toString().trim());
				request.put("password", et_password.getText().toString().trim());
				request.put("usertype", "N");
		  	}
		  	Log.e("TAG13", "Sign in response=====>>"+request.toString());
		  		JSONObject response = KlHttpClient.SendHttpPost(URL.LOGIN.getUrl(), request);
		  		Log.e("TAG13", "Sign in response=====>>"+response.toString());
		  		System.out.println("!-- Sign in response "+response.toString());
		  		if(response!=null){
		  			flg =  response.getBoolean("status");
		  			if(flg){
		  				String imgurl;
		  				if(!response.isNull("image")){
		  					imgurl = URL.IMAGE_PATH.getUrl()+response.getString("image");		  					
		  				}else{
		  					imgurl = "https://graph.facebook.com/" + response.getString("user_id")	+ "/picture";
		  				}
		  				myApp.getAppInfo().setUserInfo(response.getString("first_name"),response.getString("last_name"), response.getString("user_id"),imgurl);
	    	        	myApp.getAppInfo().setSession(true);
		  			}
		  			
		  		}	   
				
			} catch (Exception e) {
				//dismissProgressDialog();
				e.printStackTrace();
			}
			return flg;
		}
		@Override
		protected void onPostExecute(Boolean status) {
			//dismissProgressDialog();
		    signin_progress.setVisibility(View.INVISIBLE);
			if(status){
			// getFriendList();
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

    	Request request = new Request(session, "/fql", params, HttpMethod.GET,    new Request.Callback(){       
    	    public void onCompleted(Response response) {        

    	        try{
    	            Log.i("TAG15", "Result: " + response.toString());
    	            System.out.println("!-- Friend List "+response.toString());
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
    
    public boolean fieldValidation(){
    	boolean flg = true;
    	if(et_username.getText().toString().trim().equals("")){
    		et_username.setError("Please enter username");
    		flg = false;
    	}
    	
    	if(et_password.getText().toString().trim().equals("")){
    		et_password.setError("Please enter password");
    		flg = false;
    	}
    	return flg;
    }
   
}
