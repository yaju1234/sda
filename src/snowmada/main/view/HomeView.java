package snowmada.main.view;

import static com.strapin.common.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.strapin.common.CommonUtilities.EXTRA_MESSAGE;
import static com.strapin.common.CommonUtilities.SENDER_ID;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.strapin.Enum.URL;
import com.strapin.Interface.IHome;
import com.strapin.Util.ImageLoader;
import com.strapin.adapter.AddFriendAdapter;
import com.strapin.adapter.ChatAdapter;
import com.strapin.adapter.DealsAdapter;
import com.strapin.adapter.InviteFriendAdapter;
import com.strapin.adapter.OfflineMessageAdapter;
import com.strapin.application.SnomadaApp;
import com.strapin.bean.AppUserInfoBean;
import com.strapin.bean.ChatBean;
import com.strapin.bean.DealsBean;
import com.strapin.bean.AppusersBean;
import com.strapin.bean.MeetUpBean;
import com.strapin.bean.NewMessage;
import com.strapin.bean.Patrol;
import com.strapin.common.AlertDialogManager;
import com.strapin.common.ConnectionDetector;
import com.strapin.common.ServerUtilities;
import com.strapin.common.WakeLocker;
import com.strapin.global.Constants;
import com.strapin.global.Global;
import com.strapin.network.KlHttpClient;
import com.strapin.presenter.HomePresenter;

public class HomeView extends BaseView implements IHome {

	private Sliding      mViewSlider;

	private VerticalTextView mBtnSlider;
	
	public Button        btnaddFriend;
	public Button        btnInviteFriend;
	public Button        btnPendingReq;
	public Button        btnMenu;

	public TextView      lblUserName;
	public TextView      lblCountPendingReq;
	public TextView      lblActiveChatFriend;	
	public TextView      lblDlgDisplayTime;
	public TextView      tvDisplayDate;
	public TextView      tv_profile_title;
	public TextView      tvTitleLocalDeals;
	
	private TextView     tvTitleTab;

	private EditText      mSearchAddFriend;
	private EditText      et_search_invite_friend;
	private EditText      mEtInputChatMsg;

	public ImageLoader    imageLoader;

	public ListView       lv_friend_list;
	public ListView      mDealsList;
	public ListView      mAddFriendList;
	public ListView      mRequestList;
	public ListView      mChatList;
	public ListView      mLvMessageList;
	public ListView      mLvInviteFriendList;

	public HomePresenter  presenter;

	public GoogleMap      map;

	public LinearLayout   ll_meetup;
	public LinearLayout   ll_chat;
	public LinearLayout   ll_gooddeals;
	public LinearLayout   ll_track;
	public LinearLayout   ll_addfriend;
	public LinearLayout   ll_viewprofile;

	private LinearLayout  mGreenBarMeetUploc;
	private LinearLayout  mGreenBarChatLive;
	private LinearLayout  mGreenBarGoodDeals;
	private LinearLayout  mGreenBarTrack;
	private LinearLayout  mGreenBarAdd;
	private LinearLayout  mGreenBarViewProfile;

	private LinearLayout  mMassageLayout;
	private LinearLayout  mLayoutMsgNotiList;
	private LinearLayout  mChatSendButton;
	private LinearLayout  mReqTab;

	private RelativeLayout   mProfileLayout;
	private RelativeLayout   mDealsLayout;
	private RelativeLayout   mAddFriendLayout;
	private RelativeLayout   mChatLayout;
	private RelativeLayout   mAddFriendSearchLayout;
	private RelativeLayout   mInviteFriendSearch;
	private RelativeLayout   mRlProgressBarLayout;

	private ImageView mUserImage;
	private ImageView mProfileImage;
	private ImageView mIvSkyPatrol;
	

	private Dialog menu_dialog, meetupUserDlg;

	private ArrayList<DealsBean> mDealsArr                     = new ArrayList<DealsBean>();
	private ArrayList<AppUserInfoBean> mAddFriendArr             = new ArrayList<AppUserInfoBean>();
	private ArrayList<AppUserInfoBean> mAddFriendSearchArr       = new ArrayList<AppUserInfoBean>();

	private ArrayList<AppUserInfoBean> mInviteFriendArr       = new ArrayList<AppUserInfoBean>();
	private ArrayList<AppUserInfoBean> mInviteFriendSearchArr = new ArrayList<AppUserInfoBean>();
	private ArrayList<AppusersBean> mContactList           = new ArrayList<AppusersBean>();
	private ArrayList<AppUserInfoBean> mAppUserList           = new ArrayList<AppUserInfoBean>();
	private ArrayList<NewMessage> msg                         = new ArrayList<NewMessage>();

	private DealsAdapter mAdapter;
	private AddFriendAdapter mAddFriendAdapter;
	private InviteFriendAdapter mInviteFriendAdapter;
	private ChatAdapter mChatAdapter;
	private OfflineMessageAdapter mMassageAdapter;

	private Handler handler                                    = new Handler();
	private Runnable runnable;

	private AsyncTask<Void, Void, Void> mRegisterTask;
	private AlertDialogManager alert                           = new AlertDialogManager();
	private ConnectionDetector cd;

	private int mHighlightPos                                  = 6;
	private int key                                            = 0;
	public static final int TIME_DIALOG_ID                     = 999;
	public static final int DATE_DIALOG_ID                     = 998;

	public static final int MEET_UP_LOCATION                   = 3;
	public static final int CHAT_LIVE                          = 4;
	public static final int GOOD_DEALS                         = 5;
	public static final int TRACK_FRIENDS                      = 6;
	public static final int ADD_FRIENDS                        = 7;
	public static final int VIEW_PROFILE                       = 8;

	public static final int BUTTON_ADD_FRIEND                  = 100;
	public static final int BUTTON_INVITE_FRIEND               = 101;
	public static final int BUTTON_PENDING_REQUEST             = 102;

	public List<Integer> deletedPos                          = new ArrayList<Integer>();
	public List<String> invalidMarkerIDs                     = new ArrayList<String>();
	public ArrayList<MeetUpBean> meetupinfoarr               = new ArrayList<MeetUpBean>();
	public HashMap<Marker, String> markerIdHasMap            = new HashMap<Marker, String>();
	public String current_selected_marker_id                 = "";
	public int hour;
	public int minute;
	public int year;
	public int month;
	public int day;
	public long current_time_in_millisecond                 = 0;

	public Marker m;
	public int pos                                          = -1;

	public String[] markersst;
	public int updatepos                                    = -1;

	protected UiLifecycleHelper uiHelper;
	
	public static final int REQUEST_CODE_GALLERY           = 0x1;
	public static final int REQUEST_CODE_TAKE_PICTURE      = 0x2;
	private final int PIC_CROP                             = 0x3;
	
	public HttpEntity resEntity;
    public Bitmap bitmap;
    public Bitmap scaleBitmap;
    public String song_url                                = "";
    public String filepath                                = "";
    public String imagepath                               = null;
    public SnomadaApp myApp;
    public String TAG                                     = "snomada";

	public Handler handle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				meetupinfoarr.get(updatepos).setName(myApp.getAppInfo().userFirstName + " "+ myApp.getAppInfo().userLastName);
				meetupinfoarr.get(updatepos).setLocation(markersst[2]);
				meetupinfoarr.get(updatepos).setDescription(markersst[3]);
				meetupinfoarr.get(updatepos).setDate1(markersst[7]);
				meetupinfoarr.get(updatepos).setTime(markersst[4]);
				meetupinfoarr.get(updatepos).setLat(Double.parseDouble(markersst[5]));
				meetupinfoarr.get(updatepos).setLng(Double.parseDouble(markersst[6]));

