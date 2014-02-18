package snowmada.main.view;

import static snowmada.main.view.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static snowmada.main.view.CommonUtilities.EXTRA_MESSAGE;
import static snowmada.main.view.CommonUtilities.SENDER_ID;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.strapin.Interface.IHome;
import com.strapin.Util.ImageLoader;
import com.strapin.adapter.AddFriendAdapter;
import com.strapin.adapter.ChatAdapter;
import com.strapin.adapter.DealsAdapter;
import com.strapin.adapter.InviteFriendAdapter;
import com.strapin.adapter.OfflineMessageAdapter;
import com.strapin.application.SnomadaApp;
import com.strapin.bean.AddFriendBean;
import com.strapin.bean.ChatBean;
import com.strapin.bean.DealsBean;
import com.strapin.bean.FacebookFriendBean;
import com.strapin.bean.InviteFriendBean;
import com.strapin.bean.MeetUpBean;
import com.strapin.bean.MessageBean;
import com.strapin.db.SnowmadaDbAdapter;
import com.strapin.global.Global;
import com.strapin.network.KlHttpClient;
import com.strapin.presenter.HomePresenter;


public class HomeView extends BaseView implements IHome {	
	
	private  Sliding mViewSlider;
	
	//private Button mBtnSlider;
	private VerticalTextView mBtnSlider;
	
	private Button mBtnAddFriend;
	private Button mBtnInviteFriend;
	private Button mBtnPendingReq;	
	
	private TextView mTvUserName;
	private TextView mTvPendingReqCounter;
	private TextView mTvMsgNotiCounter;
	private TextView mTvActiveChatFriend;
	private TextView mTvMenuBottom;
	private TextView tvDisplayTime;
	private TextView tvDisplayDate;
	
	
	private EditText mSearchAddFriend;
	private EditText et_search_invite_friend;
	private EditText mEtInputChatMsg;
	
	public ImageLoader imageLoader;
	
	private ListView mLvSnomadaFriendsList;
	private ListView mDealsList;
	private ListView mAddFriendList;
	private ListView mRequestList;
	private ListView mChatList;
	private ListView mLvMessageList;
	private ListView mLvInviteFriendList;
	
	private HomePresenter mPresenter;
	
	private GoogleMap map;
	
	private LinearLayout mIconMeetup;
	private LinearLayout mIconChatLive;
	private LinearLayout mIconGoodDeals;
	private LinearLayout mIconTrack;
	private LinearLayout mIconAdd;
	private LinearLayout mIconProfile;
	
	private LinearLayout mGreenBarMeetUplocation;
	private LinearLayout mGreenBarChatLive;
	private LinearLayout mGreenBarGoodDeals;
	private LinearLayout mGreenBarTrack;
	private LinearLayout mGreenBarAdd;
	private LinearLayout mGreenBarViewProfile;
	
	private LinearLayout mMassageBox;
	private LinearLayout mMassageBoxWithIcon;	
	private LinearLayout mMassageLayout;
	private LinearLayout mLayoutMessageNotificationList; 	
	private LinearLayout mBottonMenu;
	private LinearLayout mChatSendButton;
	private LinearLayout mReqTab;
	
	private RelativeLayout mProfileLayout;
	private RelativeLayout mDealsLayout;
	private RelativeLayout mAddFriendLayout;
	private RelativeLayout mChatLayout;
	private RelativeLayout mAddFriendSearchLayout;
	private RelativeLayout mInviteFriendSearchLayout;
	private RelativeLayout mRlProgressBarLayout;
	
	private ImageView mUserImage;
	private ImageView mProfileImage;
	private ImageView mIvSkyPatrol;
	private ImageView mTabSElectImage;	
	
	private  Dialog dialog,meetupUserDlg,infowindoDlg;	
	
	private ArrayList<DealsBean> mDealsArr = new ArrayList<DealsBean>();
	private ArrayList<AddFriendBean> mAddFriendArr = new ArrayList<AddFriendBean>();
	private ArrayList<AddFriendBean> mAddFriendSearchArr = new ArrayList<AddFriendBean>();
	
	private ArrayList<InviteFriendBean> mInviteFriendArr = new ArrayList<InviteFriendBean>();
	private ArrayList<InviteFriendBean> mInviteFriendSearchArr = new ArrayList<InviteFriendBean>();
	private ArrayList<FacebookFriendBean> facebookfriend = new ArrayList<FacebookFriendBean>();
	private ArrayList<String> mAppUserFriend = new ArrayList<String>();
	
	private SnowmadaDbAdapter mDb;
	private DealsAdapter mAdapter;
	private AddFriendAdapter mAddFriendAdapter;
	private InviteFriendAdapter mInviteFriendAdapter;
	private ChatAdapter mChatAdapter;
	private OfflineMessageAdapter mMassageAdapter;
	
	private Handler handler = new Handler();
	private Runnable runnable;
	
	private AsyncTask<Void, Void, Void> mRegisterTask;
	private AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;
	private ProgressDialog mDialog;
	
	private int mHighlightPos = 6;
	private int key=0;
	static final int TIME_DIALOG_ID = 999;
	static final int DATE_DIALOG_ID = 998;
	    
	private static final int MEET_UP_LOCATION = 3;
	private static final int CHAT_LIVE = 4;
	private static final int GOOD_DEALS = 5;
	private static final int TRACK_FRIENDS = 6;
	private static final int ADD_FRIENDS = 7;
	private static final int VIEW_PROFILE = 8;
	
	private static final int BUTTON_ADD_FRIEND = 100;
	private static final int BUTTON_INVITE_FRIEND = 101;
	private static final int BUTTON_PENDING_REQUEST = 102;
	
	
	public List<Integer> deleted_pos = new ArrayList<Integer>();
	public List<String> invalidMarkerIDs = new ArrayList<String>();
	public ArrayList<MeetUpBean> meetupinfoarr = new ArrayList<MeetUpBean>();
	public ArrayList<MeetUpBean> tempmeetupinfoarr = new ArrayList<MeetUpBean>();
	public HashMap<Marker, Long> hasmapinfo = new  HashMap<Marker, Long>();
	public long current_selected_marker_id = 0;
	private int hour;
	private int minute;
	private String am_pm;
	
	private int year;
	private int month;
	private int day;
	private long current_time_in_millisecond = 0;
	
	
	public SnomadaApp app;
		

	  	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		app = (SnomadaApp) getApplication();
		mDb = SnowmadaDbAdapter.databaseHelperInstance(getApplicationContext());
		
		mViewSlider = (Sliding) findViewById(R.id.slide_view);
		
		mBtnSlider = (VerticalTextView)findViewById(R.id.btn_slider);
		mBtnSlider.setText(Html.fromHtml("<font color=\"#ffffff\">FRIE</font><font color=\"#28b6ff\">NDS</font>"));
		mBtnAddFriend = (Button)findViewById(R.id.btn_add_friend);
		mBtnInviteFriend = (Button)findViewById(R.id.btn_invite_friend);
		mBtnPendingReq = (Button)findViewById(R.id.btn_request);
		
		mTvUserName = (TextView)findViewById(R.id.tv_home_view_user_name);
		mTvMsgNotiCounter = (TextView)findViewById(R.id.tv_msg_notification_count);
		mTvActiveChatFriend = (TextView)findViewById(R.id.tv_chat_friend);
		mTvPendingReqCounter = (TextView)findViewById(R.id.tv_pending_req_counter);
		mTvMenuBottom = (TextView)findViewById(R.id.tv_menu_bottom);
		mTvMenuBottom.setText(Html.fromHtml("<font color=\"#ffffff\">ME</font><font color=\"#28b6ff\">NU</font>"));
		
		mLvSnomadaFriendsList = (ListView)findViewById(R.id.lv_snomada_friends);
		mLvMessageList = (ListView)findViewById(R.id.lv_message);
		mChatList = (ListView)findViewById(R.id.lv_chat_instant_msg);
		mAddFriendList = (ListView)findViewById(R.id.lv_facebook_friends);		
		mDealsList = (ListView)findViewById(R.id.lv_deal);
		mRequestList = (ListView)findViewById(R.id.lv_pending_friend_req);
		mLvInviteFriendList = (ListView)findViewById(R.id.lv_invite_friend_list);
		
		
		mSearchAddFriend = (EditText)findViewById(R.id.et_input_key_for_search_add_friends);
		et_search_invite_friend=(EditText)findViewById(R.id.et_input_key_for_search_invite_friends);
		mEtInputChatMsg = (EditText)findViewById(R.id.et_input_chat_msg);
		
