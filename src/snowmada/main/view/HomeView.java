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
import android.os.Message;
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
import com.strapin.bean.AddFriendBean;
import com.strapin.bean.ChatBean;
import com.strapin.bean.DealsBean;
import com.strapin.bean.FacebookFriendBean;
import com.strapin.bean.InviteFriendBean;
import com.strapin.bean.MeetUpBean;
import com.strapin.bean.NewMessage;
import com.strapin.global.Global;
import com.strapin.network.KlHttpClient;
import com.strapin.presenter.HomePresenter;

public class HomeView extends BaseView implements IHome {	
	
	private  Sliding mViewSlider;
	
	private VerticalTextView mBtnSlider;
	
	private Button mTabAddFriend;
	private Button mTabInviteFriend;
	private Button mTabPendingRequest;	
	
	private TextView mUserName;
	private TextView mPendingReqCounter;
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
	
	public HomePresenter presenter;
	
	public GoogleMap map;
	
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
	
	private  Dialog dialog,meetupUserDlg;	
	
	private ArrayList<DealsBean> mDealsArr = new ArrayList<DealsBean>();
	private ArrayList<AddFriendBean> mAddFriendArr = new ArrayList<AddFriendBean>();
	private ArrayList<AddFriendBean> mAddFriendSearchArr = new ArrayList<AddFriendBean>();
	
	private ArrayList<InviteFriendBean> mInviteFriendArr = new ArrayList<InviteFriendBean>();
	private ArrayList<InviteFriendBean> mInviteFriendSearchArr = new ArrayList<InviteFriendBean>();
	private ArrayList<FacebookFriendBean> facebookfriend = new ArrayList<FacebookFriendBean>();
	private ArrayList<String> mAppUserFriend = new ArrayList<String>();
	private ArrayList<NewMessage> msg = new ArrayList<NewMessage>();
	
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
	
	private int mHighlightPos = 6;
	private int key=0;
	static final int TIME_DIALOG_ID = 999;
	static final int DATE_DIALOG_ID = 998;
	    
	public static final int MEET_UP_LOCATION = 3;
	public static final int CHAT_LIVE = 4;
	public static final int GOOD_DEALS = 5;
	public static final int TRACK_FRIENDS = 6;
	public static final int ADD_FRIENDS = 7;
	public static final int VIEW_PROFILE = 8;
	
	public static final int BUTTON_ADD_FRIEND = 100;
	public static final int BUTTON_INVITE_FRIEND = 101;
	public static final int BUTTON_PENDING_REQUEST = 102;
	
	
	public List<Integer> deleted_pos = new ArrayList<Integer>();
	public List<String> invalidMarkerIDs = new ArrayList<String>();
	public ArrayList<MeetUpBean> meetupinfoarr = new ArrayList<MeetUpBean>();
	public HashMap<Marker, Long> markerIdHasMap = new  HashMap<Marker, Long>();
	public long current_selected_marker_id = 0;
	public int hour;
	public int minute;
	public int year;
	public int month;
	public int day;
	public long current_time_in_millisecond = 0;
	
	public   Marker m;
	public int pos = -1;
	
	public String[] markersst;
	public int updatepos = -1;
	
	public Handler handle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				meetupinfoarr.get(updatepos).setName(db.getUserFirstName()+" "+db.getUserLastName());
				meetupinfoarr.get(updatepos).setLocation(markersst[2]);
				meetupinfoarr.get(updatepos).setDescription(markersst[3]);
				meetupinfoarr.get(updatepos).setDate1(markersst[7]);
				meetupinfoarr.get(updatepos).setTime(markersst[4]);
				meetupinfoarr.get(updatepos).setLat(Double.parseDouble(markersst[5]));
				meetupinfoarr.get(updatepos).setLng(Double.parseDouble(markersst[6]));
				
