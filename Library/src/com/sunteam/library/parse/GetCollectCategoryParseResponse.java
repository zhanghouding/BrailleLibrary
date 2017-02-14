package com.sunteam.library.parse;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sunteam.library.entity.CollectCategoryEntity;
import com.sunteam.library.utils.LogUtils;

//解析收藏分类列表
public class GetCollectCategoryParseResponse extends AbsParseResponse 
{
	public static final String TAG = "GetCollectCategoryParseResponse";
	
	/**
	 * 方法(解析items)
	 * 
	 * @param jsonArray
	 * @param list
	 * @return
	 * @author wzp
	 * @Created 2017/02/14
	 */
	private void parseItems( JSONArray jsonArray, ArrayList<CollectCategoryEntity> list )
	{
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			
			CollectCategoryEntity entity = new CollectCategoryEntity();
			
			entity.id = obj.optInt("Id");								//记录id
			entity.userName = obj.optString("UserName");				//用户名
			entity.resType = obj.optInt("ResType");						//资源类型 0:电子图书 1:有声读物 3:视频影像
		    entity.categoryName = obj.optString("CategoryName");		//分类名
		    entity.categoryFullName = obj.optString("CategoryFullName");//完整的分类名，格式"有声读物-刘兰芳-古今荣耻谈"
		    entity.categoryCode = obj.optString("CategoryCode");		//分类编码
			
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
			
			ArrayList<CollectCategoryEntity> list = new ArrayList<CollectCategoryEntity>();
			
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
