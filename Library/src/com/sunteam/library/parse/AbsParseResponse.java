package com.sunteam.library.parse;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 抽象处理器
 * @author wzp
 * @date 2017/01/24
 */
public abstract class AbsParseResponse implements IParseResponse 
{
	@Override
	public Object parseResponse(InputStream inputStream) throws Exception
	{
		Object reponseResult = null;
		try 
		{
			String responseStr = streamToString(inputStream);
			reponseResult = parseResponse(responseStr);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			throw e;
		}
		
		return reponseResult;
	}

	abstract public Object parseResponse(String responseStr) throws Exception;
	
	private String streamToString(InputStream inputStream) throws Exception
	{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuffer sb = new StringBuffer();
		String str = null;
		while ((str = bufferedReader.readLine()) != null) 
		{
			sb.append(str);
		}
		bufferedReader.close();
		
		return sb.toString();
	}
}
