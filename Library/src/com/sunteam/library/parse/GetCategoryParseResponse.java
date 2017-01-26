package com.sunteam.library.parse;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sunteam.library.entity.CategoryInfoNodeEntity;
import com.sunteam.library.utils.LogUtils;

//解析分类列表
public class GetCategoryParseResponse extends AbsParseResponse 
{
	public static final String TAG = "GetCategoryParseResponse";
	
	/**
	 * 方法(解析items)
	 * 
	 * @param father
	 * 					父节点序号
	 * @param level
	 * 					节点等级
	 * @param jsonArray
	 * @param list
	 * @return
	 * @author wzp
	 * @Created 2017/01/24
	 */
	private void parseItems( int father, int level, JSONArray jsonArray, ArrayList<CategoryInfoNodeEntity> list )
	{
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			
			CategoryInfoNodeEntity entity = new CategoryInfoNodeEntity();
			
			entity.father = father;
			entity.level = level;
			entity.seq = list.size();
			entity.name = obj.optString("CategoryName");
			entity.code = obj.optString("CategoryCode");
			entity.type = obj.optString("CategoryType");
			entity.recordCount = obj.optInt("RecordCount");
			
			list.add(entity);
			
			try
			{
				JSONArray itemsArray = obj.optJSONArray("Items");
				if( (  null == itemsArray ) || ( 0 == itemsArray.length() ) )
				{
					continue;
				}
				
				parseItems( entity.seq, entity.level+1, itemsArray, list );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
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
			
			ArrayList<CategoryInfoNodeEntity> list = new ArrayList<CategoryInfoNodeEntity>();
			
			parseItems( -1, 0, jsonArray, list );
			
			return	list;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