		mUserImage = (ImageView)findViewById(R.id.user_image);
		mTabSElectImage = (ImageView)findViewById(R.id.add_friend_text);
		mIvSkyPatrol = (ImageView)findViewById(R.id.iv_ski_patrol);
		mProfileImage = (ImageView)findViewById(R.id.profile_page_image);
		
		mReqTab = (LinearLayout)findViewById(R.id.tab_request);
		mMassageBox = (LinearLayout)findViewById(R.id.massagebox_layout);
		mMassageBoxWithIcon = (LinearLayout)findViewById(R.id.massagenotification_layout);
		mMassageLayout = (LinearLayout)findViewById(R.id.massage_layout);
		mLayoutMessageNotificationList = (LinearLayout)findViewById(R.id.layout_message_notification);
		mChatSendButton = (LinearLayout)findViewById(R.id.chat_send_button);
		mBottonMenu = (LinearLayout)findViewById(R.id.bottom_menu);		
		
		mAddFriendLayout = (RelativeLayout)findViewById(R.id.add_friend_layout);		
		mChatLayout = (RelativeLayout)findViewById(R.id.chat_outer_layout);
		mDealsLayout = (RelativeLayout)findViewById(R.id.local_deals_layout);
		mProfileLayout = (RelativeLayout)findViewById(R.id.user_profile_main_layout);
		mAddFriendSearchLayout = (RelativeLayout)findViewById(R.id.add_friend_search_layout);
		mInviteFriendSearchLayout = (RelativeLayout)findViewById(R.id.invite_friend_search_layout);
		mRlProgressBarLayout = (RelativeLayout)findViewById(R.id.rl_progress_bar);
		
		map =  ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
		map.getUiSettings().setCompassEnabled(true);	
		Global.setMap(map);
		
		mPresenter = new HomePresenter(this);
		
		mBtnSlider.setOnClickListener(this);
		mUserImage.setOnClickListener(this);
		mBottonMenu.setOnClickListener(this);
		mIvSkyPatrol.setOnClickListener(this);
		mBtnAddFriend.setOnClickListener(this);
		mBtnInviteFriend.setOnClickListener(this);
		mBtnPendingReq.setOnClickListener(this);
		mMassageLayout.setOnClickListener(this);
		mChatSendButton.setOnClickListener(this);
		map.setOnMapLongClickListener(this);		
		map.setOnMarkerClickListener(this);
		map.setOnInfoWindowClickListener(this);
		mSearchAddFriend.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable s) {	
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String searchString = mSearchAddFriend.getText().toString();							
				int textLength = searchString.length();
				//if(textLength>0){
					mAddFriendSearchArr.clear();
					for (int i = 0; i < mAddFriendArr.size(); i++) {
						String retailerName = mAddFriendArr.get(i).getName();
						if (textLength <= retailerName.length()) {
							if (searchString.equalsIgnoreCase(retailerName.substring(0, textLength))) {
								mAddFriendSearchArr.add(new AddFriendBean(mAddFriendArr.get(i).getFacebookId(), mAddFriendArr.get(i).getName()));
							}
						}
					}						
					mAddFriendAdapter = new AddFriendAdapter(mDb,HomeView.this, R.layout.add_friend_row, mAddFriendSearchArr);
					mAddFriendList.setAdapter(mAddFriendAdapter);
				//}
				

			}
		});
		et_search_invite_friend.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				String searchString = et_search_invite_friend.getText().toString();							
				int textLength = searchString.length();
				//if(textLength>0){
					mInviteFriendSearchArr.clear();
					for (int i = 0; i < mInviteFriendArr.size(); i++) {
						String retailerName = mInviteFriendArr.get(i).getName();
						if (textLength <= retailerName.length()) {
							if (searchString.equalsIgnoreCase(retailerName.substring(0, textLength))) {
								mInviteFriendSearchArr.add(new InviteFriendBean(mInviteFriendArr.get(i).getFacebookId(), mInviteFriendArr.get(i).getName()));
							}
						}
					}						
					mInviteFriendAdapter = new InviteFriendAdapter(mDb,HomeView.this, R.layout.add_friend_row, mInviteFriendSearchArr);
					mLvInviteFriendList.setAdapter(mInviteFriendAdapter);
			//	}
				

			
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				
			}
		});
		init();
		createRunnableThread();
		callService();		
		getDeviceId();		
		isSkyPetrolShow();
	    TrackLocation.databaseHelperInstance(getApplicationContext());
	    createMenuDialog();
	    defaultChatWindoOpenFromNotificationList();		
		
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
		
		mAdapter = new DealsAdapter(getApplicationContext(), R.layout.deals_row, mDealsArr);
		mDealsList.setAdapter(mAdapter);
		imageLoader=new ImageLoader(this);		
		imageLoader.DisplayImage("https://graph.facebook.com/"+mDb.getUserFbID()+"/picture",mUserImage);
		mTvUserName.setText(mDb.getUserFirstName());		
		
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
			mDb.updateSession(0);
			//Intent intent = new Intent(HomeView.this, SigninView.class);startActivity(intent);
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onClick(View v) {
/////////////**************** Header part Click listener START*****************************///////////////////
		if (v == mBtnSlider) {
			if (key == 0) {
				key = 1;
				mViewSlider.setVisibility(View.VISIBLE);
				//mBottonMenu.setBackgroundResource(R.drawable.menu_deactive);
				if (Global.isAddSnowmadaFriend) {
					mPresenter.callAdapter();
					Global.isAddSnowmadaFriend = false;
				}

			} else if (key == 1) {
				key = 0;
				mViewSlider.setVisibility(View.GONE);

			}
		}
		if(v == mUserImage){
			mHighlightPos = 8;
			mProfileLayout.setVisibility(View.VISIBLE);
			mDealsLayout.setVisibility(View.GONE);
			imageLoader.DisplayImage("https://graph.facebook.com/"+mDb.getUserFbID()+"/picture",mProfileImage);
			mBtnSlider.setVisibility(View.GONE);
		    mViewSlider.setVisibility(View.GONE);
		    //mBottonMenu.setBackgroundResource(R.drawable.menu_deactive);
		}
		
		if(v == mIvSkyPatrol){
			mPresenter.doSkiPatrolFunction();	
		}
		if(v == mMassageLayout){
			
			if(mLayoutMessageNotificationList.getVisibility() == View.VISIBLE){
				mLayoutMessageNotificationList.setVisibility(View.GONE);
			}else{
				int count = mDb.getTotalMessageCount();
				if(count>0){
					
					mLayoutMessageNotificationList.setVisibility(View.VISIBLE);
					ArrayList<MessageBean> mList = new ArrayList<MessageBean>();
					mList = mDb.getAllChatMessageInfo();
					mMassageAdapter = new OfflineMessageAdapter(mLayoutMessageNotificationList,mPresenter,HomeView.this, getApplicationContext(), R.layout.message_notification_row, mList);
					mLvMessageList.setAdapter(mMassageAdapter);
				}
			}		
		}
		
/////////////**************** Header part Click listener END*****************************///////////////////		

/////////////**************** Add friend part Click listener Start*****************************///////////////////	
	if(v == mBtnAddFriend){
		setFriendView(BUTTON_ADD_FRIEND);
		/*Global.mSelectedTab = 1;
		mRequestList.setVisibility(View.GONE);
		mAddFriendList.setVisibility(View.VISIBLE);
		mAddFriendSearchLayout.setVisibility(View.VISIBLE);
		
		mBtnPendingReq.setBackgroundResource(R.drawable.tab_unselect);
		mBtnAddFriend.setBackgroundResource(R.drawable.tab_select);
		mBtnInviteFriend.setBackgroundResource(R.drawable.tab_unselect);
		mBtnPendingReq.setTextColor(Color.parseColor("#ffffff"));
		mBtnAddFriend.setTextColor(Color.parseColor("#00ccff"));
		mBtnInviteFriend.setTextColor(Color.parseColor("#ffffff"));
		mTabSElectImage.setBackgroundResource(R.drawable.add_friend_text);*/
	}
	
	if(v == mBtnInviteFriend){
		setFriendView(BUTTON_INVITE_FRIEND);
	}
	
	if( v== mBtnPendingReq){
		setFriendView(BUTTON_PENDING_REQUEST);
		/*Global.mSelectedTab = 3;
		
		mRequestList.setVisibility(View.VISIBLE);
		mAddFriendList.setVisibility(View.GONE);
		mAddFriendSearchLayout.setVisibility(View.GONE);
		
		mReqTab.setBackgroundResource(R.drawable.tab_select);
		mBtnAddFriend.setBackgroundResource(R.drawable.tab_unselect);
		mBtnInviteFriend.setBackgroundResource(R.drawable.tab_unselect);
		mBtnPendingReq.setTextColor(Color.parseColor("#00ccff"));
		mBtnAddFriend.setTextColor(Color.parseColor("#ffffff"));
		mBtnInviteFriend.setTextColor(Color.parseColor("#ffffff"));
		mTabSElectImage.setBackgroundResource(R.drawable.requests_friend_text);
		mPresenter.getFriendRequest();*/
	
	}
/////////////**************** Add friend part Click listener End*****************************///////////////////
/////////////**************** Chat  Click listener Start*****************************///////////////////	
		if(v == mChatSendButton){

			if(!mTvActiveChatFriend.getText().toString().equalsIgnoreCase("Please Select A Friend To Chat With")){

				String msg = mEtInputChatMsg.getText().toString().trim();
				if(msg.length()>0){
					 Global.mChatArr.add(new ChatBean(mDb.getUserFirstName(), msg));
					 mChatAdapter = new ChatAdapter(HomeView.this, R.layout.chat_row, Global.mChatArr);
					 mChatList.setAdapter(mChatAdapter);
					 mChatList.setSelection(Global.mChatArr.size()-1);
					 mEtInputChatMsg.setText("");
					 new ChatWeb().execute(mDb.getUserFbID(),Global.mChatSenderID,msg);
				}
			}else{
				Toast.makeText(getApplicationContext(), "Please select a friend", Toast.LENGTH_LONG).show();
			}
		}
/////////////**************** Add  Click listener End*****************************///////////////////		
/////////////**************** Footer part Click listener Start*****************************///////////////////
		if(v == mBottonMenu){
		
				setVisibility(mHighlightPos);	
				
				mIconMeetup.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						setLayoutVisibility(MEET_UP_LOCATION);
					}
				});
				
				mIconChatLive.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						setLayoutVisibility(CHAT_LIVE);
					}
				});
				
				mIconGoodDeals.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						setLayoutVisibility(GOOD_DEALS);
					}
				});
				
				mIconTrack.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						setLayoutVisibility(TRACK_FRIENDS);
					}
				});
				
				mIconAdd.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						setLayoutVisibility(ADD_FRIENDS);
					}
				});
				
				mIconProfile.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						setLayoutVisibility(VIEW_PROFILE);
					}
				});
				
				
				DisplayMetrics displaymetrics = new DisplayMetrics();
				HomeView.this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
				int height = displaymetrics.heightPixels;
				int width = displaymetrics.widthPixels;
				if(width > 320 && height > 480){
					Window sleepWindow = dialog.getWindow();
			        WindowManager.LayoutParams lp = sleepWindow.getAttributes();
			       lp.gravity = Gravity.BOTTOM;
			        sleepWindow.setAttributes(lp);					
				}else{
					Window sleepWindow = dialog.getWindow();
			        WindowManager.LayoutParams lp = sleepWindow.getAttributes();
			        lp.gravity = Gravity.BOTTOM;
			        sleepWindow.setAttributes(lp);
					
				}
				dialog.show();
				 
				
			}
