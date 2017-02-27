package com.sunteam.library.parse;

import org.json.JSONObject;

import com.sunteam.library.entity.HistoryEntity;
import com.sunteam.library.utils.LogUtils;

//添加阅读历史
public class AddHistoryParseResponse extends AbsParseResponse 
{
	public static final String TAG = "AddHistoryParseResponse";

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
			
			HistoryEntity entity = new HistoryEntity();
			entity.id = json.optInt("Id");
	        entity.userName = json.optString("UserName");
	        entity.title = json.optString("Title");
	        entity.dbCode = json.optString("DbCode");
	        entity.sysId = json.optString("SysId");
	        entity.resType = json.optInt("ResType");
	        entity.lastChapterIndex = json.optInt("LastChapterIndex");
	        entity.enterPoint = json.optString("EnterPoint");
	        entity.url = json.optString("Url");
	        entity.createTime = json.optString("CreateTime");
	        entity.updateTime = json.optString("UpdateTime");
	        entity.bookTitle = json.optString("BookTitle");
	        entity.coverUrl = json.optString("CoverUrl");
	        entity.percent = json.optString("Percent");
	        entity.categoryFullName = json.optString("CategoryFullName");
	        entity.categoryCode = json.optString("CategoryCode");
	        
	        return	entity;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
