package com.sunteam.library.db;

import java.util.ArrayList;

import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.utils.DatabaseConstants;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class BookMarkDBDao 
{
	private LibraryDBHelper mLibraryDBHelper = null;

	public BookMarkDBDao( Context context, String name, CursorFactory factory, int version ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, factory, version );
	}
	
	public BookMarkDBDao( Context context, String name ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public BookMarkDBDao( Context context ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, DatabaseConstants.LIBRARY_DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public void insert( BookmarkEntity entity ) 
	{
		if( null == entity )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		String sql = 
				"insert into " + DatabaseConstants.BOOKMARK_TABLE_NAME +
				" (" +
				DatabaseConstants.BOOKMARK_ID + "," +
				DatabaseConstants.BOOKMARK_BEGIN + "," +
				DatabaseConstants.BOOKMARK_CHAPTER_INDEX + "," +
				DatabaseConstants.BOOKMARK_USERNAME + "," +
				DatabaseConstants.BOOKMARK_BOOKID + "," +
				DatabaseConstants.BOOKMARK_ADDEDTIME + "," +
				DatabaseConstants.BOOKMARK_CHAPTER_TITLE + "," +
				DatabaseConstants.BOOKMARK_MARKNAME + "," +
				DatabaseConstants.BOOKMARK_PERCENT + ") values (?,?,?,?,?,?,?,?,?)";
		db.execSQL( sql, new Object[]{entity.id,entity.begin,entity.chapterIndex,entity.userName,entity.bookId,entity.addedTime,entity.chapterTitle,entity.markName,entity.percent});
		db.close();
	}

	//顺序插入
	public void insert( ArrayList<BookmarkEntity> list ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = 0; i < size; i++ )
		{
			BookmarkEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.BOOKMARK_TABLE_NAME +
					" (" +
					DatabaseConstants.BOOKMARK_ID + "," +
					DatabaseConstants.BOOKMARK_BEGIN + "," +
					DatabaseConstants.BOOKMARK_CHAPTER_INDEX + "," +
					DatabaseConstants.BOOKMARK_USERNAME + "," +
					DatabaseConstants.BOOKMARK_BOOKID + "," +
					DatabaseConstants.BOOKMARK_ADDEDTIME + "," +
					DatabaseConstants.BOOKMARK_CHAPTER_TITLE + "," +
					DatabaseConstants.BOOKMARK_MARKNAME + "," +
					DatabaseConstants.BOOKMARK_PERCENT + ") values (?,?,?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{entity.id,entity.begin,entity.chapterIndex,entity.userName,entity.bookId,entity.addedTime,entity.chapterTitle,entity.markName,entity.percent});
		}
		db.close();
	}
	
	//倒序插入
	public void insertDescending( ArrayList<BookmarkEntity> list ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = size-1; i >= 0; i-- )
		{
			BookmarkEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.BOOKMARK_TABLE_NAME +
					" (" +
					DatabaseConstants.BOOKMARK_ID + "," +
					DatabaseConstants.BOOKMARK_BEGIN + "," +
					DatabaseConstants.BOOKMARK_CHAPTER_INDEX + "," +
					DatabaseConstants.BOOKMARK_USERNAME + "," +
					DatabaseConstants.BOOKMARK_BOOKID + "," +
					DatabaseConstants.BOOKMARK_ADDEDTIME + "," +
					DatabaseConstants.BOOKMARK_CHAPTER_TITLE + "," +
					DatabaseConstants.BOOKMARK_MARKNAME + "," +
					DatabaseConstants.BOOKMARK_PERCENT + ") values (?,?,?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{entity.id,entity.begin,entity.chapterIndex,entity.userName,entity.bookId,entity.addedTime,entity.chapterTitle,entity.markName,entity.percent});
		}
		db.close();
	}
	
	//清除整个表数据
	public void clearTable()
	{
		String sql1 = "DELETE FROM " + DatabaseConstants.BOOKMARK_TABLE_NAME +";";
		String sql2= "update sqlite_sequence set seq=0 where name='" + DatabaseConstants.BOOKMARK_TABLE_NAME + "'";
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
		
		Cursor cursor = db.rawQuery("select count(*) from " + DatabaseConstants.BOOKMARK_TABLE_NAME, null);
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
	public BookmarkEntity find( String userName, String bookId ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.BOOKMARK_TABLE_NAME + " where " + DatabaseConstants.BOOKMARK_USERNAME + " = ? and " + DatabaseConstants.BOOKMARK_BOOKID + " = ?", new String[]{userName, bookId});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		BookmarkEntity entity = null;
		
		while(cursor.moveToNext())
		{	
			entity = new BookmarkEntity();
			entity.id = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_ID));
			entity.begin = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_BEGIN));
			entity.chapterIndex = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_CHAPTER_INDEX));
			entity.userName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_USERNAME));		
			entity.bookId = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_BOOKID));
			entity.addedTime = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_ADDEDTIME));
			entity.chapterTitle = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_CHAPTER_TITLE));
			entity.markName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_MARKNAME));
			entity.percent = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_PERCENT));
			
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
	public ArrayList<BookmarkEntity> findAll( String userName, String bookId ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.BOOKMARK_TABLE_NAME + " where " + DatabaseConstants.BOOKMARK_USERNAME + " = ? and " + DatabaseConstants.BOOKMARK_BOOKID + " = ?", new String[]{userName, bookId});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		ArrayList<BookmarkEntity> list = new ArrayList<BookmarkEntity>();
		while(cursor.moveToNext())
		{
			BookmarkEntity entity = new BookmarkEntity();
			entity.id = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_ID));
			entity.begin = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_BEGIN));
			entity.chapterIndex = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_CHAPTER_INDEX));
			entity.userName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_USERNAME));		
			entity.bookId = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_BOOKID));
			entity.addedTime = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_ADDEDTIME));
			entity.chapterTitle = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_CHAPTER_TITLE));
			entity.markName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_MARKNAME));
			entity.percent = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_PERCENT));
			
		    list.add(0,entity);	//逆序插入
		}
		
		if (!cursor.isClosed()) 
		{
			cursor.close();
		}
		db.close();
		
		return	list;
	}
	
	//查找所有数据
	public ArrayList<BookmarkEntity> findAll( String userName ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.BOOKMARK_TABLE_NAME + " where " + DatabaseConstants.BOOKMARK_USERNAME + " = ?", new String[]{userName});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		ArrayList<BookmarkEntity> list = new ArrayList<BookmarkEntity>();
		while(cursor.moveToNext())
		{
			BookmarkEntity entity = new BookmarkEntity();
			entity.id = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_ID));
			entity.begin = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_BEGIN));
			entity.chapterIndex = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_CHAPTER_INDEX));
			entity.userName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_USERNAME));		
			entity.bookId = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_BOOKID));
			entity.addedTime = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_ADDEDTIME));
			entity.chapterTitle = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_CHAPTER_TITLE));
			entity.markName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_MARKNAME));
			entity.percent = cursor.getString(cursor.getColumnIndex(DatabaseConstants.BOOKMARK_PERCENT));
			
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
	public void delete( String userName, String bookId, int begin )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.BOOKMARK_TABLE_NAME + " where " + DatabaseConstants.BOOKMARK_USERNAME + " = ? and " + DatabaseConstants.BOOKMARK_BOOKID + " = ? and " + DatabaseConstants.BOOKMARK_BEGIN + " = ?", new String[]{userName, bookId, begin+""});
		db.close();
	}
	
	//删除数据
	public void delete( BookmarkEntity entity )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.BOOKMARK_TABLE_NAME + " where " + DatabaseConstants.BOOKMARK_USERNAME + " = ? and " + DatabaseConstants.BOOKMARK_BOOKID + " = ? and " + DatabaseConstants.BOOKMARK_BEGIN + " = ?", new String[]{entity.userName, entity.bookId, entity.begin+""});
		db.close();
	}

	//删除数据
	public void deleteAll( String username )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.BOOKMARK_TABLE_NAME + " where " + DatabaseConstants.BOOKMARK_USERNAME + " = ?", new String[]{username});
		db.close();
	}
	
	//删除数据
	public void deleteAll( String userName, String bookId )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.BOOKMARK_TABLE_NAME + " where " + DatabaseConstants.BOOKMARK_USERNAME + " = ? and " + DatabaseConstants.BOOKMARK_BOOKID + " = ?", new String[]{userName, bookId});
		db.close();
	}
			
	//删除所有的数据
	public void deleteAll() 
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.BOOKMARK_TABLE_NAME, null);
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
