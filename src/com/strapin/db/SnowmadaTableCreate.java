package com.strapin.db;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class SnowmadaTableCreate {
	
	/*private static final String DATABASE_SNOMADA = "CREATE TABLE "
			+ TableConstantName.TABLE_NAME + " ("
			+ TableConstantName.ID
			+ "  INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ TableConstantName.FACEBOOK_ID + "		  TEXT);";*/
	
	private static final String DATABASE_SNOMADA = "CREATE TABLE "
			+ TableConstantName.TABLE_NAME + " ("
			+ TableConstantName.ID
			+ "  INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ TableConstantName.FB_USER_FIRSTNAME + " TEXT, "
			+ TableConstantName.FB_USER_LASTNAME + " TEXT, "
			+ TableConstantName.FACEBOOK_ID + "		  TEXT);";
	
	private static final String DATABASE_SKI = "CREATE TABLE "
			+ TableConstantName.TABLE_SKI + " ("
			+ TableConstantName.ID
			+ "  INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ TableConstantName.STATUS + "		  TEXT);";
	
	private static final String DATABASE_MEETUP = "CREATE TABLE "
			+ TableConstantName.TABLE_MEETUP + " ("
			+ TableConstantName.ID
			+ "  INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ TableConstantName.FACEBOOK_ID + " TEXT, "
			+ TableConstantName.NAME + " TEXT, "
			+ TableConstantName.LOCATION + " TEXT, "
			+ TableConstantName.LATITUDE + " TEXT, "
			+ TableConstantName.LONGITUDE + " TEXT, "
			+ TableConstantName.CLOCKTIME + " TEXT, "
			+ TableConstantName.ABOUT + " TEXT, "
			+ TableConstantName.STATUS + " TEXT, "
			+ TableConstantName.CREATOR + "		  TEXT);";
	
	private static final String DATABASE_SKIPETROL = "CREATE TABLE "
			+ TableConstantName.TABLE_SKIPETROL + " ("
			+ TableConstantName.ID
			+ "  INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ TableConstantName.FIRSTNAME + " TEXT, "
			+ TableConstantName.LASTNAME + " TEXT, "
			+ TableConstantName.LATITUDE + " TEXT, "
			+ TableConstantName.LONGITUDE + "		  TEXT);";
	
	private static final String DATABASE_CHAT_MESSAGE = "CREATE TABLE "
			+ TableConstantName.TABLE_MESSAGE + " ("
			+ TableConstantName.ID
			+ "  INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ TableConstantName.SENDER_FB_ID + " TEXT, "
			+ TableConstantName.SENDER_NAME + " TEXT, "
			+ TableConstantName.READ_STATUS + " TEXT, "
			+ TableConstantName.TEXT_MESSAGE + "		  TEXT);";
	
	private static final String DATABASE_FACEBOK_FRIENDS = "CREATE TABLE "
			+ TableConstantName.TABLE_FB_FRIENDS + " ("
			+ TableConstantName.ID
			+ "  INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ TableConstantName.FB_ID + " TEXT, "
			+ TableConstantName.FB_NAME + "		  TEXT);";
	
	private static final String DATABASE_SESSION = "CREATE TABLE "
			+ TableConstantName.TABLE_SESSION + " ("
			+ TableConstantName.ID
			+ "  INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ TableConstantName.IS_SESSION_VALID + "		  TEXT);";
	
	public static void onCreate(SQLiteDatabase database) {
		Log.v("SendQueueTable OnCreate", "Reached Here");
		database.beginTransaction();
		try {
			database.execSQL(DATABASE_SNOMADA);
			database.execSQL(DATABASE_SKI);
			database.execSQL(DATABASE_MEETUP);
			database.execSQL(DATABASE_SKIPETROL);
			database.execSQL(DATABASE_CHAT_MESSAGE);
			database.execSQL(DATABASE_FACEBOK_FRIENDS);
			database.execSQL(DATABASE_SESSION);
			database.setTransactionSuccessful();
		} catch (SQLException e) {
			throw e;
		} finally {
			database.endTransaction();
		}
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		    Log.w(SnowmadaTableCreate.class.getName(),"Upgrading database from version " + oldVersion + " to "+ newVersion + ", which will destroy all old data");
			database.execSQL("DROP TABLE IF EXISTS" + TableConstantName.TABLE_NAME);
			database.execSQL("DROP TABLE IF EXISTS" + TableConstantName.TABLE_SKI);
			database.execSQL("DROP TABLE IF EXISTS" + TableConstantName.TABLE_MEETUP);
			database.execSQL("DROP TABLE IF EXISTS" + TableConstantName.TABLE_SKIPETROL);
			database.execSQL("DROP TABLE IF EXISTS" + TableConstantName.TABLE_MESSAGE);
			database.execSQL("DROP TABLE IF EXISTS" + TableConstantName.TABLE_FB_FRIENDS);
			database.execSQL("DROP TABLE IF EXISTS" + TableConstantName.TABLE_SESSION);
		onCreate(database);
	}
}