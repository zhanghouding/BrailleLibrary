package com.sunteam.library.db;

import java.util.ArrayList;

import com.sunteam.library.entity.DownloadResourceEntity;
import com.sunteam.library.utils.DatabaseConstants;
import com.sunteam.library.utils.LibraryConstant;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DownloadResourceDBDao 
{
	private LibraryDBHelper mLibraryDBHelper = null;

	public DownloadResourceDBDao( Context context, String name, CursorFactory factory, int version ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, factory, version );
	}
	
	public DownloadResourceDBDao( Context context, String name ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public DownloadResourceDBDao( Context context ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, DatabaseConstants.LIBRARY_DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public void insert( DownloadResourceEntity entity ) 
	{
		if( null == entity )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		String sql = 
				"insert into " + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME +
				" (" +
				DatabaseConstants.DOWNLOAD_RESOURCE_CHAPTER_COUNT + "," +
				DatabaseConstants.DOWNLOAD_RESOURCE_RESTYPE + "," +
				DatabaseConstants.DOWNLOAD_RESOURCE_STATUS + "," +
				DatabaseConstants.DOWNLOAD_RESOURCE_USERNAME + "," +
				DatabaseConstants.DOWNLOAD_RESOURCE_TITLE + "," +
				DatabaseConstants.DOWNLOAD_RESOURCE_DBCODE + "," +
				DatabaseConstants.DOWNLOAD_RESOURCE_SYSID + "," +
				DatabaseConstants.DOWNLOAD_RESOURCE_IDENTIFIER + "," +
				DatabaseConstants.DOWNLOAD_RESOURCE_FULLNAME + ") values (?,?,?,?,?,?,?,?,?)";
		db.execSQL( sql, new Object[]{entity.chapterCount,entity.resType,entity.status,entity.userName,entity.title,entity.dbCode,entity.sysId,entity.identifier,entity.categoryFullName});
		db.close();
	}

	//顺序插入
	public void insert( ArrayList<DownloadResourceEntity> list ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = 0; i < size; i++ )
		{
			DownloadResourceEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME +
					" (" +
					DatabaseConstants.DOWNLOAD_RESOURCE_CHAPTER_COUNT + "," +
					DatabaseConstants.DOWNLOAD_RESOURCE_RESTYPE + "," +
					DatabaseConstants.DOWNLOAD_RESOURCE_STATUS + "," +
					DatabaseConstants.DOWNLOAD_RESOURCE_USERNAME + "," +
					DatabaseConstants.DOWNLOAD_RESOURCE_TITLE + "," +
					DatabaseConstants.DOWNLOAD_RESOURCE_DBCODE + "," +
					DatabaseConstants.DOWNLOAD_RESOURCE_SYSID + "," +
					DatabaseConstants.DOWNLOAD_RESOURCE_IDENTIFIER + "," +
					DatabaseConstants.DOWNLOAD_RESOURCE_FULLNAME + ") values (?,?,?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{entity.chapterCount,entity.resType,entity.status,entity.userName,entity.title,entity.dbCode,entity.sysId,entity.identifier,entity.categoryFullName});
		}
		db.close();
	}
	
	//倒序插入
	public void insertDescending( ArrayList<DownloadResourceEntity> list ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = size-1; i >= 0; i-- )
		{
			DownloadResourceEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME +
					" (" +
					DatabaseConstants.DOWNLOAD_RESOURCE_CHAPTER_COUNT + "," +
					DatabaseConstants.DOWNLOAD_RESOURCE_RESTYPE + "," +
					DatabaseConstants.DOWNLOAD_RESOURCE_STATUS + "," +
					DatabaseConstants.DOWNLOAD_RESOURCE_USERNAME + "," +
					DatabaseConstants.DOWNLOAD_RESOURCE_TITLE + "," +
					DatabaseConstants.DOWNLOAD_RESOURCE_DBCODE + "," +
					DatabaseConstants.DOWNLOAD_RESOURCE_SYSID + "," +
					DatabaseConstants.DOWNLOAD_RESOURCE_IDENTIFIER + "," +
					DatabaseConstants.DOWNLOAD_RESOURCE_FULLNAME + ") values (?,?,?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{entity.chapterCount,entity.resType,entity.status,entity.userName,entity.title,entity.dbCode,entity.sysId,entity.identifier,entity.categoryFullName});
		}
		db.close();
	}
	
	//清除整个表数据
	public void clearTable()
	{
		String sql1 = "DELETE FROM " + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME +";";
		String sql2= "update sqlite_sequence set seq=0 where name='" + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME + "'";
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
		
		Cursor cursor = db.rawQuery("select count(*) from " + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME, null);
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
	public DownloadResourceEntity find( String userName, int resType, String bookId ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = null;
		if( LibraryConstant.LIBRARY_DATATYPE_EBOOK == resType )
		{
			cursor = db.rawQuery("select * from " + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME + " where " + DatabaseConstants.DOWNLOAD_RESOURCE_USERNAME + " = ? and " + DatabaseConstants.DOWNLOAD_RESOURCE_RESTYPE + " = ? and " + DatabaseConstants.DOWNLOAD_RESOURCE_IDENTIFIER + " = ?", new String[]{userName, resType+"", bookId});
		}
		else
		{
			cursor = db.rawQuery("select * from " + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME + " where " + DatabaseConstants.DOWNLOAD_RESOURCE_USERNAME + " = ? and " + DatabaseConstants.DOWNLOAD_RESOURCE_RESTYPE + " = ? and " + DatabaseConstants.DOWNLOAD_RESOURCE_SYSID + " = ?", new String[]{userName, resType+"", bookId});
		}
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		DownloadResourceEntity entity = null;
		
		while(cursor.moveToNext())
		{	
			entity = new DownloadResourceEntity();
			entity.chapterCount = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_CHAPTER_COUNT));
			entity.resType = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_RESTYPE));
			entity.status = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_STATUS));
			entity.userName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_USERNAME));		
			entity.title = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_TITLE));
			entity.dbCode = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_DBCODE));
			entity.sysId = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_SYSID));
			entity.identifier = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_IDENTIFIER));
			entity.categoryFullName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_FULLNAME));
			
			break;
		}
		
		if (!cursor.isClosed()) 
		{
			cursor.close();
		}
		db.close();
		
		return	entity;
	}
	
	//查找所有已完成数据
	public ArrayList<DownloadResourceEntity> findAllCompleted( String userName ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME + " where " + DatabaseConstants.DOWNLOAD_RESOURCE_USERNAME + " = ? and " + DatabaseConstants.DOWNLOAD_RESOURCE_STATUS + " = ?", new String[]{userName, "2"});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		ArrayList<DownloadResourceEntity> list = new ArrayList<DownloadResourceEntity>();
		while(cursor.moveToNext())
		{
			DownloadResourceEntity entity = new DownloadResourceEntity();
			entity.chapterCount = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_CHAPTER_COUNT));
			entity.resType = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_RESTYPE));
			entity.status = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_STATUS));
			entity.userName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_USERNAME));		
			entity.title = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_TITLE));
			entity.dbCode = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_DBCODE));
			entity.sysId = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_SYSID));
			entity.identifier = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_IDENTIFIER));
			entity.categoryFullName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_FULLNAME));
			
		    list.add(0,entity);	//逆序插入
		}
		
		if (!cursor.isClosed()) 
		{
			cursor.close();
		}
		db.close();
		
		return	list;
	}
	
	//查找所有未完成数据
	public ArrayList<DownloadResourceEntity> findAllUnCompleted( String userName ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME + " where " + DatabaseConstants.DOWNLOAD_RESOURCE_USERNAME + " = ? and " + DatabaseConstants.DOWNLOAD_RESOURCE_STATUS + " != ?", new String[]{userName, "2"});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		ArrayList<DownloadResourceEntity> list = new ArrayList<DownloadResourceEntity>();
		while(cursor.moveToNext())
		{
			DownloadResourceEntity entity = new DownloadResourceEntity();
			entity.chapterCount = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_CHAPTER_COUNT));
			entity.resType = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_RESTYPE));
			entity.status = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_STATUS));
			entity.userName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_USERNAME));		
			entity.title = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_TITLE));
			entity.dbCode = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_DBCODE));
			entity.sysId = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_SYSID));
			entity.identifier = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_IDENTIFIER));
			entity.categoryFullName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_RESOURCE_FULLNAME));
			
		    list.add(entity);
		}
		
		if (!cursor.isClosed()) 
		{
			cursor.close();
		}
		db.close();
		
		return	list;
	}
	
	//删除数据
	public void delete( String userName, int resType, String bookId )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		if( LibraryConstant.LIBRARY_DATATYPE_EBOOK == resType )
		{
			db.execSQL("delete from " + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME + " where " + DatabaseConstants.DOWNLOAD_RESOURCE_USERNAME + " = ? and " + DatabaseConstants.DOWNLOAD_RESOURCE_RESTYPE + " = ? and " + DatabaseConstants.DOWNLOAD_RESOURCE_IDENTIFIER + " = ?", new String[]{userName, resType+"", bookId});
		}
		else
		{
			db.execSQL("delete from " + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME + " where " + DatabaseConstants.DOWNLOAD_RESOURCE_USERNAME + " = ? and " + DatabaseConstants.DOWNLOAD_RESOURCE_RESTYPE + " = ? and " + DatabaseConstants.DOWNLOAD_RESOURCE_SYSID + " = ?", new String[]{userName, resType+"", bookId});
		}
		db.close();
	}
	
	//删除数据
	public void delete( DownloadResourceEntity entity )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		if( LibraryConstant.LIBRARY_DATATYPE_EBOOK == entity.resType )
		{
			db.execSQL("delete from " + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME + " where " + DatabaseConstants.DOWNLOAD_RESOURCE_USERNAME + " = ? and " + DatabaseConstants.DOWNLOAD_RESOURCE_RESTYPE + " = ? and " + DatabaseConstants.DOWNLOAD_RESOURCE_IDENTIFIER + " = ?", new String[]{entity.userName, entity.resType+"", entity.identifier});
		}
		else
		{
			db.execSQL("delete from " + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME + " where " + DatabaseConstants.DOWNLOAD_RESOURCE_USERNAME + " = ? and " + DatabaseConstants.DOWNLOAD_RESOURCE_RESTYPE + " = ? and " + DatabaseConstants.DOWNLOAD_RESOURCE_SYSID + " = ?", new String[]{entity.userName, entity.resType+"", entity.sysId});
		}
		db.close();
	}

	//删除所有已完成数据
	public void deleteAllCompleted( String username )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME + " where " + DatabaseConstants.DOWNLOAD_RESOURCE_USERNAME + " = ? and " + DatabaseConstants.DOWNLOAD_RESOURCE_STATUS + " = ?", new String[]{username, "2"});
		db.close();
	}

	//删除所有未完成数据
	public void deleteAllUnCompleted( String username )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME + " where " + DatabaseConstants.DOWNLOAD_RESOURCE_USERNAME + " = ? and " + DatabaseConstants.DOWNLOAD_RESOURCE_STATUS + " != ?", new String[]{username, "2"});
		db.close();
	}
		
	//删除所有的数据
	public void deleteAll() 
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.DOWNLOAD_RESOURCE_TABLE_NAME, null);
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
