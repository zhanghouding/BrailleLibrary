package com.sunteam.library.db;

import java.util.ArrayList;

import com.sunteam.library.entity.CollectResourceEntity;
import com.sunteam.library.utils.DatabaseConstants;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class CollectResourceDBDao 
{
	private LibraryDBHelper mLibraryDBHelper = null;

	public CollectResourceDBDao( Context context, String name, CursorFactory factory, int version ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, factory, version );
	}
	
	public CollectResourceDBDao( Context context, String name ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public CollectResourceDBDao( Context context ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, DatabaseConstants.LIBRARY_DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public void insert( CollectResourceEntity entity ) 
	{
		if( null == entity )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		String sql = 
				"insert into " + DatabaseConstants.COLLECT_RESOURCE_TABLE_NAME +
				" (" +
				DatabaseConstants.COLLECT_RESOURCE_ID + "," +
				DatabaseConstants.COLLECT_RESOURCE_RESTYPE + "," +
				DatabaseConstants.COLLECT_RESOURCE_USERNAME + "," +
				DatabaseConstants.COLLECT_RESOURCE_TITLE + "," +
				DatabaseConstants.COLLECT_RESOURCE_DBCODE + "," +
				DatabaseConstants.COLLECT_RESOURCE_SYSID + "," +
				DatabaseConstants.COLLECT_RESOURCE_FULLNAME + "," +
				DatabaseConstants.COLLECT_RESOURCE_COVERURL + "," +
				DatabaseConstants.COLLECT_RESOURCE_CREATETIME + ") values (?,?,?,?,?,?,?,?,?)";
		db.execSQL( sql, new Object[]{entity.id,entity.resType,entity.userName,entity.title,entity.dbCode,entity.sysId,entity.categoryFullName,entity.coverUrl,entity.createTime});
		db.close();
	}

	//顺序插入
	public void insert( ArrayList<CollectResourceEntity> list ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = 0; i < size; i++ )
		{
			CollectResourceEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.COLLECT_RESOURCE_TABLE_NAME +
					" (" +
					DatabaseConstants.COLLECT_RESOURCE_ID + "," +
					DatabaseConstants.COLLECT_RESOURCE_RESTYPE + "," +
					DatabaseConstants.COLLECT_RESOURCE_USERNAME + "," +
					DatabaseConstants.COLLECT_RESOURCE_TITLE + "," +
					DatabaseConstants.COLLECT_RESOURCE_DBCODE + "," +
					DatabaseConstants.COLLECT_RESOURCE_SYSID + "," +
					DatabaseConstants.COLLECT_RESOURCE_FULLNAME + "," +
					DatabaseConstants.COLLECT_RESOURCE_COVERURL + "," +
					DatabaseConstants.COLLECT_RESOURCE_CREATETIME + ") values (?,?,?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{entity.id,entity.resType,entity.userName,entity.title,entity.dbCode,entity.sysId,entity.categoryFullName,entity.coverUrl,entity.createTime});
		}
		db.close();
	}
	
	//倒序插入
	public void insertDescending( ArrayList<CollectResourceEntity> list ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = size-1; i >= 0; i-- )
		{
			CollectResourceEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.COLLECT_RESOURCE_TABLE_NAME +
					" (" +
					DatabaseConstants.COLLECT_RESOURCE_ID + "," +
					DatabaseConstants.COLLECT_RESOURCE_RESTYPE + "," +
					DatabaseConstants.COLLECT_RESOURCE_USERNAME + "," +
					DatabaseConstants.COLLECT_RESOURCE_TITLE + "," +
					DatabaseConstants.COLLECT_RESOURCE_DBCODE + "," +
					DatabaseConstants.COLLECT_RESOURCE_SYSID + "," +
					DatabaseConstants.COLLECT_RESOURCE_FULLNAME + "," +
					DatabaseConstants.COLLECT_RESOURCE_COVERURL + "," +
					DatabaseConstants.COLLECT_RESOURCE_CREATETIME + ") values (?,?,?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{entity.id,entity.resType,entity.userName,entity.title,entity.dbCode,entity.sysId,entity.categoryFullName,entity.coverUrl,entity.createTime});
		}
		db.close();
	}
	
	//清除整个表数据
	public void clearTable()
	{
		String sql1 = "DELETE FROM " + DatabaseConstants.COLLECT_RESOURCE_TABLE_NAME +";";
		String sql2= "update sqlite_sequence set seq=0 where name='" + DatabaseConstants.COLLECT_RESOURCE_TABLE_NAME + "'";
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
		
		Cursor cursor = db.rawQuery("select count(*) from " + DatabaseConstants.COLLECT_RESOURCE_TABLE_NAME, null);
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
	public CollectResourceEntity find( String userName, String dbCode, String sysId ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.COLLECT_RESOURCE_TABLE_NAME + " where " + DatabaseConstants.COLLECT_RESOURCE_USERNAME + " = ? and " + DatabaseConstants.COLLECT_RESOURCE_DBCODE + " = ? and " + DatabaseConstants.COLLECT_RESOURCE_SYSID + " = ?", new String[]{userName, dbCode, sysId});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		CollectResourceEntity entity = null;
		
		while(cursor.moveToNext())
		{	
			entity = new CollectResourceEntity();
			entity.id = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_ID));
			entity.resType = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_RESTYPE));
			entity.userName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_USERNAME));		
			entity.title = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_TITLE));
			entity.dbCode = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_DBCODE));
			entity.sysId = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_SYSID));
			entity.categoryFullName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_FULLNAME));
			entity.coverUrl = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_COVERURL));
			entity.createTime = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_CREATETIME));
			
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
	public ArrayList<CollectResourceEntity> findAll( String userName ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.COLLECT_RESOURCE_TABLE_NAME + " where " + DatabaseConstants.COLLECT_RESOURCE_USERNAME + " = ?", new String[]{userName});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		ArrayList<CollectResourceEntity> list = new ArrayList<CollectResourceEntity>();
		while(cursor.moveToNext())
		{
			CollectResourceEntity entity = new CollectResourceEntity();
			entity.id = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_ID));
			entity.resType = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_RESTYPE));
			entity.userName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_USERNAME));		
			entity.title = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_TITLE));
			entity.dbCode = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_DBCODE));
			entity.sysId = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_SYSID));
			entity.categoryFullName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_FULLNAME));
			entity.coverUrl = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_COVERURL));
			entity.createTime = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLLECT_RESOURCE_CREATETIME));
			
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
	public void delete( String userName, String dbCode, String sysId )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.COLLECT_RESOURCE_TABLE_NAME + " where " + DatabaseConstants.COLLECT_RESOURCE_USERNAME + " = ? and " + DatabaseConstants.COLLECT_RESOURCE_DBCODE + " = ? and " + DatabaseConstants.COLLECT_RESOURCE_SYSID + " = ?", new String[]{userName, dbCode, sysId});
		db.close();
	}
	
	//删除数据
	public void delete( CollectResourceEntity entity )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.COLLECT_RESOURCE_TABLE_NAME + " where " + DatabaseConstants.COLLECT_RESOURCE_USERNAME + " = ? and " + DatabaseConstants.COLLECT_RESOURCE_DBCODE + " = ? and " + DatabaseConstants.COLLECT_RESOURCE_SYSID + " = ?", new String[]{entity.userName, entity.dbCode, entity.sysId});
		db.close();
	}

	//删除数据
	public void deleteAll( String username )
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.COLLECT_RESOURCE_TABLE_NAME + " where " + DatabaseConstants.COLLECT_RESOURCE_USERNAME + " = ?", new String[]{username});
		db.close();
	}
		
	//删除所有的数据
	public void deleteAll() 
	{
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.COLLECT_RESOURCE_TABLE_NAME, null);
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
