package com.sunteam.library.db;

import java.io.Serializable;
import java.util.ArrayList;

import com.sunteam.library.entity.AudioChapterInfoEntity;
import com.sunteam.library.entity.EbookChapterInfoEntity;
import com.sunteam.library.entity.VideoChapterInfoEntity;
import com.sunteam.library.utils.DatabaseConstants;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ChapterDBDao 
{
	private ChapterDBHelper ChapterDBHelper = null;

	public ChapterDBDao( Context context, String name, CursorFactory factory, int version ) 
	{
		ChapterDBHelper = new ChapterDBHelper( context, name, factory, version );
	}
	
	public ChapterDBDao( Context context, String name ) 
	{
		ChapterDBHelper = new ChapterDBHelper( context, name, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public ChapterDBDao( Context context ) 
	{
		ChapterDBHelper = new ChapterDBHelper( context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION );
	}
	
	public void insert( Object obj, int resourceType ) 
	{
		if( null == obj )
		{
			return;
		}
		
		Chapter chapter = new Chapter();
		switch( resourceType )
		{
			case 0:
				{
					EbookChapterInfoEntity entity = (EbookChapterInfoEntity)obj;
					chapter.father = entity.father;
					chapter.seq = entity.seq;
					chapter.level = entity.level;
					chapter.index = entity.chapterIndex;
					chapter.name = entity.chapterName;
				}
				break;
			case 1:
				{
					AudioChapterInfoEntity entity = (AudioChapterInfoEntity)obj;
					chapter.father = entity.father;
					chapter.seq = entity.seq;
					chapter.level = entity.level;
					chapter.name = entity.title;
					chapter.url = entity.audioUrl;
				}
				break;
			case 2:
				{
					VideoChapterInfoEntity entity = (VideoChapterInfoEntity)obj;
					chapter.father = entity.father;
					chapter.seq = entity.seq;
					chapter.level = entity.level;
					chapter.name = entity.title;
					chapter.url = entity.videoUrl;
				}
				break;
			default:
				return;
		}
		
		SQLiteDatabase db = ChapterDBHelper.getWritableDatabase();
		String sql = 
				"insert into " + DatabaseConstants.CHAPTER_TABLE_NAME +
				" (" +
				DatabaseConstants.RESOURCE_TYPE + "," +
				DatabaseConstants.CHAPTER_FATHER + "," +
				DatabaseConstants.CHAPTER_SEQ + "," +
				DatabaseConstants.CHAPTER_LEVEL + "," +
				DatabaseConstants.CHAPTER_INDEX + "," +
				DatabaseConstants.CHAPTER_NAME + "," +
				DatabaseConstants.CHAPTER_URL + ") values (?,?,?,?,?,?,?)";
		db.execSQL( sql, new Object[]{resourceType,chapter.father,chapter.seq,chapter.level,chapter.index,chapter.name,chapter.url});
		db.close();
	}

	//顺序插入
	public void insert( ArrayList<Object> list, int resourceType ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = ChapterDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = 0; i < size; i++ )
		{
			Object obj = list.get(i);
			Chapter chapter = new Chapter();
			switch( resourceType )
			{
				case 0:
					{
						EbookChapterInfoEntity entity = (EbookChapterInfoEntity)obj;
						chapter.father = entity.father;
						chapter.seq = entity.seq;
						chapter.level = entity.level;
						chapter.index = entity.chapterIndex;
						chapter.name = entity.chapterName;
					}
					break;
				case 1:
					{
						AudioChapterInfoEntity entity = (AudioChapterInfoEntity)obj;
						chapter.father = entity.father;
						chapter.seq = entity.seq;
						chapter.level = entity.level;
						chapter.name = entity.title;
						chapter.url = entity.audioUrl;
					}
					break;
				case 2:
					{
						VideoChapterInfoEntity entity = (VideoChapterInfoEntity)obj;
						chapter.father = entity.father;
						chapter.seq = entity.seq;
						chapter.level = entity.level;
						chapter.name = entity.title;
						chapter.url = entity.videoUrl;
					}
					break;
				default:
					continue;
			}
			String sql = 
					"insert into " + DatabaseConstants.CHAPTER_TABLE_NAME +
					" (" +
					DatabaseConstants.RESOURCE_TYPE + "," +
					DatabaseConstants.CHAPTER_FATHER + "," +
					DatabaseConstants.CHAPTER_SEQ + "," +
					DatabaseConstants.CHAPTER_LEVEL + "," +
					DatabaseConstants.CHAPTER_INDEX + "," +
					DatabaseConstants.CHAPTER_NAME + "," +
					DatabaseConstants.CHAPTER_URL + ") values (?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{resourceType,chapter.father,chapter.seq,chapter.level,chapter.index,chapter.name,chapter.url});
		}
		db.close();
	}
	
	//倒序插入
	public void insertDescending( ArrayList<Object> list, int resourceType ) 
	{
		if( ( null == list ) || ( list.size() == 0 ) )
		{
			return;
		}
		
		SQLiteDatabase db = ChapterDBHelper.getWritableDatabase();
		
		int size = list.size();
		for( int i = size-1; i >= 0; i-- )
		{
			Object obj = list.get(i);
			Chapter chapter = new Chapter();
			switch( resourceType )
			{
				case 0:
					{
						EbookChapterInfoEntity entity = (EbookChapterInfoEntity)obj;
						chapter.father = entity.father;
						chapter.seq = entity.seq;
						chapter.level = entity.level;
						chapter.index = entity.chapterIndex;
						chapter.name = entity.chapterName;
					}
					break;
				case 1:
					{
						AudioChapterInfoEntity entity = (AudioChapterInfoEntity)obj;
						chapter.father = entity.father;
						chapter.seq = entity.seq;
						chapter.level = entity.level;
						chapter.name = entity.title;
						chapter.url = entity.audioUrl;
					}
					break;
				case 2:
					{
						VideoChapterInfoEntity entity = (VideoChapterInfoEntity)obj;
						chapter.father = entity.father;
						chapter.seq = entity.seq;
						chapter.level = entity.level;
						chapter.name = entity.title;
						chapter.url = entity.videoUrl;
					}
					break;
				default:
					continue;
			}
			String sql = 
					"insert into " + DatabaseConstants.CHAPTER_TABLE_NAME +
					" (" +
					DatabaseConstants.RESOURCE_TYPE + "," +
					DatabaseConstants.CHAPTER_FATHER + "," +
					DatabaseConstants.CHAPTER_SEQ + "," +
					DatabaseConstants.CHAPTER_LEVEL + "," +
					DatabaseConstants.CHAPTER_INDEX + "," +
					DatabaseConstants.CHAPTER_NAME + "," +
					DatabaseConstants.CHAPTER_URL + ") values (?,?,?,?,?,?,?)";
			db.execSQL( sql, new Object[]{resourceType,chapter.father,chapter.seq,chapter.level,chapter.index,chapter.name,chapter.url});
		}
		db.close();
	}
	
	//清除整个表数据
	public void clearTable()
	{
		String sql1 = "DELETE FROM " + DatabaseConstants.CHAPTER_TABLE_NAME +";";
		String sql2= "update sqlite_sequence set seq=0 where name='" + DatabaseConstants.CHAPTER_TABLE_NAME + "'";
		SQLiteDatabase db = ChapterDBHelper.getWritableDatabase();
		db.execSQL(sql1);
		db.execSQL(sql2);
		db.close();
	}

	//查找所有资源类型为resourceType的数据条数
	public long getCount( int resourceType ) 
	{
		SQLiteDatabase db = ChapterDBHelper.getReadableDatabase();
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
	public ArrayList<EbookChapterInfoEntity> findAllEbookChapter() 
	{
		SQLiteDatabase db = ChapterDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.CHAPTER_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ?", new String[]{"0"});
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
	public ArrayList<AudioChapterInfoEntity> findAllAudioChapter() 
	{
		SQLiteDatabase db = ChapterDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.CHAPTER_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ?", new String[]{"1"});
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
	public ArrayList<VideoChapterInfoEntity> findAllVideoChapter() 
	{
		SQLiteDatabase db = ChapterDBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseConstants.CHAPTER_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ?", new String[]{"2"});
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
		SQLiteDatabase db = ChapterDBHelper.getWritableDatabase();
		db.execSQL("delete from " + DatabaseConstants.CHAPTER_TABLE_NAME + " where " + DatabaseConstants.RESOURCE_TYPE + " = ?", new String[]{resourceType+""});
		db.close();
	}

	//关闭数据库
    public void closeDb() 
    {
    	if (ChapterDBHelper != null)
    	{
    		ChapterDBHelper.close();
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
    	public String index;		//章节索引
    	public String name;			//章节名称
    	public String url;			//url
    }
}
