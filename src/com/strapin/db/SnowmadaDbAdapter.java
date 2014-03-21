package com.strapin.db;

import java.util.ArrayList;

import com.strapin.bean.ContactListBean;
import com.strapin.bean.AppusersBean;
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

public ArrayList<AppusersBean> getFacebookFriends() {
	
	ArrayList<AppusersBean> arr = new ArrayList<AppusersBean>();
	Cursor cursor = sDb.rawQuery("select * from " +TableConstantName.TABLE_FB_FRIENDS, null);		
	if(cursor.getCount()>0){
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){			
			String facebookid = cursor.getString(1);
			String name = cursor.getString(2);
			arr.add(new AppusersBean(facebookid,name));
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


////////////////////////////////////CONTACT list List Start//////////////////////////////////
	public long insertcontact(final String name, final String ph) {
		final ContentValues values = new ContentValues();
		values.put(TableConstantName.CONTACT_PHONE, ph);
		values.put(TableConstantName.CONTACT_NAME, name);
		

		try {
			sDb.beginTransaction();
			final long state = sDb.insert(TableConstantName.TABLE_CONTACT_LIST,
					null, values);
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			throw e;
		} finally {
			sDb.endTransaction();
		}
	}

	public ArrayList<AppusersBean> getContact() {

		ArrayList<AppusersBean> arr = new ArrayList<AppusersBean>();
		Cursor cursor = sDb.rawQuery("select * from "
				+ TableConstantName.TABLE_CONTACT_LIST, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String ph = cursor.getString(1);
				String name = cursor.getString(2);
				arr.add(new AppusersBean( ph,name));
				cursor.moveToNext();
			}
		}
		cursor.close();
		return arr;
	}

	public int getContactCount() {
		int count = -1;

		final Cursor cursor = sDb.query(TableConstantName.TABLE_CONTACT_LIST,
				new String[] { "count(*) " + TableConstantName.CONTACT_PHONE }, null,
				null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			count = cursor.getInt(cursor
					.getColumnIndex(TableConstantName.CONTACT_PHONE));
			cursor.close();
		}
		return count;
	}

	public void emptyContactTable() {
		sDb.delete(TableConstantName.TABLE_CONTACT_LIST, null, null);

	}
////////////////////////////////////CONTACT list END//////////////////////////////////

}
