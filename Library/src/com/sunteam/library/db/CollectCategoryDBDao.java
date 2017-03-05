package com.sunteam.library.db;

import java.util.ArrayList;

import com.sunteam.library.entity.CollectCategoryEntity;
import com.sunteam.library.utils.DatabaseConstants;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class CollectCategoryDBDao 
{
	private LibraryDBHelper mLibraryDBHelper = null;

	public CollectCategoryDBDao( Context context, String name, CursorFactory factory, int version ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, factory, version );
	}
	
	public CollectCategoryDBDao( Context context, String name ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public CollectCategoryDBDao( Context context ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, DatabaseConstants.LIBRARY_DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public void insert( CollectCategoryEntity entity ) 
	{
		if( null == entity )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		String sql = 
				"insert into " + DatabaseConstants.COLLECT_CATEGORY_TABLE_NAME +
				" (" +
				DatabaseConstants.COLLECT_CATEGORY_ID + "," +
				DatabaseConstants.COLLECT_CATEGORY_RESTYPE + "," +
				DatabaseConstants.COLLECT_CATEGORY_USERNAME + "," +
				DatabaseConstants.COLLECT_CATEGORY_NAME + "," +
				DatabaseConstants.COLLECT_CATEGORY_FULLNAME + "," +
				DatabaseConstants.COLLECT_CATEGORY_CODE + ") values (?,?,?,?,?,?)";
		db.execSQL( sql, new Object[]{entity.id,entity.resType,entity.userName,entity.categoryName,entity.categoryFullName,entity.categoryCode});
		db.close();
	}

	//顺序插入
	public void insert( ArrayList<CollectCategoryEntity> list ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = 0; i < size; i++ )
		{
			CollectCategoryEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.COLLECT_CATEGORY_TABLE_NAME +
					" (" +
					DatabaseConstants.COLLECT_CATEGORY_ID + "," +
					DatabaseConstants.COLLECT_CATEGORY_RESTYPE + "," +
					DatabaseConstants.COLLECT_CATEGORY_USERNAME + "," +
					DatabaseConstants.COLLECT_CATEGORY_NAME + "," +
					DatabaseConstants.COLLECT_CATEGORY_FULLNAME + "," +
					DatabaseConstants.COLLECT_CATEGORY_CODE + ") values (?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{entity.id,entity.resType,entity.userName,entity.categoryName,entity.categoryFullName,entity.categoryCode});
		}
		db.close();
	}
	
	//倒序插入
	public void insertDescending( ArrayList<CollectCategoryEntity> list ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = size-1; i >= 0; i-- )
		{
			CollectCategoryEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.COLLECT_CATEGORY_TABLE_NAME +
					" (" +
					DatabaseConstants.COLLECT_CATEGORY_ID + "," +
					DatabaseConstants.COLLECT_CATEGORY_RESTYPE + "," +
					DatabaseConstants.COLLECT_CATEGORY_USERNAME + "," +
					DatabaseConstants.COLLECT_CATEGORY_NAME + "," +
					DatabaseConstants.COLLECT_CATEGORY_FULLNAME + "," +
					DatabaseConstants.COLLECT_CATEGORY_CODE + ") values (?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{entity.id,entity.resType,entity.userName,entity.categoryName,entity.categoryFullName,entity.categoryCode});
		}
		db.close();
	}
	
	//清除整个表数据
	public void clearTable()
	{
		String sql1 = "DELETE FROM " + DatabaseConstants.COLLECT_CATEGORY_TABLE_NAME +";";
		String sql2= "update sqlite_sequence set seq=0 where name='" + DatabaseConstants.COLLECT_CATEGORY_TABLE_NAME + "'";
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL(sql1);
		db.execSQL(sql2);
		db.close();
	}

	//查找所有数据条数
	public long getCount() 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		if( null == db )
		{
			return	0;
		}
		
		Cursor cursor = db.rawQuery("select count(*) from " + DatabaseConstants.COLLECT_CATEGORY_TABLE_NAME, null);
		if( null == cursor )
		{
			return	0;
		}
		
		cursor.moveToFirst();
		long count = cursor.getLong(0);
		
		if (!cursor.isClosed())
		{
			cursor.close();
		}
		db.close();
		  
		return count;
	}

	//查找数据
	public CollectCategoryEntity find( String userName, int resType, String categoryCode ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.COLLECT_CATEGORY_TABLE_NAME + " where " + DatabaseConstants.COLLECT_CATEGORY_USERNAME + " = ? and " + DatabaseConstants.COLLECT_CATEGORY_RESTYPE + " = ? and " + DatabaseConstants.COLLECT_CATEGORY_CODE + " = ?", new String[]{userName, resType+"", categoryCode});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		CollectCategoryEntity entity = null;
		
		while(cursor.moveToNext())
		{	
			entity = new CollectCategoryEntity();
			entity.id = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLLECT_CATEGORY_ID));
			entity.resType = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLLECT_CATEGORY_RESTYPE));
			entity.userName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_CATEGORY_USERNAME));		
			entity.categoryName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_CATEGORY_NAME));
			entity.categoryFullName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_CATEGORY_FULLNAME));
			entity.categoryCode = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_CATEGORY_CODE));
			
			break;
		}
		
		if (!cursor.isClosed()) 
		{
			cursor.close();
		}
		db.close();
		
		return	entity;
	}
	
	//查找所有数据
	public ArrayList<CollectCategoryEntity> findAll( String userName ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.COLLECT_CATEGORY_TABLE_NAME + " where " + DatabaseConstants.COLLECT_CATEGORY_USERNAME + " = ?", new String[]{userName});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		ArrayList<CollectCategoryEntity> list = new ArrayList<CollectCategoryEntity>();
		while(cursor.moveToNext())
		{
			CollectCategoryEntity entity = new CollectCategoryEntity();
			entity.id = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLLECT_CATEGORY_ID));
			entity.resType = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLLECT_CATEGORY_RESTYPE));
			entity.userName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_CATEGORY_USERNAME));		
			entity.categoryName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_CATEGORY_NAME));
			entity.categoryFullName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_CATEGORY_FULLNAME));
			entity.categoryCode = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_CATEGORY_CODE));
			
		    list.add(0,entity);	//逆序插入
		}
		
		if (!cursor.isClosed()) 
		{
			cursor.close();
		}
		db.close();
		
		return	list;
	}
	
	//删除数据
	public void delete( String userName, int resType, String categoryCode )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.COLLECT_CATEGORY_TABLE_NAME + " where " + DatabaseConstants.COLLECT_CATEGORY_USERNAME + " = ? and " + DatabaseConstants.COLLECT_CATEGORY_RESTYPE + " = ? and " + DatabaseConstants.COLLECT_CATEGORY_CODE + " = ?", new String[]{userName, resType+"", categoryCode});
		db.close();
	}
	
	//删除数据
	public void delete( CollectCategoryEntity entity )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.COLLECT_CATEGORY_TABLE_NAME + " where " + DatabaseConstants.COLLECT_CATEGORY_USERNAME + " = ? and " + DatabaseConstants.COLLECT_CATEGORY_RESTYPE + " = ? and " + DatabaseConstants.COLLECT_CATEGORY_CODE + " = ?", new String[]{entity.userName, entity.resType+"", entity.categoryCode});
		db.close();
	}

	//删除数据
	public void deleteAll( String username )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.COLLECT_CATEGORY_TABLE_NAME + " where " + DatabaseConstants.COLLECT_CATEGORY_USERNAME + " = ?", new String[]{username});
		db.close();
	}
		
	//删除所有的数据
	public void deleteAll() 
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.COLLECT_CATEGORY_TABLE_NAME, null);
		db.close();
	}

	//关闭数据库
    public void closeDb() 
    {
    	if (mLibraryDBHelper != null)
    	{
    		mLibraryDBHelper.close();
    	}
   }
}
