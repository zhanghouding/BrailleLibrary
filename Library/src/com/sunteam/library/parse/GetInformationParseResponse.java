package com.sunteam.library.parse;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sunteam.library.entity.InformationEntity;
import com.sunteam.library.utils.LogUtils;

//解析盲人咨询列表
public class GetInformationParseResponse extends AbsParseResponse 
{
	public static final String TAG = "GetInformationParseResponse";
	
	/**
	 * 方法(解析Html)
	 * 
	 * @param html
	 * @return
	 * @author wzp
	 * @Created 2017/01/24
	 */
	private String parseHtml( String html )
	{
		if( TextUtils.isEmpty(html) )
		{
			return	"";
		}
		
		String txtcontent = html.replaceAll("</?[^>]+>", ""); 			//剔出<html>的标签  
		txtcontent = txtcontent.replaceAll("&nbsp;", "");				//替换空格
        txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");	//去除字符串中的空格,回车,换行符,制表符  
        
        return txtcontent;
	}
		
	/**
	 * 方法(解析items)
	 * 
	 * @param jsonArray
	 * @param list
	 * @return
	 * @author wzp
	 * @Created 2017/01/24
	 */
	private void parseItems( JSONArray jsonArray, ArrayList<InformationEntity> list )
	{
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			
			InformationEntity entity = new InformationEntity();
			
			entity.title = obj.optString("Title");
			entity.date = obj.optString("PubTime");
			entity.content = parseHtml(obj.optString("Content"));
			
			list.add(entity);
		}
	}
	
	@Override
	public Object parseResponse(String responseStr) throws Exception 
	{
		// TODO Auto-generated method stub
		LogUtils.e(TAG,"responseStr---" + responseStr);
		
		try
		{
			JSONObject jsonObject = new JSONObject(responseStr);
			Boolean result = jsonObject.optBoolean("IsException") ;
			if( result )
			{
				return	null;
			}
			
			JSONArray jsonArray = jsonObject.optJSONArray("Items");
			if( (  null == jsonArray ) || ( 0 == jsonArray.length() ) )
			{
				return	null;
			}
			
			ArrayList<InformationEntity> list = new ArrayList<InformationEntity>();
			
			parseItems( jsonArray, list );
			
			return	list;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