//////////////**************** Footer part Click listener END****************************///////////////////		 

/////////////**************** Footer part Click listener END*****************************///////////////////
		
		
		 }
		
	@Override
	public Activity getActivity() {
		return this;
	}
	@Override
	public Context getContext() {
		return this;
	}
	@Override
	public ListView getList() {
		return mLvSnomadaFriendsList;
	}
	
	public void setLayoutVisibility(int i){
		dialog.cancel();
		switch (i) {
		case MEET_UP_LOCATION:			
			mChatLayout.setVisibility(View.GONE);
			mDealsLayout.setVisibility(View.GONE);
			mAddFriendLayout.setVisibility(View.GONE);
			mProfileLayout.setVisibility(View.GONE);
			mBtnSlider.setVisibility(View.GONE);
			mViewSlider.setVisibility(View.GONE);
			setVisibility(MEET_UP_LOCATION);
			app.doTrackFriendLocation = false;
			app.isMeetuplocationWindoEnable = true;
			mHighlightPos = 3;			
			Global.isChatActive = false;
			map.clear();
			new GetMeetUplocation().execute();
			//setMeetUplocationMarker();
			
			/*if(mDb.getMeetUpRowCount()>0){
				ArrayList<MeetUpInfoBean> arr = new ArrayList<MeetUpInfoBean>();
				arr = mDb.getAllMeetUpInfo();
				for(int i1=0; i1<arr.size(); i1++){
					map.addMarker(new MarkerOptions()
			        .position(new LatLng(Double.valueOf(arr.get(i1).getLatitude()), Double.valueOf(arr.get(i1).getLongitude())))
				       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
				}
			}		*/				
			
			break;
			
		case CHAT_LIVE:
			mChatLayout.setVisibility(View.VISIBLE);
			mDealsLayout.setVisibility(View.GONE);
			mAddFriendLayout.setVisibility(View.GONE);
			mProfileLayout.setVisibility(View.GONE);
			mBtnSlider.setVisibility(View.VISIBLE);
			mViewSlider.setVisibility(View.GONE);
			setVisibility(CHAT_LIVE);
			mHighlightPos = 4;			
			Global.isChatActive = true;
			break;
			
		case GOOD_DEALS:			
			mChatLayout.setVisibility(View.GONE);
			mDealsLayout.setVisibility(View.VISIBLE);
			mAddFriendLayout.setVisibility(View.GONE);
			mProfileLayout.setVisibility(View.INVISIBLE);
			mBtnSlider.setVisibility(View.VISIBLE);
			mViewSlider.setVisibility(View.GONE);
			setVisibility(GOOD_DEALS);
			mHighlightPos = 5;			
			Global.isChatActive = false;
			
		break;
		
		case TRACK_FRIENDS:			
			mChatLayout.setVisibility(View.GONE);
			mDealsLayout.setVisibility(View.GONE);
			mAddFriendLayout.setVisibility(View.GONE);
			mProfileLayout.setVisibility(View.GONE);
			mBtnSlider.setVisibility(View.VISIBLE);
			mViewSlider.setVisibility(View.GONE);
			setVisibility(TRACK_FRIENDS);
			mHighlightPos = 6;			
			app.doTrackFriendLocation = false;
			app.isMeetuplocationWindoEnable = false;
			map.clear();
			Global.isChatActive = false;
		break;
		
		case ADD_FRIENDS:
			mChatLayout.setVisibility(View.GONE);
			mDealsLayout.setVisibility(View.GONE);
			mAddFriendLayout.setVisibility(View.VISIBLE);
			mProfileLayout.setVisibility(View.GONE);			
			mBtnSlider.setVisibility(View.GONE);
			mViewSlider.setVisibility(View.GONE);
			setVisibility(ADD_FRIENDS);
			mHighlightPos = 7;			
			Global.isChatActive = false;
			
			if(!(mAddFriendArr.size()>0)){
				//facebookfriend = mDb.getFacebookFriends();
			//	mAddFriendArr = mDb.getFacebookFriends();
				/*JSONArray json = new JSONArray();
				json = Global.getFriendJSOn();
				try {
					for(int i1=0; i1<json.length(); i1++){
						JSONObject c = json.getJSONObject(i1);
						String id = c.getString("uid");							
						String name = c.getString("name");
						mAddFriendArr.add(new AddFriendBean(id, name));
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} */
				/*..
				mAddFriendAdapter = new AddFriendAdapter(mDb,HomeView.this, R.layout.add_friend_row, mAddFriendArr);
				mAddFriendList.setAdapter(mAddFriendAdapter);*/
				new getAppUsers().execute();
				
			}
			
			
		
		
		break;
		
		case VIEW_PROFILE:
			mChatLayout.setVisibility(View.INVISIBLE);
			mDealsLayout.setVisibility(View.GONE);
			mAddFriendLayout.setVisibility(View.GONE);
			mProfileLayout.setVisibility(View.VISIBLE);
			mBtnSlider.setVisibility(View.GONE);
		    mViewSlider.setVisibility(View.GONE);
		    
			setVisibility(VIEW_PROFILE);
			mHighlightPos = 8;
			 Global.isChatActive = false;
			
			imageLoader.DisplayImage("https://graph.facebook.com/"+mDb.getUserFbID()+"/picture",mProfileImage);
			
		  	
		break;

		default:
			break;
		}
		
	}
	
	private void setMeetUplocationMarker() {
		
		
	}



	public void setFriendView(int i){
		switch (i) {
		case BUTTON_ADD_FRIEND:
			Global.mSelectedTab = 1;
			mRequestList.setVisibility(View.GONE);
			mLvInviteFriendList.setVisibility(View.GONE);
			mAddFriendList.setVisibility(View.VISIBLE);
			mAddFriendSearchLayout.setVisibility(View.VISIBLE);
			mInviteFriendSearchLayout.setVisibility(View.GONE);
			
			mBtnPendingReq.setBackgroundResource(R.drawable.tab_unselect);
			mBtnAddFriend.setBackgroundResource(R.drawable.tab_select);
			mBtnInviteFriend.setBackgroundResource(R.drawable.tab_unselect);
			mBtnPendingReq.setTextColor(Color.parseColor("#ffffff"));
			mBtnAddFriend.setTextColor(Color.parseColor("#00ccff"));
			mBtnInviteFriend.setTextColor(Color.parseColor("#ffffff"));
			mTabSElectImage.setBackgroundResource(R.drawable.add_friend_text);
			break;
		case BUTTON_INVITE_FRIEND:
			Global.mSelectedTab = 2;
			mRequestList.setVisibility(View.GONE);
			mAddFriendList.setVisibility(View.GONE);
			mLvInviteFriendList.setVisibility(View.VISIBLE);
			mAddFriendSearchLayout.setVisibility(View.GONE);
			mInviteFriendSearchLayout.setVisibility(View.VISIBLE);
			
			mBtnPendingReq.setBackgroundResource(R.drawable.tab_unselect);
			mBtnAddFriend.setBackgroundResource(R.drawable.tab_unselect);
			mBtnInviteFriend.setBackgroundResource(R.drawable.tab_select);
			mBtnPendingReq.setTextColor(Color.parseColor("#ffffff"));
			mBtnAddFriend.setTextColor(Color.parseColor("#ffffff"));
			mBtnInviteFriend.setTextColor(Color.parseColor("#00ccff"));
			mTabSElectImage.setBackgroundResource(R.drawable.add_friend_text);
			break;
		case BUTTON_PENDING_REQUEST:
			Global.mSelectedTab = 3;
			
			mRequestList.setVisibility(View.VISIBLE);
			mAddFriendList.setVisibility(View.GONE);
			mLvInviteFriendList.setVisibility(View.GONE);
			mAddFriendSearchLayout.setVisibility(View.GONE);
			mInviteFriendSearchLayout.setVisibility(View.GONE);
			
			mReqTab.setBackgroundResource(R.drawable.tab_select);
			mBtnAddFriend.setBackgroundResource(R.drawable.tab_unselect);
			mBtnInviteFriend.setBackgroundResource(R.drawable.tab_unselect);
			mBtnPendingReq.setTextColor(Color.parseColor("#00ccff"));
			mBtnAddFriend.setTextColor(Color.parseColor("#ffffff"));
			mBtnInviteFriend.setTextColor(Color.parseColor("#ffffff"));
			mTabSElectImage.setBackgroundResource(R.drawable.requests_friend_text);
			mPresenter.getFriendRequest();
	break;

		default:
			break;
		}
	}
	
	public void setVisibility(int i){
		switch (i) {
	
		case MEET_UP_LOCATION:
			mGreenBarMeetUplocation.setVisibility(View.VISIBLE);
			mGreenBarChatLive.setVisibility(View.INVISIBLE);
			mGreenBarGoodDeals.setVisibility(View.INVISIBLE);
			mGreenBarTrack.setVisibility(View.INVISIBLE);
			mGreenBarAdd.setVisibility(View.INVISIBLE);
			mGreenBarViewProfile.setVisibility(View.INVISIBLE);
			/*new GetMeetUplocation().execute();*/
			
			break;
			
		case CHAT_LIVE:
			mGreenBarMeetUplocation.setVisibility(View.INVISIBLE);
			mGreenBarChatLive.setVisibility(View.VISIBLE);
			mGreenBarGoodDeals.setVisibility(View.INVISIBLE);
			mGreenBarTrack.setVisibility(View.INVISIBLE);
			mGreenBarAdd.setVisibility(View.INVISIBLE);
			mGreenBarViewProfile.setVisibility(View.INVISIBLE);
			
			break;
			
		case GOOD_DEALS:
			mGreenBarMeetUplocation.setVisibility(View.INVISIBLE);
			mGreenBarChatLive.setVisibility(View.INVISIBLE);
			mGreenBarGoodDeals.setVisibility(View.VISIBLE);
			mGreenBarTrack.setVisibility(View.INVISIBLE);
			mGreenBarAdd.setVisibility(View.INVISIBLE);
			mGreenBarViewProfile.setVisibility(View.INVISIBLE);
			
			break;
			
		case TRACK_FRIENDS:
			mGreenBarMeetUplocation.setVisibility(View.INVISIBLE);
			mGreenBarChatLive.setVisibility(View.INVISIBLE);
			mGreenBarGoodDeals.setVisibility(View.INVISIBLE);
			mGreenBarTrack.setVisibility(View.VISIBLE);
			mGreenBarAdd.setVisibility(View.INVISIBLE);
			mGreenBarViewProfile.setVisibility(View.INVISIBLE);
			
			break;
			
		case ADD_FRIENDS:
			mGreenBarMeetUplocation.setVisibility(View.INVISIBLE);
			mGreenBarChatLive.setVisibility(View.INVISIBLE);
			mGreenBarGoodDeals.setVisibility(View.INVISIBLE);
			mGreenBarTrack.setVisibility(View.INVISIBLE);
			mGreenBarAdd.setVisibility(View.VISIBLE);
			mGreenBarViewProfile.setVisibility(View.INVISIBLE);
			
			break;
			
		case VIEW_PROFILE:
			mGreenBarMeetUplocation.setVisibility(View.INVISIBLE);
			mGreenBarChatLive.setVisibility(View.INVISIBLE);
			mGreenBarGoodDeals.setVisibility(View.INVISIBLE);
			mGreenBarTrack.setVisibility(View.INVISIBLE);
			mGreenBarAdd.setVisibility(View.INVISIBLE);
			mGreenBarViewProfile.setVisibility(View.VISIBLE);
			
			break;

		default:
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
	    switch(keyCode){
	    case KeyEvent.KEYCODE_BACK:
	    	 app.doTrackFriendLocation = false;
	         Global.isApplicationForeground = false;
	         handler.removeCallbacks(runnable);
	         HomeView.this.finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		
		
	}


	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			WakeLocker.acquire(getApplicationContext());
			if(newMessage!=null){
				  try {
				 JSONObject json = new JSONObject(newMessage);
				 if(json.getString("status").equalsIgnoreCase("3")){
					 String onlinestatus = json.getString("onlinestatus");
					 String onlinefbid = json.getString("fbid");
					 mPresenter.findListPosition(onlinestatus, onlinefbid);
				 }else if(json.getString("status").equalsIgnoreCase("4")){
					 Log.e("Global.mChatUserName", Global.mChatUserName);
					 Log.e("json.getString name", json.getString("name"));
					 if(Global.mChatUserName.equalsIgnoreCase(json.getString("name"))){
						 String msg = json.getString("chatmessage");
						 String name = json.getString("name");
						 Global.mChatArr.add(new ChatBean(name, msg));
						 mChatAdapter = new ChatAdapter(HomeView.this, R.layout.chat_row, Global.mChatArr);
						 mChatList.setAdapter(mChatAdapter);
						 mChatList.setSelection(Global.mChatArr.size()-1);
					 }
				 }else if(json.getString("status").equalsIgnoreCase("5")){//Status five for current friend request receive
					 String sender_id = json.getString("senderid");
					 String sender_name = json.getString("sendername");
					 String receiver_fbid = json.getString("receiver_fb_id");
					 String record_id = json.getString("recordid");
					 String track_status = json.getString("trackstatus");
					 
					 mPresenter.updatePendingFriendList(sender_id,sender_name,receiver_fbid,record_id,track_status);
					 
				 }else if(json.getString("status").equalsIgnoreCase("6")){//Request acknowledgment
					 Global.isAddSnowmadaFriend = true;
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
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
			//handler1.removeCallbacks(runnable1);
			handler.removeCallbacks(runnable);
			//handler3.removeCallbacks(runnable3);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		
		super.onDestroy();
	} 
	

	



public class ChatWeb extends AsyncTask<String, Void, Boolean>{
	protected void onPreExecute() {
		
	}

	@Override
	protected Boolean doInBackground(String... params) {
		
	  	try {
			JSONObject mJsonObject = new JSONObject();
			mJsonObject.put("sender_fb_id", params[0]);
			mJsonObject.put("receiver_fb_id", params[1]);
			mJsonObject.put("message", params[2]);
			mJsonObject.put("sender_name", mDb.getUserFirstName());
			
			JSONObject json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/chat.php", mJsonObject);
			if(json!=null){
				return json.getBoolean("status");	
			}else{
				return false;
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		
		if(result){
			
		}
	}
}



public class FriendRequestNotificationCount extends AsyncTask<String, Void, Integer>{

	
	protected void onPreExecute() {
		
	}

	@Override
	protected Integer doInBackground(String... params) {
		int count = 0;
		
	  	try {
			JSONObject mJsonObject = new JSONObject();
			mJsonObject.put("fbid", params[0]);
			JSONObject json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/count_pending_friend.php", mJsonObject);
			if(json!=null){
				count = Integer.parseInt(json.getString("totalpendingfriend"));
				return count;	
			}else{
				return count;
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		
		if(result>0){
			mTvPendingReqCounter.setVisibility(View.VISIBLE);
			mTvPendingReqCounter.setText(""+result);
		}else{
			mTvPendingReqCounter.setVisibility(View.GONE);
		}
	}
}

@Override
public TextView getChatFriend() {
	return mTvActiveChatFriend;
}

@Override
public ListView getChatListView() {
	return mChatList;
}

public void getMassageNotification(){
	int count = mDb.getMassageNotificationCount();
	if(count>0){
		mMassageBoxWithIcon.setVisibility(View.VISIBLE);
		mMassageBox.setVisibility(View.GONE);
		mTvMsgNotiCounter.setText(""+count);
		
	}else{
		mMassageBoxWithIcon.setVisibility(View.GONE);
		mMassageBox.setVisibility(View.VISIBLE);
	}
}

public void getChatWindowActive(String friendName,String fbid){
	Global.isApplicationForeground = true;
	//mBottonMenu.setBackgroundResource(R.drawable.menu_deactive);
	setVisibility(4);
	mHighlightPos = 4;
	mChatLayout.setVisibility(View.VISIBLE);
	mBtnSlider.setVisibility(View.VISIBLE);
	mProfileLayout.setVisibility(View.GONE);
	//mBottonMenu.setBackgroundResource(R.drawable.menu_deactive);
	mAddFriendLayout.setVisibility(View.GONE);
	Global.isChatActive = true;
	
	mPresenter.functionChat(fbid,friendName);
	mTvActiveChatFriend.setText(friendName);
}

@Override
public ListView getRequestList() {
	return mRequestList;
	}



@Override
public void init() {
	mLayoutMessageNotificationList.setVisibility(View.GONE);
	mMassageBox.setVisibility(View.VISIBLE);
	mMassageBoxWithIcon.setVisibility(View.GONE);		
	mTvPendingReqCounter.setVisibility(View.GONE);
	mRequestList.setVisibility(View.GONE);
	mAddFriendList.setVisibility(View.VISIBLE);
	mAddFriendSearchLayout.setVisibility(View.VISIBLE);	
	mProfileLayout.setVisibility(View.GONE);
	mChatLayout.setVisibility(View.GONE);
	mDealsLayout.setVisibility(View.GONE);
	mAddFriendLayout.setVisibility(View.GONE);		
	mBtnSlider.setVisibility(View.VISIBLE);
	mViewSlider.setVisibility(View.GONE);
	mRlProgressBarLayout.setVisibility(View.GONE);
	
	Global.isChatActive = false;		
	Global.mSelectedTab = 1;
	Global.isAddSnowmadaFriend = false;
	Global.isZoomAtUSerLocationFirstTime = true;	
	
}

@Override
public void defaultChatWindoOpenFromNotificationList() {
	Bundle bundle = getIntent().getExtras();
	if(bundle!=null){
		if(bundle.getString("event").equalsIgnoreCase("chat")){
			Global.isApplicationForeground = true;
			//mBottonMenu.setBackgroundResource(R.drawable.menu_deactive);
			setVisibility(CHAT_LIVE);
			mHighlightPos = CHAT_LIVE;
			mChatLayout.setVisibility(View.VISIBLE);
			mBtnSlider.setVisibility(View.VISIBLE);
			mProfileLayout.setVisibility(View.GONE);
			//mBottonMenu.setBackgroundResource(R.drawable.menu_deactive);
			mAddFriendLayout.setVisibility(View.GONE);
			Global.isChatActive = true;
				
			String sender_name = getIntent().getExtras().getString("sender_name");
			String sender_fb_id = getIntent().getExtras().getString("sender_fb_id");
			String[] splitStr = sender_name.split("\\s+");
			Global.mChatUserName = splitStr[0];
			Global.mChatSenderID = getIntent().getExtras().getString("sender_fb_id");
			
			mPresenter.functionChat(sender_fb_id, sender_name);
			mTvActiveChatFriend.setText(sender_name);
		}else{
			Global.isApplicationForeground = true;
			app.isMeetuplocationWindoEnable = true;
			setVisibility(MEET_UP_LOCATION);
			mHighlightPos = MEET_UP_LOCATION;
			mChatLayout.setVisibility(View.GONE);
			mBtnSlider.setVisibility(View.VISIBLE);
			mProfileLayout.setVisibility(View.GONE);
			//mBottonMenu.setBackgroundResource(R.drawable.menu_deactive);
			mAddFriendLayout.setVisibility(View.GONE);
			Global.isChatActive = false;
			new GetMeetUplocation().execute();
		}
		
	
	}

	
}



@Override
public void createMenuDialog() {

	dialog = new Dialog(HomeView.this);				
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	dialog.setContentView(R.layout.dialog);
	mIconMeetup = (LinearLayout)dialog.findViewById(R.id.submenu_3);
	mIconChatLive = (LinearLayout)dialog.findViewById(R.id.submenu_4);
	mIconGoodDeals = (LinearLayout)dialog.findViewById(R.id.submenu_5);
	mIconTrack = (LinearLayout)dialog.findViewById(R.id.submenu_6);
	mIconAdd = (LinearLayout)dialog.findViewById(R.id.submenu_7);
	mIconProfile = (LinearLayout)dialog.findViewById(R.id.submenu_8);
	
	mGreenBarMeetUplocation = (LinearLayout)dialog.findViewById(R.id.bar_3);
	mGreenBarChatLive = (LinearLayout)dialog.findViewById(R.id.bar_4);
	mGreenBarGoodDeals = (LinearLayout)dialog.findViewById(R.id.bar_5);
	mGreenBarTrack = (LinearLayout)dialog.findViewById(R.id.bar_6);
	mGreenBarAdd = (LinearLayout)dialog.findViewById(R.id.bar_7);
	mGreenBarViewProfile = (LinearLayout)dialog.findViewById(R.id.bar_8);
	dialog.setCanceledOnTouchOutside(true);
	
}

@Override
public void isSkyPetrolShow() {
	if(mDb.getSKIStatus().equalsIgnoreCase("1")){
		Global.isZoomAtUSerLocationFirstTime = false;
		mDb.updateSKI("0");
		String st[] = mDb.getSkiPetrolInfo();
		map.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(st[2]), Double.valueOf(st[3]))).title("Name:"+st[0]+" "+st[1]).snippet("Time:"+new Date().getHours()+":"+new Date().getMinutes()+":"+new Date().getSeconds()).snippet("Time:"+new Date().getHours()+":"+new Date().getMinutes()+":"+new Date().getSeconds())  .icon(BitmapDescriptorFactory.fromResource(R.drawable.friend)));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(st[2]), Double.valueOf(st[3])), 16));
	}
	
}

@Override
public void getDeviceId() {
	cd = new ConnectionDetector(getApplicationContext());
	if (!cd.isConnectingToInternet()) {
		alert.showAlertDialog(HomeView.this,
				"Internet Connection Error",
				"Please connect to working Internet connection", false);
		return;
	}		

	Intent i = getIntent();
	GCMRegistrar.checkDevice(this);
	GCMRegistrar.checkManifest(this);	
	registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
	
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
					 ServerUtilities.register(context, regId,""+mDb.getUserFbID());
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
public void createRunnableThread() {
	
	runnable = new Runnable(){
		   public void run() {
			    handler.postDelayed(runnable, 3000);
			    getMassageNotification();
			    if(app.isNetworkConnected(getApplicationContext())){
			    	 new FriendRequestNotificationCount().execute(mDb.getUserFbID());
			    }
			   
			   if(app.doTrackFriendLocation){
				   mPresenter.getFriendCurrentLocation();
			   }
			
		   } 
		 };
		 handler.postDelayed(runnable,0);	
	
}

@Override
public void callService() {
	Intent  i2  = new Intent(getApplicationContext(),ServerStatus.class);
	startService(i2);	
	
}

@Override
public RelativeLayout getProgressBarLayout() {
	return mRlProgressBarLayout;
}


@Override
public boolean onMarkerClick(Marker marker) {	
	
	if(app.isMeetuplocationWindoEnable){/*
		current_selected_marker_id = hasmapinfo.get(marker);
		for(int i=0; i<meetupinfoarr.size(); i++){
			if(current_selected_marker_id == meetupinfoarr.get(i).getId()){
				if(meetupinfoarr.get(i).getOwner().equalsIgnoreCase("ME")){
					app.isMeetuplocationEditTextEditable = true;
					setMeetuplocationDialog(marker, current_selected_marker_id,meetupinfoarr.get(i).getName(),meetupinfoarr.get(i).getLocation(),meetupinfoarr.get(i).getDescription(),meetupinfoarr.get(i).getTime(),meetupinfoarr.get(i).getOwner());
				}else{
					app.isMeetuplocationEditTextEditable = false;
					setMeetuplocationDialog(marker, current_selected_marker_id,meetupinfoarr.get(i).getName(),meetupinfoarr.get(i).getLocation(),meetupinfoarr.get(i).getDescription(),meetupinfoarr.get(i).getTime(),meetupinfoarr.get(i).getOwner());
				}
				break;
			}
		}
		
	*/}else{
		Global.isInfoWindow = !Global.isInfoWindow;	
	}
	
	
	return false;
}

@Override
public void onInfoWindowClick(Marker marker) {
	if(app.isMeetuplocationWindoEnable){

		current_selected_marker_id = hasmapinfo.get(marker);
		for(int i=0; i<meetupinfoarr.size(); i++){
			if(current_selected_marker_id == meetupinfoarr.get(i).getId()){
				if(meetupinfoarr.get(i).getOwner().equalsIgnoreCase("ME")){
					app.isMeetuplocationEditTextEditable = true;
					setMeetuplocationDialog(marker, current_selected_marker_id,meetupinfoarr.get(i).getName(),meetupinfoarr.get(i).getLocation(),meetupinfoarr.get(i).getDescription(),meetupinfoarr.get(i).getDate1(),meetupinfoarr.get(i).getTime(),meetupinfoarr.get(i).getOwner());
				}else{
					app.isMeetuplocationEditTextEditable = false;
					setMeetuplocationDialog(marker, current_selected_marker_id,meetupinfoarr.get(i).getName(),meetupinfoarr.get(i).getLocation(),meetupinfoarr.get(i).getDescription(),meetupinfoarr.get(i).getDate1(),meetupinfoarr.get(i).getTime(),meetupinfoarr.get(i).getOwner());
				}
				break;
			}
		}
		
	
	}
	
}
@Override
public void onMapLongClick(final LatLng point) {
	Log.e("Reach here", "Reach here");
	if(app.isMeetuplocationWindoEnable){
		
		 AlertDialog.Builder builder = new AlertDialog.Builder(HomeView.this);
         builder.setCancelable(true);
         builder.setTitle("Craete meet up location?");
         builder.setInverseBackgroundForced(true);
         builder.setPositiveButton("Yes",
                 new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog,
                             int which) {
                         dialog.dismiss();
                         
                        Marker m =  map.addMarker(new MarkerOptions()
                   	    .position(point) 
                   	    .title(""+mDb.getUserFirstName()+" "+mDb.getUserLastName())
                        .snippet("Update your information")

                   	    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));   
                        m.setDraggable(true);
                        current_time_in_millisecond = System.currentTimeMillis();
                        meetupinfoarr.add(new MeetUpBean(current_time_in_millisecond, "","", "", "","","ME",point.latitude,point.longitude));
                        hasmapinfo.put(m, current_time_in_millisecond);
                         // map.setInfoWindowAdapter(new CustominFoWindo());
                     }
                 });
         builder.setNegativeButton("No",
                 new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog,
                             int which) {
                         dialog.dismiss();
                         
                     }
                 });
         AlertDialog alert = builder.create();
         alert.show();
		
		
		
		 
	}
	
	
}



