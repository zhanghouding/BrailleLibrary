package com.sunteam.library.db;

import java.util.ArrayList;

import com.sunteam.library.entity.CategoryInfoNodeEntity;
import com.sunteam.library.entity.EbookNodeEntity;
import com.sunteam.library.utils.DatabaseConstants;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ResourceDBDao 
{
	private LibraryDBHelper mLibraryDBHelper = null;

	public ResourceDBDao( Context context, String name, CursorFactory factory, int version ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, factory, version );
	}
	
	public ResourceDBDao( Context context, String name ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public ResourceDBDao( Context context ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, DatabaseConstants.LIBRARY_DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public void insert( EbookNodeEntity entity, int resourceType ) 
	{
		if( null == entity )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		String sql = 
				"insert into " + DatabaseConstants.RESOURCE_TABLE_NAME +
				" (" +
				DatabaseConstants.RESOURCE_TYPE + "," +
				DatabaseConstants.RESOURCE_CATEGORYCODE + "," +
				DatabaseConstants.RESOURCE_DBCODE + "," +
				DatabaseConstants.RESOURCE_SYSID + "," +
				DatabaseConstants.RESOURCE_TITLE + "," +
				DatabaseConstants.RESOURCE_AUTHOR + "," +
				DatabaseConstants.RESOURCE_KEYWORDS + "," +
				DatabaseConstants.RESOURCE_ABS + "," +
				DatabaseConstants.RESOURCE_PUBLISH + "," +
				DatabaseConstants.RESOURCE_IDENTIFIER + ") values (?,?,?,?,?,?,?,?,?,?)";
		db.execSQL( sql, new Object[]{resourceType,entity.categoryCode,entity.dbCode,entity.sysId,entity.title,entity.author,entity.keyWords,entity.abs,entity.publish,entity.identifier});
		db.close();
	}
	
	//是否存在
	private boolean isExist( ArrayList<EbookNodeEntity> list, EbookNodeEntity entity )
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return	false;
		}
		
		int size = list.size();
		for( int i = 0; i < size; i++ )
		{
			EbookNodeEntity entityOld = list.get(i);
			if( ( entityOld.dbCode.equals(entity.dbCode) ) &&
				( entityOld.sysId.equals(entity.sysId) ) &&
				( entityOld.title.equals(entity.title) ) &&
				( entityOld.author.equals(entity.author) ) &&
				( entityOld.keyWords.equals(entity.keyWords) ) &&
				( entityOld.abs.equals(entity.abs) ) &&
				( entityOld.publish.equals(entity.publish) ) &&
				( entityOld.identifier.equals(entity.identifier) ) )
			{
				return	true;
			}
		}
		
		return	false;
	}

	//顺序插入(只插入新数据)
	public void insert( ArrayList<EbookNodeEntity> listOld, ArrayList<EbookNodeEntity> list, String categoryCode, int resourceType ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = 0; i < size; i++ )
		{
			EbookNodeEntity entity = list.get(i);
			if( isExist( listOld, entity ) )
			{
				continue;
			}	//如果数据库中已经存在此记录，则不再插入。
			
			String sql = 
					"insert into " + DatabaseConstants.RESOURCE_TABLE_NAME +
					" (" +
					DatabaseConstants.RESOURCE_TYPE + "," +
					DatabaseConstants.RESOURCE_CATEGORYCODE + "," +
					DatabaseConstants.RESOURCE_DBCODE + "," +
					DatabaseConstants.RESOURCE_SYSID + "," +
					DatabaseConstants.RESOURCE_TITLE + "," +
					DatabaseConstants.RESOURCE_AUTHOR + "," +
					DatabaseConstants.RESOURCE_KEYWORDS + "," +
					DatabaseConstants.RESOURCE_ABS + "," +
					DatabaseConstants.RESOURCE_PUBLISH + "," +
					DatabaseConstants.RESOURCE_IDENTIFIER + ") values (?,?,?,?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{resourceType,categoryCode,entity.dbCode,entity.sysId,entity.title,entity.author,entity.keyWords,entity.abs,entity.publish,entity.identifier});
		}
		db.close();
	}

	//顺序插入
	public void insert( ArrayList<EbookNodeEntity> list, String categoryCode, int resourceType ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = 0; i < size; i++ )
		{
			EbookNodeEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.RESOURCE_TABLE_NAME +
					" (" +
					DatabaseConstants.RESOURCE_TYPE + "," +
					DatabaseConstants.RESOURCE_CATEGORYCODE + "," +
					DatabaseConstants.RESOURCE_DBCODE + "," +
					DatabaseConstants.RESOURCE_SYSID + "," +
					DatabaseConstants.RESOURCE_TITLE + "," +
					DatabaseConstants.RESOURCE_AUTHOR + "," +
					DatabaseConstants.RESOURCE_KEYWORDS + "," +
					DatabaseConstants.RESOURCE_ABS + "," +
					DatabaseConstants.RESOURCE_PUBLISH + "," +
					DatabaseConstants.RESOURCE_IDENTIFIER + ") values (?,?,?,?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{resourceType,categoryCode,entity.dbCode,entity.sysId,entity.title,entity.author,entity.keyWords,entity.abs,entity.publish,entity.identifier});
		}
		db.close();
	}
	
	//倒序插入
	public void insertDescending( ArrayList<EbookNodeEntity> list, int resourceType ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = size-1; i >= 0; i-- )
		{
			EbookNodeEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.RESOURCE_TABLE_NAME +
					" (" +
					DatabaseConstants.RESOURCE_TYPE + "," +
					DatabaseConstants.RESOURCE_CATEGORYCODE + "," +
					DatabaseConstants.RESOURCE_DBCODE + "," +
					DatabaseConstants.RESOURCE_SYSID + "," +
					DatabaseConstants.RESOURCE_TITLE + "," +
					DatabaseConstants.RESOURCE_AUTHOR + "," +
					DatabaseConstants.RESOURCE_KEYWORDS + "," +
					DatabaseConstants.RESOURCE_ABS + "," +
					DatabaseConstants.RESOURCE_PUBLISH + "," +
					DatabaseConstants.RESOURCE_IDENTIFIER + ") values (?,?,?,?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{resourceType,entity.categoryCode,entity.dbCode,entity.sysId,entity.title,entity.author,entity.keyWords,entity.abs,entity.publish,entity.identifier});
		}
		db.close();
	}
	
	//清除整个表数据
	public void clearTable()
	{
		String sql1 = "DELETE FROM " + DatabaseConstants.RESOURCE_TABLE_NAME +";";
		String sql2= "update sqlite_sequence set seq=0 where name='" + DatabaseConstants.RESOURCE_TABLE_NAME + "'";
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL(sql1);
		db.execSQL(sql2);
		db.close();
	}

	//查找所有资源类型为resourceType的数据条数
	public long getCount( String categoryCode, int resourceType ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		if( null == db )
		{
			return	0;
		}
		
		Cursor cursor = db.rawQuery("select count(*) from " + DatabaseConstants.RESOURCE_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ? and " + DatabaseConstants.RESOURCE_CATEGORYCODE + " = ?", new String[]{resourceType+"", categoryCode});
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
	public ArrayList<EbookNodeEntity> findAll( String categoryCode, int resourceType ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.RESOURCE_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ? and " + DatabaseConstants.RESOURCE_CATEGORYCODE + " = ?", new String[]{resourceType+"", categoryCode});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		ArrayList<EbookNodeEntity> list = new ArrayList<EbookNodeEntity>();
		while(cursor.moveToNext())
		{
			EbookNodeEntity entity = new EbookNodeEntity();
			entity.categoryCode = cursor.getString(cursor.getColumnIndex(DatabaseConstants.RESOURCE_CATEGORYCODE));
			entity.dbCode = cursor.getString(cursor.getColumnIndex(DatabaseConstants.RESOURCE_DBCODE));
			entity.sysId = cursor.getString(cursor.getColumnIndex(DatabaseConstants.RESOURCE_SYSID));
			entity.title = cursor.getString(cursor.getColumnIndex(DatabaseConstants.RESOURCE_TITLE));
			entity.author = cursor.getString(cursor.getColumnIndex(DatabaseConstants.RESOURCE_AUTHOR));
			entity.keyWords = cursor.getString(cursor.getColumnIndex(DatabaseConstants.RESOURCE_KEYWORDS));
			entity.abs = cursor.getString(cursor.getColumnIndex(DatabaseConstants.RESOURCE_ABS));
			entity.publish = cursor.getString(cursor.getColumnIndex(DatabaseConstants.RESOURCE_PUBLISH));
			entity.identifier = cursor.getString(cursor.getColumnIndex(DatabaseConstants.RESOURCE_IDENTIFIER));
			
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
	public void deleteAll( String categoryCode, int resourceType ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.RESOURCE_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ? and " + DatabaseConstants.RESOURCE_CATEGORYCODE + " = ?", new String[]{resourceType+"", categoryCode});
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
