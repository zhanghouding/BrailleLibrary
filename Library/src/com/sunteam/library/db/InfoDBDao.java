package com.sunteam.library.db;

import java.util.ArrayList;

import com.sunteam.library.entity.InformationEntity;
import com.sunteam.library.utils.DatabaseConstants;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class InfoDBDao 
{
	private LibraryDBHelper mLibraryDBHelper = null;

	public InfoDBDao( Context context, String name, CursorFactory factory, int version ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, factory, version );
	}
	
	public InfoDBDao( Context context, String name ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public InfoDBDao( Context context ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, DatabaseConstants.LIBRARY_DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public void insert( InformationEntity entity, int resourceType ) 
	{
		if( null == entity )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		String sql = 
				"insert into " + DatabaseConstants.INFO_TABLE_NAME +
				" (" +
				DatabaseConstants.RESOURCE_TYPE + "," +
				DatabaseConstants.INFO_TITLE + "," +
				DatabaseConstants.INFO_DATE + ") values (?,?,?)";
		db.execSQL( sql, new Object[]{resourceType,entity.title,entity.date});
		db.close();
	}

	//顺序插入
	public void insert( ArrayList<InformationEntity> list, int resourceType ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = 0; i < size; i++ )
		{
			InformationEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.INFO_TABLE_NAME +
					" (" +
					DatabaseConstants.RESOURCE_TYPE + "," +
					DatabaseConstants.INFO_TITLE + "," +
					DatabaseConstants.INFO_DATE + ") values (?,?,?)";
			db.execSQL( sql, new Object[]{resourceType,entity.title,entity.date});
		}
		db.close();
	}
	
	//倒序插入
	public void insertDescending( ArrayList<InformationEntity> list, int resourceType ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = size-1; i >= 0; i-- )
		{
			InformationEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.INFO_TABLE_NAME +
					" (" +
					DatabaseConstants.RESOURCE_TYPE + "," +
					DatabaseConstants.INFO_TITLE + "," +
					DatabaseConstants.INFO_DATE + ") values (?,?,?)";
			db.execSQL( sql, new Object[]{resourceType,entity.title,entity.date});
		}
		db.close();
	}
	
	//清除整个表数据
	public void clearTable()
	{
		String sql1 = "DELETE FROM " + DatabaseConstants.INFO_TABLE_NAME +";";
		String sql2= "update sqlite_sequence set seq=0 where name='" + DatabaseConstants.INFO_TABLE_NAME + "'";
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL(sql1);
		db.execSQL(sql2);
		db.close();
	}

	//查找所有资源类型为resourceType的数据条数
	public long getCount( int resourceType ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		if( null == db )
		{
			return	0;
		}
		
		Cursor cursor = db.rawQuery("select count(*) from " + DatabaseConstants.INFO_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ?", new String[]{resourceType+""});
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

	//查找所有资源类型为resourceType的数据
	public ArrayList<InformationEntity> findAll( int resourceType ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.INFO_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ?", new String[]{resourceType+""});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		ArrayList<InformationEntity> list = new ArrayList<InformationEntity>();
		while(cursor.moveToNext())
		{
			InformationEntity entity = new InformationEntity();
			entity.title = cursor.getString(cursor.getColumnIndex(DatabaseConstants.INFO_TITLE));
			entity.date = cursor.getString(cursor.getColumnIndex(DatabaseConstants.INFO_DATE));
			
		    list.add(entity);
		}
		
		if (!cursor.isClosed()) 
		{
			cursor.close();
		}
		db.close();
		
		return	list;
	}

	//删除所有资源类型为resourceType的数据
	public void deleteAll( int resourceType ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.INFO_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ?", new String[]{resourceType+""});
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