				map.clear();
				mViewSlider.setVisibility(View.VISIBLE);
				mViewSlider.setVisibility(View.GONE);
				for(int i=0; i<meetupinfoarr.size();i++){
						if(meetupinfoarr.get(i).getOwner().equalsIgnoreCase("ME")){
							 m =  map.addMarker(new MarkerOptions()
					    	     .position(new LatLng(meetupinfoarr.get(i).getLat(), meetupinfoarr.get(i).getLng())) 
					    	     .title("Name:"+meetupinfoarr.get(i).getName())
					    	     .snippet("Location:"+meetupinfoarr.get(i).getLocation()+"\nDescription: "+meetupinfoarr.get(i).getDescription()+"\nDate: "+meetupinfoarr.get(i).getDate1()+"\nTime: "+meetupinfoarr.get(i).getTime())
					             .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));   
					             m.setDraggable(true);					         
					             markerIdHasMap.put(m, meetupinfoarr.get(i).getId());
						}else{
							 m =  map.addMarker(new MarkerOptions()
					    	     .position(new LatLng(meetupinfoarr.get(i).getLat(), meetupinfoarr.get(i).getLng())) 
					    	     .title("Name:"+meetupinfoarr.get(i).getName())
					    	     .snippet("Location:"+meetupinfoarr.get(i).getLocation()+"\nDescription: "+meetupinfoarr.get(i).getDescription()+"\nDate: "+meetupinfoarr.get(i).getDate1()+"\nTime: "+meetupinfoarr.get(i).getTime())
					             .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));   
					             m.setDraggable(false);					         
					             markerIdHasMap.put(m, meetupinfoarr.get(i).getId());
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
		setContentView(R.layout.home);
		
		
		mViewSlider = (Sliding) findViewById(R.id.slide_view);
		
		mBtnSlider = (VerticalTextView)findViewById(R.id.btn_slider);
		mBtnSlider.setText(Html.fromHtml("<font color=\"#ffffff\">FRIE</font><font color=\"#28b6ff\">NDS</font>"));
		mTabAddFriend = (Button)findViewById(R.id.btn_add_friend);
		mTabInviteFriend = (Button)findViewById(R.id.btn_invite_friend);
		mTabPendingRequest = (Button)findViewById(R.id.btn_request);
		
		mUserName = (TextView)findViewById(R.id.tv_home_view_user_name);
		mTvMsgNotiCounter = (TextView)findViewById(R.id.tv_msg_notification_count);
		mTvActiveChatFriend = (TextView)findViewById(R.id.tv_chat_friend);
		mPendingReqCounter = (TextView)findViewById(R.id.tv_pending_req_counter);
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
		map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
		Global.setMap(map);
		
		presenter = new HomePresenter(this);
		
