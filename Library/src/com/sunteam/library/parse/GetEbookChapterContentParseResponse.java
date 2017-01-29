package com.sunteam.library.parse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sunteam.library.utils.LogUtils;
import com.sunteam.library.utils.PublicUtils;

//解析电子图书章节内容
public class GetEbookChapterContentParseResponse extends AbsParseResponse 
{
	public static final String TAG = "GetEbookChapterContentParseResponse";
	
	/**
	 * 方法(解析ChapterList)
	 * 
	 * @param jsonArray
	 * @param list
	 * @return
	 * @author wzp
	 * @Created 2017/01/24
	 */
	private String parseChapterList( JSONArray jsonArray )
	{
		String content = "";
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			
			content += obj.optString("Content");
		}
		
		return	content;
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
	private String parseItems( JSONArray jsonArray )
	{
		String content = "";
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			JSONArray array = obj.optJSONArray("ChapterList");
			
			content += parseChapterList( array );
		}
		
		return	content;
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
			
			return	PublicUtils.parseHtml(parseItems( jsonArray ));
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
