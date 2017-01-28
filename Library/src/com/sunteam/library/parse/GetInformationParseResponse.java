package com.sunteam.library.parse;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sunteam.library.entity.InformationEntity;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.LogUtils;

//解析盲人咨询列表
public class GetInformationParseResponse extends AbsParseResponse 
{
	public static final String TAG = "GetInformationParseResponse";
	
	/**
	 * 方法(保存Content)
	 * 
	 * @param content
	 * @return
	 * @author wzp
	 * @Created 2017/01/24
	 */
	private void saveContent( String fullpath, String content )
	{
		if( !TextUtils.isEmpty(content) )
		{
			try
			{
				File f = new File(LibraryConstant.LIBRARY_INFORMATION_PATH);
				if( !f.exists() )
				{
					f.mkdirs();
				}
				File file = new File(fullpath);
				if( !file.exists() )
				{
					FileOutputStream outStream = new FileOutputStream(file);
					outStream.write(content.getBytes());
					outStream.close();
				}
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
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
			entity.fullpath = LibraryConstant.LIBRARY_INFORMATION_PATH+entity.title+".inf";
			saveContent( entity.fullpath, obj.optString("Content") );
			
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