		mBtnSlider.setOnClickListener(this);
		mUserImage.setOnClickListener(this);
		mBottonMenu.setOnClickListener(this);
		mIvSkyPatrol.setOnClickListener(this);
		mTabAddFriend.setOnClickListener(this);
		mTabInviteFriend.setOnClickListener(this);
		mTabPendingRequest.setOnClickListener(this);
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
					mAddFriendSearchArr.clear();
					for (int i = 0; i < mAddFriendArr.size(); i++) {
						String retailerName = mAddFriendArr.get(i).getName();
						if (textLength <= retailerName.length()) {
							if (searchString.equalsIgnoreCase(retailerName.substring(0, textLength))) {
								mAddFriendSearchArr.add(new AddFriendBean(mAddFriendArr.get(i).getFacebookId(), mAddFriendArr.get(i).getName()));
							}
						}
					}						
					mAddFriendAdapter = new AddFriendAdapter(db,HomeView.this, R.layout.add_friend_row, mAddFriendSearchArr);
					mAddFriendList.setAdapter(mAddFriendAdapter);
				
			}
		});
		et_search_invite_friend.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				String searchString = et_search_invite_friend.getText().toString();							
				int textLength = searchString.length();
					mInviteFriendSearchArr.clear();
					for (int i = 0; i < mInviteFriendArr.size(); i++) {
						String retailerName = mInviteFriendArr.get(i).getName();
						if (textLength <= retailerName.length()) {
							if (searchString.equalsIgnoreCase(retailerName.substring(0, textLength))) {
								mInviteFriendSearchArr.add(new InviteFriendBean(mInviteFriendArr.get(i).getFacebookId(), mInviteFriendArr.get(i).getName()));
							}
						}
					}						
					mInviteFriendAdapter = new InviteFriendAdapter(db,HomeView.this, R.layout.add_friend_row, mInviteFriendSearchArr);
					mLvInviteFriendList.setAdapter(mInviteFriendAdapter);
		
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				
			}
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
		
		mAdapter = new DealsAdapter(getApplicationContext(), R.layout.deals_row, mDealsArr);
		mDealsList.setAdapter(mAdapter);
		imageLoader=new ImageLoader(this);		
				
		
	}
	
	
	@Override
	public void init() {
		mLayoutMessageNotificationList.setVisibility(View.GONE);
		mMassageBox.setVisibility(View.VISIBLE);
		mMassageBoxWithIcon.setVisibility(View.GONE);		
		mPendingReqCounter.setVisibility(View.GONE);
		mRequestList.setVisibility(View.GONE);
		mAddFriendSearchLayout.setVisibility(View.VISIBLE);	
		
		mAddFriendList.setVisibility(View.VISIBLE);
		
		mProfileLayout.setVisibility(View.GONE);
		mChatLayout.setVisibility(View.GONE);
		mDealsLayout.setVisibility(View.GONE);
		mAddFriendLayout.setVisibility(View.GONE);		
		mBtnSlider.setVisibility(View.VISIBLE);
		mViewSlider.setVisibility(View.GONE);
		
		mRlProgressBarLayout.setVisibility(View.GONE);
		
		myApp.isChatActive = false;		
		Global.mSelectedTab = 1;
		Global.isAddSnowmadaFriend = false;
		Global.isZoomAtUSerLocationFirstTime = true;	
		
		doCallasyncWeb();
		callService();		
		getDeviceId();		
		isSkyPetrolShow();
	    
	    createMenuDialog();
		defaultChatWindoOpenFromNotificationList();
		TrackLocation.databaseHelperInstance(getApplicationContext());
		imageLoader.DisplayImage("https://graph.facebook.com/"+db.getUserFbID()+"/picture",mUserImage);
		mUserName.setText(db.getUserFirstName());
		new GetMeetUplocation().execute();
		new MessageWeb().execute();
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
					presenter.callAdapter();
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
			imageLoader.DisplayImage("https://graph.facebook.com/"+db.getUserFbID()+"/picture",mProfileImage);
			mBtnSlider.setVisibility(View.GONE);
		    mViewSlider.setVisibility(View.GONE);
		    //mBottonMenu.setBackgroundResource(R.drawable.menu_deactive);
		}
		
		if(v == mIvSkyPatrol){
			presenter.doSkiPatrolFunction();	
		}
		if(v == mMassageLayout){
			Log.e("Message icon click", ""+msg.size());
			if(msg.size()>0){
				if(mLayoutMessageNotificationList.getVisibility() ==View.VISIBLE){
					mLayoutMessageNotificationList.setVisibility(View.GONE);
				}else{
					mLayoutMessageNotificationList.setVisibility(View.VISIBLE);
					mMassageAdapter = new OfflineMessageAdapter(presenter,HomeView.this, R.layout.message_notification_row, msg);
					mLvMessageList.setAdapter(mMassageAdapter);
				}
				
			}
		
		}
		
//************************* Header part Click listener END*****************************************************************	

//************************ Add friend part Click listener Start************************************************************	
		if (v == mTabAddFriend) {
			setFriendView(BUTTON_ADD_FRIEND);
		}

		if (v == mTabInviteFriend) {
			setFriendView(BUTTON_INVITE_FRIEND);
		}

		if (v == mTabPendingRequest) {
			setFriendView(BUTTON_PENDING_REQUEST);
		}
