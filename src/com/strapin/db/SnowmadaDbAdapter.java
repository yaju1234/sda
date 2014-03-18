package com.strapin.db;

import java.util.ArrayList;

import com.strapin.bean.FacebookFriendBean;
import com.strapin.bean.MeetUpInfoBean;
import com.strapin.bean.MessageBean;
import com.strapin.bean.Patrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class SnowmadaDbAdapter {
	
	private static Context sContext;
	private static SQLiteDatabase sDb;
	private static SnowmadaDbHelper sDbHelper;
	private static SnowmadaDbAdapter sInstance;
	private ArrayList<MeetUpInfoBean> mCheckBeans = new  ArrayList<MeetUpInfoBean>();
	
	private SnowmadaDbAdapter(final Context context) {
		sContext = context;
	}
	
	public static SnowmadaDbAdapter databaseHelperInstance(final Context context) {
		if (sInstance == null) {
			sInstance = new SnowmadaDbAdapter(context);
			open();
		}
		return sInstance;
	}

	private static void open() {
		Log.e("Database open", "Database opened");
		sDbHelper = new SnowmadaDbHelper(sContext);
		sDb = sDbHelper.getWritableDatabase();		
	}
	public void close() {
		sDbHelper.close();		
	}
	
////////////////////////////////////////  For Snomada Table Strat  ///////////////////////
	
	/*public long insertUserInfo(final String facebookId, final String fname,final String lname) {
		final ContentValues values = new ContentValues();
		values.put(TableConstantName.FACEBOOK_ID, facebookId);
		values.put(TableConstantName.FB_USER_FIRSTNAME, fname);
		values.put(TableConstantName.FB_USER_LASTNAME, lname);
		
		try {
			sDb.beginTransaction();
			final long state = sDb.insert(TableConstantName.TABLE_NAME, null,	values);
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			throw e;
		} finally {
			sDb.endTransaction();
		}
	}
	
	public boolean updateUserInfo(String facebookId,  String fname, String lname){
		//Log.e("Status", ""+status);
		int i = 1;
		final ContentValues values = new ContentValues();
	
		values.put(TableConstantName.FACEBOOK_ID, facebookId);
		values.put(TableConstantName.FB_USER_FIRSTNAME, fname);
		values.put(TableConstantName.FB_USER_LASTNAME, lname);
		try {
			sDb.beginTransaction();
			final boolean state = sDb.update(TableConstantName.TABLE_NAME, values, TableConstantName.ID + "=" + "'"+i+"'", null)>0;
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			throw e;
		} finally {
			sDb.endTransaction();
		}
	}
	
	public String getUserFbID(){
		int i = 1;
		String fbid = "";
		Cursor cursor = sDb.rawQuery("select * from " +TableConstantName.TABLE_NAME+ " where " +TableConstantName.ID+ " = '"+i+"'", null);
		//Log.e("cursor.getCount()", ""+cursor.getCount());
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			fbid = cursor.getString(3);
			while(!cursor.isAfterLast()){
				 fbid = cursor.getString(1);
				 cursor.moveToNext();
				//int CouponStatus = cursor.getInt(3);			
				
			}	
		}
		cursor.close();
		return fbid;
	}
	
	public String getUserFirstName(){
		int i = 1;
		String fbid = "";
		Cursor cursor = sDb.rawQuery("select * from " +TableConstantName.TABLE_NAME+ " where " +TableConstantName.ID+ " = '"+i+"'", null);
		//Log.e("cursor.getCount()", ""+cursor.getCount());
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			fbid = cursor.getString(1);
			while(!cursor.isAfterLast()){
				 fbid = cursor.getString(1);
				 cursor.moveToNext();
				//int CouponStatus = cursor.getInt(3);			
				
			}	
		}
		cursor.close();
		return fbid;
	}
	
	public String getUserLastName(){
		int i = 1;
		String fbid = "";
		Cursor cursor = sDb.rawQuery("select * from " +TableConstantName.TABLE_NAME+ " where " +TableConstantName.ID+ " = '"+i+"'", null);
		//Log.e("cursor.getCount()", ""+cursor.getCount());
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			fbid = cursor.getString(2);
			while(!cursor.isAfterLast()){
				 fbid = cursor.getString(1);
				 cursor.moveToNext();
				//int CouponStatus = cursor.getInt(3);			
				
			}	
		}
		cursor.close();
		return fbid;
	}
	
	public int getRowCount() {
		int count = -1;

		final Cursor cursor = sDb.query(TableConstantName.TABLE_NAME,new String[] { "count(*) " + TableConstantName.FACEBOOK_ID },
				null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			count = cursor.getInt(cursor
					.getColumnIndex(TableConstantName.FACEBOOK_ID));
			cursor.close();
		}
		return count;
	}*/
	
////////////////////////////////////////For Snomada Table END  ///////////////////////
	
	////////////////////////////////////////////// For SKY table START////////////////////////////
	
/*	public int getSKIRowCount() {
		int count = -1;

		final Cursor cursor = sDb.query(TableConstantName.TABLE_SKI,new String[] { "count(*) " + TableConstantName.STATUS },
				null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			count = cursor.getInt(cursor
					.getColumnIndex(TableConstantName.STATUS));
			cursor.close();
		}
		return count;
	}*/
	
	

	/*public String getSKIStatus(){
		int i = 1;
		String status = "";
		Cursor cursor = sDb.rawQuery("select * from " +TableConstantName.TABLE_SKI+ " where " +TableConstantName.ID+ " = '"+i+"'", null);
		//Log.e("cursor.getCount()", ""+cursor.getCount());
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			status = cursor.getString(1);
			while(!cursor.isAfterLast()){
				 fbid = cursor.getString(1);
				 cursor.moveToNext();
				//int CouponStatus = cursor.getInt(3);			
				
			}	
		}
		cursor.close();
		return status;
	}*/
	
	/*public boolean updateSKI(String status){
		//Log.e("Status", ""+status);
		int i = 1;
		final ContentValues values = new ContentValues();
	
		values.put(TableConstantName.STATUS, status);
		try {
			sDb.beginTransaction();
			final boolean state = sDb.update(TableConstantName.TABLE_SKI, values, TableConstantName.ID + "=" + "'"+i+"'", null)>0;
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			throw e;
		} finally {
			sDb.endTransaction();
		}
	}*/
	
	/*public long insertSKI(final String status) {
		final ContentValues values = new ContentValues();
		values.put(TableConstantName.STATUS, status);
		
		try {
			sDb.beginTransaction();
			final long state = sDb.insert(TableConstantName.TABLE_SKI, null,	values);
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			throw e;
		} finally {
			sDb.endTransaction();
		}
	}*/
	
	
//////////////////////////////////////////////For SKY table END////////////////////////////
	
	
///////////////////////////////////////////// For MEETUP table Start ////////////////////////
	
public long insertMeetUpInfo(final String lat,final String lng,String status) {
	Log.e("lat1", lat);
	Log.e("lng1", lng);
	Log.e("status", status);
	final ContentValues values = new ContentValues();

	values.put(TableConstantName.LATITUDE, lat);
	values.put(TableConstantName.LONGITUDE, lng);		
	values.put(TableConstantName.STATUS, status);
	
	try {
		sDb.beginTransaction();
		final long state = sDb.insert(TableConstantName.TABLE_MEETUP, null,	values);
		sDb.setTransactionSuccessful();
		return state;
	} catch (SQLException e) {
		throw e;
	} finally {
		sDb.endTransaction();
	}
}
	
public void getMeetUpLocation1(){	
	
	//String args[] = new String[3];
	Cursor cursor = sDb.rawQuery("SELECT *   FROM    meetup",null);	
	Log.e("cursor.getCount()", ""+cursor.getCount());
	if(cursor.getCount()>0){
		mCheckBeans.clear();
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Log.e("ID", cursor.getString(0));
			Log.e("lat", cursor.getString(1));
			Log.e("lng", cursor.getString(2));
			cursor.moveToNext();
		}
	}
	cursor.close();
	//return args;
}


public String[] getMeetUpLocation(){	
	
	String args[] = new String[3];
	Cursor cursor = sDb.rawQuery("SELECT *   FROM    "+ TableConstantName.TABLE_MEETUP +"   WHERE    "+ TableConstantName.ID  +" = (SELECT MAX( "+ TableConstantName.ID +")  FROM "+ TableConstantName.TABLE_MEETUP +")",null);		
	if(cursor.getCount()>0){
		mCheckBeans.clear();
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			args[0] = cursor.getString(0);
			args[1] = cursor.getString(1);
			args[2] = cursor.getString(2);
			cursor.moveToNext();
		}
	}
	cursor.close();
	return args;
}