				map.clear();
				mViewSlider.setVisibility(View.VISIBLE);
				mViewSlider.setVisibility(View.GONE);
				for (int i = 0; i < meetupinfoarr.size(); i++) {
					if (meetupinfoarr.get(i).getOwner().equalsIgnoreCase("ME")) {
						m   =  map.addMarker (new MarkerOptions()
								.position (new LatLng(meetupinfoarr.get(i).getLat(), meetupinfoarr.get(i).getLng()))
								.title ("Name:" + meetupinfoarr.get(i).getName())
								.snippet ("Location:"+ meetupinfoarr.get(i).getLocation()
								+ "\nDescription: "+ meetupinfoarr.get(i).getDescription()
								+ "\nDate: "+ meetupinfoarr.get(i).getDate1()
								+ "\nTime: "+ meetupinfoarr.get(i).getTime())
								.icon (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
						m.setDraggable(true);
						markerIdHasMap.put(m, meetupinfoarr.get(i).getId());
					} else {
						m  =   map.addMarker (new MarkerOptions()
								.position (new LatLng(meetupinfoarr.get(i).getLat(), meetupinfoarr.get(i).getLng()))
								.title ("Name:" + meetupinfoarr.get(i).getName())
								.snippet ("Location:"+ meetupinfoarr.get(i).getLocation()
								+ "\nDescription: "	+ meetupinfoarr.get(i).getDescription()
								+ "\nDate: "+ meetupinfoarr.get(i).getDate1()
								+ "\nTime: "+ meetupinfoarr.get(i).getTime())
								.icon (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
						m.setDraggable (false);
						markerIdHasMap.put (m, meetupinfoarr.get(i).getId());
					}
				}
				if (invalidMarkerIDs != null) {
					if (invalidMarkerIDs.size() > 0) {
						deleteOldMarker();
					}
				}
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.home);
		myApp                   = (SnomadaApp) getApplication();
		uiHelper                = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		mViewSlider             = (Sliding) findViewById(R.id.slide_view);

		mBtnSlider              = (VerticalTextView) findViewById(R.id.btn_slider);
		mBtnSlider.setText(Html	.fromHtml("<font color=\"#ffffff\">FRIE</font><font color=\"#28b6ff\">NDS</font>"));
		btnaddFriend            = (Button) findViewById(R.id.btn_add_friend);
		btnInviteFriend         = (Button) findViewById(R.id.btn_invite_friend);
		btnPendingReq           = (Button) findViewById(R.id.btn_request);

		lblUserName             = (TextView) findViewById(R.id.tv_home_view_user_name);
		lblActiveChatFriend     = (TextView) findViewById(R.id.tv_chat_friend);
		lblCountPendingReq      = (TextView) findViewById(R.id.tv_pending_req_counter);
		tvTitleLocalDeals       = (TextView) findViewById(R.id.text_local_deals);
		tvTitleLocalDeals.setText(Html.fromHtml("<font color=\"#ffffff\">LOCAL&nbsp;&nbsp;</font><font color=\"#28b6ff\">DEALS</font>"));
		btnMenu                 = (Button) findViewById(R.id.tv_menu_bottom);
		btnMenu.setText(Html.fromHtml("<font color=\"#ffffff\">ME</font><font color=\"#28b6ff\">NU</font>"));
		tv_profile_title        = (TextView)findViewById(R.id.tv_profile_title_text);
		tv_profile_title.setText(Html.fromHtml("<font color=\"#ffffff\">PROFILE&nbsp;&nbsp;&nbsp;&nbsp;</font><font color=\"#28b6ff\">DETAILS</font>"));
		lv_friend_list          = (ListView) findViewById(R.id.lv_snomada_friends);
		mLvMessageList          = (ListView) findViewById(R.id.lv_message);
		mChatList               = (ListView) findViewById(R.id.lv_chat_instant_msg);
		mAddFriendList          = (ListView) findViewById(R.id.lv_facebook_friends);
		mDealsList              = (ListView) findViewById(R.id.lv_deal);
		mRequestList            = (ListView) findViewById(R.id.lv_pending_friend_req);
		mLvInviteFriendList     = (ListView) findViewById(R.id.lv_invite_friend_list);

		mSearchAddFriend        = (EditText) findViewById(R.id.et_input_key_for_search_add_friends);
		et_search_invite_friend = (EditText) findViewById(R.id.et_input_key_for_search_invite_friends);
		mEtInputChatMsg         = (EditText) findViewById(R.id.et_input_chat_msg);

		mUserImage              = (ImageView) findViewById(R.id.user_image);
		tvTitleTab              = (TextView) findViewById(R.id.add_friend_text);
		mIvSkyPatrol            = (ImageView) findViewById(R.id.iv_ski_patrol);
		mProfileImage           = (ImageView) findViewById(R.id.profile_page_image);

		mReqTab                 = (LinearLayout) findViewById(R.id.tab_request);
		mMassageLayout          = (LinearLayout) findViewById(R.id.massage_layout);
		mLayoutMsgNotiList      = (LinearLayout) findViewById(R.id.layout_message_notification);
		mChatSendButton         = (LinearLayout) findViewById(R.id.chat_send_button);
		
		mAddFriendLayout        = (RelativeLayout) findViewById(R.id.add_friend_layout);
		mChatLayout             = (RelativeLayout) findViewById(R.id.chat_outer_layout);
		mDealsLayout            = (RelativeLayout) findViewById(R.id.local_deals_layout);
		mProfileLayout          = (RelativeLayout) findViewById(R.id.user_profile_main_layout);
		mAddFriendSearchLayout  = (RelativeLayout) findViewById(R.id.add_friend_search_layout);
		mInviteFriendSearch     = (RelativeLayout) findViewById(R.id.invite_friend_search_layout);
		mRlProgressBarLayout    = (RelativeLayout) findViewById(R.id.rl_progress_bar);

		map                     = ((SupportMapFragment) getSupportFragmentManager()	.findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
		map.getUiSettings().setCompassEnabled(true);
		map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
		Global.setMap(map);

		//

		mBtnSlider.setOnClickListener(this);
		mUserImage.setOnClickListener(this);
		btnMenu.setOnClickListener(this);
		mIvSkyPatrol.setOnClickListener(this);
		btnaddFriend.setOnClickListener(this);
		btnInviteFriend.setOnClickListener(this);
		btnPendingReq.setOnClickListener(this);
		mMassageLayout.setOnClickListener(this);
		mChatSendButton.setOnClickListener(this);
		map.setOnMapLongClickListener(this);
		map.setOnMarkerClickListener(this);
		map.setOnInfoWindowClickListener(this);
		mProfileImage.setOnClickListener(this);

		mSearchAddFriend.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
				String searchString = mSearchAddFriend.getText().toString();
				int textLength = searchString.length();
				mAddFriendSearchArr.clear();
				for (int i = 0; i < mAddFriendArr.size(); i++) {
					String retailerName = mAddFriendArr.get(i).getFirstName()+" "+mAddFriendArr.get(i).getLastName();
					if (textLength <= retailerName.length()) {
						if (searchString.equalsIgnoreCase(retailerName.substring(0, textLength))) {
							mAddFriendSearchArr.add(new AppUserInfoBean(mAddFriendArr.get(i).getId(),mAddFriendArr.get(i).getEmail(),mAddFriendArr.get(i).getFirstName(),mAddFriendArr.get(i).getLastName(),mAddFriendArr.get(i).getImage(),mAddFriendArr.get(i).getUserType(),mAddFriendArr.get(i).getPhone()));
						}
					}
				}
				mAddFriendAdapter = new AddFriendAdapter(HomeView.this,	R.layout.add_friend_row, mAddFriendSearchArr);
				mAddFriendList.setAdapter(mAddFriendAdapter);

			}
		});
		et_search_invite_friend.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
				String searchString = et_search_invite_friend.getText()	.toString();
				int textLength = searchString.length();
				mInviteFriendSearchArr.clear();
				for (int i = 0; i < mInviteFriendArr.size(); i++) {
					String retailerName = mInviteFriendArr.get(i).getFirstName()+" "+mInviteFriendArr.get(i).getLastName();
					if (textLength <= retailerName.length()) {
						if (searchString.equalsIgnoreCase(retailerName.substring(0, textLength))) {
							mInviteFriendSearchArr.add(new AppUserInfoBean(mInviteFriendArr.get(i).getId(),mInviteFriendArr.get(i).getEmail(),mInviteFriendArr.get(i).getFirstName(),mInviteFriendArr.get(i).getLastName(),mInviteFriendArr.get(i).getImage(),mInviteFriendArr.get(i).getUserType(),mInviteFriendArr.get(i).getPhone()));
						}
					}
				}
				mInviteFriendAdapter = new InviteFriendAdapter(HomeView.this,R.layout.add_friend_row, mInviteFriendSearchArr);
				mLvInviteFriendList.setAdapter(mInviteFriendAdapter);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}

