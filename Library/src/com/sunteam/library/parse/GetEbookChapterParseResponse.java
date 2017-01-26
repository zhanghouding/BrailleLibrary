package com.sunteam.library.parse;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sunteam.library.entity.EbookChapterInfoEntity;
import com.sunteam.library.utils.LogUtils;

//解析电子图书章节列表
public class GetEbookChapterParseResponse extends AbsParseResponse 
{
	public static final String TAG = "GetEbookChapterParseResponse";
	
	/**
	 * 方法(解析ChapterList)
	 * 
	 * @param jsonArray
	 * @param list
	 * @return
	 * @author wzp
	 * @Created 2017/01/24
	 */
	private void parseChapterList( int father, int level, JSONArray jsonArray, ArrayList<EbookChapterInfoEntity> list )
	{
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			
			EbookChapterInfoEntity entity = new EbookChapterInfoEntity();
			
			entity.father = father;
			entity.level = level;
			entity.seq = list.size();
			
			entity.chapterName = obj.optString("ChapterName");
			entity.chapterIndex = obj.optString("ChapterIndex");
			entity.content = obj.optString("Content");
			
			list.add(entity);
			
			try
			{
				JSONArray array = obj.optJSONArray("ChapterList");
				if( (  null == array ) || ( 0 == array.length() ) )
				{
					continue;
				}
				
				parseChapterList( entity.seq, entity.level+1, array, list );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 方法(解析items)
	 * 
	 * @param jsonArray
	 * @param list
	 * @return
	 * @author wzp
	 * @Created 2017/01/24
	 */
	private void parseItems( int father, int level, JSONArray jsonArray, ArrayList<EbookChapterInfoEntity> list )
	{
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			JSONArray array = obj.optJSONArray("ChapterList");
			
			parseChapterList( father, level, array, list );
		}
	}
	
	@Override
	public Object parseResponse(String responseStr) throws Exception 
	{
		// TODO Auto-generated method stub
		LogUtils.e(TAG,"responseStr---" + responseStr);
		
		try
		{
			JSONObject jsonObject = new JSONObject(responseStr);
			Boolean result = jsonObject.optBoolean("IsException") ;
			if( result )
			{
				return	null;
			}
			
			JSONArray jsonArray = jsonObject.optJSONArray("Items");
			if( (  null == jsonArray ) || ( 0 == jsonArray.length() ) )
			{
				return	null;
			}
			
			ArrayList<EbookChapterInfoEntity> list = new ArrayList<EbookChapterInfoEntity>();
			
			parseItems( -1, 0, jsonArray, list );
			
			return	list;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