public boolean updateMeetUpStatus(final int Id){
int status = 0;
	final ContentValues values = new ContentValues();
	values.put(TableConstantName.STATUS, ""+status);
	try {
		sDb.beginTransaction();
		final boolean state = sDb.update(TableConstantName.TABLE_MEETUP, values, TableConstantName.ID + "=" + "'"+Id+"'", null)>0;
		sDb.setTransactionSuccessful();
		return state;
	} catch (SQLException e) {
		throw e;
	} finally {
		sDb.endTransaction();
	}
}



///////////////////////////////////////  For MEETUP table END ////////////////////////////////////////////////

///////////////////////////////////////  For SKIPETROL table start ////////////////////////////////////////////////

public long insertSkiPetrolInfo(final String patrolerid,final String FirstName,final String LastName,final String Latitude,final String Longitude) {
	final ContentValues values = new ContentValues();
	values.put(TableConstantName.PATROLER_ID, patrolerid);
	values.put(TableConstantName.FIRSTNAME, FirstName);
	values.put(TableConstantName.LASTNAME, LastName);
	values.put(TableConstantName.LATITUDE, Latitude);
	values.put(TableConstantName.LONGITUDE, Longitude);		

	try {
		sDb.beginTransaction();
		final long state = sDb.insert(TableConstantName.TABLE_SKIPETROL, null,	values);
		sDb.setTransactionSuccessful();
		return state;
	} catch (SQLException e) {
		throw e;
	} finally {
		sDb.endTransaction();
	}
}

public Patrol getSkiPetrolInfo(){
	Log.e("Reach", "Reach");
	Toast.makeText(sContext, "Reach", 1000).show();
	Patrol p = null;
	Cursor cursor = sDb.rawQuery("select * from " +TableConstantName.TABLE_SKIPETROL, null);		
	if(cursor.getCount()>0){
		Log.e("Cursor greater than 0", "Cursor greater than 0");
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			p = new Patrol(cursor.getString(1), cursor.getString(2), cursor.getString(3));
			cursor.moveToNext();
		}
	}
	Log.e("Cursor greater than 0", "!!!!!Cursor greater than 0");
	cursor.close();
	return p;
}

public int getSkiPetrolRowCount() {
	int count = -1;

	final Cursor cursor = sDb.query(TableConstantName.TABLE_SKIPETROL,new String[] { "count(*) " + TableConstantName.ID },
			null, null, null, null, null);
	if (cursor != null) {
		cursor.moveToFirst();
		count = cursor.getInt(cursor
				.getColumnIndex(TableConstantName.ID));
		cursor.close();
	}
	return count;
}

public boolean updateSkiPetrolInfo(final String patrolerid,final String FirstName,final String LastName, final String Latitude,final String Longitude){
	int id = 1;
	final ContentValues values = new ContentValues();
	values.put(TableConstantName.PATROLER_ID, patrolerid);
	values.put(TableConstantName.FIRSTNAME, FirstName);
	values.put(TableConstantName.LASTNAME, LastName);
	values.put(TableConstantName.LATITUDE, Latitude);
	values.put(TableConstantName.LONGITUDE, Longitude);		
	
	try {
		sDb.beginTransaction();
		final boolean state = sDb.update(TableConstantName.TABLE_SKIPETROL, values, TableConstantName.ID + "=" + "'"+id+"'", null)>0;
		sDb.setTransactionSuccessful();
		return state;
	} catch (SQLException e) {
		throw e;
	} finally {
		sDb.endTransaction();
	}
}

///////////////////////////////////////  For SKIPETROL table END ////////////////////////////////////////////////

//////////////////////////////////////   For CHAT_MESSAGE table start  /////////////////////////////////////////

public long insertChatMessage(final String SenderFbId, final String SenderName,final String read_staus,final String TextMessage) {
	final ContentValues values = new ContentValues();

	values.put(TableConstantName.SENDER_FB_ID, SenderFbId);
	values.put(TableConstantName.SENDER_NAME, SenderName);
	values.put(TableConstantName.READ_STATUS, read_staus);
	values.put(TableConstantName.TEXT_MESSAGE, TextMessage);
	
	try {
		sDb.beginTransaction();
		final long state = sDb.insert(TableConstantName.TABLE_MESSAGE, null,values);
		sDb.setTransactionSuccessful();
		return state;
	} catch (SQLException e) {
		throw e;
	} finally {
		sDb.endTransaction();
	}
}

public ArrayList<MessageBean> getAllChatMessageInfo(){
	ArrayList<MessageBean> mChatMessage = new  ArrayList<MessageBean>();
	
	Cursor cursor = sDb.rawQuery("select * from " +TableConstantName.TABLE_MESSAGE +" order by "+ TableConstantName.ID  +" desc", null);		
	if(cursor.getCount()>0){
		Log.e("reach here", "reach here");
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			int id = cursor.getInt(0);
			
			String mFacebookId = cursor.getString(1);
			
			Log.e("id", mFacebookId);
			String mName = cursor.getString(2);
			Log.e("name", mName);
			String staus = cursor.getString(3);
			String mMessage = cursor.getString(4);
			Log.e("mMessage", mMessage);
			mChatMessage.add(new MessageBean(id,mFacebookId,mName,staus,mMessage));
			cursor.moveToNext();
		}
	}
	cursor.close();
	return mChatMessage;
}

/*public boolean updateChatMessageInfo(final String fbId,final String message){

	final ContentValues values = new ContentValues();	
	values.put(TableConstantName.TEXT_MESSAGE, message);	
	
	try {
		sDb.beginTransaction();
		final boolean state = sDb.update(TableConstantName.TABLE_MESSAGE, values, TableConstantName.SENDER_FB_ID + "=" + "'"+fbId+"'", null)>0;
		sDb.setTransactionSuccessful();
		return state;
	} catch (SQLException e) {
		throw e;
	} finally {
		sDb.endTransaction();
	}
}*/

public boolean UpdateMessageStatus(final String Id) {

int value = 0;
final ContentValues values = new ContentValues();
values.put(TableConstantName.READ_STATUS, value);

try {
	sDb.beginTransaction();
	final boolean state = sDb.update(TableConstantName.TABLE_MESSAGE, values, TableConstantName.ID + "=" + "'"+Id+"'", null)>0;
	sDb.setTransactionSuccessful();
	return state;
} catch (SQLException e) {
	throw e;
} finally {
	sDb.endTransaction();
}
}

public boolean isUserMessageExist(final String fbId) {
	boolean flag = false;

	Cursor cursor = sDb.rawQuery("select * from " +TableConstantName.TABLE_MESSAGE+ " where " + TableConstantName.SENDER_FB_ID + "='"+fbId+"'", null);
	if (cursor.getCount()>0) {
		flag = true;
	}
	return flag;
}

/*public void deleteChatMessage(String fbid){	

	sDb.delete(TableConstantName.TABLE_MESSAGE, TableConstantName.SENDER_FB_ID + "=" + "'"+fbid+"'" , null);
}*/

public int getMassageNotificationCount() {
	int count = -1;

	final Cursor cursor = sDb.rawQuery("select * from message where isread = '1'",null);
	if (cursor != null) {
		/*cursor.moveToFirst();
		count = cursor.getInt(cursor
				.getColumnIndex(TableConstantName.ID));*/
		count=cursor.getCount();
		Log.e("count", ""+count);
		cursor.close();
	}
	return count;
}

public int getTotalMessageCount() {
	int count = -1;

	final Cursor cursor = sDb.rawQuery("select * from message",null);
	if (cursor != null) {
		/*cursor.moveToFirst();
		count = cursor.getInt(cursor
				.getColumnIndex(TableConstantName.ID));*/
		count=cursor.getCount();
		Log.e("count", ""+count);
		cursor.close();
	}
	return count;
}
//////////////////////////////////////For CHAT_MESSAGE table END  /////////////////////////////////////////

////////////////////////////////////  For Facebook Friend List Start//////////////////////////////////
public long insertfacebookFriends(final String fbid, final String name) {
	final ContentValues values = new ContentValues();

	values.put(TableConstantName.FB_ID, fbid);
	values.put(TableConstantName.FB_NAME, name);
	
	try {
		sDb.beginTransaction();
		final long state = sDb.insert(TableConstantName.TABLE_FB_FRIENDS, null,values);
		sDb.setTransactionSuccessful();
		return state;
	} catch (SQLException e) {
		throw e;
	} finally {
		sDb.endTransaction();
	}
}

