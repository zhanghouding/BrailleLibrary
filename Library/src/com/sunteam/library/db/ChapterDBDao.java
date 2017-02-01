package com.sunteam.library.db;

import java.io.Serializable;
import java.util.ArrayList;

import com.sunteam.library.entity.AudioChapterInfoEntity;
import com.sunteam.library.entity.EbookChapterInfoEntity;
import com.sunteam.library.entity.VideoChapterInfoEntity;
import com.sunteam.library.utils.DatabaseConstants;
import com.sunteam.library.utils.LibraryConstant;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ChapterDBDao 
{
	private LibraryDBHelper mLibraryDBHelper = null;

	public ChapterDBDao( Context context, String name, CursorFactory factory, int version ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, factory, version );
	}
	
	public ChapterDBDao( Context context, String name ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, name, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public ChapterDBDao( Context context ) 
	{
		mLibraryDBHelper = new LibraryDBHelper( context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION );
	}
		
	//顺序插入电子书章节
	public void insertEbookChapterInfo( ArrayList<EbookChapterInfoEntity> list, String dbCode, String identifier ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = 0; i < size; i++ )
		{
			EbookChapterInfoEntity entity = list.get(i);
			Chapter chapter = new Chapter();
			chapter.father = entity.father;
			chapter.seq = entity.seq;
			chapter.level = entity.level;
			chapter.dbCode = dbCode;
			chapter.identifier = identifier;
			chapter.index = entity.chapterIndex;
			chapter.name = entity.chapterName;
			
			String sql = 
					"insert into " + DatabaseConstants.CHAPTER_TABLE_NAME +
					" (" +
					DatabaseConstants.RESOURCE_TYPE + "," +
					DatabaseConstants.CHAPTER_FATHER + "," +
					DatabaseConstants.CHAPTER_SEQ + "," +
					DatabaseConstants.CHAPTER_LEVEL + "," +
					DatabaseConstants.CHAPTER_DBCODE + "," +
					DatabaseConstants.CHAPTER_SYSID + "," +
					DatabaseConstants.CHAPTER_IDENTIFIER + "," +
					DatabaseConstants.CHAPTER_INDEX + "," +
					DatabaseConstants.CHAPTER_NAME + "," +
					DatabaseConstants.CHAPTER_URL + ") values (?,?,?,?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{LibraryConstant.LIBRARY_DATATYPE_EBOOK,chapter.father,chapter.seq,chapter.level,chapter.dbCode,chapter.sysId,chapter.identifier,chapter.index,chapter.name,chapter.url});
		}
		db.close();
	}
	
	//顺序插入有声书章节
	public void insertAudioChapterInfo( ArrayList<AudioChapterInfoEntity> list, String dbCode, String sysId ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = 0; i < size; i++ )
		{
			AudioChapterInfoEntity entity = list.get(i);
			Chapter chapter = new Chapter();
			chapter.father = entity.father;
			chapter.seq = entity.seq;
			chapter.level = entity.level;
			chapter.dbCode = dbCode;
			chapter.sysId = sysId;
			chapter.name = entity.title;
			chapter.url = entity.audioUrl;
			
			String sql = 
					"insert into " + DatabaseConstants.CHAPTER_TABLE_NAME +
					" (" +
					DatabaseConstants.RESOURCE_TYPE + "," +
					DatabaseConstants.CHAPTER_FATHER + "," +
					DatabaseConstants.CHAPTER_SEQ + "," +
					DatabaseConstants.CHAPTER_LEVEL + "," +
					DatabaseConstants.CHAPTER_DBCODE + "," +
					DatabaseConstants.CHAPTER_SYSID + "," +
					DatabaseConstants.CHAPTER_IDENTIFIER + "," +
					DatabaseConstants.CHAPTER_INDEX + "," +
					DatabaseConstants.CHAPTER_NAME + "," +
					DatabaseConstants.CHAPTER_URL + ") values (?,?,?,?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{LibraryConstant.LIBRARY_DATATYPE_AUDIO,chapter.father,chapter.seq,chapter.level,chapter.dbCode,chapter.sysId,chapter.identifier,chapter.index,chapter.name,chapter.url});
		}
		db.close();
	}
	
	//顺序插入口述影像章节
	public void insertVideoChapterInfo( ArrayList<VideoChapterInfoEntity> list, String dbCode, String sysId ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = mLibraryDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = 0; i < size; i++ )
		{
			VideoChapterInfoEntity entity = list.get(i);
			Chapter chapter = new Chapter();
			chapter.father = entity.father;
			chapter.seq = entity.seq;
			chapter.level = entity.level;
			chapter.dbCode = dbCode;
			chapter.sysId = sysId;
			chapter.name = entity.title;
			chapter.url = entity.videoUrl;
			
			String sql = 
					"insert into " + DatabaseConstants.CHAPTER_TABLE_NAME +
					" (" +
					DatabaseConstants.RESOURCE_TYPE + "," +
					DatabaseConstants.CHAPTER_FATHER + "," +
					DatabaseConstants.CHAPTER_SEQ + "," +
					DatabaseConstants.CHAPTER_LEVEL + "," +
					DatabaseConstants.CHAPTER_DBCODE + "," +
					DatabaseConstants.CHAPTER_SYSID + "," +
					DatabaseConstants.CHAPTER_IDENTIFIER + "," +
					DatabaseConstants.CHAPTER_INDEX + "," +
					DatabaseConstants.CHAPTER_NAME + "," +
					DatabaseConstants.CHAPTER_URL + ") values (?,?,?,?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{LibraryConstant.LIBRARY_DATATYPE_VIDEO,chapter.father,chapter.seq,chapter.level,chapter.dbCode,chapter.sysId,chapter.identifier,chapter.index,chapter.name,chapter.url});
		}
		db.close();
	}

	//清除整个表数据
	public void clearTable()
	{
		String sql1 = "DELETE FROM " + DatabaseConstants.CHAPTER_TABLE_NAME +";";
		String sql2= "update sqlite_sequence set seq=0 where name='" + DatabaseConstants.CHAPTER_TABLE_NAME + "'";
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
		
		Cursor cursor = db.rawQuery("select count(*) from " + DatabaseConstants.CHAPTER_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ?", new String[]{resourceType+""});
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

	//查找所有资源类型为电子书的数据
	public ArrayList<EbookChapterInfoEntity> findAllEbookChapter( String dbCode, String identifier) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.CHAPTER_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ? and " + DatabaseConstants.CHAPTER_DBCODE + " = ? and " + DatabaseConstants.CHAPTER_IDENTIFIER + " = ?", new String[]{"0", dbCode, identifier});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		ArrayList<EbookChapterInfoEntity> list = new ArrayList<EbookChapterInfoEntity>();
		while(cursor.moveToNext())
		{
			EbookChapterInfoEntity entity = new EbookChapterInfoEntity();
			entity.father = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.CHAPTER_FATHER));
			entity.seq = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.CHAPTER_SEQ));
			entity.level = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.CHAPTER_LEVEL));
			entity.chapterIndex = cursor.getString(cursor.getColumnIndex(DatabaseConstants.CHAPTER_INDEX));
			entity.chapterName = cursor.getString(cursor.getColumnIndex(DatabaseConstants.CHAPTER_NAME));
			
		    list.add(entity);
		}
		
		if (!cursor.isClosed()) 
		{
			cursor.close();
		}
		db.close();
		
		return	list;
	}

	//查找所有资源类型为有声书的数据
	public ArrayList<AudioChapterInfoEntity> findAllAudioChapter( String dbCode, String sysId ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.CHAPTER_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ? and " + DatabaseConstants.CHAPTER_DBCODE + " = ? and " + DatabaseConstants.CHAPTER_SYSID + " = ?", new String[]{"1", dbCode, sysId});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		ArrayList<AudioChapterInfoEntity> list = new ArrayList<AudioChapterInfoEntity>();
		while(cursor.moveToNext())
		{
			AudioChapterInfoEntity entity = new AudioChapterInfoEntity();
			entity.father = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.CHAPTER_FATHER));
			entity.seq = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.CHAPTER_SEQ));
			entity.level = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.CHAPTER_LEVEL));
			entity.title = cursor.getString(cursor.getColumnIndex(DatabaseConstants.CHAPTER_NAME));
			entity.audioUrl = cursor.getString(cursor.getColumnIndex(DatabaseConstants.CHAPTER_URL));
			
		    list.add(entity);
		}
		
		if (!cursor.isClosed()) 
		{
			cursor.close();
		}
		db.close();
		
		return	list;
	}

	//查找所有资源类型为口述影像的数据
	public ArrayList<VideoChapterInfoEntity> findAllVideoChapter( String dbCode, String sysId ) 
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.CHAPTER_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ? and " + DatabaseConstants.CHAPTER_DBCODE + " = ? and " + DatabaseConstants.CHAPTER_SYSID + " = ?", new String[]{"2", dbCode, sysId});
		if( null == cursor )
		{
			db.close();
			return	null;
		}
		
		ArrayList<VideoChapterInfoEntity> list = new ArrayList<VideoChapterInfoEntity>();
		while(cursor.moveToNext())
		{
			VideoChapterInfoEntity entity = new VideoChapterInfoEntity();
			entity.father = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.CHAPTER_FATHER));
			entity.seq = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.CHAPTER_SEQ));
			entity.level = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.CHAPTER_LEVEL));
			entity.title = cursor.getString(cursor.getColumnIndex(DatabaseConstants.CHAPTER_NAME));
			entity.videoUrl = cursor.getString(cursor.getColumnIndex(DatabaseConstants.CHAPTER_URL));
			
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
		db.execSQL("delete from " + DatabaseConstants.CHAPTER_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ?", new String[]{resourceType+""});
		db.close();
	}

	//删除指定的电子书
	public void deleteAllEbookChapter( String dbCode, String identifier)
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		db.execSQL("delete from " + DatabaseConstants.CHAPTER_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ? and " + DatabaseConstants.CHAPTER_DBCODE + " = ? and " + DatabaseConstants.CHAPTER_IDENTIFIER + " = ?", new String[]{"0", dbCode, identifier});
		db.close();
	}

	//删除指定的有声书
	public void deleteAllAudioChapter( String dbCode, String sysId)
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		db.execSQL("delete from " + DatabaseConstants.CHAPTER_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ? and " + DatabaseConstants.CHAPTER_DBCODE + " = ? and " + DatabaseConstants.CHAPTER_SYSID + " = ?", new String[]{"1", dbCode, sysId});
		db.close();
	}

	//删除指定的口述影像
	public void deleteAllVideoChapter( String dbCode, String sysId)
	{
		SQLiteDatabase db = mLibraryDBHelper.getReadableDatabase();
		db.execSQL("delete from " + DatabaseConstants.CHAPTER_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ? and " + DatabaseConstants.CHAPTER_DBCODE + " = ? and " + DatabaseConstants.CHAPTER_SYSID + " = ?", new String[]{"2", dbCode, sysId});
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
    
    class Chapter implements Serializable
    {
    	/**
		 * 
		 */
		private static final long serialVersionUID = -6504884913344086743L;
		
		public int father;			//父节点序号
    	public int seq;				//节点序号
    	public int level;			//节点等级
    	public String dbCode;		//数据编码
    	public String sysId;		//系统ID
    	public String identifier;	//电子书ID
    	public String index;		//章节索引
    	public String name;			//章节名称
    	public String url;			//url
    }
}
