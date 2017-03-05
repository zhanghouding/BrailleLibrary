package com.sunteam.library.db;

import java.util.ArrayList;

import com.sunteam.library.entity.HistoryEntity;
import com.sunteam.library.utils.DatabaseConstants;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class HistoryDBDao 
{
	private LibraryDBHelper mLibraryDBHelper = null;

	public HistoryDBDao( Context context, String name, CursorFactory factory, int version ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, factory, version );
	}
	
	public HistoryDBDao( Context context, String name ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public HistoryDBDao( Context context ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, DatabaseConstants.LIBRARY_DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public void insert( HistoryEntity entity ) 
	{
		if( null == entity )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		String sql = 
				"insert into " + DatabaseConstants.HISTORY_TABLE_NAME +
				" (" +
				DatabaseConstants.HISTORY_ID + "," +
				DatabaseConstants.HISTORY_RESTYPE + "," +
				DatabaseConstants.HISTORY_LCINDEX + "," +
				DatabaseConstants.HISTORY_USERNAME + "," +
				DatabaseConstants.HISTORY_TITLE + "," +
				DatabaseConstants.HISTORY_DBCODE + "," +
				DatabaseConstants.HISTORY_SYSID + "," +
				DatabaseConstants.HISTORY_ENTERPOINT + "," +
				DatabaseConstants.HISTORY_URL + "," +
				DatabaseConstants.HISTORY_CTIME + "," +
				DatabaseConstants.HISTORY_UTIME + "," +
				DatabaseConstants.HISTORY_BOOKTITLE + "," +
				DatabaseConstants.HISTORY_COVERURL + "," +
				DatabaseConstants.HISTORY_PERCENT + "," +
				DatabaseConstants.HISTORY_CFULLNAME + "," +
				DatabaseConstants.HISTORY_CATEGORYCODE + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		db.execSQL( sql, new Object[]{entity.id,entity.resType,entity.lastChapterIndex,entity.userName,entity.title,entity.dbCode,entity.sysId,
				entity.enterPoint,entity.url,entity.createTime,entity.updateTime,entity.bookTitle,entity.coverUrl,entity.percent,entity.categoryFullName,entity.categoryCode});
		db.close();
	}

	//顺序插入
	public void insert( ArrayList<HistoryEntity> list ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = 0; i < size; i++ )
		{
			HistoryEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.HISTORY_TABLE_NAME +
					" (" +
					DatabaseConstants.HISTORY_ID + "," +
					DatabaseConstants.HISTORY_RESTYPE + "," +
					DatabaseConstants.HISTORY_LCINDEX + "," +
					DatabaseConstants.HISTORY_USERNAME + "," +
					DatabaseConstants.HISTORY_TITLE + "," +
					DatabaseConstants.HISTORY_DBCODE + "," +
					DatabaseConstants.HISTORY_SYSID + "," +
					DatabaseConstants.HISTORY_ENTERPOINT + "," +
					DatabaseConstants.HISTORY_URL + "," +
					DatabaseConstants.HISTORY_CTIME + "," +
					DatabaseConstants.HISTORY_UTIME + "," +
					DatabaseConstants.HISTORY_BOOKTITLE + "," +
					DatabaseConstants.HISTORY_COVERURL + "," +
					DatabaseConstants.HISTORY_PERCENT + "," +
					DatabaseConstants.HISTORY_CFULLNAME + "," +
					DatabaseConstants.HISTORY_CATEGORYCODE + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{entity.id,entity.resType,entity.lastChapterIndex,entity.userName,entity.title,entity.dbCode,entity.sysId,
					entity.enterPoint,entity.url,entity.createTime,entity.updateTime,entity.bookTitle,entity.coverUrl,entity.percent,entity.categoryFullName,entity.categoryCode});
		}
		db.close();
	}
	
	//倒序插入
	public void insertDescending( ArrayList<HistoryEntity> list ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = size-1; i >= 0; i-- )
		{
			HistoryEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.HISTORY_TABLE_NAME +
					" (" +
					DatabaseConstants.HISTORY_ID + "," +
					DatabaseConstants.HISTORY_RESTYPE + "," +
					DatabaseConstants.HISTORY_LCINDEX + "," +
					DatabaseConstants.HISTORY_USERNAME + "," +
					DatabaseConstants.HISTORY_TITLE + "," +
					DatabaseConstants.HISTORY_DBCODE + "," +
					DatabaseConstants.HISTORY_SYSID + "," +
					DatabaseConstants.HISTORY_ENTERPOINT + "," +
					DatabaseConstants.HISTORY_URL + "," +
					DatabaseConstants.HISTORY_CTIME + "," +
					DatabaseConstants.HISTORY_UTIME + "," +
					DatabaseConstants.HISTORY_BOOKTITLE + "," +
					DatabaseConstants.HISTORY_COVERURL + "," +
					DatabaseConstants.HISTORY_PERCENT + "," +
					DatabaseConstants.HISTORY_CFULLNAME + "," +
					DatabaseConstants.HISTORY_CATEGORYCODE + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{entity.id,entity.resType,entity.lastChapterIndex,entity.userName,entity.title,entity.dbCode,entity.sysId,
					entity.enterPoint,entity.url,entity.createTime,entity.updateTime,entity.bookTitle,entity.coverUrl,entity.percent,entity.categoryFullName,entity.categoryCode});
		}
		db.close();
	}
	
	//清除整个表数据
	public void clearTable()
	{
		String sql1 = "DELETE FROM " + DatabaseConstants.HISTORY_TABLE_NAME +";";
		String sql2= "update sqlite_sequence set seq=0 where name='" + DatabaseConstants.HISTORY_TABLE_NAME + "'";
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
		
		Cursor cursor = db.rawQuery("select count(*) from " + DatabaseConstants.HISTORY_TABLE_NAME, null);
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

	//查找所有数据
	public ArrayList<HistoryEntity> findAll( String userName ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.HISTORY_TABLE_NAME + " where " + DatabaseConstants.HISTORY_USERNAME + " = ?", new String[]{userName});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		ArrayList<HistoryEntity> list = new ArrayList<HistoryEntity>();
		while(cursor.moveToNext())
		{
			HistoryEntity entity = new HistoryEntity();
			entity.id = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.HISTORY_ID));
			entity.resType = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.HISTORY_RESTYPE));
			entity.lastChapterIndex = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.HISTORY_LCINDEX));
			
			entity.userName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.HISTORY_USERNAME));
			entity.title = cursor.getString(cursor.getColumnIndex(DatabaseConstants.HISTORY_TITLE));
			entity.dbCode = cursor.getString(cursor.getColumnIndex(DatabaseConstants.HISTORY_DBCODE));
			entity.sysId = cursor.getString(cursor.getColumnIndex(DatabaseConstants.HISTORY_SYSID));
			entity.enterPoint = cursor.getString(cursor.getColumnIndex(DatabaseConstants.HISTORY_ENTERPOINT));
			entity.url = cursor.getString(cursor.getColumnIndex(DatabaseConstants.HISTORY_URL));
			entity.createTime = cursor.getString(cursor.getColumnIndex(DatabaseConstants.HISTORY_CTIME));
			entity.updateTime = cursor.getString(cursor.getColumnIndex(DatabaseConstants.HISTORY_UTIME));
			entity.bookTitle = cursor.getString(cursor.getColumnIndex(DatabaseConstants.HISTORY_BOOKTITLE));
			entity.coverUrl = cursor.getString(cursor.getColumnIndex(DatabaseConstants.HISTORY_COVERURL));
			entity.percent = cursor.getString(cursor.getColumnIndex(DatabaseConstants.HISTORY_PERCENT));
			entity.categoryFullName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.HISTORY_CFULLNAME));
			entity.categoryCode = cursor.getString(cursor.getColumnIndex(DatabaseConstants.HISTORY_CATEGORYCODE));
			
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
	public void delete( HistoryEntity entity )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.HISTORY_TABLE_NAME + " where " + DatabaseConstants.HISTORY_USERNAME + " = ? and " + DatabaseConstants.HISTORY_DBCODE + " = ? and " + DatabaseConstants.HISTORY_SYSID + " = ?", new String[]{entity.userName, entity.dbCode, entity.sysId});
		db.close();
	}

	//删除数据
	public void deleteAll( String username )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.HISTORY_TABLE_NAME + " where " + DatabaseConstants.HISTORY_USERNAME + " = ?", new String[]{username});
		db.close();
	}
		
	//删除所有的数据
	public void deleteAll() 
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.HISTORY_TABLE_NAME, null);
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
