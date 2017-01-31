package com.sunteam.library.db;

import com.sunteam.library.utils.DatabaseConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

//创建章节数据库
public class ChapterDBHelper extends SQLiteOpenHelper 
{	
	public ChapterDBHelper( Context context, String name, CursorFactory factory, int version ) 
	{
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		// TODO Auto-generated method stub
		String sql = 
				"create table if not exists " + DatabaseConstants.CHAPTER_TABLE_NAME +
				" (_id integer PRIMARY KEY AUTOINCREMENT," +
				DatabaseConstants.RESOURCE_TYPE + " integer," +
				DatabaseConstants.CHAPTER_FATHER + " integer," +
				DatabaseConstants.CHAPTER_SEQ + " integer," +
				DatabaseConstants.CHAPTER_LEVEL + " integer," +
				DatabaseConstants.CHAPTER_INDEX + " varchar(128)," +
				DatabaseConstants.CHAPTER_NAME + " varchar(128)," +
				DatabaseConstants.CHAPTER_URL + " varchar(512))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		// TODO Auto-generated method stub
	}
}