//****************************** Add friend part Click listener End********************************************************
//****************************** Chat  Click listener Start****************************************************************	
		if(v == mChatSendButton){

			if(!mTvActiveChatFriend.getText().toString().equalsIgnoreCase("Please Select A Friend To Chat With")){

				String msg = mEtInputChatMsg.getText().toString().trim();
				if(msg.length()>0){
					 Global.mChatArr.add(new ChatBean(db.getUserFirstName(), msg));
					 mChatAdapter = new ChatAdapter(HomeView.this, R.layout.chat_row, Global.mChatArr);
					 mChatList.setAdapter(mChatAdapter);
					 mChatList.setSelection(Global.mChatArr.size()-1);
					 mEtInputChatMsg.setText("");
					 new ChatWeb().execute(db.getUserFbID(),myApp.getAppInfo().senderIdChat,msg);
				}
			}else{
				Toast.makeText(getApplicationContext(), "Please select a friend", Toast.LENGTH_LONG).show();
			}
		}
//******************************* Add  Click listener End*************************************************************	
//******************************* Footer part Click listener Start****************************************************
		if(v == mBottonMenu){
			mIconMeetup.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						myApp.doTrackFriendLocation = false;
						setLayoutVisibility(View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE,View.VISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,false,true,false,MEET_UP_LOCATION);
						
					}
				});
				
				mIconChatLive.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						setLayoutVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE,View.INVISIBLE,View.VISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,false,false,true,CHAT_LIVE);
						
					}
				});
				
				mIconGoodDeals.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						setLayoutVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE, View.VISIBLE, View.GONE,View.INVISIBLE,View.INVISIBLE,View.VISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,false,false,false,GOOD_DEALS);
						
					}
				});
				
				mIconTrack.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						setLayoutVisibility(View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.VISIBLE,View.INVISIBLE,View.INVISIBLE,false,false,false,TRACK_FRIENDS);
						
					}
				});
				
				mIconAdd.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						setLayoutVisibility(View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.VISIBLE,View.INVISIBLE,false,false,false,ADD_FRIENDS);
						if(!(mAddFriendArr.size()>0)){
							new AppUsers().execute();
							
						}
						
					}
				});
				
				mIconProfile.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						setLayoutVisibility(View.GONE, View.GONE, View.GONE, View.VISIBLE, View.VISIBLE, View.GONE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.VISIBLE,false,false,false,VIEW_PROFILE);
						imageLoader.DisplayImage("https://graph.facebook.com/"+db.getUserFbID()+"/picture",mProfileImage);
						
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
//************************************ Footer part Click listener END*************************************************		 

