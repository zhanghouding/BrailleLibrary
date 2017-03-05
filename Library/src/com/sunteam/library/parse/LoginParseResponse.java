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
			boolean result = (jsonObject.optInt("CheckState") == 1) ? true : false;
			if( false == result )
			{
				return	null;
			}
			
			JSONObject json = jsonObject.optJSONObject("ResultObject");
			if( null == json )
			{
				return	null;
			}
			
			return	json.optString("Password");
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