public ArrayList<FacebookFriendBean> getFacebookFriends() {
	
	ArrayList<FacebookFriendBean> arr = new ArrayList<FacebookFriendBean>();
	Cursor cursor = sDb.rawQuery("select * from " +TableConstantName.TABLE_FB_FRIENDS, null);		
	if(cursor.getCount()>0){
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){			
			String facebookid = cursor.getString(1);
			String name = cursor.getString(2);
			arr.add(new FacebookFriendBean(facebookid,name));
			cursor.moveToNext();
		}
	}
	cursor.close();
	return arr;
}

public int getFbFriendCount() {
	int count = -1;

	final Cursor cursor = sDb.query(TableConstantName.TABLE_FB_FRIENDS,new String[] { "count(*) " + TableConstantName.FB_ID },
			null, null, null, null, null);
	if (cursor != null) {
		cursor.moveToFirst();
		count = cursor.getInt(cursor
				.getColumnIndex(TableConstantName.FB_ID));
		cursor.close();
	}
	return count;
}

public void emptyFriendTable(){
	sDb.delete(TableConstantName.TABLE_FB_FRIENDS, null, null);
}
////////////////////////////////////For Facebook Friend List END//////////////////////////////////
////////////////////////////////////SESSION START//////////////////////////////////
/*public long insertSessionvalue(int n) {
	final ContentValues values = new ContentValues();

	values.put(TableConstantName.IS_SESSION_VALID, ""+n);
	
	try {
		sDb.beginTransaction();
		final long state = sDb.insert(TableConstantName.TABLE_SESSION, null,values);
		sDb.setTransactionSuccessful();
		return state;
	} catch (SQLException e) {
		throw e;
	} finally {
		sDb.endTransaction();
	}
}*/

/*public int getSessionValueRowCount() {
	int count = -1;

	final Cursor cursor = sDb.query(TableConstantName.TABLE_SESSION,new String[] { "count(*) " + TableConstantName.IS_SESSION_VALID },
			null, null, null, null, null);
	if (cursor != null) {
		cursor.moveToFirst();
		count = cursor.getInt(cursor
				.getColumnIndex(TableConstantName.IS_SESSION_VALID));
		cursor.close();
	}
	return count;
}*/

/*public boolean isSessionvalid() {
	boolean flag = false;
	String value = "0";
	Cursor cursor = sDb.rawQuery("select * from " +TableConstantName.TABLE_SESSION, null);		
	if(cursor.getCount()>0){
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){			
			value = cursor.getString(1);
			cursor.moveToNext();
		}
	}
	cursor.close();
	if(value.equalsIgnoreCase("1")){
		flag = true;
	}
	return flag;
}*/

/*public boolean updateSession(int n){
	int id = 1;
	final ContentValues values = new ContentValues();

	values.put(TableConstantName.IS_SESSION_VALID, ""+n);
	try {
		sDb.beginTransaction();
		final boolean state = sDb.update(TableConstantName.TABLE_SESSION, values, TableConstantName.ID + "=" + "'"+id+"'", null)>0;
		sDb.setTransactionSuccessful();
		return state;
	} catch (SQLException e) {
		throw e;
	} finally {
		sDb.endTransaction();
	}
}*/
////////////////////////////////////SESSION  END//////////////////////////////////