@Override
public void onMarkerDrag(Marker marker) {
	
	
}



@Override
public void onMarkerDragEnd(Marker marker) {
	
	
}



@Override
public void onMarkerDragStart(Marker marker) {
	
	
}





public class CustominFoWindo implements InfoWindowAdapter{

	@Override
	public View getInfoContents(Marker marker) {
		  ContextThemeWrapper cw = new ContextThemeWrapper(
                  getApplicationContext(), R.style.Transparent);
          // AlertDialog.Builder b = new AlertDialog.Builder(cw);
          LayoutInflater inflater = (LayoutInflater) cw
                  .getSystemService(LAYOUT_INFLATER_SERVICE);
          View layout = inflater.inflate(R.layout.custom_infowindow,
                  null);
          return layout;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}
	
}

public class getAppUsers extends AsyncTask<String, Void, ArrayList<String>>{		
	protected void onPreExecute() {
		mDialog = new ProgressDialog(HomeView.this);
		mDialog.setMessage("Please wait...");
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(false);
		mDialog.show();
		
	}
	@Override
	protected ArrayList<String> doInBackground(String... params) {			
	  	try {
	  		JSONObject jsonObject = new JSONObject();
	  		jsonObject.put("fbid", mDb.getUserFbID());
	  		JSONObject json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/app_user.php", jsonObject);
	        if(json.getBoolean("status")){
	        	JSONArray  jsonArray = json.getJSONArray("app_users");
	        	for(int i=0; i<jsonArray.length(); i++){
	        		JSONObject c = jsonArray.getJSONObject(i);
	        		String ids = c.getString("id");
	        		mAppUserFriend.add(ids);
	        	}
	        }
	        return mAppUserFriend;
			
		} catch (Exception e) {
			//mDialog.dismiss();
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(ArrayList<String> appusersArr) {
		mDialog.dismiss();
		boolean flag = false;
		if(appusersArr != null){
			facebookfriend = mDb.getFacebookFriends();
			try {
				
					for(int i=0; i<facebookfriend.size(); i++){
						flag = false;
					for(int j=0; j<appusersArr.size();j++){
						if(facebookfriend.get(i).getId().equalsIgnoreCase(appusersArr.get(j))){
							
							mAddFriendArr.add(new AddFriendBean(facebookfriend.get(i).getId(),facebookfriend.get(i).getName()));
							flag = true;
							break;
						}
						
					}
					if(!flag){
						mInviteFriendArr.add(new InviteFriendBean(facebookfriend.get(i).getId(),facebookfriend.get(i).getName()));
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
			mAddFriendAdapter = new AddFriendAdapter(mDb,HomeView.this, R.layout.add_friend_row, mAddFriendArr);
			mAddFriendList.setAdapter(mAddFriendAdapter);
			
			mInviteFriendAdapter = new InviteFriendAdapter(mDb,HomeView.this, R.layout.add_friend_row, mInviteFriendArr);
			mLvInviteFriendList.setAdapter(mInviteFriendAdapter);
		}
		
	}	

}
@Override
protected Dialog onCreateDialog(int id) {
	switch (id) {
	case TIME_DIALOG_ID:
		// set time picker as current time
		 return new TimePickerDialog(this, timePickerListener, hour, minute,
				true);
		
	case DATE_DIALOG_ID:
		// set date picker as current date
		 return new DatePickerDialog(this, datePickerListener, year, month,
				day);
		
	}
	return null;
	
}
private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
	public void onTimeSet(TimePicker view, int selectedHour,int selectedMinute) {
		hour = selectedHour;
		minute = selectedMinute;
		int sec = 00;
    tvDisplayTime.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)).append(":").append(0).append(0));
		   
		
		

	}
};

private static String pad(int c) {
	if (c >= 10)
		return String.valueOf(c);
	else
		return "0" + String.valueOf(c);
}

public void setMeetuplocationDialog(final Marker marker,final long current_selected_marker_id,final String meetupusername,final String meetuplocation, final String meetupdesc, final String meetupdate,final String meetuptime, final String owner){
	
	if(owner.equalsIgnoreCase("ME")){
		meetupUserDlg = new Dialog(HomeView.this);				
		meetupUserDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		meetupUserDlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		meetupUserDlg.setContentView(R.layout.meetup_info_dialog);
		Button submit = (Button)meetupUserDlg.findViewById(R.id.btn_submit);
		setCustomizeColorText(submit, "SUB", "MIT");
		Button cancel = (Button)meetupUserDlg.findViewById(R.id.btn_cancel);
		setCustomizeColorText(cancel, "CAN", "CEL");
		
		ImageView iv_date =(ImageView)meetupUserDlg.findViewById(R.id.image_date);
		
		ImageView clock =(ImageView)meetupUserDlg.findViewById(R.id.image_clock);
		tvDisplayDate= (TextView)meetupUserDlg.findViewById(R.id.tvDisplayDate1);
		tvDisplayDate.setText(meetupdate);
		tvDisplayTime = (TextView)meetupUserDlg.findViewById(R.id.tvDisplayTime1);
		tvDisplayTime.setText(meetuptime);
		final TextView name = (TextView)meetupUserDlg.findViewById(R.id.ed_name);
		name.setText(mDb.getUserFirstName()+" "+mDb.getUserLastName());
		name.setClickable(app.isMeetuplocationEditTextEditable);
		final EditText location = (EditText)meetupUserDlg.findViewById(R.id.ed_location);
		location.setText(meetuplocation);
		location.setClickable(app.isMeetuplocationEditTextEditable);
		final EditText desc = (EditText)meetupUserDlg.findViewById(R.id.ed_desc);
		desc.setText(meetupdesc);
		desc.setClickable(app.isMeetuplocationEditTextEditable);
		clock.setClickable(app.isMeetuplocationEditTextEditable);
		iv_date.setClickable(app.isMeetuplocationEditTextEditable);
		setCurrentDateOnView();
		clock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
				
			}
		});
		
		iv_date.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
				
			}
		});
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				meetupUserDlg.dismiss();
				if(owner.equalsIgnoreCase("ME")){
					String _name = name.getText().toString().trim();
					String _loc_name = location.getText().toString().trim();
					String _desc = desc.getText().toString().trim();
					String _date = tvDisplayDate.getText().toString().trim();
					String _time = tvDisplayTime.getText().toString().trim();
					long _id = current_selected_marker_id;
					double _lat = marker.getPosition().latitude;
					double _lng = marker.getPosition().longitude;
					if(_loc_name.length()==0){
						location.setError("Please enter Location Name");
					}else if(_desc.length()==0){
						desc.setError("Please enter Description");
					}else if(_date.length()==0){
						tvDisplayDate.setError("Please enter Date");
					}else if(_time.length()==0){
						tvDisplayTime.setError("Please enter Time");
					}else{
					
			        	try {
			        		
							
							
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
							
							Date date = new Date();
							String _currentdate= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
							Date currentdate = sdf.parse(_currentdate);
							Log.e("Current Date", ""+currentdate);
							Date scheduledate = sdf.parse(_date+" "+_time);
							Log.e("scheduledate Date", ""+scheduledate);
							if(scheduledate.compareTo(currentdate)<0){
								Toast.makeText(getApplicationContext(), "Plesase insert a valid Date&TIme", Toast.LENGTH_LONG).show();
							}else{
								
							
							
							Log.e("_name", _name);
							Log.e("_loc_name", _loc_name);
							Log.e("_desc", _desc);
							Log.e("_desc", _desc);
							Log.e("_date", _date);
							Log.e("_time", _time);
							Log.e("_id", ""+_id);
							Log.e("lat", ""+_lat);
							Log.e("lng", ""+_lng);
							new SubmitMeetUplocation().execute(""+current_selected_marker_id,_name,_loc_name,_desc,_time,""+_lat,""+_lng,_date);
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
	}else{
		infowindoDlg = new Dialog(HomeView.this);				
		infowindoDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		infowindoDlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		infowindoDlg.setContentView(R.layout.metup_marker_info_window);
		infowindoDlg.setCanceledOnTouchOutside(true);
		TextView _name = (TextView)infowindoDlg.findViewById(R.id.tv_meet_up_setter_name) ;
		_name.setText("Name: "+meetupusername);
		TextView _locname= (TextView)infowindoDlg.findViewById(R.id.tv_meet_up_loc_name) ;
		_locname.setText("Location: "+meetuplocation);
		TextView _desc = (TextView)infowindoDlg.findViewById(R.id.tv_meet_up_desc) ;
		_desc.setText("Description: "+meetupdesc);
		TextView _time = (TextView)infowindoDlg.findViewById(R.id.tv_meet_up_time) ;
		_time.setText("Date: "+meetupdate +"  Time: "+meetuptime);
		infowindoDlg.show();
	}	

}

public class SubmitMeetUplocation extends AsyncTask<String, String, ArrayList<MeetUpBean>>{

	@Override
	protected ArrayList<MeetUpBean> doInBackground(String... params) {
		boolean flg = false;
	  	try {
	  		
	  		JSONObject jsonObject = new JSONObject();
	  		jsonObject.put("fbid", mDb.getUserFbID());
	  		jsonObject.put("fname", mDb.getUserFirstName());
	  		jsonObject.put("marker_id", params[0]);
	  		jsonObject.put("person_name", params[1]);
	  		jsonObject.put("loc_name", params[2]);
	  		jsonObject.put("loc_desc", params[3]);
	  		jsonObject.put("meet_time", params[4]);	  		
	  		jsonObject.put("lat", params[5]);
	  		jsonObject.put("lng", params[6]);
	  		jsonObject.put("meet_date", params[7]);
	  		jsonObject.put("actn", "add");
	  		JSONObject json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/add_meet_up.php", jsonObject);
	  		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");				
			Date date = new Date();
			String _currentdate= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
			Date currentdate = sdf.parse(_currentdate);
			Log.e("Current Date", ""+currentdate);
	  		//if(json.getBoolean("status")){
	        	meetupinfoarr.clear();
	        	invalidMarkerIDs.clear();
	        	deleted_pos.clear();
	        	JSONArray jArr = json.getJSONArray("MeetList");
	        	for(int i=0;i<jArr.length();i++){
	        		JSONObject c = jArr.getJSONObject(i);
	        		long _marker_id = Long.parseLong(c.getString("marker_id"));
	        		String _name = c.getString("person_name");
	        		String _loc = c.getString("loc_name");
	        		String _desc = c.getString("loc_desc");
	        		String _identifier = c.getString("identifier");
	        		String _date = c.getString("meet_date");
	        		String time = c.getString("meet_time");
	        		double _lat = Double.parseDouble(c.getString("lat"));
	        		double _lng = Double.parseDouble(c.getString("lng"));
	        		Date scheduledate = sdf.parse(_date+" "+time);
	        		if(currentdate.getTime()<(scheduledate.getTime()+3600000)){
	        			meetupinfoarr.add(new MeetUpBean(_marker_id, _name, _loc, _desc,_date, time, _identifier,_lat,_lng));
	        		}else{
	        			invalidMarkerIDs.add(""+_marker_id);
	        		}
	        	}
	        	
	        /*	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				
				Date date = new Date();
				String _currentdate= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
				Date currentdate = sdf.parse(_currentdate);
				Log.e("Current Date", ""+currentdate);
	        	for(int i=0;i<meetupinfoarr.size();i++){
	        		
					Date scheduledate = sdf.parse(meetupinfoarr.get(i).getDate1()+" "+meetupinfoarr.get(i).getTime());
					if(currentdate.getTime()>(scheduledate.getTime()+3600000)){
						invalidMarkerIDs.add(""+meetupinfoarr.get(i).getId());
						deleted_pos.add(i);
					}
	        	}
	        	
	        	for(int j=0; j<deleted_pos.size();j++){
	        		Log.e("deleted_pos.get(j)", ""+deleted_pos.get(j));
	        		meetupinfoarr.remove(deleted_pos.get(j));
	        	}*/
	        	return meetupinfoarr;
	      //  }
	        	
	  		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(ArrayList<MeetUpBean> result) {
		super.onPostExecute(result);
		mDialog.cancel();
		map.clear();
		if(result != null){
			Log.e("Meet up Size", ""+result.size());
			
			for(int i=0; i<result.size();i++){
				Log.e("name", result.get(i).getName());
				//if(result.get(i).getName().equalsIgnoreCase("asanti namrata")){
					Log.i("Meet Loc", ""+result.get(i).getLocation());
					if(result.get(i).getOwner().equalsIgnoreCase("ME")){
						 Marker m =  map.addMarker(new MarkerOptions()
				    	    .position(new LatLng(result.get(i).getLat(), result.get(i).getLng())) 
				    	    .title("Name:"+result.get(i).getName())
				         .snippet("Location:"+result.get(i).getLocation())
				    	    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));   
				         m.setDraggable(true);
				         
				         hasmapinfo.put(m, result.get(i).getId());
					}else{
						 Marker m =  map.addMarker(new MarkerOptions()
				    	    .position(new LatLng(result.get(i).getLat(), result.get(i).getLng())) 
				    	    .title("Name:"+result.get(i).getName())
				         .snippet("Location:"+result.get(i).getLocation())
				    	    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));   
				         m.setDraggable(false);
				         
				         hasmapinfo.put(m, result.get(i).getId());
					}
					
		}
		
				
			//}
			
		}
		if (invalidMarkerIDs != null) {
			if (invalidMarkerIDs.size() > 0) {
				deleteOldMarker();
			}
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDialog = new ProgressDialog(HomeView.this);
		mDialog.setMessage("Please wait...");
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(false);
		mDialog.show();
	}
	
	
	
}
public class GetMeetUplocation extends AsyncTask<String, String, ArrayList<MeetUpBean>>{

	@Override
	protected ArrayList<MeetUpBean> doInBackground(String... params) {
		boolean flg = false;
	  	try {
	  		
	  		JSONObject jsonObject = new JSONObject();
	  		jsonObject.put("fbid", mDb.getUserFbID());
	  		JSONObject json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/list_meetup.php", jsonObject);
	  		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");				
			Date date = new Date();
			String _currentdate= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
			Date currentdate = sdf.parse(_currentdate);
			Log.e("Current Date111", ""+currentdate);
	  		//if(json.getBoolean("status")){
	  			Log.e("Reach here", "Reach here");
	        	meetupinfoarr.clear();
	        	invalidMarkerIDs.clear();
	        	deleted_pos.clear();
	        	JSONArray jArr = json.getJSONArray("MeetList");
	        	Log.e("JSON Array Length", ""+jArr.length());
	        	for(int i=0;i<jArr.length();i++){
	        		JSONObject c = jArr.getJSONObject(i);
	        		long _marker_id = Long.parseLong(c.getString("marker_id"));
	        		String _name = c.getString("person_name");
	        		String _loc = c.getString("loc_name");
	        		String _desc = c.getString("loc_desc");
	        		String _identifier = c.getString("identifier");
	        		String _date = c.getString("meet_date");
	        		String time = c.getString("meet_time");
	        		double _lat = Double.parseDouble(c.getString("lat"));
	        		double _lng = Double.parseDouble(c.getString("lng"));
	        		Date scheduledate = sdf.parse(_date+" "+time);
	        		if(currentdate.getTime()<(scheduledate.getTime()+3600000)){
	        			meetupinfoarr.add(new MeetUpBean(_marker_id, _name, _loc, _desc,_date, time, _identifier,_lat,_lng));
	        		}else{
	        			invalidMarkerIDs.add(""+_marker_id);
	        		}
	        		
	        	}
	        	
	        	/*for(int i=0;i<meetupinfoarr.size();i++){
	        		
					Date scheduledate = sdf.parse(meetupinfoarr.get(i).getDate1()+" "+meetupinfoarr.get(i).getTime());
					if(currentdate.getTime()<(scheduledate.getTime()+3600000)){
						invalidMarkerIDs.add(""+meetupinfoarr.get(i).getId());
						deleted_pos.add(i);
					}
	        	}
	        	
	        	for(int j=0; j<deleted_pos.size();j++){
	        		Log.e("deleted_pos.get(j)", ""+deleted_pos.get(j));
	        		meetupinfoarr.remove(deleted_pos.get(j));
	        	}*/
	        	return meetupinfoarr;
	       // }
	        	
	  		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(ArrayList<MeetUpBean> result) {
		super.onPostExecute(result);
		mDialog.cancel();
		//Log.e("Meet up Size", ""+result.size());
		if(result != null){
			Log.e("Meet up Size", ""+result.size());
			
			for(int i=0; i<result.size();i++){
				Log.e("name", result.get(i).getName());
				//if(result.get(i).getName().equalsIgnoreCase("asanti namrata")){
					Log.i("Meet Loc", ""+result.get(i).getLocation());
					if(result.get(i).getOwner().equalsIgnoreCase("ME")){
						 Marker m =  map.addMarker(new MarkerOptions()
				    	    .position(new LatLng(result.get(i).getLat(), result.get(i).getLng())) 
				    	    .title("Name:"+result.get(i).getName())
				         .snippet("Location:"+result.get(i).getLocation())
				    	    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));   
				         m.setDraggable(true);
				         
				         hasmapinfo.put(m, result.get(i).getId());
					}else{
						 Marker m =  map.addMarker(new MarkerOptions()
				    	    .position(new LatLng(result.get(i).getLat(), result.get(i).getLng())) 
				    	    .title("Name:"+result.get(i).getName())
				         .snippet("Location:"+result.get(i).getLocation())
				    	    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));   
				         m.setDraggable(false);
				         
				         hasmapinfo.put(m, result.get(i).getId());
					}
					
					
		}
				if (invalidMarkerIDs != null) {
					if (invalidMarkerIDs.size() > 0) {
						deleteOldMarker();
					}
				}
			
		}
		
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDialog = new ProgressDialog(HomeView.this);
		mDialog.setMessage("Please wait...");		
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(false);
		mDialog.show();
	}
	
	
	
}



private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

	// when dialog box is closed, below method will be called.
	public void onDateSet(DatePicker view, int selectedYear,
			int selectedMonth, int selectedDay) {
		year = selectedYear;
		month = selectedMonth;
		day = selectedDay;

		// set selected date into textview
		if((month + 1)<10 && day<10){
			tvDisplayDate.setText(new StringBuilder().append(year).append("-").append(0).append(month + 1).append("-").append(0).append(day));	
		}else if((month + 1)<10){
			tvDisplayDate.setText(new StringBuilder().append(year).append("-").append(0).append(month + 1).append("-").append(day));
		}else if(day<10){
			tvDisplayDate.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(0).append(day));
		}else{
			tvDisplayDate.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day));
		}
	
	}
};

	public void setCurrentDateOnView() {
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
}
	
	public void deleteOldMarker(){
		Thread t = new Thread(){
			String st;
			public void run(){				
				try {
					for(int i=0; i<invalidMarkerIDs.size();i++){
						if(i==0){
							st = invalidMarkerIDs.get(i);
						}else{
							st = st+","+invalidMarkerIDs.get(i);
						}
						
					}
					JSONObject json  = new JSONObject();
					json.put("deleted_ids", st);
					Log.e("IDS", st);
					KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/del_meet_up.php", json);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
}

