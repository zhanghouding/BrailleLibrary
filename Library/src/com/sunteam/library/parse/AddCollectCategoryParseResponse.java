package com.sunteam.library.parse;

import org.json.JSONObject;

import com.sunteam.library.entity.CollectCategoryEntity;
import com.sunteam.library.utils.LogUtils;

//添加收藏分类
public class AddCollectCategoryParseResponse extends AbsParseResponse 
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
			
			CollectCategoryEntity entity = new CollectCategoryEntity();
			entity.id = json.optInt("Id");
	        entity.userName = json.optString("UserName");
	        entity.categoryName = json.optString("CategoryName");
	        entity.categoryCode = json.optString("CategoryCode");
	        entity.categoryFullName = json.optString("CategoryFullName");
	        entity.resType = json.optInt("ResType");
	        
	        return	entity;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