/* -------------------------------------------Coupon table start------------------------------------------------------
	
	*//**
	 * 
	 * @param CouponDetails
	 * @param RetailerId
	 * @param RetailerName
	 * @param RetailerType
	 * @param CouponProductUpcCode
	 * @param PriceOff
	 * @param ProductSKuId
	 * @param CouponType
	 * @param CouponProductname
	 * @param CouponImage
	 * @param CouponId
	 * @param FreeProductId
	 * @param CouponProductId
	 * @param CouponCode
	 * @param FreeProductQTY
	 * @param CouponStatus
	 * @param CouponOnlyStatus
	 * @param Catagory
	 * @return
	 *  INSERT value into COUPON table
	 *  Catagory: 
	 *   "C"--> Common </br>
	 *   "Y" -> Deals for you</br>
	 *   "S" -> Treading Now
	 *//*
	public long insertCouponInfo(final String CouponDetails, final String RetailerId, final String RetailerName,final String RetailerType,final String  CouponProductUpcCode,final String PriceOff,final String ProductSKuId,final String CouponType,final String CouponProductname,final String CouponImage,final String CouponId,final String FreeProductId,final String CouponProductId,final String CouponCode, final String FreeProductQTY,  final int CouponStatus,  final int CouponOnlyStatus, final String Catagory) {
		final ContentValues values = new ContentValues();
		values.put(TableConstantName.COUPON_DESCRIPTION, CouponDetails);
		values.put(TableConstantName.COUPON_RETAILER_ID, RetailerId);
		values.put(TableConstantName.COUPON_RETAILER_NAME, RetailerName);
		values.put(TableConstantName.COUPON_RETAILER_TYPE, RetailerType);
		values.put(TableConstantName.COUPON_PRODUCT_UPC_CODE, CouponProductUpcCode);
		values.put(TableConstantName.COUPON_PRICE_OFF, PriceOff);		
		values.put(TableConstantName.COUPON_PRODUCT_SKU_ID, ProductSKuId);
		values.put(TableConstantName.COUPON_TYPE, CouponType);
		values.put(TableConstantName.COUPON_PRODUCT_NAME, CouponProductname);
		values.put(TableConstantName.COUPON_IMAGE, CouponImage);
		values.put(TableConstantName.COUPON_ID, CouponId);
		values.put(TableConstantName.FREE_PRODUCT_ID, FreeProductId);
		values.put(TableConstantName.COUPON_PRODUCT_ID, CouponProductId);
		values.put(TableConstantName.COUPON_CODE, CouponCode);
		values.put(TableConstantName.FREE_PRODUCT_QTY, FreeProductQTY);		
		values.put(TableConstantName.COUPON_SRORE_DEALS_STATUS, CouponStatus);
		values.put(TableConstantName.COUPON_ONLY_STATUS, CouponOnlyStatus);
		values.put(TableConstantName.COUPON_CATAGORY, Catagory);
		try {
			sDb.beginTransaction();
			final long state = sDb.insert(TableConstantName.COUPON_TABLE, null,	values);
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			throw e;
		} finally {
			sDb.endTransaction();
		}
	}
	
	*//**
	 * 
	 * @param RetailerName
	 * @param CouponId
	 * @param UpdateStatus
	 * @return
	 *  Update Coupon STATUS into COUPON Table .</br>
	 *  If coupon are saved into CART Table then those coupons will show default check in CouponListView. 
	 *//*
	public boolean updateCouponStatusfromCart(String RetailerName, String CouponId, int UpdateStatus){
		//Log.e("Status", ""+status);
		final ContentValues values = new ContentValues();
	
		values.put(TableConstantName.COUPON_SRORE_DEALS_STATUS, UpdateStatus);
		try {
			sDb.beginTransaction();
			final boolean state = sDb.update(TableConstantName.COUPON_TABLE, values, TableConstantName.CART_COUPON_RETAILER_NAME + "=" + "'"+RetailerName+"'"+ " AND "+TableConstantName.CART_COUPON_ID + "=" + "'"+CouponId+"'", null)>0;
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			throw e;
		} finally {
			sDb.endTransaction();
		}
	}
	
	public boolean updateCouponTablefromCartOnly(String retailerName, String mCouponId, int status){
		final ContentValues values = new ContentValues();
	
		values.put(TableConstantName.COUPON_ONLY_STATUS, status);
		try {
			sDb.beginTransaction();
			final boolean state = sDb.update(TableConstantName.COUPON_TABLE, values, TableConstantName.CART_COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'"+ " AND "+TableConstantName.CART_COUPON_ID + "=" + "'"+mCouponId+"'", null)>0;
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			throw e;
		} finally {
			sDb.endTransaction();
		}
	}
	
	public long insertCartInfo(final String couponDetails, final String retailerId, final String retailerName,final String retailerType,final String  couponProductUpcCode,final String priceOff,final String productSKuId,final String couponType,final String couponProductname,final String couponImage,final String couponId,final String freeProductId,final String couponProductId,final String couponCode, final String freeProductQTY,  final int couponStatus,final int couponOnlyStatus, final String catagory) {
		final ContentValues values = new ContentValues();
		values.put(TableConstantName.CART_COUPON_DESCRIPTION, couponDetails);
		values.put(TableConstantName.CART_COUPON_RETAILER_ID, retailerId);
		values.put(TableConstantName.CART_COUPON_RETAILER_NAME, retailerName);
		values.put(TableConstantName.CART_COUPON_RETAILER_TYPE, retailerType);
		values.put(TableConstantName.CART_COUPON_PRODUCT_UPC_CODE, couponProductUpcCode);
		values.put(TableConstantName.CART_COUPON_PRICE_OFF, priceOff);
		
		values.put(TableConstantName.CART_COUPON_PRODUCT_SKU_ID, productSKuId);
		values.put(TableConstantName.CART_COUPON_TYPE, couponType);
		values.put(TableConstantName.CART_COUPON_PRODUCT_NAME, couponProductname);
		values.put(TableConstantName.CART_COUPON_IMAGE, couponImage);
		values.put(TableConstantName.CART_COUPON_ID, couponId);
		values.put(TableConstantName.CART_FREE_PRODUCT_ID, freeProductId);
		values.put(TableConstantName.CART_COUPON_PRODUCT_ID, couponProductId);
		values.put(TableConstantName.CART_COUPON_CODE, couponCode);
		values.put(TableConstantName.CART_FREE_PRODUCT_QTY, freeProductQTY);		
		values.put(TableConstantName.CART_COUPON_STORE_DEALS_STATUS, couponStatus);
		values.put(TableConstantName.CART_COUPON_ONLY_STATUS, couponOnlyStatus);
		values.put(TableConstantName.CART_COUPON_CATAGORY, catagory);
		try {
			sDb.beginTransaction();
			final long state = sDb.insert(TableConstantName.CART_TABLE_NAME, null,
					values);
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			//Log.d(TAG, e.getMessage());
			throw e;
		} finally {
			sDb.endTransaction();
		}
	}
	
	
	public boolean updateCartTable(String retailerName, String mCouponId){
		final ContentValues values = new ContentValues();
	
		values.put(TableConstantName.COUPON_ONLY_STATUS, 1);
		values.put(TableConstantName.COUPON_SRORE_DEALS_STATUS, 1);
		values.put(TableConstantName.COUPON_CATAGORY, "C");
		try {
			sDb.beginTransaction();
			final boolean state = sDb.update(TableConstantName.COUPON_TABLE, values, TableConstantName.CART_COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'"+ " AND "+TableConstantName.CART_COUPON_ID + "=" + "'"+mCouponId+"'", null)>0;
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			throw e;
		} finally {
			sDb.endTransaction();
		}
	}
	
	public boolean IsWebServiceCallForTrandingNowCoupons(String retailerName){
		boolean IsExists = true;
		String catagory = "S";
		Cursor cursor =  sDb.query(TableConstantName.COUPON_TABLE,new String[] {TableConstantName.TABLE_ID}, TableConstantName.COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'"+ " AND "+ TableConstantName.COUPON_CATAGORY + "=" + "'"+catagory+"'", null, null,
				null, null);
		if(cursor.getCount()>0){
			IsExists = false;
		}
		cursor.close();
		return IsExists;
	}
	public boolean IsWebServiceCallForDealsForYouCoupons(String retailerName){
		boolean IsExists = true;
		String catagory = "Y";
		String category_Common ="C";
		Cursor cursor =  sDb.query(TableConstantName.COUPON_TABLE,new String[] {TableConstantName.TABLE_ID}, TableConstantName.COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'"+ " AND ("+TableConstantName.COUPON_CATAGORY + "=" + "'"+catagory+"'"+" OR "+TableConstantName.COUPON_CATAGORY + "=" + "'"+category_Common+"'"+")", null, null,
				null, null);
		if(cursor.getCount()>0){
			IsExists = false;
		}
		cursor.close();
		return IsExists;
	}
	
	public boolean isRetailerExistInCouponTable(String retailerName){
		boolean IsExists = false;
		Cursor cursor =  sDb.query(TableConstantName.COUPON_TABLE,new String[] {TableConstantName.TABLE_ID}, TableConstantName.COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'", null, null,
				null, null);
		if(cursor.getCount()>0){
			IsExists = true;
		}
		cursor.close();
		return IsExists;
	}
	
	public boolean isTreadingNowRetailerExist(String retailerName){
		String catagory = "S";
		boolean IsExists = false;
		Cursor cursor =  sDb.query(TableConstantName.COUPON_TABLE,new String[] {TableConstantName.TABLE_ID}, TableConstantName.COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'"+ " AND "+ TableConstantName.COUPON_CATAGORY + "=" + "'"+catagory+"'", null, null,
				null, null);
		if(cursor.getCount()>0){
			IsExists = true;
		}
		cursor.close();
		return IsExists;
	}
	
	
	public int couponCountForStoreDeals(String retailerName){
		String category_treadingNow = "S";
		String category_Common = "C";
		int val = 0;
		Cursor cursor =  sDb.query(TableConstantName.COUPON_TABLE,new String[] {TableConstantName.TABLE_ID}, TableConstantName.COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'"+ " AND ("+TableConstantName.COUPON_CATAGORY + "=" + "'"+category_treadingNow+"'"+" OR "+TableConstantName.COUPON_CATAGORY + "=" + "'"+category_Common+"'"+")", null, null,
				null, null);
		if(cursor.getCount()>0){
			val = cursor.getCount();
		}
		cursor.close();
		return val;
	}
	
	public boolean isDealsForYouRetailerExist(String retailerName){
		String catagory = "Y";
		boolean IsExists = false;
		Cursor cursor =  sDb.query(TableConstantName.COUPON_TABLE,new String[] {TableConstantName.TABLE_ID}, TableConstantName.COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'"+ " AND "+ TableConstantName.COUPON_CATAGORY + "=" + "'"+catagory+"'", null, null,
				null, null);
		if(cursor.getCount()>0){
			IsExists = true;
		}
		cursor.close();
		return IsExists;
	}
	public boolean IsRetailerExistsfromDealsForYou(String retailerName){
		boolean IsExists = false;
		String catagory = "Y";
		Cursor cursor =  sDb.query(TableConstantName.COUPON_TABLE,new String[] {TableConstantName.TABLE_ID}, TableConstantName.COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'"+ " AND "+ TableConstantName.COUPON_CATAGORY + "=" + "'"+catagory+"'", null, null,
				null, null);
		if(cursor.getCount()>0){
			IsExists = true;
		}
		cursor.close();
		return IsExists;
	}
	
	public boolean IsRetailerExistsfromTreadingNow(String retailerName){
		boolean IsExists = false;
		String catagory = "S";
		Cursor cursor =  sDb.query(TableConstantName.COUPON_TABLE,new String[] {TableConstantName.TABLE_ID}, TableConstantName.COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'"+ " AND "+ TableConstantName.COUPON_CATAGORY + "=" + "'"+catagory+"'", null, null,
				null, null);
		if(cursor.getCount()>0){
			IsExists = true;
		}
		cursor.close();
		return IsExists;
	}
	
	public boolean updateCartStatusforOnlyForyou(String retailerName, String mCouponId, String catagory){
		final ContentValues values = new ContentValues();
	
		values.put(TableConstantName.COUPON_CATAGORY, catagory);
		try {
			sDb.beginTransaction();
			final boolean state = sDb.update(TableConstantName.COUPON_TABLE, values, TableConstantName.CART_COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'"+ " AND "+TableConstantName.CART_COUPON_ID + "=" + "'"+mCouponId+"'", null)>0;
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			throw e;
		} finally {
			sDb.endTransaction();
		}
	}
	
	public boolean updateCartStatusforStoreDeals(String retailerName, String mCouponId, String catagory){
		final ContentValues values = new ContentValues();
	
		values.put(TableConstantName.COUPON_CATAGORY, catagory);
		try {
			sDb.beginTransaction();
			final boolean state = sDb.update(TableConstantName.COUPON_TABLE, values, TableConstantName.CART_COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'"+ " AND "+TableConstantName.CART_COUPON_ID + "=" + "'"+mCouponId+"'", null)>0;
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			throw e;
		} finally {
			sDb.endTransaction();
		}
	}
	
	
	
	public boolean IsCouponIdAlreadyExistIntoCouponTable(String retailerName, String couponId){
		boolean IsExists = false;
		Cursor cursor =  sDb.query(TableConstantName.COUPON_TABLE,new String[] {TableConstantName.TABLE_ID}, TableConstantName.COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'"+ " AND "+ TableConstantName.COUPON_ID + "=" + "'"+couponId+"'", null, null,
				null, null);
		if(cursor.getCount()>0){
			IsExists = true;
		}
		cursor.close();
		return IsExists;
	}
	
	
	
	public void getRetailerValue(String retailerName){
		Cursor cursor = sDb.rawQuery("select * from " +TableConstantName.COUPON_TABLE+ " where " +TableConstantName.COUPON_RETAILER_NAME+ " = '"+retailerName+"'", null);
		//Log.e("cursor.getCount()", ""+cursor.getCount());
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			while(cursor.moveToNext()){
				String coupon = cursor.getString(2);				
				int CouponStatus = cursor.getInt(3);			
				
			}
				
		}
		cursor.close();
	}
	
	public void deleteRetalerName(String retailerName){		
		
		sDb.delete(TableConstantName.COUPON_TABLE, TableConstantName.COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'", null);
		
	}
	
	
	public String getRetailerImage(String retailerName){
		String image = null;
		Cursor cursor = sDb.rawQuery("select image from " +TableConstantName.COUPON_TABLE+ " where " +TableConstantName.COUPON_RETAILER_NAME+ " = '"+retailerName+"'"+" limit 0,1", null);
		//Log.e("cursor.getCount()", ""+cursor.getCount());
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			while(cursor.moveToNext()){
				image = cursor.getString(0);
				Log.e("Image", ""+image);
				Log.e("Image1", ""+cursor.getString(1));
			}
				
		}
		cursor.close();
		return image;
	}
       *//**
        * Empty Coupon table.
       *//*
	public void emptyCouponTable(){
		sDb.delete(TableConstantName.COUPON_TABLE, null, null);
	}

	public ArrayList<StoreDealsBean> getTrandingNowCouponsFromDb(String retailerName){
		ArrayList<StoreDealsBean> mCheckBeans = new ArrayList<StoreDealsBean>();
		String category_treadingNow = "S";
		String category_Common = "C";
		Cursor cursor = sDb.rawQuery("select * from " +TableConstantName.COUPON_TABLE+ " where " +TableConstantName.COUPON_RETAILER_NAME+ " = '"+retailerName+"'"+ " AND ("+TableConstantName.COUPON_CATAGORY + "=" + "'"+category_treadingNow+"'"+" OR "+TableConstantName.COUPON_CATAGORY + "=" + "'"+category_Common+"'"+")", null);		
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				String mCouponDescription = cursor.getString(1);
				String mRetailerId = cursor.getString(2);
				String mRetailerName = cursor.getString(3);
				String mRetailerType = cursor.getString(4);
				String mCouponProductUpcCode = cursor.getString(5);
				String mCouponPriceOff = cursor.getString(6);
				String mCouponProductSkuId = cursor.getString(7);
				String mCouponType = cursor.getString(8);
				String mCouponProductname = cursor.getString(9);
				String image = cursor.getString(10);
				String mCouponId = cursor.getString(11);
				String mCouponfreeProductId = cursor.getString(12);
				String mCouponProductId = cursor.getString(13);
				String mCouponCode = cursor.getString(14);
				String mCouponFreeProductQty = cursor.getString(15);
				int CouponStatus = cursor.getInt(16);	
				int CouponOnlyStatus = cursor.getInt(17);
				String Couponcatagory = cursor.getString(18);
				//mCheckBeans.add(new CouponCheckBean(coupon, CouponStatus== 1?true:false));
				mCheckBeans.add(new StoreDealsBean(mCouponDescription, mRetailerId, mRetailerName, mRetailerType, mCouponProductUpcCode, mCouponPriceOff, mCouponProductSkuId, mCouponType, mCouponProductname, image, mCouponId, mCouponfreeProductId, mCouponProductId, mCouponCode, mCouponFreeProductQty, CouponStatus== 1?true:false, CouponOnlyStatus ==1?true:false,Couponcatagory ));
				cursor.moveToNext();
				
			}
				
		}
		cursor.close();
		return mCheckBeans;
	}
	
	public ArrayList<CustomerCouponBean> getDealsForYouCouponsFromServer(String retailerName){
		ArrayList<CustomerCouponBean> mCheckBeans = new  ArrayList<CustomerCouponBean>();
		String catagory = "Y";
		String category_Common = "C";
		
		Cursor cursor = sDb.rawQuery("select * from " +TableConstantName.COUPON_TABLE+ " where " +TableConstantName.COUPON_RETAILER_NAME+ " = '"+retailerName+"'"+ " AND ("+TableConstantName.COUPON_CATAGORY + "=" + "'"+catagory+"'"+" OR "+TableConstantName.COUPON_CATAGORY + "=" + "'"+category_Common+"'"+")", null);		
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				String mCouponDescription = cursor.getString(1);
				String mRetailerId = cursor.getString(2);
				String mRetailerName = cursor.getString(3);
				String mRetailerType = cursor.getString(4);
				String mCouponProductUpcCode = cursor.getString(5);
				String mCouponPriceOff = cursor.getString(6);
				String mCouponProductSkuId = cursor.getString(7);
				String mCouponType = cursor.getString(8);
				String mCouponProductname = cursor.getString(9);
				String image = cursor.getString(10);
				String mCouponId = cursor.getString(11);
				String mCouponfreeProductId = cursor.getString(12);
				String mCouponProductId = cursor.getString(13);
				String mCouponCode = cursor.getString(14);
				String mCouponFreeProductQty = cursor.getString(15);
				int CouponStatus = cursor.getInt(16);	
				int CouponOnlyStatus = cursor.getInt(17);
				String Couponcatagory = cursor.getString(18);
				//mCheckBeans.add(new CouponCheckBean(coupon, CouponStatus== 1?true:false));
				mCheckBeans.add(new CustomerCouponBean(mCouponDescription, mRetailerId, mRetailerName, mRetailerType, mCouponProductUpcCode, mCouponPriceOff, mCouponProductSkuId, mCouponType, mCouponProductname, image, mCouponId, mCouponfreeProductId, mCouponProductId, mCouponCode, mCouponFreeProductQty, CouponStatus== 1?true:false, CouponOnlyStatus ==1?true:false,Couponcatagory ));
				cursor.moveToNext();
				
			}
				
		}
		cursor.close();
		return mCheckBeans;
	}
	
	
	public boolean updateCartStatusfromCouponTable(String retailerName, String mCouponId, int status){
		final ContentValues values = new ContentValues();
	
		values.put(TableConstantName.COUPON_SRORE_DEALS_STATUS, status);
		values.put(TableConstantName.COUPON_ONLY_STATUS, status);
		try {
			sDb.beginTransaction();
			final boolean state = sDb.update(TableConstantName.COUPON_TABLE, values, TableConstantName.CART_COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'"+ " AND "+TableConstantName.CART_COUPON_ID + "=" + "'"+mCouponId+"'", null)>0;
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			throw e;
		} finally {
			sDb.endTransaction();
		}
	}
	
	
	
	public boolean updateCommonCouponStatus(String retailerName, String mCouponId){
		int status = -1;
		final ContentValues values = new ContentValues();
		Cursor cursor = sDb.rawQuery("select "+TableConstantName.COUPON_ONLY_STATUS+" from " +TableConstantName.COUPON_TABLE+ " where " +TableConstantName.COUPON_ID+ " = '"+mCouponId+"'", null);
		Log.e("Coupon Status", ""+cursor.getCount());
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				status = cursor.getInt(0);
				cursor.moveToNext();
			}
		}
		cursor.close();
		values.put(TableConstantName.COUPON_SRORE_DEALS_STATUS, status);
		try {
			sDb.beginTransaction();
			final boolean state = sDb.update(TableConstantName.COUPON_TABLE, values, TableConstantName.COUPON_ID + "=" + "'"+mCouponId+"'", null)>0;
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			throw e;
		} finally {
			sDb.endTransaction();
			cursor.close();
		}
	}
	
	
	
	public boolean updateCommonCouponOnlyStatus(String retailerName, String mCouponId){
		int status = -1;
		final ContentValues values = new ContentValues();
		Cursor cursor = sDb.rawQuery("select "+TableConstantName.COUPON_SRORE_DEALS_STATUS+" from " +TableConstantName.COUPON_TABLE+ " where " +TableConstantName.COUPON_ID+ " = '"+mCouponId+"'", null);
		Log.e("Coupon Status", ""+cursor.getCount());
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				status = cursor.getInt(0);
				cursor.moveToNext();
			}
		}
		cursor.close();
		values.put(TableConstantName.COUPON_ONLY_STATUS, status);
		
		try {
			sDb.beginTransaction();
			final boolean state = sDb.update(TableConstantName.COUPON_TABLE, values, TableConstantName.COUPON_ID + "=" + "'"+mCouponId+"'", null)>0;
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			throw e;
		} finally {
			sDb.endTransaction();
			cursor.close();
		}
	}
	
	
	//update coupon set `Coupon_Status` = `Coupon_Only` where COUPON_ID = '12'
	
------------------------------------------------- Coupon table end---------------------------------------------------
	
-------------------------------------------------- Cart table start--------------------------------------------------
	
	public boolean IsCartValueExists(String retailerName){
		boolean IsExists = true;
		//String catagory = "S";
		Cursor cursor =  sDb.query(TableConstantName.CART_TABLE_NAME,new String[] {TableConstantName.TABLE_ID}, TableConstantName.COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'", null, null,
				null, null);
		Cursor cursor = sDb.rawQuery("select Id from cart where Retailer_Name = '"+retailerName+"' AND ("+TableConstantName.CART_COUPON_CATAGORY+" = 'S' OR " +TableConstantName.CART_COUPON_CATAGORY+ " = 'C')", null);
		if(cursor.getCount()>0){
			IsExists = false;
		}
		cursor.close();
		return IsExists;
	}
	
	public boolean IsCartValueExists1(String retailerName){
		boolean IsExists = true;
		//String catagory = "S";
		Cursor cursor =  sDb.query(TableConstantName.CART_TABLE_NAME,new String[] {TableConstantName.TABLE_ID}, TableConstantName.COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'", null, null,
				null, null);
		Cursor cursor = sDb.rawQuery("select Id from cart where Retailer_Name = '"+retailerName+"' AND ("+TableConstantName.CART_COUPON_CATAGORY+" = 'Y' OR " +TableConstantName.CART_COUPON_CATAGORY+ " = 'C')", null);
		if(cursor.getCount()>0){
			IsExists = false;
		}
		cursor.close();
		return IsExists;
	}
	
public void getRetailerValueFromCart(String retailerName){
		
		Cursor cursor = sDb.rawQuery("select * from " +TableConstantName.CART_TABLE_NAME+ " where " +TableConstantName.CART_COUPON_RETAILER_NAME+ " = '"+retailerName+"'", null);
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			while(cursor.moveToNext()){
				String coupon = cursor.getString(2);				
				int CouponStatus = cursor.getInt(3);				
				
			}
				
		}
		cursor.close();
	}
	
	public void deleteRetalerNameFromCart(String retailerName){	
		String catagory = "S";
		String catagory1 = "C";
	
	sDb.delete(TableConstantName.CART_TABLE_NAME, TableConstantName.COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'" + " AND ("+ TableConstantName.CART_COUPON_CATAGORY + "=" + "'"+catagory+"'"+ " OR "+ TableConstantName.CART_COUPON_CATAGORY + "=" + "'"+catagory1+"'"+")" , null);
	
	}
	
	public void deleteRetalerNameFromCart1(String retailerName){	
		String catagory = "Y";
		String catagory1 = "C";
	
	sDb.delete(TableConstantName.CART_TABLE_NAME, TableConstantName.COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'" + " AND ("+ TableConstantName.CART_COUPON_CATAGORY + "=" + "'"+catagory+"'"+ " OR "+ TableConstantName.CART_COUPON_CATAGORY + "=" + "'"+catagory1+"'"+")" , null);
	
	}

	
	public void emptyCartTable(){
	sDb.delete(TableConstantName.CART_TABLE_NAME, null, null);
	}
	
	



public ArrayList<CartDetailsCouponBean> fetchDetailsCartValue(String retailerName, ArrayList<CartDetailsCouponBean> mCartDetailsCouponBeans1){
	
	Cursor cursor = sDb.rawQuery("select * from "+TableConstantName.CART_TABLE_NAME+" where "+TableConstantName.CART_COUPON_RETAILER_NAME+" = '"+retailerName+"'", null);
	
	if(cursor.getCount()>0){
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			String mCouponDescription = cursor.getString(1);
			String mRetailerId = cursor.getString(2);
			String mRetailerName = cursor.getString(3);
			String mRetailerType = cursor.getString(4);
			String mCouponProductUpcCode = cursor.getString(5);
			String mCouponPriceOff = cursor.getString(6);
			String mCouponProductSkuId = cursor.getString(7);
			String mCouponType = cursor.getString(8);
			String mCouponProductname = cursor.getString(9);
			String image = cursor.getString(10);
			String mCouponId = cursor.getString(11);
			String mCouponfreeProductId = cursor.getString(12);
			String mCouponProductId = cursor.getString(13);
			String mCouponCode = cursor.getString(14);
			String mCouponFreeProductQty = cursor.getString(15);
			int CouponStatus = cursor.getInt(16);
			
			
			mCartDetailsCouponBeans1.add(new CartDetailsCouponBean(mCouponDescription, mRetailerId, mRetailerName, mRetailerType, mCouponProductUpcCode, mCouponPriceOff, mCouponProductSkuId, mCouponType, mCouponProductname, image, mCouponId, mCouponfreeProductId, mCouponProductId, mCouponCode, mCouponFreeProductQty, CouponStatus== 1?true:false));
			cursor.moveToNext();
			
		}
			
	}
	cursor.close();
	return mCartDetailsCouponBeans1;
}

public void couponSubmissionFromCart(String mRetailerName){
	sDb.delete(TableConstantName.CART_TABLE_NAME, TableConstantName.CART_COUPON_RETAILER_NAME + "=" + "'"+mRetailerName+"'", null);
}


public void deleteRowFromCart(String retailerName, String mCouponId){
	sDb.delete(TableConstantName.CART_TABLE_NAME, TableConstantName.CART_COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'"+ " AND "+ TableConstantName.CART_COUPON_ID + "=" + "'"+mCouponId+"'", null);
}


public int getNumberOfCouponsFromcarttable() {
	int count = -1;

	final Cursor cursor = sDb.query(TableConstantName.CART_TABLE_NAME,
			new String[] { "count(*) " + TableConstantName.CART_ID },
			null, null, null, null, null);
	if (cursor != null) {
		cursor.moveToFirst();
		count = cursor.getInt(cursor
				.getColumnIndex(TableConstantName.CART_ID));
		cursor.close();
	}

	return count;
}

public ArrayList<CartDetailsRetailerBean> getDistinctRetailerFromCart(){
	
	ArrayList<CartDetailsRetailerBean> mCartDetailsRetailerBean = new ArrayList<CartDetailsRetailerBean>();
	
	Cursor cursor = sDb.rawQuery("select distinct Retailer_name,image from cart", null);
	
	if(cursor.getCount()>0){
		cursor.moveToLast();
		while(!cursor.isBeforeFirst()){
			String mRetailer = cursor.getString(0);
			String image = cursor.getString(1);
			
			mCartDetailsRetailerBean.add(new CartDetailsRetailerBean(mRetailer,image));
			cursor.moveToPrevious();
		}
			
	}
	cursor.close();
	return mCartDetailsRetailerBean;
}


public ArrayList<CartDetailsCouponBean> getCouponsFromCart(String retailerName, ArrayList<CartDetailsCouponBean> mCartDetailsCouponBeans){
	
	ArrayList<CartDetailsCouponBean> mCartDetailsCouponBeans = new ArrayList<CartDetailsCouponBean>();
	Cursor cursor = sDb.rawQuery("select *  from cart where Retailer_name = '"+retailerName+"'", null);
	//Log.e("cursor.getCount()", ""+cursor.getCount());
	if(cursor.getCount()>0){
		cursor.moveToLast();
		while(!cursor.isBeforeFirst()){
			String mCouponDescription = cursor.getString(1);
			String mRetailerId = cursor.getString(2);
			String mRetailerName = cursor.getString(3);
			String mRetailerType = cursor.getString(4);
			String mCouponProductUpcCode = cursor.getString(5);
			String mCouponPriceOff = cursor.getString(6);
			String mCouponProductSkuId = cursor.getString(7);
			String mCouponType = cursor.getString(8);
			String mCouponProductname = cursor.getString(9);
			String image = cursor.getString(10);
			String mCouponId = cursor.getString(11);
			String mCouponfreeProductId = cursor.getString(12);
			String mCouponProductId = cursor.getString(13);
			String mCouponCode = cursor.getString(14);
			String mCouponFreeProductQty = cursor.getString(15);
			int CouponStatus = cursor.getInt(16);
		
			mCartDetailsCouponBeans.add(new CartDetailsCouponBean(mCouponDescription, mRetailerId, mRetailerName, mRetailerType, mCouponProductUpcCode, mCouponPriceOff, mCouponProductSkuId, mCouponType, mCouponProductname, image, mCouponId, mCouponfreeProductId, mCouponProductId, mCouponCode, mCouponFreeProductQty, CouponStatus== 1?true:false));
			cursor.moveToPrevious();
		}
			
	}
	cursor.close();
	return mCartDetailsCouponBeans;
}

public boolean isCartPreviousSaved(String retailerName , String mCouponId){
	boolean IsExists = false;
	Cursor cursor =  sDb.query(TableConstantName.CART_TABLE_NAME,new String[] {TableConstantName.CART_ID}, TableConstantName.CART_COUPON_RETAILER_NAME + "=" + "'"+retailerName+"'"+ " AND "+ TableConstantName.CART_COUPON_ID + "=" + "'"+mCouponId+"'", null, null,
			null, null);
	if(cursor.getCount()>0){
		IsExists = true;
	}
	cursor.close();
	return IsExists;
}

public ArrayList<CouponIdBean> getCouponIdFromCart(String retailer){
	ArrayList<CouponIdBean> arr = new ArrayList<CouponIdBean>();
	Cursor cursor = sDb.rawQuery("select COUPON_ID from cart where Retailer_name = '"+retailer+"'", null);
	if(cursor.getCount()>0){
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			arr.add(new CouponIdBean(cursor.getString(0)));
			cursor.moveToNext();
		}

	}
	return arr;
	
}


public boolean isCouponIdExist(String couponid) {
	boolean flag = false;
	int count = -1;
	
	final Cursor cursor = sDb.rawQuery("select count(*) from cart where COUPON_ID='"+couponid+"'", null);
	Log.e("Count", ""+cursor.getCount());
	if (cursor != null) {
		cursor.moveToFirst();
		count = cursor.getInt(0);
		cursor.close();
	}
	
	if(count>0){
		flag = true;
	}else{
		flag = false;
	}
	
	return flag;
	
}







----------------------------------------- Cart table end-------------------------------------------------------------
	
		

-------------------------------- Start Submit table details----------------------------------------------------------
	
	public long createSubmitInfo(final String couponDetails, final String retailerId, final String retailerName,final String retailerType,final String  couponProductUpcCode,final String priceOff,final String productSKuId,final String couponType,final String couponProductname,final String couponImage,final String couponId,final String freeProductId,final String couponProductId,final String couponCode, final String freeProductQTY) {
		final ContentValues values = new ContentValues();
		values.put(TableConstantName.SUBMIT_CART_COUPON_DESCRIPTION, couponDetails);
		values.put(TableConstantName.SUBMIT_CART_COUPON_RETAILER_ID, retailerId);
		values.put(TableConstantName.SUBMIT_CART_COUPON_RETAILER_NAME, retailerName);
		values.put(TableConstantName.SUBMIT_CART_COUPON_RETAILER_TYPE, retailerType);
		values.put(TableConstantName.SUBMIT_CART_COUPON_PRODUCT_UPC_CODE, couponProductUpcCode);
		values.put(TableConstantName.SUBMIT_CART_COUPON_PRICE_OFF, priceOff);
		
		values.put(TableConstantName.SUBMIT_CART_COUPON_PRODUCT_SKU_ID, productSKuId);
		values.put(TableConstantName.SUBMIT_CART_COUPON_TYPE, couponType);
		values.put(TableConstantName.SUBMIT_CART_COUPON_PRODUCT_NAME, couponProductname);
		values.put(TableConstantName.SUBMIT_CART_COUPON_IMAGE, couponImage);
		values.put(TableConstantName.SUBMIT_CART_COUPON_ID, couponId);
		values.put(TableConstantName.SUBMIT_CART_FREE_PRODUCT_ID, freeProductId);
		values.put(TableConstantName.SUBMIT_CART_COUPON_PRODUCT_ID, couponProductId);
		values.put(TableConstantName.SUBMIT_CART_COUPON_CODE, couponCode);
		values.put(TableConstantName.SUBMIT_CART_FREE_PRODUCT_QTY, freeProductQTY);		
		try {
			sDb.beginTransaction();
			final long state = sDb.insert(TableConstantName.SUBMIT_TABLE_NAME, null,
					values);
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			//Log.d(TAG, e.getMessage());
			throw e;
		} finally {
			sDb.endTransaction();
		}
	}
	
	
	
	public boolean isCouponSubmitted(String retailerName, String mCouponId){
		
		Cursor cursor = sDb.rawQuery("select *  from submit where Retailer_name = '"+retailerName+"'"+ " AND "+TableConstantName.SUBMIT_CART_COUPON_ID + "=" + "'"+mCouponId+"'", null);
		//Log.e("cursor.getCount()", ""+cursor.getCount());
		if(cursor.getCount()>0){
			cursor.close();
			return false;
		}
		cursor.close();
		return true;
	}
	
	*//**
	 * Empty Submit Table
	 *//*
	
	public void emptySubmittable(){
		sDb.delete(TableConstantName.SUBMIT_TABLE_NAME, null, null);
	}
	
	public ArrayList<HistotyRetailerbean> getRetailerNameforHistory(){
		ArrayList<HistotyRetailerbean> mList = new ArrayList<HistotyRetailerbean>();
		Cursor cursor = sDb.rawQuery("select distinct Retailer_name from submit order by Id", null);
		
		if(cursor.getCount()>0){
			cursor.moveToLast();
			while(!cursor.isBeforeFirst()){
				String mRetailer = cursor.getString(0);
				
				mList.add(new HistotyRetailerbean(mRetailer));
				cursor.moveToPrevious();
			}
				
		}
		cursor.close();
		return mList;
		
	}
	
	public ArrayList<HistoryCartBean> getCouponDetaileForHistory(String retailerName, ArrayList<CartDetailsCouponBean> mCartDetailsCouponBeans){
		
		ArrayList<HistoryCartBean> mCartDetailsCouponBeans = new ArrayList<HistoryCartBean>();
		Cursor cursor = sDb.rawQuery("select *  from submit where Retailer_name = '"+retailerName+"'", null);
		//Log.e("cursor.getCount()", ""+cursor.getCount());
		if(cursor.getCount()>0){
			cursor.moveToLast();
			while(!cursor.isBeforeFirst()){
				String mCouponDescription = cursor.getString(1);
				
			
				mCartDetailsCouponBeans.add(new HistoryCartBean(mCouponDescription));
				cursor.moveToPrevious();
			}
				
		}
		cursor.close();
		return mCartDetailsCouponBeans;
	}
	
	public int getNumberOfCouponsFromSubmittable() {
		int count = -1;

		final Cursor cursor = sDb.query(TableConstantName.SUBMIT_TABLE_NAME,
				new String[] { "count(*) " + TableConstantName.CART_ID },
				null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			count = cursor.getInt(cursor
					.getColumnIndex(TableConstantName.CART_ID));
			cursor.close();
		}

		return count;
	}
	
------------------------------------- End Submit table details------------------------------------------------------
	
	
---------------------------------------Global Table start-----------------------------------------------------------
	
	public int getRowNumberFromGlobal() {
		int count = -1;

		final Cursor cursor = sDb.query(TableConstantName.GLOBAL_TABLE,
				new String[] { "count(*) " + TableConstantName.GLOBAL_STATUS },
				null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			count = cursor.getInt(cursor
					.getColumnIndex(TableConstantName.GLOBAL_STATUS));
			cursor.close();
		}

		return count;
		
	}
	
	
	public long insertIntoGlobal(int value) {
		final ContentValues values = new ContentValues();
		values.put(TableConstantName.GLOBAL_STATUS, value);
		
		try {
			sDb.beginTransaction();
			final long state = sDb.insert(TableConstantName.GLOBAL_TABLE, null,
					values);
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			//Log.d(TAG, e.getMessage());
			throw e;
		} finally {
			sDb.endTransaction();
		}
	}
	
	
	public boolean updateGlobalValue(int value, int Id){
		final ContentValues values = new ContentValues();
	
		values.put(TableConstantName.GLOBAL_STATUS, value);
		try {
			sDb.beginTransaction();
			final boolean state = sDb.update(TableConstantName.GLOBAL_TABLE, values, TableConstantName.GLOBAL_ID + "=" + "'"+Id+"'", null)>0;
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			//Log.d(TAG, e.getMessage());
			throw e;
		} finally {
			sDb.endTransaction();
		}
	}
	
	
public int getGlobalValue(){
	
	int value = -1;
		
		Cursor cursor = sDb.rawQuery("select * from global where Id = 1", null);
		if(cursor.getCount()>0){
			if(cursor.moveToFirst()){
				value = cursor.getInt(cursor.getColumnIndex(TableConstantName.GLOBAL_STATUS));
			}
			
			cursor.close();
			return value;
		}
		cursor.close();
		return value;
	}

----------------------------------------Global Table end------------------------------------------------------------

----------------------------------------USER Table start-------------------------------------------------------------

public long insertIntoUsertable(String username) {
	final ContentValues values = new ContentValues();
	values.put(TableConstantName.USER_NAME, username);
	
	try {
		sDb.beginTransaction();
		final long state = sDb.insert(TableConstantName.USER_TABLE, null,
				values);
		sDb.setTransactionSuccessful();
		return state;
	} catch (SQLException e) {
		//Log.d(TAG, e.getMessage());
		throw e;
	} finally {
		sDb.endTransaction();
	}
}

public int getNumberOfUserName() {
	int count = 0;

	final Cursor cursor = sDb.query(TableConstantName.USER_TABLE,
			new String[] { "count(*) " + TableConstantName.USER_NAME },
			null, null, null, null, null);
	if (cursor != null) {
		cursor.moveToFirst();
		count = cursor.getInt(cursor
				.getColumnIndex(TableConstantName.USER_NAME));
		cursor.close();
	}

	return count;
	
}

public boolean isUserNameExist(String UserName){
	boolean IsExists = false;
	Cursor cursor =  sDb.query(TableConstantName.USER_TABLE,new String[] {TableConstantName.USER_ID}, TableConstantName.USER_NAME + "=" + "'"+UserName+"'", null, null,
			null, null);
	if(cursor.getCount()>0){
		IsExists = true;
	}
	cursor.close();
	return IsExists;
}

public ArrayList<UserBean> getUserName(){
	
	ArrayList<UserBean> mUserBeans = new ArrayList<UserBean>();
	
	Cursor cursor = sDb.rawQuery("select * from user ", null);
	
	if(cursor.getCount()>0){
		cursor.moveToLast();
		while(!cursor.isBeforeFirst()){
			String mUser = cursor.getString(1);			
			mUserBeans.add(new UserBean(mUser));
			cursor.moveToPrevious();
		}
			
	}
	cursor.close();
	return mUserBeans;
}

USER Table end

*//*********************************** Conf Table start *****************************************//*
public long insertConfInfo(String Confid, String reatiler) {
	Log.e("Reach here Conf", "Reach here Conf db");
	final ContentValues values = new ContentValues();
	values.put(TableConstantName.CONF_ID, Confid);
	values.put(TableConstantName.CONF_RETAILERNAME, reatiler);
		
	try {
		sDb.beginTransaction();
		final long state = sDb.insert(TableConstantName.CONF_TABLE, null,
				values);
		sDb.setTransactionSuccessful();
		return state;
	} catch (SQLException e) {
		//Log.d(TAG, e.getMessage());
		throw e;
	} finally {
		sDb.endTransaction();
	}
}

public boolean isReatilerExistIntoConfTable(String retailer1){
	boolean IsExists = false;
	Cursor cursor =  sDb.rawQuery("select * from "+TableConstantName.CONF_TABLE+" where "+TableConstantName.CONF_RETAILERNAME+" = '"+retailer1+"'",null);
	if(cursor.getCount()>0){
		IsExists = true;
	}
	cursor.close();
	return IsExists;
}

public String getConformationNo(String retailer){
	String mConf = "";
	Cursor cursor =  sDb.rawQuery("select "+TableConstantName.CONF_ID+" from "+TableConstantName.CONF_TABLE+" where "+TableConstantName.CONF_RETAILERNAME+"  = '"+retailer+"'",null);
	if(cursor.getCount()>0){
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
		mConf = cursor.getString(0);
		cursor.moveToNext();
		}
	}
	cursor.close();
	return mConf;
}

public void emptyConfTable(){
	sDb.delete(TableConstantName.CONF_TABLE, null, null);
}


*//****SIgn in Table Start *//*

public int getRowNumberFromSign() {
	int count = -1;

	final Cursor cursor = sDb.query(TableConstantName.SIGN_TABLE,
			new String[] { "count(*) " + TableConstantName.SIGN_STATUS },
			null, null, null, null, null);
	if (cursor != null) {
		cursor.moveToFirst();
		count = cursor.getInt(cursor
				.getColumnIndex(TableConstantName.SIGN_STATUS));
		cursor.close();
	}

	return count;
	
}

*//**
 * 
 * @param status
 * @param patrolerFirstName
 * @param lastname
 * @param cid
 * @return
 * 
 *   Status can be 0 OR 1
 *   Status->0 : Directly go to the Dashboard
 *   Status->1: Go to the sign in Page.
 *//*
public long insertIntoSigninTable(String status,String patrolerFirstName, String lastname, String cid ) {
	final ContentValues values = new ContentValues();
	values.put(TableConstantName.SIGN_STATUS, status);
	values.put(TableConstantName.SIGN_FIRSTNAME, patrolerFirstName);
	values.put(TableConstantName.SIGN_LASTNAME, lastname);
	values.put(TableConstantName.SIGN_CID, cid);
	
	try {
		sDb.beginTransaction();
		final long state = sDb.insert(TableConstantName.SIGN_TABLE, null,
				values);
		sDb.setTransactionSuccessful();
		return state;
	} catch (SQLException e) {
		//Log.d(TAG, e.getMessage());
		throw e;
	} finally {
		sDb.endTransaction();
	}
}

public boolean updateSigninStatus(String status){
	int Id = 1;
	final ContentValues values = new ContentValues();

	values.put(TableConstantName.SIGN_STATUS, status);
	try {
		sDb.beginTransaction();
		final boolean state = sDb.update(TableConstantName.SIGN_TABLE, values, TableConstantName.SIGN_ID + "=" + "'"+Id+"'", null)>0;
		sDb.setTransactionSuccessful();
		return state;
	} catch (SQLException e) {
		//Log.d(TAG, e.getMessage());
		throw e;
	} finally {
		sDb.endTransaction();
	}
}


public boolean updateSigninValue(String patrolerFirstName, String lastname, String cid ){
	int Id = 1;
	final ContentValues values = new ContentValues();
	values.put(TableConstantName.SIGN_FIRSTNAME, patrolerFirstName);
	values.put(TableConstantName.SIGN_LASTNAME, lastname);
	values.put(TableConstantName.SIGN_CID, cid);

	
	try {
		sDb.beginTransaction();
		final boolean state = sDb.update(TableConstantName.SIGN_TABLE, values, TableConstantName.SIGN_ID + "=" + "'"+Id+"'", null)>0;
		sDb.setTransactionSuccessful();
		return state;
	} catch (SQLException e) {
		//Log.d(TAG, e.getMessage());
		throw e;
	} finally {
		sDb.endTransaction();
	}
}


public String getSigninStatus(){

String value = "";
	
	Cursor cursor = sDb.rawQuery("select * from signin where Id = 1", null);
	if(cursor.getCount()>0){
		if(cursor.moveToFirst()){
			value = cursor.getString(cursor.getColumnIndex(TableConstantName.SIGN_STATUS));
		}
		
		cursor.close();
		return value;
	}
	cursor.close();
	return value;
}

public String getSigninFirstName(){

String value = "";
	
	Cursor cursor = sDb.rawQuery("select * from signin where Id = 1", null);
	if(cursor.getCount()>0){
		if(cursor.moveToFirst()){
			value = cursor.getString(cursor.getColumnIndex(TableConstantName.SIGN_FIRSTNAME));
		}
		
		cursor.close();
		return value;
	}
	cursor.close();
	return value;
}

public String getSigninLastname(){

String value = "";
	
	Cursor cursor = sDb.rawQuery("select * from signin where Id = 1", null);
	if(cursor.getCount()>0){
		if(cursor.moveToFirst()){
			value = cursor.getString(cursor.getColumnIndex(TableConstantName.SIGN_LASTNAME));
		}
		
		cursor.close();
		return value;
	}
	cursor.close();
	return value;
}

public String getSigninCid(){

String value = "";
	
	Cursor cursor = sDb.rawQuery("select * from signin where Id = 1", null);
	if(cursor.getCount()>0){
		if(cursor.moveToFirst()){
			value = cursor.getString(cursor.getColumnIndex(TableConstantName.SIGN_CID));
		}
		
		cursor.close();
		return value;
	}
	cursor.close();
	return value;
}
*//** Sign in Table End ............................*//*

*/
}
