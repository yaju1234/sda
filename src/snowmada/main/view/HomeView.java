package snowmada.main.view;

import static snowmada.main.view.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static snowmada.main.view.CommonUtilities.EXTRA_MESSAGE;
import static snowmada.main.view.CommonUtilities.SENDER_ID;

import java.util.ArrayList;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.strapin.Util.Utility;
import com.strapin.adapter.AddFriendAdapter;
import com.strapin.adapter.ChatAdapter;
import com.strapin.adapter.DealsAdapter;
import com.strapin.adapter.OfflineMessageAdapter;
import com.strapin.bean.AddFriendBean;
import com.strapin.bean.ChatBean;
import com.strapin.bean.DealsBean;
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
	
	private EditText mEtSearchFbFriend;
	private EditText mEtInputChatMsg;
	
	public ImageLoader imageLoader;
	
	private ListView mLvSnomadaFriendsList;
	private ListView mDealsList;
	private ListView mAddFriendList;
	private ListView mRequestList;
	private ListView mChatList;
	private ListView mLvMessageList;
	
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
	private RelativeLayout mSearchLayout;
	private RelativeLayout mRlProgressBarLayout;
	
	private ImageView mUserImage;
	private ImageView mProfileImage;
	private ImageView mIvSkyPatrol;
	private ImageView mTabSElectImage;	
	
	private  Dialog dialog,meetupDlg;	
	
	private ArrayList<DealsBean> mDealsArr = new ArrayList<DealsBean>();
	private ArrayList<AddFriendBean> mAddFriendArr = new ArrayList<AddFriendBean>();
	private ArrayList<AddFriendBean> mAddFriendSearchArr = new ArrayList<AddFriendBean>();
	
	private SnowmadaDbAdapter mDb;
	private DealsAdapter mAdapter;
	private AddFriendAdapter mAddFriendAdapter;
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
	    
	private static final int MEET_UP_LOCATION = 3;
	private static final int CHAT_LIVE = 4;
	private static final int GOOD_DEALS = 5;
	private static final int TRACK_FRIENDS = 6;
	private static final int ADD_FRIENDS = 7;
	private static final int VIEW_PROFILE = 8;
		

	  	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		
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
		
		
		mEtSearchFbFriend = (EditText)findViewById(R.id.et_input_key_for_search_fb_friends);
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
		mSearchLayout = (RelativeLayout)findViewById(R.id.friend_search_layout);
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
		mBtnPendingReq.setOnClickListener(this);
		mMassageLayout.setOnClickListener(this);
		mChatSendButton.setOnClickListener(this);
		map.setOnMapLongClickListener(this);		
		
		mEtSearchFbFriend.addTextChangedListener(this);		
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
		Global.mSelectedTab = 1;
		mRequestList.setVisibility(View.GONE);
		mAddFriendList.setVisibility(View.VISIBLE);
		mSearchLayout.setVisibility(View.VISIBLE);
		
		mBtnPendingReq.setBackgroundResource(R.drawable.tab_unselect);
		mBtnAddFriend.setBackgroundResource(R.drawable.tab_select);
		mBtnInviteFriend.setBackgroundResource(R.drawable.tab_unselect);
		mBtnPendingReq.setTextColor(Color.parseColor("#ffffff"));
		mBtnAddFriend.setTextColor(Color.parseColor("#00ccff"));
		mBtnInviteFriend.setTextColor(Color.parseColor("#ffffff"));
		mTabSElectImage.setBackgroundResource(R.drawable.add_friend_text);
	}
	
	if( v== mBtnPendingReq){

		Global.mSelectedTab = 3;
		
		mRequestList.setVisibility(View.VISIBLE);
		mAddFriendList.setVisibility(View.GONE);
		mSearchLayout.setVisibility(View.GONE);
		
		mReqTab.setBackgroundResource(R.drawable.tab_select);
		mBtnAddFriend.setBackgroundResource(R.drawable.tab_unselect);
		mBtnInviteFriend.setBackgroundResource(R.drawable.tab_unselect);
		mBtnPendingReq.setTextColor(Color.parseColor("#00ccff"));
		mBtnAddFriend.setTextColor(Color.parseColor("#ffffff"));
		mBtnInviteFriend.setTextColor(Color.parseColor("#ffffff"));
		mTabSElectImage.setBackgroundResource(R.drawable.requests_friend_text);
		mPresenter.getFriendRequest();
	
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
			Global.isTrackFriendLocation = false;
			Global.isMeetUploaction = true;
			mHighlightPos = 3;			
			Global.isChatActive = false;
			map.clear();
			
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
			Global.isTrackFriendLocation = false;
			Global.isMeetUploaction = false;
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
				mAddFriendArr = mDb.getFacebookFriends();
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
				
				mAddFriendAdapter = new AddFriendAdapter(mDb,HomeView.this, R.layout.add_friend_row, mAddFriendArr);
				mAddFriendList.setAdapter(mAddFriendAdapter);
				
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
	
	public void setVisibility(int i){
		switch (i) {
	
		case MEET_UP_LOCATION:
			mGreenBarMeetUplocation.setVisibility(View.VISIBLE);
			mGreenBarChatLive.setVisibility(View.INVISIBLE);
			mGreenBarGoodDeals.setVisibility(View.INVISIBLE);
			mGreenBarTrack.setVisibility(View.INVISIBLE);
			mGreenBarAdd.setVisibility(View.INVISIBLE);
			mGreenBarViewProfile.setVisibility(View.INVISIBLE);
			
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
	    	 Global.isTrackFriendLocation = false;
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
public void afterTextChanged(Editable s) {	
	
}

@Override
public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	
}

@Override
public void onTextChanged(CharSequence s, int start, int before, int count) {
	String searchString = mEtSearchFbFriend.getText().toString();							
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
	mAddFriendAdapter = new AddFriendAdapter(mDb,HomeView.this, R.layout.add_friend_row, mAddFriendSearchArr);
	mAddFriendList.setAdapter(mAddFriendAdapter);

}

@Override
public void init() {
	mLayoutMessageNotificationList.setVisibility(View.GONE);
	mMassageBox.setVisibility(View.VISIBLE);
	mMassageBoxWithIcon.setVisibility(View.GONE);		
	mTvPendingReqCounter.setVisibility(View.GONE);
	mRequestList.setVisibility(View.GONE);
	mAddFriendList.setVisibility(View.VISIBLE);
	mSearchLayout.setVisibility(View.VISIBLE);	
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
			
		String sender_name = getIntent().getExtras().getString("sender_name");
		String sender_fb_id = getIntent().getExtras().getString("sender_fb_id");
		String[] splitStr = sender_name.split("\\s+");
		Global.mChatUserName = splitStr[0];
		Global.mChatSenderID = getIntent().getExtras().getString("sender_fb_id");
		
		mPresenter.functionChat(sender_fb_id, sender_name);
		mTvActiveChatFriend.setText(sender_name);
	
	}

	
}

@Override
public boolean onMarkerClick(Marker marker) {
	if(Global.isMeetUploaction){
		meetupDlg = new Dialog(HomeView.this);				
		meetupDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		meetupDlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		meetupDlg.setContentView(R.layout.meetup_info_dialog);
		Button submit = (Button)meetupDlg.findViewById(R.id.btn_submit);
		setCustomizeColorText(submit, "SUB", "MIT");
		Button cancel = (Button)meetupDlg.findViewById(R.id.btn_cancel);
		setCustomizeColorText(cancel, "CAN", "CEL");
		ImageView clock =(ImageView)meetupDlg.findViewById(R.id.image_clock);
		clock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		meetupDlg.show();
		
	}else{
		Global.isInfoWindow = !Global.isInfoWindow;	
	}
	
	
	return false;
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
			    if(Utility.isNetworkConnected(getApplicationContext())){
			    	 new FriendRequestNotificationCount().execute(mDb.getUserFbID());
			    }
			   
			   if(Global.isTrackFriendLocation){
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
public void onMapLongClick(final LatLng point) {
	Log.e("Reach here", "Reach here");
	if(Global.isMeetUploaction){
		
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
                         
                         map.addMarker(new MarkerOptions()
                 	    .position(point)                 	    
                 	    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).setDraggable(true);   
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



@Override
public void onInfoWindowClick(Marker marker) {
	
	
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

}