			@Override
			public void afterTextChanged(Editable s) {	}
		});

		DealsBean bean1 = new DealsBean("a");
		DealsBean bean2 = new DealsBean("a");
		DealsBean bean3 = new DealsBean("a");
		DealsBean bean4 = new DealsBean("a");
		DealsBean bean5 = new DealsBean("a");

		mDealsArr.add(bean1);
		mDealsArr.add(bean2);
		mDealsArr.add(bean3);
		mDealsArr.add(bean4);
		mDealsArr.add(bean5);

		mAdapter = new DealsAdapter(getApplicationContext(),R.layout.deals_row, mDealsArr);
		mDealsList.setAdapter(mAdapter);
		imageLoader = new ImageLoader(this);
		init1();

	}

	public Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	protected void onSessionStateChange(Session session, SessionState state,Exception exception) {
		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");

		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");

		}
	}

	
	public void init1() {
		presenter = new HomePresenter(this);
		presenter.getFriendList();
		createMenuDialog();
		setLayoutVisibility(View.GONE, View.GONE, View.GONE, View.GONE,	View.VISIBLE, View.GONE, View.INVISIBLE, View.INVISIBLE,View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE,false, false, false, TRACK_FRIENDS);
		setFriendTab(1, View.VISIBLE, View.GONE, View.GONE, View.VISIBLE,	View.GONE, R.drawable.tab_select, R.drawable.tab_unselect,	R.drawable.tab_unselect, "#00ccff", "#ffffff", "#ffffff","ADD", "FRIENDS");
		myApp.isWebServiceCallForRefreshFriendList = false;
		Global.isZoom = true;

		if (myApp.getAppInfo().isAlertForSKIPatrol) {
			myApp.doTrackFriendLocation = true;
			myApp.getAppInfo().setIsAlertForSKIPatrol(false);
			Patrol p = db.getSkiPetrolInfo();
			myApp.friendId = p.getPatrolerId();
			presenter.trackingpersionname = p.getPatrolerFirstName() + " "	+ p.getPatrolerLastName();
			presenter.isTracking = false;
			HomePresenter.COUNT = 0;
			presenter.TrackDurationControllFlag = true;
		
		}

		doSetUp();
		getPushNotificationDeviceID();
		defaultChatWindoOpenFromNotificationList();
		TrackLocation.createInstance(getApplicationContext(), myApp);
		//imageLoader.DisplayImage("https://graph.facebook.com/" + myApp.getAppInfo().userId	+ "/picture", mUserImage);
		imageLoader.DisplayImage(myApp.getAppInfo().image, mUserImage);
		lblUserName.setText(myApp.getAppInfo().userFirstName);

	}

	/**
	 * 1. Start Service for update online status 2. Fetch Meet up location from
	 * server 3. get the Message from server. 4. Check - if any friend request
	 * come
	 */
	public void doSetUp() {

		// Send a signal 1/
		Intent i2 = new Intent(HomeView.this, ServerStatus.class);
		startService(i2);
		new GetMeetUplocation().execute();
		new MessageWeb().execute();
		runnable = new Runnable() {
			public void run() {
				
					handler.postDelayed(runnable, 3000);
					if (myApp.isNetworkConnected(HomeView.this)) {
						new FriendRequestNotificationCount().execute();
					}
					if (myApp.doTrackFriendLocation	&& presenter.isFriendListFetched) {
						presenter.getFriendCurrentLocation();
					}
				
			}
		};
		handler.postDelayed(runnable, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_logout:
			myApp.getAppInfo().setSession(false);
			finish();

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// ****************************************************** Header part
		// Click listener START*****************************
		if (v == mBtnSlider) {
			if (key == 0) {
				key = 1;
				mViewSlider.setVisibility(View.VISIBLE);
				if (myApp.isWebServiceCallForRefreshFriendList) {
					presenter.getFriendList();
					myApp.isWebServiceCallForRefreshFriendList = false;
				}

			} else if (key == 1) {
				key = 0;
				mViewSlider.setVisibility(View.GONE);
			}
		}
		if (v == mUserImage) {
			setLayoutVisibility(View.GONE, View.GONE, View.GONE,
					View.VISIBLE, View.GONE, View.GONE,
					View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
					View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
					false, false, false, VIEW_PROFILE);
			Log.e(TAG, myApp.getAppInfo().image);
			imageLoader.DisplayImage(myApp.getAppInfo().image,	mProfileImage);

		}
		
		if(v == mProfileImage){

			 AlertDialog.Builder builder = new AlertDialog.Builder(HomeView.this);
            builder.setCancelable(true);
            builder.setTitle("Updolad Image");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Camera",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.dismiss();
                            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(i , REQUEST_CODE_TAKE_PICTURE);      
                        }
                    });
            builder.setNegativeButton("Gallery",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.dismiss();
                            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, REQUEST_CODE_GALLERY);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();		
		}

		if (v == mIvSkyPatrol) {
			presenter.doSkiPatrolFunction();
		}
		if (v == mMassageLayout) {
			if (msg.size() > 0) {
				if (mLayoutMsgNotiList.getVisibility() == View.VISIBLE) {
					mLayoutMsgNotiList.setVisibility(View.GONE);
				} else {
					mLayoutMsgNotiList.setVisibility(View.VISIBLE);
					mMassageAdapter = new OfflineMessageAdapter(HomeView.this,	R.layout.message_notification_row, msg);
					mLvMessageList.setAdapter(mMassageAdapter);
				}

			}

		}

		// ************************* Header part Click listener
		// END*****************************************************************
		// ************************ Add friend part Click listener
		// Start************************************************************
		if (v == btnaddFriend) {
			setFriendView(BUTTON_ADD_FRIEND);
		}
		if (v == btnInviteFriend) {
			setFriendView(BUTTON_INVITE_FRIEND);
		}
		if (v == btnPendingReq) {
			setFriendView(BUTTON_PENDING_REQUEST);
		}
		// ****************************** Add friend part Click listener
		// End********************************************************
		// ****************************** Chat Click listener
		// Start****************************************************************
		if (v == mChatSendButton) {
			if (!lblActiveChatFriend.getText().toString().equalsIgnoreCase("Please Select A Friend To Chat With")) {
				String msg = mEtInputChatMsg.getText().toString().trim();
				if (msg.length() > 0) {
					Global.mChatArr.add(new ChatBean(myApp.getAppInfo().userFirstName, msg));
					mChatAdapter = new ChatAdapter(HomeView.this,	R.layout.chat_row, Global.mChatArr);
					mChatList.setAdapter(mChatAdapter);
					mChatList.setSelection(Global.mChatArr.size() - 1);
					mEtInputChatMsg.setText("");
					new ChatWeb().execute(myApp.getAppInfo().userId,myApp.getAppInfo().senderIdChat, msg);
				}
			} else {
				Toast.makeText(getApplicationContext(),	"Please select a friend", Toast.LENGTH_LONG).show();
			}
		}
		// ******************************* Add Click listener
		// End*************************************************************
		// ******************************* Footer part Click listener
		// Start****************************************************
		if (v == btnMenu) {
			ll_meetup.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					menu_dialog.dismiss();
					myApp.doTrackFriendLocation = false;
					setLayoutVisibility(View.GONE, View.GONE, View.GONE,View.GONE, View.VISIBLE, View.GONE, View.VISIBLE,View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,	View.INVISIBLE, View.INVISIBLE, false, true, false,	MEET_UP_LOCATION);
				}
			});
			ll_chat.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					menu_dialog.dismiss();
					setLayoutVisibility(View.VISIBLE, View.GONE, View.GONE,	View.GONE, View.VISIBLE, View.GONE, View.INVISIBLE,	View.VISIBLE, View.INVISIBLE, View.INVISIBLE,View.INVISIBLE, View.INVISIBLE, false, false, true,	CHAT_LIVE);
				}
			});
			ll_gooddeals.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					menu_dialog.dismiss();
					setLayoutVisibility(View.GONE, View.VISIBLE, View.GONE,	View.GONE, View.GONE, View.GONE, View.INVISIBLE,	View.INVISIBLE, View.VISIBLE, View.INVISIBLE,View.INVISIBLE, View.INVISIBLE, false, false,false, GOOD_DEALS);
				}
			});
			ll_track.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					menu_dialog.dismiss();
					setLayoutVisibility(View.GONE, View.GONE, View.GONE,View.GONE, View.VISIBLE, View.GONE, View.INVISIBLE,	View.INVISIBLE, View.INVISIBLE, View.VISIBLE,View.INVISIBLE, View.INVISIBLE, false, false,false, TRACK_FRIENDS);
				}
			});
			ll_addfriend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					menu_dialog.dismiss();
					setLayoutVisibility(View.GONE, View.GONE, View.VISIBLE,	View.GONE, View.GONE, View.GONE, View.INVISIBLE,View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,	View.VISIBLE, View.INVISIBLE, false, false, false,ADD_FRIENDS);
					if (!(mAddFriendArr.size() > 0)) {
						new AppUsers().execute();
					}
				}
			});
			ll_viewprofile.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					menu_dialog.dismiss();
					setLayoutVisibility(View.GONE, View.GONE, View.GONE,View.VISIBLE, View.GONE, View.GONE,	View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,	View.INVISIBLE, View.INVISIBLE, View.VISIBLE,false, false, false, VIEW_PROFILE);
					imageLoader.DisplayImage("https://graph.facebook.com/"+ myApp.getAppInfo().userId + "/picture",mProfileImage);
				}
			});

			DisplayMetrics displaymetrics = new DisplayMetrics();
			HomeView.this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int height = displaymetrics.heightPixels;
			int width = displaymetrics.widthPixels;
			
			
	         WindowManager.LayoutParams wmlp = menu_dialog.getWindow().getAttributes();
	         wmlp.gravity = Gravity.TOP | Gravity.LEFT;
	         wmlp.x = (int)(width*0.142);   //x position
	         wmlp.y = height;   //y position
	         menu_dialog.show();
			
			
			
			/*if (width > 320 && height > 480) {
				Window sleepWindow = menu_dialog.getWindow();
				WindowManager.LayoutParams lp = sleepWindow.getAttributes();
				lp.gravity = Gravity.BOTTOM;
				sleepWindow.setAttributes(lp);
			} else {
				Window sleepWindow = menu_dialog.getWindow();
				WindowManager.LayoutParams lp = sleepWindow.getAttributes();
				lp.gravity = Gravity.BOTTOM;
				sleepWindow.setAttributes(lp);

			}*/
			//menu_dialog.show();

		}
	

	}

	/*
	 * @Override public ListView getList() { return lv_friend_list; }
	 */

	public void setFriendView(int i) {
		switch (i) {
		case BUTTON_ADD_FRIEND:
			    setFriendTab(1, View.VISIBLE, View.GONE, View.GONE, View.VISIBLE,View.GONE, R.drawable.tab_select, R.drawable.tab_unselect,R.drawable.tab_unselect, "#00ccff", "#ffffff", "#ffffff","ADD", "FRIENDS");
			break;
		case BUTTON_INVITE_FRIEND:
			    setFriendTab(2, View.GONE, View.VISIBLE, View.GONE, View.GONE,	View.VISIBLE, R.drawable.tab_unselect,R.drawable.tab_select, R.drawable.tab_unselect, "#ffffff",	"#00ccff", "#ffffff", "INVITE", "FRIENDS");
			break;
		case BUTTON_PENDING_REQUEST:
			    setFriendTab(3, View.GONE, View.GONE, View.VISIBLE, View.GONE,	View.GONE, R.drawable.tab_unselect,	R.drawable.tab_unselect, R.drawable.tab_select, "#ffffff","#ffffff", "#00ccff", "REQ", "ESTS");
			presenter.getFriendRequest();
			break;
		}
	}

	@Override
	public GoogleMap getMap() {
		return map;
	}

	@Override
	public Sliding hideSlide() {
		return mViewSlider;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			myApp.doTrackFriendLocation = false;
			myApp.getAppInfo().isAppForeground = false;
			handler.removeCallbacks(runnable);
			HomeView.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			WakeLocker.acquire(getApplicationContext());
			Log.i("Receive push homeview ======>", newMessage);
			if (newMessage != null && newMessage.startsWith("{")
					&& newMessage.endsWith("}")) {
				try {
					JSONObject json = new JSONObject(newMessage);
					int status = Integer.parseInt(json.getString("status"));
					if (status == Constants.ONLINE_STATUS_PUSH_NOTIFICATION) {
						boolean onlinestatus = Integer.parseInt(json.getString("onlinestatus")) == 1 ? true : false;	String onlinefbid = json.getString("fbid");
						presenter.findListPosition(onlinestatus, onlinefbid);
					} else if (status == Constants.CHAT_PUSH_NOTIFICATION) {
						new MessageWeb().execute();
						if (myApp.IMname.equalsIgnoreCase(json.getString("name"))) {
							String msg        = json.getString("chatmessage");
							String name       = json.getString("name");
							Global.mChatArr.add(new ChatBean(name, msg));
							mChatAdapter = new ChatAdapter(HomeView.this,R.layout.chat_row, Global.mChatArr);
							mChatList.setAdapter(mChatAdapter);
							mChatList.setSelection(Global.mChatArr.size() - 1);
						}
					} else if (status == Constants.SKI_PATROL_PUSH_NOTIFICATION) {
						
							if(myApp.getAppInfo().isAppForeground){
								String patroler_id      = json.getString("patroler_id");
								String latitude         = json.getString("lat");
								String longitude        = json.getString("lng");
								String fname            = json.getString("fname");
								String lname            = json.getString("lname");
								
								
								if (db.getSkiPetrolRowCount() > 0) {
										db.updateSkiPetrolInfo(patroler_id,fname, lname, latitude,	longitude);
									} else {
										db.insertSkiPetrolInfo(patroler_id,fname, lname, latitude,longitude);
									}
								handler.removeCallbacks(runnable);
									Intent intent1 = new Intent("SKI_PATROL_INTENT");				
									context.sendBroadcast(intent1);
									HomeView.this.finish();
							}
							
						
					} else if (status == Constants.FRIEND_REQUEST_COME_PUSH_NOTIFICATION) {// Status five for current  friend request receive
																							
						String sender_id        = json.getString("senderid");
						String sender_name      = json.getString("sendername");
						String receiver_fbid    = json.getString("receiver_fb_id");
						String record_id        = json.getString("recordid");
						String image            = json.getString("image");
						int track_status        = Integer.parseInt(json.getString("trackstatus"));

						presenter.updatePendingFriendList(sender_id,sender_name, receiver_fbid, record_id,	track_status,image);

					} else if (status == Constants.FRIEND_REQUEST_ACCEPT_PUSH_NOTIFICATION) {																							
						myApp.isWebServiceCallForRefreshFriendList = true;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		myApp.getAppInfo().setIsAppForgroung(false);
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
			handler.removeCallbacks(runnable);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		uiHelper.onDestroy();
		super.onDestroy();
	}

	@Override
	public TextView getChatFriend() {
		return lblActiveChatFriend;
	}

	@Override
	public ListView getChatListView() {
		return mChatList;
	}

	public void getChatWindowActive(String friendName, String fbid) {
		myApp.getAppInfo().isAppForeground = true;
		setLayoutVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE,
				View.VISIBLE, View.GONE, View.INVISIBLE, View.VISIBLE,
				View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
				false, false, true, CHAT_LIVE);
		presenter.functionChat(fbid, friendName);
		lblActiveChatFriend.setText(friendName);
	}

	@Override
	public ListView getRequestList() {
		return mRequestList;
	}

	@Override
	public void defaultChatWindoOpenFromNotificationList() {
		Bundle bundle = getIntent().getExtras();
		/*
		 * if(myApp.getAppInfo().isAlertForSKIPatrol){ myApp.isZoom = true;
		 * myApp.getAppInfo().setIsAlertForSKIPatrol(false); Patrol p =
		 * db.getSkiPetrolInfo(); presenter.trackSKIPatrol(p.getPatrolerId(),
		 * p.getPatrolerFirstName(),p.getPatrolerLastName());
		 * 
		 * }
		 */
		if (bundle != null) {
			if (bundle.getString("event").equalsIgnoreCase("chat")) {
				myApp.getAppInfo().isAppForeground = true;
				setLayoutVisibility(View.VISIBLE, View.GONE, View.GONE,
						View.GONE, View.VISIBLE, View.GONE, View.INVISIBLE,
						View.VISIBLE, View.INVISIBLE, View.INVISIBLE,
						View.INVISIBLE, View.INVISIBLE, false, false, true,
						CHAT_LIVE);

				String sender_name = getIntent().getExtras().getString(	"sender_name");
				String sender_fb_id = getIntent().getExtras().getString("sender_fb_id");
				String[] splitStr = sender_name.split("\\s+");
				myApp.IMname = splitStr[0];
				myApp.getAppInfo().setSenderIDChat(	getIntent().getExtras().getString("sender_fb_id"));
				presenter.functionChat(sender_fb_id, sender_name);
				lblActiveChatFriend.setText(sender_name);
			} else if (bundle.getString("event").equalsIgnoreCase("meetup")) {
				Global.isZoom = false;
				myApp.getAppInfo().setIsAlertForSKIPatrol(false);
				db.getMeetUpLocation1();
				String st[] = db.getMeetUpLocation();
				db.updateMeetUpStatus(Integer.parseInt(st[0]));
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(st[1]), Double.valueOf(st[2])), 16));

			}
		}

	}

	/**
	 * Menu Dialog open from footer. 3. Meet-UP location 4. Chat 5. Good Deals
	 * 6. Location Track 7. Add friends 8. View profile
	 */
	@Override
	public void createMenuDialog() {
		menu_dialog           = new Dialog(HomeView.this);
		menu_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		menu_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		menu_dialog.setContentView(R.layout.dialog);
		ll_meetup             = (LinearLayout) menu_dialog.findViewById(R.id.submenu_3);
		ll_chat               = (LinearLayout) menu_dialog.findViewById(R.id.submenu_4);
		ll_gooddeals          = (LinearLayout) menu_dialog.findViewById(R.id.submenu_5);
		ll_track              = (LinearLayout) menu_dialog.findViewById(R.id.submenu_6);
		ll_addfriend          = (LinearLayout) menu_dialog.findViewById(R.id.submenu_7);
		ll_viewprofile        = (LinearLayout) menu_dialog.findViewById(R.id.submenu_8);

		mGreenBarMeetUploc    = (LinearLayout) menu_dialog.findViewById(R.id.bar_3);
		mGreenBarChatLive     = (LinearLayout) menu_dialog.findViewById(R.id.bar_4);
		mGreenBarGoodDeals    = (LinearLayout) menu_dialog	.findViewById(R.id.bar_5);
		mGreenBarTrack        = (LinearLayout) menu_dialog.findViewById(R.id.bar_6);
		mGreenBarAdd          = (LinearLayout) menu_dialog.findViewById(R.id.bar_7);
		mGreenBarViewProfile  = (LinearLayout) menu_dialog.findViewById(R.id.bar_8);
		menu_dialog.setCanceledOnTouchOutside(true);

	}

	/**
	 * GET THE DEVICE ID FOR PUSH NOTIFICATION
	 */
	@Override
	public void getPushNotificationDeviceID() {
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(HomeView.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		registerReceiver(mHandleMessageReceiver, new IntentFilter(	DISPLAY_MESSAGE_ACTION));

		final String regId = GCMRegistrar.getRegistrationId(this);

		if (regId.equals("")) {
			GCMRegistrar.register(getApplicationContext(), SENDER_ID);
		} else {
			GCMRegistrar.register(getApplicationContext(), SENDER_ID);
			if (GCMRegistrar.isRegisteredOnServer(this)) {

			} else {
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						ServerUtilities.register(context, regId,"" + myApp.getAppInfo().userId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}

	}

	@Override
	public RelativeLayout getProgressBarLayout() {
		return mRlProgressBarLayout;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		current_selected_marker_id = markerIdHasMap.get(marker);
		for (int i = 0; i < meetupinfoarr.size(); i++) {
			if (current_selected_marker_id.equalsIgnoreCase(meetupinfoarr.get(i).getId())) {
				if (meetupinfoarr.get(i).getOwner().equalsIgnoreCase("ME")) {
					myApp.isMeetuplocationEditTextEditable = true;
					setMeetuplocationDialog(marker, current_selected_marker_id,	meetupinfoarr.get(i).getName(), meetupinfoarr.get(i).getLocation(), meetupinfoarr.get(i)	.getDescription(), meetupinfoarr.get(i)	.getDate1(),meetupinfoarr.get(i).getTime(), meetupinfoarr.get(i).getOwner());
				}
				break;
			}
		}

	}

	@Override
	public void onMapLongClick(final LatLng point) {
		if (myApp.isMeetuplocationWindoEnable) {
			AlertDialog.Builder builder = new AlertDialog.Builder(HomeView.this);
			builder.setCancelable(true);
			builder.setTitle("Craete meet up location?");
			builder.setInverseBackgroundForced(true);
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

							m = map.addMarker(new MarkerOptions()
									.position(point)
									.title(myApp.getAppInfo().userFirstName	+ " "+ myApp.getAppInfo().userLastName)
									.snippet("Click here update info")
									.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
							m.setDraggable(true);

							current_time_in_millisecond = System.currentTimeMillis();
							meetupinfoarr.add(new MeetUpBean(""	+ current_time_in_millisecond, "", "", "",	"", "", "ME", point.latitude,	point.longitude));
							markerIdHasMap.put(m, ""+ current_time_in_millisecond);

						}
					});
			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, timePickerListener, hour, minute,
					true);
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;
			int sec = 00;
			lblDlgDisplayTime.setText(new StringBuilder().append(pad(hour))
					.append(":").append(pad(minute)).append(":").append(0)
					.append(0));

		}
	};

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	public void setMeetuplocationDialog(final Marker marker,
			final String current_selected_marker_id,
			final String meetupusername, final String meetuplocation,
			final String meetupdesc, final String meetupdate,
			final String meetuptime, final String owner) {

		meetupUserDlg = new Dialog(HomeView.this);
		meetupUserDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		meetupUserDlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		meetupUserDlg.setContentView(R.layout.meetup_info_dialog);
		Button submit = (Button) meetupUserDlg.findViewById(R.id.btn_submit);
		setCustomizeColorText(submit, "SUB", "MIT");
		Button cancel = (Button) meetupUserDlg.findViewById(R.id.btn_cancel);
		setCustomizeColorText(cancel, "CAN", "CEL");
		ImageView iv_date = (ImageView) meetupUserDlg	.findViewById(R.id.image_date);
		ImageView clock = (ImageView) meetupUserDlg	.findViewById(R.id.image_clock);
		tvDisplayDate = (TextView) meetupUserDlg.findViewById(R.id.tvDisplayDate1);
		tvDisplayDate.setText(meetupdate);
		lblDlgDisplayTime = (TextView) meetupUserDlg.findViewById(R.id.tvDisplayTime1);
		lblDlgDisplayTime.setText(meetuptime);
		final TextView name = (TextView) meetupUserDlg	.findViewById(R.id.ed_name);
		name.setText(myApp.getAppInfo().userFirstName + " "	+ myApp.getAppInfo().userLastName);
		name.setClickable(myApp.isMeetuplocationEditTextEditable);
		final EditText location = (EditText) meetupUserDlg	.findViewById(R.id.ed_location);
		location.setText(meetuplocation);
		location.setClickable(myApp.isMeetuplocationEditTextEditable);
		final EditText desc = (EditText) meetupUserDlg	.findViewById(R.id.ed_desc);
		desc.setText(meetupdesc);
		desc.setClickable(myApp.isMeetuplocationEditTextEditable);
		clock.setClickable(myApp.isMeetuplocationEditTextEditable);
		iv_date.setClickable(myApp.isMeetuplocationEditTextEditable);
		setCurrentDateOnView();
		clock.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);

			}
		});

		iv_date.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);

			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				meetupUserDlg.dismiss();
				if (owner.equalsIgnoreCase("ME")) {
					String _name         = name.getText().toString().trim();
					String _loc_name     = location.getText().toString().trim();
					String _desc         = desc.getText().toString().trim();
					String _date         = tvDisplayDate.getText().toString().trim();
					String _time         = lblDlgDisplayTime.getText().toString().trim();
					String _id           = current_selected_marker_id;
					double _lat          = marker.getPosition().latitude;
					double _lng          = marker.getPosition().longitude;
					if (_loc_name.length()      == 0) {
						     location.setError("Please enter Location Name");
					} else if (_desc.length()   == 0) {
						     desc.setError("Please enter Description");
					} else if (_date.length()   == 0) {
						     tvDisplayDate.setError("Please enter Date");
					} else if (_time.length()   == 0) {
						     lblDlgDisplayTime.setError("Please enter Time");
					} else {

						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
							Date date = new Date();
							String _currentdate = new SimpleDateFormat(	"yyyy-MM-dd hh:mm:ss").format(date);
							Date currentdate = sdf.parse(_currentdate);
							Date scheduledate = sdf.parse(_date + " " + _time);
							if (scheduledate.compareTo(currentdate) < 0) {
								Toast.makeText(getApplicationContext(),	"Plesase insert a valid Date&TIme",	Toast.LENGTH_LONG).show();
							} else {
								new SubmitMeetUplocation().execute(""+ current_selected_marker_id, _name,_loc_name, _desc, _time, "" + _lat, ""	+ _lng, _date);
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}

					}
				}
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				meetupUserDlg.dismiss();

			}
		});

		meetupUserDlg.show();
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			if ((month + 1) < 10 && day < 10) {
				tvDisplayDate.setText(new StringBuilder().append(year)
						.append("-").append(0).append(month + 1).append("-")
						.append(0).append(day));
			} else if ((month + 1) < 10) {
				tvDisplayDate.setText(new StringBuilder().append(year)
						.append("-").append(0).append(month + 1).append("-")
						.append(day));
			} else if (day < 10) {
				tvDisplayDate.setText(new StringBuilder().append(year)
						.append("-").append(month + 1).append("-").append(0)
						.append(day));
			} else {
				tvDisplayDate.setText(new StringBuilder().append(year)
						.append("-").append(month + 1).append("-").append(day));
			}

		}
	};

	public void setCurrentDateOnView() {
		final Calendar c    = Calendar.getInstance();
		year                = c.get(Calendar.YEAR);
		month               = c.get(Calendar.MONTH);
		day                 = c.get(Calendar.DAY_OF_MONTH);
	}

	public void deleteOldMarker() {
		Thread t = new Thread() {
			String st;

			public void run() {
				try {
					for (int i = 0; i < invalidMarkerIDs.size(); i++) {
						if (i == 0) {
							st = invalidMarkerIDs.get(i);
						} else {
							st = st + "," + invalidMarkerIDs.get(i);
						}

					}
					JSONObject json = new JSONObject();
					json.put("deleted_ids", st);
					Log.e("IDS", st);
					KlHttpClient.SendHttpPost (URL.MEET_UP_DETELE.getUrl(), json);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	private class CustomInfoWindowAdapter implements InfoWindowAdapter {
		private View view;

		public CustomInfoWindowAdapter() {
			view = getLayoutInflater().inflate(R.layout.custom_info_window,
					null);
		}

		@Override
		public View getInfoContents(Marker marker) {
			if (HomeView.this.m != null && HomeView.this.m.isInfoWindowShown()) {
				HomeView.this.m.hideInfoWindow();
				HomeView.this.m.showInfoWindow();
			}
			return null;
		}

		@Override
		public View getInfoWindow(final Marker marker) {
			HomeView.this.m = marker;

			final TextView tv_name = ((TextView) view.findViewById(R.id.tv_marker_name));
			tv_name.setText(marker.getTitle());
			final TextView tv_loc = ((TextView) view.findViewById(R.id.tv_marker_loc));
			Log.e("Snippet ", marker.getSnippet());
			tv_loc.setText(marker.getSnippet());
			return view;
		}
	}

	public void setLayoutVisibility(int viewchat, int viewgooddeals,
			int viewaddfriend, int viewprofile, int btnslider, int viewslider,
			int barmeetup, int barchat, int bardeals, int bartrack,
			int baraddfriend, int barprofile, boolean trackfriends,
			boolean showmeetup, boolean ischatactive, int hightmenu) {

		mChatLayout.            setVisibility(viewchat);
		mDealsLayout.           setVisibility(viewgooddeals);
		mAddFriendLayout.       setVisibility(viewaddfriend);
		mProfileLayout.         setVisibility(viewprofile);
		mBtnSlider.             setVisibility(btnslider);
		mViewSlider.            setVisibility(viewslider);

		mGreenBarMeetUploc.     setVisibility(barmeetup);
		mGreenBarChatLive.      setVisibility(barchat);
		mGreenBarGoodDeals.     setVisibility(bardeals);
		mGreenBarTrack.         setVisibility(bartrack);
		mGreenBarAdd.           setVisibility(baraddfriend);
		mGreenBarViewProfile.   setVisibility(barprofile);

		myApp.doTrackFriendLocation             = trackfriends;
		myApp.isMeetuplocationWindoEnable       = showmeetup;
		myApp.isChatActive                      = ischatactive;
		mHighlightPos                           = hightmenu;

	}

	public void doTrack() {
		setLayoutVisibility(View.GONE, View.GONE, View.GONE, View.GONE,	View.VISIBLE, View.GONE, View.INVISIBLE, View.INVISIBLE,View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE,false, false, false, TRACK_FRIENDS);
		map.setInfoWindowAdapter(null);
	}

	public class ChatWeb extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {

			try {
				JSONObject mJsonObject     = new JSONObject();
				mJsonObject.put("sender_fb_id", params[0]);
				mJsonObject.put("receiver_fb_id", params[1]);
				mJsonObject.put("message", params[2]);
				mJsonObject.put("sender_name", myApp.getAppInfo().userFirstName);

				JSONObject json           = KlHttpClient.SendHttpPost(URL.CHAT.getUrl(),	mJsonObject);
				if (json != null) {
					return json.getBoolean("status");
				} else {
					return false;
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			if (result) {

			}
		}
	}

	public class GetMeetUplocation extends	AsyncTask<String, String, ArrayList<MeetUpBean>> {

		@Override
		protected ArrayList<MeetUpBean> doInBackground(String... params) {
			boolean flg = false;
			try {

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("fbid", myApp.getAppInfo().userId);
				JSONObject json          = KlHttpClient.SendHttpPost(URL.MEET_UP_MERKER_LIST.getUrl(), jsonObject);
				Log.e(TAG, json.toString());
				SimpleDateFormat sdf     = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date date = new Date();
				String _currentdate      = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
				Date currentdate         = sdf.parse(_currentdate);
				meetupinfoarr.clear();
				invalidMarkerIDs.clear();
				deletedPos.clear();
				JSONArray jArr = json.getJSONArray("MeetList");
				for (int i = 0; i < jArr.length(); i++) {
					JSONObject c         = jArr.getJSONObject(i);
					String _marker_id    = c.getString("marker_id");
					String _name         = c.getString("person_name");
					String _loc          = c.getString("loc_name");
					String _desc         = c.getString("loc_desc");
					String _identifier   = c.getString("identifier");
					String _date         = c.getString("meet_date");
					String time          = c.getString("meet_time");
					double _lat          = Double.parseDouble(c.getString("lat"));
					double _lng          = Double.parseDouble(c.getString("lng"));
					Date scheduledate    = sdf.parse(_date + " " + time);
					if (currentdate.getTime() < (scheduledate.getTime() + 3600000)) {
						meetupinfoarr.add(new MeetUpBean(_marker_id, _name,	_loc, _desc, _date, time, _identifier, _lat,_lng));
					} else {
						invalidMarkerIDs.add("" + _marker_id);
					}

				}
				return meetupinfoarr;

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<MeetUpBean> result) {
			super.onPostExecute(result);
			if (result != null) {
				for (int i = 0; i < result.size(); i++) {
					Log.e("name", result.get(i).getName());
					if (result.get(i).getOwner().equalsIgnoreCase("ME")) {
						Log.e("Add snippet", "Location:"+ result.get(i).getLocation()
								+ "\nDescription: "	+ result.get(i).getDescription() + "\nDate: "
								+ result.get(i).getDate1() + "\nTime: "+ result.get(i).getTime());
						m = map.addMarker(new MarkerOptions()
								.position(new LatLng(result.get(i).getLat(),result.get(i).getLng()))
								.title("Name:" + result.get(i).getName())
								.snippet("Location:"+ result.get(i).getLocation()
												+ "\nDescription: "
												+ result.get(i).getDescription()
												+ "\nDate: "+ result.get(i).getDate1()
												+ "\nTime: "+ result.get(i).getTime())
								.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
						m.setDraggable(true);

						markerIdHasMap.put(m, result.get(i).getId());
					} else {
						m = map.addMarker(new MarkerOptions()
								.position(new LatLng(result.get(i).getLat(),result.get(i).getLng()))
								.title("Name:" + result.get(i).getName())
								.snippet("Location:"+ result.get(i).getLocation()
												+ "\nDescription: "	+ result.get(i).getDescription()
												+ "\nDate: "+ result.get(i).getDate1()
												+ "\nTime: "+ result.get(i).getTime())
								.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
						m.setDraggable(false);

						markerIdHasMap.put(m, result.get(i).getId());
					}

				}
				if (invalidMarkerIDs != null) {
					if (invalidMarkerIDs.size() > 0) {
						deleteOldMarker();
					}
				}

			}

		}

	}

	// Put/Edit a MEET UP location into the Google map
	public class SubmitMeetUplocation extends
			AsyncTask<String, String, String[]> {

		@Override
		protected String[] doInBackground(String... params) {
			boolean flag = false;
			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("fbid", myApp.getAppInfo().userId);
				jsonObject.put("fname", myApp.getAppInfo().userFirstName);
				jsonObject.put("marker_id", params[0]);
				jsonObject.put("person_name", params[1]);
				jsonObject.put("loc_name", params[2]);
				jsonObject.put("loc_desc", params[3]);
				jsonObject.put("meet_time", params[4]);
				jsonObject.put("lat", params[5]);
				jsonObject.put("lng", params[6]);
				jsonObject.put("meet_date", params[7]);
				jsonObject.put("actn", "add");
				JSONObject json = KlHttpClient.SendHttpPost(URL.ADD_MEET_UP_LOCATION.getUrl(), jsonObject);
				if (json != null) {
					flag = json.getBoolean("status");
					if (flag) {
						return params;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			super.onPostExecute(result);
			// dismissProgressDialog();
			if (result != null) {
				int pos = -1;
				String marker_id = result[0];
				for (int i = 0; i < meetupinfoarr.size(); i++) {
					if (marker_id.equalsIgnoreCase(meetupinfoarr.get(i).getId())) {
						pos = i;
						break;
					}
				}
				markersst = result;
				updatepos = pos;
				handle.sendEmptyMessage(1);

			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// showProgressDailog();
		}

	}

	public class FriendRequestNotificationCount extends
			AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... params) {
			int count = 0;
			try {
				JSONObject req = new JSONObject();
				req.put("fbid", myApp.getAppInfo().userId);
				Log.e("Count", req.toString());
				JSONObject res = KlHttpClient.SendHttpPost(	URL.COUNT_PENDING_FRIEND.getUrl(), req);
				if (res != null) {
					count = Integer.parseInt(res.getString("totalpendingfriend"));
					return count;
				} else {
					return count;
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return count;
		}

		@Override
		protected void onPostExecute(Integer result) {
			Log.e("Count", ""+result);
			if (result > 0) {
				lblCountPendingReq.setVisibility(View.VISIBLE);
				lblCountPendingReq.setText("" + result);
				
			} else {
				lblCountPendingReq.setVisibility(View.GONE);
			}
		}
	}

	public class AppUsers extends AsyncTask<String, Void, ArrayList<AppUserInfoBean>> {
		protected void onPreExecute() {
			showProgressDailog();
		}

		@Override
		protected ArrayList<AppUserInfoBean> doInBackground(String... params) {
			try {
				JSONObject request = new JSONObject();
				request.put("fbid", myApp.getAppInfo().userId);
				request.put("usertype", myApp.getAppInfo().loginType);
				JSONObject response = KlHttpClient.SendHttpPost(URL.ACTIVE_APP_USERS.getUrl(), request);
				if (response.getBoolean("status")) {
					JSONArray jsonArray = response.getJSONArray("app_users");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject c      = jsonArray.getJSONObject(i);
						String ids        = c.getString("id");
						String email      =  !c.isNull("email") ? c.getString("email"): "";											
						String first_name = c.getString("first_name");
						String last_name  = c.getString("last_name");
																	
						String user_type  = c.getString("user_type");
						String image;
						if(user_type.equalsIgnoreCase("F")){
							 image      = !c.isNull("image")? URL.IMAGE_PATH.getUrl()+c.getString("image"): "https://graph.facebook.com/"+ids+"/picture" ;	
						}else{
							image      = !c.isNull("image")? URL.IMAGE_PATH.getUrl()+c.getString("image"): URL.IMAGE_PATH.getUrl()+"noimage.jpg" ;
						}
						
						String phone      = !c.isNull("phone1")? c.getString("phone1"): "";						
						mAppUserList.add(new AppUserInfoBean(ids,email, first_name,last_name,image, user_type,phone));
					}
				}
				return mAppUserList;

			} catch (Exception e) {
				dismissProgressDialog();
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<AppUserInfoBean> appusersArr) {
			dismissProgressDialog();
			boolean flag = false;
			if (appusersArr != null) {
				if(myApp.getAppInfo().loginType.equalsIgnoreCase("F")){					
					mContactList = db.getFacebookFriends();				
					
					try {
						for (int i = 0; i < mContactList.size(); i++) {
							flag = false;
							for (int j = 0; j < appusersArr.size(); j++) {
								if (mContactList.get(i).getId().equalsIgnoreCase(appusersArr.get(j).getId())) {
									mAddFriendArr.add(new AppUserInfoBean(appusersArr.get(j).getId(),""	,appusersArr.get(j).getFirstName(),appusersArr.get(j).getLastName(),appusersArr.get(j).getImage(),"F",""));
									flag = true;
									break;
								}
							}
							if (!flag) {
								mInviteFriendArr.add(new AppUserInfoBean(	mContactList.get(i).getId(),	"",mContactList.get(i).getName(),"","https://graph.facebook.com/"+mContactList.get(i).getId()+"/picture","F",""));
							}

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				

					mAddFriendAdapter = new AddFriendAdapter(HomeView.this,	R.layout.add_friend_row, mAddFriendArr);
					mAddFriendList.setAdapter(mAddFriendAdapter);

					mInviteFriendAdapter = new InviteFriendAdapter(HomeView.this,	R.layout.add_friend_row, mInviteFriendArr);
					mLvInviteFriendList.setAdapter(mInviteFriendAdapter);
				
				}else{
					
					mContactList = db.getContact();				
					
					try {
						for (int i = 0; i < mContactList.size(); i++) {
							flag = false;
							for (int j = 0; j < appusersArr.size(); j++) {
								if (mContactList.get(i).getId().equalsIgnoreCase(appusersArr.get(j).getPhone())) {
									Log.e("Contact NO.", mContactList.get(i).getId());
									Log.e("Contact Name.", mContactList.get(i).getName());
									mAddFriendArr.add(new AppUserInfoBean(appusersArr.get(j).getId(),""	,mContactList.get(i).getName(),"",appusersArr.get(j).getImage(),"N",mContactList.get(i).getId()));
									flag = true;
									break;
								}
							}
							if (!flag) {
								mInviteFriendArr.add(new AppUserInfoBean(	"",	"",mContactList.get(i).getName(),"",URL.IMAGE_PATH.getUrl()+"noimage.jpg","N",mContactList.get(i).getId()));
							}

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				

					mAddFriendAdapter = new AddFriendAdapter(HomeView.this,	R.layout.add_friend_row, mAddFriendArr);
					mAddFriendList.setAdapter(mAddFriendAdapter);

					mInviteFriendAdapter = new InviteFriendAdapter(HomeView.this,	R.layout.add_friend_row, mInviteFriendArr);
					mLvInviteFriendList.setAdapter(mInviteFriendAdapter);
				
				
				}
			}

		}

	}

	public class MessageWeb extends	AsyncTask<String, String, ArrayList<NewMessage>> {

		@Override
		protected ArrayList<NewMessage> doInBackground(String... params) {

			try {
				JSONObject request = new JSONObject();
				request.put("receiver_fb_id", myApp.getAppInfo().userId);
				JSONObject response = KlHttpClient.SendHttpPost(URL.MESSAGE.getUrl(), request);
				if (response != null) {
					msg.clear();
					JSONArray jsonArray = response.getJSONArray("newReply");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject c       = jsonArray.getJSONObject(i);
						String id          = c.getString("cr_id");
						String name        = c.getString("sender");
						String message     = c.getString("message");
						String date1       = c.getString("date");
						msg.add(new NewMessage(id, date1, name, message));
					}
				}
				return msg;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public void setFriendTab(int selectedTab, int addfriendlistview,
			int invitefriendlistview, int pendingreqview,
			int addsearcglayoutview, int invitesearchlayoutview,
			int drawableaddfriend, int drawableinvitefriend, int requestfriend,
			String addfriendtextcolor, String invitefriendtextcolor,
			String pendingreqtextcolor, String blueTxt, String whiteTxt) {
		myApp.selectedTab = selectedTab;
		mAddFriendList.            setVisibility(addfriendlistview);
		mLvInviteFriendList.       setVisibility(invitefriendlistview);
		mRequestList.              setVisibility(pendingreqview);

		mAddFriendSearchLayout.    setVisibility(addsearcglayoutview);
		mInviteFriendSearch.       setVisibility(invitesearchlayoutview);

		btnaddFriend.              setBackgroundResource(drawableaddfriend);
		btnInviteFriend.           setBackgroundResource(drawableinvitefriend);
		btnPendingReq.             setBackgroundResource(requestfriend);

		btnaddFriend.              setTextColor(Color.parseColor(addfriendtextcolor));
		btnInviteFriend.           setTextColor(Color.parseColor(invitefriendtextcolor));
		btnPendingReq.             setTextColor(Color.parseColor(pendingreqtextcolor));
		if(blueTxt.equalsIgnoreCase("REQ")){
			tvTitleTab.setText(Html	.fromHtml("<font color=\"#ffffff\">"+blueTxt+"</font><font color=\"#28b6ff\">"+whiteTxt+"</font>"));	
		}else{
			tvTitleTab.setText(Html	.fromHtml("<font color=\"#ffffff\">"+blueTxt+"&nbsp;&nbsp;</font><font color=\"#28b6ff\">"+whiteTxt+"</font>"));
		}
		
	}

	@Override
	public void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	     if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && null != data) {
	           Uri selectedImage = data.getData();
	           String[] filePathColumn   = { MediaStore.Images.Media.DATA };
	           Cursor cursor             = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
	           cursor.moveToFirst();
	           int columnIndex           = cursor.getColumnIndex(filePathColumn[0]);
	           String picturePath        = cursor.getString(columnIndex);
	           cursor.close();
	        
	            }
	       
	       if(requestCode == REQUEST_CODE_TAKE_PICTURE && resultCode == RESULT_OK && null != data){
	    	   
	    	   String[] projection        = { MediaStore.Images.Media.DATA };
			   Cursor cursor              = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection, null, null, null);
			   int column_index_data      = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			   cursor.moveToLast();
			   imagepath                  = cursor.getString(column_index_data);			   			   
			   performCrop(getImageContentUri(HomeView.this,new File(imagepath)));
			}
	       
	       else if (requestCode == PIC_CROP && resultCode == RESULT_OK && null != data) {
   	        Bundle extras = data.getExtras();
	        Bitmap yourSelectedImage = extras.getParcelable("data");
	        doSaveNewImage(yourSelectedImage);
	        setLayoutVisibility(View.GONE, View.GONE, View.GONE,View.VISIBLE, View.GONE, View.GONE,	View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,	View.INVISIBLE, View.INVISIBLE, View.VISIBLE,false, false, false, VIEW_PROFILE);
	        mProfileImage.setImageBitmap(yourSelectedImage);
		    mUserImage.setImageBitmap(yourSelectedImage);
		    Log.e(TAG,"PATH "+ filepath);
	        new ImageUploadTask().execute(filepath);
	        }
		   
	     

	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
	
	 public void sendRequestDialog(final String userId) { Bundle params = new Bundle();       
     params.putString("title", "Invite Friend");
     params.putString("message", "https://play.google.com/store/apps/details?id=snowmada.main.view&hl=en");
     // comma seperated list of facebook IDs to preset the recipients. If left out, it will show a Friend Picker.
     params.putString("to",  userId);  // your friend id

     WebDialog requestsDialog = ( new WebDialog.RequestsDialogBuilder(HomeView.this,
             Session.getActiveSession(), params)).setOnCompleteListener(new OnCompleteListener() {
        @Override
        public void onComplete(Bundle values, FacebookException error) {
            //   Auto-generated method stub                     
            if (error != null) {
                if (error instanceof FacebookOperationCanceledException) {
                    Toast.makeText(HomeView.this.getApplicationContext(), "Request cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeView.this.getApplicationContext(),"Network Error",  Toast.LENGTH_SHORT).show();
                }
            } else {
                final String requestId = values.getString("request");
                if (requestId != null) {
                    Toast.makeText(HomeView.this.getApplicationContext(), "Request sent",  Toast.LENGTH_SHORT).show();
                    Log.i("TAG", " onComplete req dia ");                                   
                } else {
                    Toast.makeText(HomeView.this.getApplicationContext(),"Request cancelled", Toast.LENGTH_SHORT).show();
                }
            }                   
        }
     }).build();
     requestsDialog.show();}
	 
	 public static Uri getImageContentUri(Context context, File imageFile) {
	        String filePath = imageFile.getAbsolutePath();
	        Cursor cursor = context.getContentResolver().query(
	                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	                new String[] { MediaStore.Images.Media._ID },
	                MediaStore.Images.Media.DATA + "=? ",
	                new String[] { filePath }, null);
	        if (cursor != null && cursor.moveToFirst()) {
	            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
	            Uri baseUri = Uri.parse("content://media/external/images/media");
	            return Uri.withAppendedPath(baseUri, "" + id);
	        } else {
	            if (imageFile.exists()) {
	                ContentValues values = new ContentValues();
	                values.put(MediaStore.Images.Media.DATA, filePath);
	                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	            } else {
	                return null;
	            }
	        }
	    }
	
	 
	 
	 class ImageUploadTask extends AsyncTask<String, Void, String> {
			@Override
			protected String doInBackground(String... param) {
				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpContext localContext = new BasicHttpContext();					
				    Log.e("Image path", "USRE ID "+myApp.getAppInfo().userId);
						HttpPost httpPost = new HttpPost("http://clickfordevelopers.com/demo/snowmada/update_profile.php");
						MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);						
						entity.addPart("file", new FileBody(new File(param[0])));
						entity.addPart("fbid", new StringBody(myApp.getAppInfo().userId));
						httpPost.setEntity(entity);
						HttpResponse response;
						response = httpClient.execute(httpPost);
						resEntity = response.getEntity();
					
					
		            final String response_str = EntityUtils.toString(resEntity);
		            Log.e(TAG,"Response "+ response_str);
					return response_str;
				} catch (Exception e) {	
					
					return null;
				}
			}

			@Override
			protected void onProgressUpdate(Void... unsued) {

			}

			@Override
			protected void onPostExecute(String sResponse) {				
			if(sResponse!=null){
				try {
					JSONObject obj = new JSONObject(sResponse);
					if(obj.getBoolean("status")){
						myApp.getAppInfo().setImage(URL.IMAGE_PATH.getUrl()+obj.getString("image"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
				}
		}
	  private void performCrop(Uri picUri) {
		    try {
		    	 Intent cropIntent = new Intent("com.android.camera.action.CROP");
		         cropIntent.setDataAndType(picUri, "image/*");
		         cropIntent.putExtra("crop", "true");
		         cropIntent.putExtra("aspectX", 1);
		         cropIntent.putExtra("aspectY", 1);
		         cropIntent.putExtra("outputX", 160);
		         cropIntent.putExtra("outputY", 120);
		         cropIntent.putExtra("return-data", true);
		         startActivityForResult(cropIntent, PIC_CROP);
		    }
		    catch (ActivityNotFoundException anfe) {
		        String errorMessage = "Whoops - your device doesn't support the crop action!";
		        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
		        toast.show();
		    }
		}
	  
	  
		private void doSaveNewImage(Bitmap bitmap) {
			 String root = Environment.getExternalStorageDirectory().toString();
			 File myDir = new File(root + "/snomada");    
			 myDir.mkdirs();
			 String fname = ""+System.currentTimeMillis()+".jpg";
			 filepath = root + "/snomada/"+fname;		 
			 Log.e("path", root + "/snomada/"+fname);
			 File file = new File (myDir, fname);
			 if (file.exists ()) file.delete (); 
			 try {
			        FileOutputStream out = new FileOutputStream(file);
			        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			        out.flush();
			        out.close();

			 } catch (Exception e) {
			        e.printStackTrace();
			 }
		}
	

}
