package com.sunteam.library.db;

import com.sunteam.library.utils.DatabaseConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

//创建资源数据库
public class ResourceDBHelper extends SQLiteOpenHelper 
{	
	public ResourceDBHelper( Context context, String name, CursorFactory factory, int version ) 
	{
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		// TODO Auto-generated method stub
		String sql = 
				"create table if not exists " + DatabaseConstants.RESOURCE_TABLE_NAME +
				" (_id integer PRIMARY KEY AUTOINCREMENT," +
				DatabaseConstants.RESOURCE_TYPE + " integer," +
				DatabaseConstants.RESOURCE_DBCODE + " varchar(128)," +
				DatabaseConstants.RESOURCE_SYSID + " varchar(128)," +
				DatabaseConstants.RESOURCE_TITLE + " varchar(128)," +
				DatabaseConstants.RESOURCE_AUTHOR + " varchar(128)," +
				DatabaseConstants.RESOURCE_KEYWORDS + " varchar(128)," +
				DatabaseConstants.RESOURCE_ABS + " varchar(128)," +
				DatabaseConstants.RESOURCE_PUBLISH + " varchar(128)," +
				DatabaseConstants.RESOURCE_IDENTIFIER + " varchar(128))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		// TODO Auto-generated method stub
	}
}
