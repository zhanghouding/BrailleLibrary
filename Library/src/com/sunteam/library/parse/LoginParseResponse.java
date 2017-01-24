package com.sunteam.library.parse;

import org.json.JSONObject;

import com.sunteam.library.utils.LogUtils;

//解析登录返回
public class LoginParseResponse extends AbsParseResponse 
{
	public static final String TAG = "LoginParseResponse";
	
	@Override
	public Object parseResponse(String responseStr) throws Exception 
	{
		// TODO Auto-generated method stub
		LogUtils.e(TAG,"responseStr---" + responseStr);
		
		try
		{
			JSONObject jsonObject = new JSONObject(responseStr);
			Boolean result = (jsonObject.optInt("CheckState") == 1) ? true : false;
			
			return	result;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return false;
	}
}
