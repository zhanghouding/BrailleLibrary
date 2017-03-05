package com.sunteam.library.db;

import com.sunteam.library.utils.DatabaseConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

//创建图书馆数据库
public class LibraryDBHelper extends SQLiteOpenHelper 
{	
	public static final String CREATE_CATEGORY_TABLE =	//创建分类表
			"create table if not exists " + DatabaseConstants.CATEGORY_TABLE_NAME +
			" (_id integer PRIMARY KEY AUTOINCREMENT," +
			DatabaseConstants.RESOURCE_TYPE + " integer," +
			DatabaseConstants.CATEGORY_FATHER + " integer," +
			DatabaseConstants.CATEGORY_SEQ + " integer," +
			DatabaseConstants.CATEGORY_LEVEL + " integer," +
			DatabaseConstants.CATEGORY_NAME + " varchar(128)," +
			DatabaseConstants.CATEGORY_CODE + " varchar(128)," +
			DatabaseConstants.CATEGORY_TYPE + " varchar(128))";
	
	public static final String CREATE_RESOURCE_TABLE =	//创建资源表 
			"create table if not exists " + DatabaseConstants.RESOURCE_TABLE_NAME +
			" (_id integer PRIMARY KEY AUTOINCREMENT," +
			DatabaseConstants.RESOURCE_TYPE + " integer," +
			DatabaseConstants.RESOURCE_CATEGORYCODE + " varchar(128)," +
			DatabaseConstants.RESOURCE_DBCODE + " varchar(128)," +
			DatabaseConstants.RESOURCE_SYSID + " varchar(128)," +
			DatabaseConstants.RESOURCE_TITLE + " varchar(128)," +
			DatabaseConstants.RESOURCE_AUTHOR + " varchar(128)," +
			DatabaseConstants.RESOURCE_KEYWORDS + " varchar(128)," +
			DatabaseConstants.RESOURCE_ABS + " varchar(1024)," +
			DatabaseConstants.RESOURCE_PUBLISH + " varchar(128)," +
			DatabaseConstants.RESOURCE_IDENTIFIER + " varchar(128))";
	
	public static final String CREATE_CHAPTER_TABLE =	//创建章节表
			"create table if not exists " + DatabaseConstants.CHAPTER_TABLE_NAME +
			" (_id integer PRIMARY KEY AUTOINCREMENT," +
			DatabaseConstants.RESOURCE_TYPE + " integer," +
			DatabaseConstants.CHAPTER_FATHER + " integer," +
			DatabaseConstants.CHAPTER_SEQ + " integer," +
			DatabaseConstants.CHAPTER_LEVEL + " integer," +
			DatabaseConstants.CHAPTER_DBCODE + " varchar(128)," +
			DatabaseConstants.CHAPTER_SYSID + " varchar(128)," +
			DatabaseConstants.CHAPTER_IDENTIFIER + " varchar(128)," +
			DatabaseConstants.CHAPTER_INDEX + " varchar(128)," +
			DatabaseConstants.CHAPTER_NAME + " varchar(128)," +
			DatabaseConstants.CHAPTER_URL + " varchar(1024))";
	
	public static final String CREATE_INFO_TABLE =	//创建资源表
			"create table if not exists " + DatabaseConstants.INFO_TABLE_NAME +
			" (_id integer PRIMARY KEY AUTOINCREMENT," +
			DatabaseConstants.RESOURCE_TYPE + " integer," +
			DatabaseConstants.INFO_TITLE + " varchar(512)," +
			DatabaseConstants.INFO_DATE + " varchar(32))";
	
	public static final String CREATE_HISTORY_TABLE =	//创建历史表
			"create table if not exists " + DatabaseConstants.HISTORY_TABLE_NAME +
			" (_id integer PRIMARY KEY AUTOINCREMENT," +
			DatabaseConstants.HISTORY_ID + " integer," +
			DatabaseConstants.HISTORY_RESTYPE + " integer," +
			DatabaseConstants.HISTORY_LCINDEX + " integer," +
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
	
	public LibraryDBHelper( Context context, String name, CursorFactory factory, int version ) 
	{
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		// TODO Auto-generated method stub
		db.execSQL(CREATE_CATEGORY_TABLE);	//创建分类表
		db.execSQL(CREATE_RESOURCE_TABLE);	//创建资源表
		db.execSQL(CREATE_CHAPTER_TABLE);	//创建章节表
		db.execSQL(CREATE_INFO_TABLE);		//创建资讯表
		db.execSQL(CREATE_HISTORY_TABLE);	//创建历史表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		// TODO Auto-generated method stub
	}
}
