package com.sunteam.library.parse;

import org.json.JSONObject;

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
			return	jsonObject.optBoolean("IsSuccess") ;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return false;
	}
}
