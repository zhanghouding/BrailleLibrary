package com.sunteam.library.net;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import com.sunteam.library.parse.IParseResponse;

/**
 * 使用Http方式请求云端API
 * 
 * @author wzp
 * @Created 2017/01/24
 */
public class HttpRequest
{
	private static final String API_URL = "http://www.blc.org.cn/API/";

	private static final int CONNECTIONTIMEOUT = 8000;
	private static final int SOTIMEOUT = 8000;

	/**
	 * get方式获取网络数据
	 * 
	 * @param uri
	 * @param params
	 * @param ihandler
	 * @return
	 * @author wzp
	 * @Created 2017/01/24
	 */
	public static Object get(String uri, Map<String, String> params, IParseResponse iParseResponse ) 
	{
		String parameter = initParamters(params); // 获取参数
		try 
		{
			HttpGet httpGet = new HttpGet(API_URL + uri + parameter);
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTIONTIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, SOTIMEOUT);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpResponse response = httpClient.execute(httpGet);
			if( null == response )
			{
				return	null;
			}
			
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
			{
				HttpEntity httpEntity = response.getEntity();
				if( null == httpEntity )
				{
					return	null;
				}
				InputStream ins = httpEntity.getContent();
				if( null == iParseResponse )
				{
					return	null;
				}
				Object resultMessage = iParseResponse.parseResponse(ins);
				
				return resultMessage;
			} 
			else 
			{
				httpGet.abort();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @TODO 签名参数字符串
	 * @param paramses
	 * @return
	 * @author wzp
	 * @Created 2017/01/24
	 */
	private static String initParamters(Map<String, String> paramses)
	{
		String parameter = "";
		int count = 1;
		for (Entry<String, String> p : paramses.entrySet()) 
		{
			if (count == 1) 
			{
				parameter += "?" + p.getKey() + "=" + p.getValue();
			} 
			else 
			{
				parameter += "&" + p.getKey() + "=" + p.getValue();
			}
			count++;
		}
		
		return parameter;
	}
}
