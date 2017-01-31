package com.sunteam.library.db;

import com.sunteam.library.utils.DatabaseConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

//创建分类数据库
public class CategoryDBHelper extends SQLiteOpenHelper 
{	
	public CategoryDBHelper( Context context, String name, CursorFactory factory, int version ) 
	{
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		// TODO Auto-generated method stub
		String sql = 
				"create table if not exists " + DatabaseConstants.CATEGORY_TABLE_NAME +
				" (_id integer PRIMARY KEY AUTOINCREMENT," +
				DatabaseConstants.RESOURCE_TYPE + " integer," +
				DatabaseConstants.CATEGORY_FATHER + " integer," +
				DatabaseConstants.CATEGORY_SEQ + " integer," +
				DatabaseConstants.CATEGORY_LEVEL + " integer," +
				DatabaseConstants.CATEGORY_NAME + " varchar(128)," +
				DatabaseConstants.CATEGORY_CODE + " varchar(128)," +
				DatabaseConstants.CATEGORY_TYPE + " varchar(128))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		// TODO Auto-generated method stub
	}
}