//************************************ Footer part Click listener END*********************************************************
		
		
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

	public void setFriendView(int i){
		switch (i) {
		case BUTTON_ADD_FRIEND:
			setFriendTab(1,View.VISIBLE,View.GONE,View.GONE,View.VISIBLE,View.GONE,R.drawable.tab_select,R.drawable.tab_unselect,R.drawable.tab_unselect,"#00ccff","#ffffff","#ffffff",R.drawable.add_friend_text);		
			break;
		case BUTTON_INVITE_FRIEND:
			setFriendTab(2,View.GONE,View.VISIBLE,View.GONE,View.GONE,View.VISIBLE,R.drawable.tab_unselect,R.drawable.tab_select,R.drawable.tab_unselect,"#ffffff","#00ccff","#ffffff",R.drawable.add_friend_text);
			break;
		case BUTTON_PENDING_REQUEST:
			setFriendTab(3,View.GONE,View.GONE,View.VISIBLE,View.GONE,View.GONE,R.drawable.tab_unselect,R.drawable.tab_unselect,R.drawable.tab_select,"#ffffff","#ffffff","#00ccff",R.drawable.requests_friend_text);			
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
	    switch(keyCode){
	    case KeyEvent.KEYCODE_BACK:
	    	 myApp.doTrackFriendLocation = false;
	         /*Global.isApplicationForeground*/ myApp.getAppInfo().isAppForeground= false;
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
					 presenter.findListPosition(onlinestatus, onlinefbid);
				 }else if(json.getString("status").equalsIgnoreCase("4")){
					 new MessageWeb().execute();
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
					 
					 presenter.updatePendingFriendList(sender_id,sender_name,receiver_fbid,record_id,track_status);
					 
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
			handler.removeCallbacks(runnable);			
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		
		super.onDestroy();
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
	int count = db.getMassageNotificationCount();
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
	myApp.getAppInfo().isAppForeground= true;
	setLayoutVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE,View.INVISIBLE,View.VISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,false,false,true,CHAT_LIVE);	
	presenter.functionChat(fbid,friendName);
	mTvActiveChatFriend.setText(friendName);
}

@Override
public ListView getRequestList() {
	return mRequestList;
	}





@Override
public void defaultChatWindoOpenFromNotificationList() {
	Bundle bundle = getIntent().getExtras();
	if(bundle!=null){
		Log.e("bundle.getString",""+bundle.getString("event"));
		if(bundle.getString("event").equalsIgnoreCase("chat")){
			Toast.makeText(getApplicationContext(), "Chat", 1000).show();
			myApp.getAppInfo().isAppForeground = true;
			setLayoutVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE,View.INVISIBLE,View.VISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,false,false,true,CHAT_LIVE);
				
			String sender_name = getIntent().getExtras().getString("sender_name");
			String sender_fb_id = getIntent().getExtras().getString("sender_fb_id");
			String[] splitStr = sender_name.split("\\s+");
			Global.mChatUserName = splitStr[0];
			myApp.getAppInfo().setSenderIDChat(getIntent().getExtras().getString("sender_fb_id"));
			presenter.functionChat(sender_fb_id, sender_name);
			mTvActiveChatFriend.setText(sender_name);
		}else{
			Log.e("bundle.getStringq4req3wr",""+bundle.getString("event"));
			Global.isZoomAtUSerLocationFirstTime = false;
			myApp.getAppInfo().setIsAlertForSKIPatrol(false);
			db.getMeetUpLocation1();
			String st[] = db.getMeetUpLocation();
			for(int i=0;i<st.length;i++){
				Log.e("LOG",""+st[i]);
			}
			db.updateMeetUpStatus(Integer.parseInt(st[0]));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(st[1]), Double.valueOf(st[2])), 16));
			
			
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

@SuppressWarnings("deprecation")
@Override
public void isSkyPetrolShow() {
	if(myApp.getAppInfo().isAlertForSKIPatrol){
		Global.isZoomAtUSerLocationFirstTime = false;
		myApp.getAppInfo().setIsAlertForSKIPatrol(false);
		String st[] = db.getSkiPetrolInfo();
		presenter.trackSKIPatrol(st[0], st[1]+" "+st[2]);
		/*map.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(st[2]), Double.valueOf(st[3]))).title("Name:"+st[0]+" "+st[1]).snippet("Time:"+new Date().getHours()+":"+new Date().getMinutes()+":"+new Date().getSeconds()).snippet("Time:"+new Date().getHours()+":"+new Date().getMinutes()+":"+new Date().getSeconds())  .icon(BitmapDescriptorFactory.fromResource(R.drawable.friend)));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(st[2]), Double.valueOf(st[3])), 16));*/
	}
	
}

