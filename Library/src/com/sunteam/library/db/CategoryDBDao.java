package com.sunteam.library.db;

import java.util.ArrayList;

import com.sunteam.library.entity.CategoryInfoNodeEntity;
import com.sunteam.library.utils.DatabaseConstants;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class CategoryDBDao 
{
	private LibraryDBHelper mLibraryDBHelper = null;

	public CategoryDBDao( Context context, String name, CursorFactory factory, int version ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, factory, version );
	}
	
	public CategoryDBDao( Context context, String name ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public CategoryDBDao( Context context ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public void insert( CategoryInfoNodeEntity entity, int resourceType ) 
	{
		if( null == entity )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		String sql = 
				"insert into " + DatabaseConstants.CATEGORY_TABLE_NAME +
				" (" +
				DatabaseConstants.RESOURCE_TYPE + "," +
				DatabaseConstants.CATEGORY_FATHER + "," +
				DatabaseConstants.CATEGORY_SEQ + "," +
				DatabaseConstants.CATEGORY_LEVEL + "," +
				DatabaseConstants.CATEGORY_NAME + "," +
				DatabaseConstants.CATEGORY_CODE + "," +
				DatabaseConstants.CATEGORY_TYPE + ") values (?,?,?,?,?,?,?)";
		db.execSQL( sql, new Object[]{resourceType,entity.father,entity.seq,entity.level,entity.name,entity.code,entity.type});
		db.close();
	}

	//顺序插入
	public void insert( ArrayList<CategoryInfoNodeEntity> list, int resourceType ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = 0; i < size; i++ )
		{
			CategoryInfoNodeEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.CATEGORY_TABLE_NAME +
					" (" +
					DatabaseConstants.RESOURCE_TYPE + "," +
					DatabaseConstants.CATEGORY_FATHER + "," +
					DatabaseConstants.CATEGORY_SEQ + "," +
					DatabaseConstants.CATEGORY_LEVEL + "," +
					DatabaseConstants.CATEGORY_NAME + "," +
					DatabaseConstants.CATEGORY_CODE + "," +
					DatabaseConstants.CATEGORY_TYPE + ") values (?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{resourceType,entity.father,entity.seq,entity.level,entity.name,entity.code,entity.type});
		}
		db.close();
	}
	
	//倒序插入
	public void insertDescending( ArrayList<CategoryInfoNodeEntity> list, int resourceType ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = size-1; i >= 0; i-- )
		{
			CategoryInfoNodeEntity entity = list.get(i);
			String sql = 
					"insert into " + DatabaseConstants.CATEGORY_TABLE_NAME +
					" (" +
					DatabaseConstants.RESOURCE_TYPE + "," +
					DatabaseConstants.CATEGORY_FATHER + "," +
					DatabaseConstants.CATEGORY_SEQ + "," +
					DatabaseConstants.CATEGORY_LEVEL + "," +
					DatabaseConstants.CATEGORY_NAME + "," +
					DatabaseConstants.CATEGORY_CODE + "," +
					DatabaseConstants.CATEGORY_TYPE + ") values (?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{resourceType,entity.father,entity.seq,entity.level,entity.name,entity.code,entity.type});
		}
		db.close();
	}
	
	//清除整个表数据
	public void clearTable()
	{
		String sql1 = "DELETE FROM " + DatabaseConstants.CATEGORY_TABLE_NAME +";";
		String sql2= "update sqlite_sequence set seq=0 where name='" + DatabaseConstants.CATEGORY_TABLE_NAME + "'";
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
		
		Cursor cursor = db.rawQuery("select count(*) from " + DatabaseConstants.CATEGORY_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ?", new String[]{resourceType+""});
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
	public ArrayList<CategoryInfoNodeEntity> findAll( int resourceType ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.CATEGORY_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ?", new String[]{resourceType+""});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		ArrayList<CategoryInfoNodeEntity> list = new ArrayList<CategoryInfoNodeEntity>();
		while(cursor.moveToNext())
		{
			CategoryInfoNodeEntity entity = new CategoryInfoNodeEntity();
			entity.father = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.CATEGORY_FATHER));
			entity.seq = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.CATEGORY_SEQ));
			entity.level = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.CATEGORY_LEVEL));
			entity.name = cursor.getString(cursor.getColumnIndex(DatabaseConstants.CATEGORY_NAME));
			entity.code = cursor.getString(cursor.getColumnIndex(DatabaseConstants.CATEGORY_CODE));
			entity.type = cursor.getString(cursor.getColumnIndex(DatabaseConstants.CATEGORY_TYPE));
			
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
		db.execSQL("delete from " + DatabaseConstants.CATEGORY_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ?", new String[]{resourceType+""});
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
