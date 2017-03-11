package com.sunteam.library.parse;

import com.sunteam.library.utils.LogUtils;

//解析分类名称
public class GetCategoryNameParseResponse extends AbsParseResponse 
{
	public static final String TAG = "GetCategoryNameParseResponse";

	@Override
	public Object parseResponse(String responseStr) throws Exception 
	{
		// TODO Auto-generated method stub
		LogUtils.e(TAG,"responseStr---" + responseStr);
		
		return	responseStr;	
	}
}
