package com.sunteam.library.parse;

import org.json.JSONObject;

import com.sunteam.library.entity.CollectResourceEntity;
import com.sunteam.library.utils.LogUtils;

//添加收藏资源
public class AddCollectResourceParseResponse extends AbsParseResponse 
{
	public static final String TAG = "AddCollectResourceParseResponse";

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
			
			CollectResourceEntity entity = new CollectResourceEntity();
			entity.id = json.optInt("Id");
	        entity.userName = json.optString("UserName");
	        entity.title = json.optString("Title");
	        entity.dbCode = json.optString("DbCode");
	        entity.sysId = json.optString("SysId");
	        entity.resType = json.optInt("ResType");
	        entity.categoryFullName = json.optString("CategoryFullName");
	        entity.coverUrl = json.optString("CoverUrl");
	        entity.createTime = json.optString("CreateTime");
	        
	        return	entity;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
