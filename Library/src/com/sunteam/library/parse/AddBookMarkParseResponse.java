package com.sunteam.library.parse;

import org.json.JSONObject;

import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.utils.LogUtils;

//添加书签
public class AddBookMarkParseResponse extends AbsParseResponse 
{
	public static final String TAG = "AddBookMarkParseResponse";

	@Override
	public Object parseResponse(String responseStr) throws Exception 
	{

		// TODO Auto-generated method stub
		LogUtils.e(TAG,"responseStr---" + responseStr);
		
		try
		{
			JSONObject jsonObject = new JSONObject(responseStr);
			boolean result = jsonObject.optBoolean("IsSuccess");
			
			if( !result )
			{
				return	null;
			}
			
			JSONObject json = jsonObject.optJSONObject("ResultObject");
			if( null == json )
			{
				return	null;
			}
			
			BookmarkEntity entity = new BookmarkEntity();
			entity.id = json.optInt("Id");
	        entity.userName = json.optString("UserName");
	        entity.bookId = json.optString("BookId");
	        entity.addedTime = json.optString("AddedTime");
	        entity.begin = json.optInt("Begin");
	        entity.chapterIndex = json.optInt("ChapterIndex");
	        entity.chapterTitle = json.optString("ChapterTitle");
	        entity.markName = json.optString("MarkName");
	        entity.percent = json.optString("Percent");
	        
	        return	entity;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
