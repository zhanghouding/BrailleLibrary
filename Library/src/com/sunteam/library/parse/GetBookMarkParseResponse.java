package com.sunteam.library.parse;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.utils.LogUtils;

//解析书签列表
public class GetBookMarkParseResponse extends AbsParseResponse 
{
	public static final String TAG = "GetBookMarkParseResponse";
	
	/**
	 * 方法(解析items)
	 * 
	 * @param jsonArray
	 * @param list
	 * @return
	 * @author wzp
	 * @Created 2017/02/14
	 */
	private void parseItems( JSONArray jsonArray, ArrayList<BookmarkEntity> list )
	{
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			
			BookmarkEntity entity = new BookmarkEntity();
			
			entity.id = obj.optInt("Id");								//记录id
			entity.userName = obj.optString("UserName");				//用户名
			entity.bookId = obj.optString("BookId");					//书目ID
			entity.begin = obj.optInt("Begin");							//开始位置
		    entity.chapterIndex = obj.optInt("ChapterIndex");			//章节序号
		    entity.chapterTitle = obj.optString("ChapterTitle");		//章节标题
		    entity.markName = obj.optString("MarkName");				//书签名称
		    entity.percent = obj.optString("Percent");					//进度
		    entity.addedTime = obj.optString("AddedTime");				//创建时间
			
			list.add(entity);
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
			
			ArrayList<BookmarkEntity> list = new ArrayList<BookmarkEntity>();
			
			parseItems( jsonArray, list );
			
			return	list;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
