package com.sunteam.library.parse;

import org.json.JSONObject;

import com.sunteam.library.utils.LibraryConstant;
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
			if( jsonObject.optBoolean("IsSuccess") )
			{
				return	LibraryConstant.RESULT_SUCCESS;
			}
			
			return	LibraryConstant.RESULT_FAIL;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return	LibraryConstant.RESULT_EXCEPTION;
	}
}