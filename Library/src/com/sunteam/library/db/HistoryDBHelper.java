package com.sunteam.library.db;

import com.sunteam.library.utils.DatabaseConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

//创建历史数据库
public class HistoryDBHelper extends SQLiteOpenHelper 
{	
	public static final String CREATE_HISTORY_TABLE =	//创建历史表
			"create table if not exists " + DatabaseConstants.HISTORY_TABLE_NAME +
			" (_id integer PRIMARY KEY AUTOINCREMENT," +
			DatabaseConstants.HISTORY_ID + " integer," +
			DatabaseConstants.HISTORY_RESTYPE + " integer," +
			DatabaseConstants.HISTORY_LCINDEX + " integer," +
			DatabaseConstants.HISTORY_SYNC + " integer," +
			DatabaseConstants.HISTORY_USERNAME + " varchar(64)," +
			DatabaseConstants.HISTORY_TITLE + " varchar(128)," +
			DatabaseConstants.HISTORY_DBCODE + " varchar(128)," +
			DatabaseConstants.HISTORY_SYSID + " varchar(128)," +		
			DatabaseConstants.HISTORY_ENTERPOINT + " varchar(16)," +
			DatabaseConstants.HISTORY_URL + " varchar(1024)," +
			DatabaseConstants.HISTORY_CTIME + " varchar(128)," +
			DatabaseConstants.HISTORY_UTIME + " varchar(128)," +
			DatabaseConstants.HISTORY_BOOKTITLE + " varchar(128)," +
			DatabaseConstants.HISTORY_COVERURL + " varchar(1024)," +
			DatabaseConstants.HISTORY_PERCENT + " varchar(16)," +
			DatabaseConstants.HISTORY_CFULLNAME + " varchar(128)," +			
			DatabaseConstants.HISTORY_CATEGORYCODE + " varchar(128))";

	public HistoryDBHelper( Context context, String name, CursorFactory factory, int version ) 
	{
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		// TODO Auto-generated method stub
		db.execSQL(CREATE_HISTORY_TABLE);	//创建历史表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		// TODO Auto-generated method stub
	}
}