@Override
public void getDeviceId() {
	cd = new ConnectionDetector(getApplicationContext());
	if (!cd.isConnectingToInternet()) {
		alert.showAlertDialog(HomeView.this,"Internet Connection Error","Please connect to working Internet connection", false);
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
					 ServerUtilities.register(context, regId,""+db.getUserFbID());
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
public void doCallasyncWeb() {
	
	runnable = new Runnable(){
		   public void run() {
			    handler.postDelayed(runnable, 3000);
			    getMassageNotification();
			    if(myApp.isNetworkConnected(getApplicationContext())){
			    	 new FriendRequestNotificationCount().execute();
			    	/* new MessageWeb().execute();*/
			    }
			   
			   if(myApp.doTrackFriendLocation){
				   presenter.getFriendCurrentLocation();
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
public void onInfoWindowClick(Marker marker) {
		current_selected_marker_id = markerIdHasMap.get(marker);
		for(int i=0; i<meetupinfoarr.size(); i++){
			if(current_selected_marker_id == meetupinfoarr.get(i).getId()){
				if(meetupinfoarr.get(i).getOwner().equalsIgnoreCase("ME")){
					myApp.isMeetuplocationEditTextEditable = true;
					setMeetuplocationDialog(marker, current_selected_marker_id,meetupinfoarr.get(i).getName(),meetupinfoarr.get(i).getLocation(),meetupinfoarr.get(i).getDescription(),meetupinfoarr.get(i).getDate1(),meetupinfoarr.get(i).getTime(),meetupinfoarr.get(i).getOwner());
				}
				break;
			}
		}

}
@Override
public void onMapLongClick(final LatLng point) {
	if(myApp.isMeetuplocationWindoEnable){
		
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
                         
                         m =  map.addMarker(new MarkerOptions()
                   	    .position(point) 
                   	    .title(""+db.getUserFirstName()+" "+db.getUserLastName())
                        .snippet("Update your information")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));   
                        m.setDraggable(true);
                       
                        current_time_in_millisecond = System.currentTimeMillis();
                        meetupinfoarr.add(new MeetUpBean(current_time_in_millisecond, "","", "", "","","ME",point.latitude,point.longitude));
                        markerIdHasMap.put(m, current_time_in_millisecond);
                       
                     }
                 });
         builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
		 return new TimePickerDialog(this, timePickerListener, hour, minute,true);		
	case DATE_DIALOG_ID:
		 return new DatePickerDialog(this, datePickerListener, year, month,	day);		
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
		name.setText(db.getUserFirstName()+" "+db.getUserLastName());
		name.setClickable(myApp.isMeetuplocationEditTextEditable);
		final EditText location = (EditText)meetupUserDlg.findViewById(R.id.ed_location);
		location.setText(meetuplocation);
		location.setClickable(myApp.isMeetuplocationEditTextEditable);
		final EditText desc = (EditText)meetupUserDlg.findViewById(R.id.ed_desc);
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
							Date scheduledate = sdf.parse(_date+" "+_time);
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
}






private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

	public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
		year = selectedYear;
		month = selectedMonth;
		day = selectedDay;
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
	
	private class CustomInfoWindowAdapter implements InfoWindowAdapter {		 
        private View view; 
        public CustomInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.custom_info_window,  null);
        } 
        @Override
        public View getInfoContents(Marker marker) { 
            if (HomeView.this.m != null
                    && HomeView.this.m.isInfoWindowShown()) {
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
	
		
	public void setLayoutVisibility(int viewchat, int viewgooddeals,int viewaddfriend,
			int viewprofile, int btnslider, int viewslider,
			int barmeetup, int barchat, int bardeals, int bartrack,
			int baraddfriend, int barprofile,boolean trackfriends,boolean showmeetup,
			boolean ischatactive, int hightmenu) {

		mChatLayout.setVisibility(viewchat);
		mDealsLayout.setVisibility(viewgooddeals);
		mAddFriendLayout.setVisibility(viewaddfriend);
		mProfileLayout.setVisibility(viewprofile);
		mBtnSlider.setVisibility(btnslider);
		mViewSlider.setVisibility(viewslider);

		mGreenBarMeetUplocation.setVisibility(barmeetup);
		mGreenBarChatLive.setVisibility(barchat);
		mGreenBarGoodDeals.setVisibility(bardeals);
		mGreenBarTrack.setVisibility(bartrack);
		mGreenBarAdd.setVisibility(baraddfriend);
		mGreenBarViewProfile.setVisibility(barprofile);
		
		myApp.doTrackFriendLocation = trackfriends;
		myApp.isMeetuplocationWindoEnable = showmeetup;				
		myApp.isChatActive = ischatactive;
		mHighlightPos = hightmenu;	

	}
	
	public void doTrack(){
		setLayoutVisibility(View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.VISIBLE,View.INVISIBLE,View.INVISIBLE,false,false,false,TRACK_FRIENDS);
		//map.clear();
		map.setInfoWindowAdapter(null);
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
				mJsonObject.put("sender_name", db.getUserFirstName());
				
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
	
	public class GetMeetUplocation extends AsyncTask<String, String, ArrayList<MeetUpBean>>{

		@Override
		protected ArrayList<MeetUpBean> doInBackground(String... params) {
			boolean flg = false;
		  	try {
		  		
		  		JSONObject jsonObject = new JSONObject();
		  		jsonObject.put("fbid", db.getUserFbID());
		  		JSONObject json = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/list_meetup.php", jsonObject);
		  		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");				
				Date date = new Date();
				String _currentdate= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
				Date currentdate = sdf.parse(_currentdate);
				
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
		        	return meetupinfoarr;
		     	
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<MeetUpBean> result) {
			super.onPostExecute(result);
			if(result != null){
				for(int i=0; i<result.size();i++){
					Log.e("name", result.get(i).getName());
						if(result.get(i).getOwner().equalsIgnoreCase("ME")){
							Log.e("Add snippet", "Location:"+result.get(i).getLocation()+"\nDescription: "+result.get(i).getDescription()+"\nDate: "+result.get(i).getDate1()+"\nTime: "+result.get(i).getTime());
							 m =  map.addMarker(new MarkerOptions()
					    	    .position(new LatLng(result.get(i).getLat(), result.get(i).getLng())) 
					    	    .title("Name:"+result.get(i).getName())
					    	    .snippet("Location:"+result.get(i).getLocation()+"\nDescription: "+result.get(i).getDescription()+"\nDate: "+result.get(i).getDate1()+"\nTime: "+result.get(i).getTime())
					           .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));   
					         m.setDraggable(true);
					         
					         markerIdHasMap.put(m, result.get(i).getId());
						}else{
							 m =  map.addMarker(new MarkerOptions()
					    	    .position(new LatLng(result.get(i).getLat(), result.get(i).getLng())) 
					    	    .title("Name:"+result.get(i).getName())
					    	    .snippet("Location:"+result.get(i).getLocation()+"\nDescription: "+result.get(i).getDescription()+"\nDate: "+result.get(i).getDate1()+"\nTime: "+result.get(i).getTime())
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
	public class SubmitMeetUplocation extends AsyncTask<String, String, String[]>{

		@Override
		protected String[] doInBackground(String... params) {
			boolean flag = false;
			try {
		  		
		  		JSONObject jsonObject = new JSONObject();
		  		jsonObject.put("fbid", db.getUserFbID());
		  		jsonObject.put("fname", db.getUserFirstName());
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
		  		if(json!=null){
		  			flag = json.getBoolean("status");
		  			if(flag){
		  				return params;
		  			}
		  		}
		  		
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			super.onPostExecute(result);
			dismissProgressDialog();
			if(result !=null){
				int pos = -1;
				long marker_id = Long.parseLong(result[0]);
				for(int i=0;i<meetupinfoarr.size();i++){
					if(marker_id == meetupinfoarr.get(i).getId()){
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
			showProgressDailog();
		}
		
		
		
	}
	public class FriendRequestNotificationCount extends AsyncTask<String, Void, Integer>{
	@Override
		protected Integer doInBackground(String... params) {
			int count = 0;			
		  	try {
				JSONObject req = new JSONObject();
				req.put("fbid",db.getUserFbID());
				JSONObject res = KlHttpClient.SendHttpPost(URL.COUNT_PENDING_FRIEND.getUrl(), req);
				if(res!=null){
					count = Integer.parseInt(res.getString("totalpendingfriend"));
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
				mPendingReqCounter.setVisibility(View.VISIBLE);
				mPendingReqCounter.setText(""+result);
			}else{
				mPendingReqCounter.setVisibility(View.GONE);
			}
		}
	}
	
	public class AppUsers extends AsyncTask<String, Void, ArrayList<String>>{		
		protected void onPreExecute() {
		showProgressDailog();
		}
		@Override
		protected ArrayList<String> doInBackground(String... params) {			
		  	try {
		  		JSONObject request = new JSONObject();
		  		request.put("fbid", db.getUserFbID());
		  		JSONObject response = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/app_user.php", request);
		        if(response.getBoolean("status")){
		        	JSONArray  jsonArray = response.getJSONArray("app_users");
		        	for(int i=0; i<jsonArray.length(); i++){
		        		JSONObject c = jsonArray.getJSONObject(i);
		        		String ids = c.getString("id");
		        		mAppUserFriend.add(ids);
		        	}
		        }
		        return mAppUserFriend;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(ArrayList<String> appusersArr) {
			dismissProgressDialog();
			boolean flag = false;
			if(appusersArr != null){
				facebookfriend = db.getFacebookFriends();
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
				
				mAddFriendAdapter = new AddFriendAdapter(db,HomeView.this, R.layout.add_friend_row, mAddFriendArr);
				mAddFriendList.setAdapter(mAddFriendAdapter);
				
				mInviteFriendAdapter = new InviteFriendAdapter(db,HomeView.this, R.layout.add_friend_row, mInviteFriendArr);
				mLvInviteFriendList.setAdapter(mInviteFriendAdapter);
			}
			
		}	

	}
	
	public class MessageWeb extends AsyncTask<String, String, ArrayList<NewMessage>>{

		@Override
		protected ArrayList<NewMessage> doInBackground(String... params) {
			
			try {
				JSONObject request  = new JSONObject();
				request.put("receiver_fb_id", db.getUserFbID());
				JSONObject response  = KlHttpClient.SendHttpPost("http://clickfordevelopers.com/demo/snowmada/new_chat_history.php", request);
		        if(response!=null){
		        	msg.clear();
		        	JSONArray  jsonArray = response.getJSONArray("newReply");
		        	for(int i=0; i<jsonArray.length(); i++){
		        		JSONObject c = jsonArray.getJSONObject(i);
		        		String id = c.getString("cr_id");
		        		String name = c.getString("sender");
		        		String message = c.getString("message");
		        		String date1 = c.getString("date");
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
			String pendingreqtextcolor, int tabselectImage) {
		Global.mSelectedTab = selectedTab;
		mAddFriendList.setVisibility(addfriendlistview);
		mLvInviteFriendList.setVisibility(invitefriendlistview);
		mRequestList.setVisibility(pendingreqview);

		mAddFriendSearchLayout.setVisibility(addsearcglayoutview);
		mInviteFriendSearchLayout.setVisibility(invitesearchlayoutview);

		mTabAddFriend.setBackgroundResource(drawableaddfriend);
		mTabInviteFriend.setBackgroundResource(drawableinvitefriend);
		mTabPendingRequest.setBackgroundResource(requestfriend);

		mTabAddFriend.setTextColor(Color.parseColor(addfriendtextcolor));
		mTabInviteFriend.setTextColor(Color.parseColor(invitefriendtextcolor));
		mTabPendingRequest.setTextColor(Color.parseColor(pendingreqtextcolor));

		mTabSElectImage.setBackgroundResource(tabselectImage);
	}
}

