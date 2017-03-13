package com.sunteam.library.db;

import java.util.ArrayList;

import com.sunteam.library.entity.DownloadChapterEntity;
import com.sunteam.library.utils.DatabaseConstants;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DownloadChapterDBDao 
{
	private LibraryDBHelper mLibraryDBHelper = null;

	public DownloadChapterDBDao( Context context, String name, CursorFactory factory, int version ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, factory, version );
	}
	
	public DownloadChapterDBDao( Context context, String name ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public DownloadChapterDBDao( Context context ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, DatabaseConstants.LIBRARY_DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public void insert( DownloadChapterEntity entity ) 
	{
		if( null == entity )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		String sql = 
				"insert into " + DatabaseConstants.DOWNLOAD_CHAPTER_TABLE_NAME +
				" (" +
				DatabaseConstants.DOWNLOAD_CHAPTER_RECORDID + "," +
				DatabaseConstants.DOWNLOAD_CHAPTER_INDEX + "," +
				DatabaseConstants.DOWNLOAD_CHAPTER_STATUS + "," +
				DatabaseConstants.DOWNLOAD_CHAPTER_NAME + "," +
				DatabaseConstants.DOWNLOAD_CHAPTER_PATH + "," +
				DatabaseConstants.DOWNLOAD_CHAPTER_URL + ") values (?,?,?,?,?,?)";
		db.execSQL( sql, new Object[]{entity.recorcdId,entity.chapterIndex,entity.chapterStatus,entity.chapterName,entity.chapterPath,entity.chapterUrl});
		db.close();
	}

	//顺序插入
	public void insert( ArrayList<DownloadChapterEntity> list ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = 0; i < size; i++ )
		{
			DownloadChapterEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.DOWNLOAD_CHAPTER_TABLE_NAME +
					" (" +
					DatabaseConstants.DOWNLOAD_CHAPTER_RECORDID + "," +
					DatabaseConstants.DOWNLOAD_CHAPTER_INDEX + "," +
					DatabaseConstants.DOWNLOAD_CHAPTER_STATUS + "," +
					DatabaseConstants.DOWNLOAD_CHAPTER_NAME + "," +
					DatabaseConstants.DOWNLOAD_CHAPTER_PATH + "," +
					DatabaseConstants.DOWNLOAD_CHAPTER_URL + ") values (?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{entity.recorcdId,entity.chapterIndex,entity.chapterStatus,entity.chapterName,entity.chapterPath,entity.chapterUrl});
		}
		db.close();
	}
	
	//倒序插入
	public void insertDescending( ArrayList<DownloadChapterEntity> list ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = size-1; i >= 0; i-- )
		{
			DownloadChapterEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.DOWNLOAD_CHAPTER_TABLE_NAME +
					" (" +
					DatabaseConstants.DOWNLOAD_CHAPTER_RECORDID + "," +
					DatabaseConstants.DOWNLOAD_CHAPTER_INDEX + "," +
					DatabaseConstants.DOWNLOAD_CHAPTER_STATUS + "," +
					DatabaseConstants.DOWNLOAD_CHAPTER_NAME + "," +
					DatabaseConstants.DOWNLOAD_CHAPTER_PATH + "," +
					DatabaseConstants.DOWNLOAD_CHAPTER_URL + ") values (?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{entity.recorcdId,entity.chapterIndex,entity.chapterStatus,entity.chapterName,entity.chapterPath,entity.chapterUrl});
		}
		db.close();
	}
	
	//清除整个表数据
	public void clearTable()
	{
		String sql1 = "DELETE FROM " + DatabaseConstants.DOWNLOAD_CHAPTER_TABLE_NAME +";";
		String sql2= "update sqlite_sequence set seq=0 where name='" + DatabaseConstants.DOWNLOAD_CHAPTER_TABLE_NAME + "'";
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
		
		Cursor cursor = db.rawQuery("select count(*) from " + DatabaseConstants.DOWNLOAD_CHAPTER_TABLE_NAME, null);
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
	public DownloadChapterEntity find( int recorcdId, int chapterIndex ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.DOWNLOAD_CHAPTER_TABLE_NAME + " where " + DatabaseConstants.DOWNLOAD_CHAPTER_RECORDID + " = ? and " + DatabaseConstants.DOWNLOAD_CHAPTER_INDEX + " = ?", new String[]{recorcdId+"", chapterIndex+""});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		DownloadChapterEntity entity = null;
		while(cursor.moveToNext())
		{
			entity = new DownloadChapterEntity();
			entity.recorcdId = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_RECORDID));
			entity.chapterIndex = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_INDEX));
			entity.chapterStatus = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_STATUS));
			entity.chapterName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_NAME));		
			entity.chapterPath = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_PATH));
			entity.chapterUrl = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_URL));
			
		    break;
		}
		
		if (!cursor.isClosed()) 
		{
			cursor.close();
		}
		db.close();
		
		return	entity;
	}

	//查找数据
	public DownloadChapterEntity find( String url ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.DOWNLOAD_CHAPTER_TABLE_NAME + " where " + DatabaseConstants.DOWNLOAD_CHAPTER_URL + " = ?", new String[]{url});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		DownloadChapterEntity entity = null;
		while(cursor.moveToNext())
		{
			entity = new DownloadChapterEntity();
			entity.recorcdId = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_RECORDID));
			entity.chapterIndex = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_INDEX));
			entity.chapterStatus = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_STATUS));
			entity.chapterName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_NAME));		
			entity.chapterPath = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_PATH));
			entity.chapterUrl = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_URL));
			
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
	public ArrayList<DownloadChapterEntity> findAll( int recorcdId ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.DOWNLOAD_CHAPTER_TABLE_NAME + " where " + DatabaseConstants.DOWNLOAD_CHAPTER_RECORDID + " = ?", new String[]{recorcdId+""});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		ArrayList<DownloadChapterEntity> list = new ArrayList<DownloadChapterEntity>();
		while(cursor.moveToNext())
		{
			DownloadChapterEntity entity = new DownloadChapterEntity();
			entity.recorcdId = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_RECORDID));
			entity.chapterIndex = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_INDEX));
			entity.chapterStatus = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_STATUS));
			entity.chapterName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_NAME));		
			entity.chapterPath = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_PATH));
			entity.chapterUrl = cursor.getString(cursor.getColumnIndex(DatabaseConstants.DOWNLOAD_CHAPTER_URL));
			
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
	public void deleteAll( int recorcdId )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.DOWNLOAD_CHAPTER_TABLE_NAME + " where " + DatabaseConstants.DOWNLOAD_CHAPTER_RECORDID + " = ?", new String[]{recorcdId+""});
		db.close();
	}

	//删除所有的数据
	public void deleteAll() 
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.DOWNLOAD_CHAPTER_TABLE_NAME, null);
		db.close();
	}
	
	//更新状态
	public void update( int status, int recorcdId ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("update " + DatabaseConstants.DOWNLOAD_CHAPTER_TABLE_NAME + " set "+ DatabaseConstants.DOWNLOAD_CHAPTER_STATUS + "=? where " + DatabaseConstants.DOWNLOAD_CHAPTER_RECORDID + " = ?", new String[]{status+"", recorcdId+""});
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
