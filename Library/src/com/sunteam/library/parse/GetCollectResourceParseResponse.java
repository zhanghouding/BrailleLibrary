package com.sunteam.library.parse;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sunteam.library.entity.CollectResourceEntity;
import com.sunteam.library.utils.LogUtils;

//解析收藏资源列表
public class GetCollectResourceParseResponse extends AbsParseResponse 
{
	public static final String TAG = "GetCollectResourceParseResponse";
	
	/**
	 * 方法(解析items)
	 * 
	 * @param jsonArray
	 * @param list
	 * @return
	 * @author wzp
	 * @Created 2017/02/14
	 */
	private void parseItems( JSONArray jsonArray, ArrayList<CollectResourceEntity> list )
	{
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			
			CollectResourceEntity entity = new CollectResourceEntity();
			
			entity.id = obj.optInt("Id");								//记录id
			entity.userName = obj.optString("UserName");				//用户名
			entity.title = obj.optString("Title");						//标题(书名)
			entity.resType = obj.optInt("ResType");						//资源类型 0:电子图书 1:有声读物 3:视频影像
		    entity.dbCode = obj.optString("DbCode");					//数据编码
		    entity.categoryFullName = obj.optString("CategoryFullName");//完整的分类名，格式"有声读物-刘兰芳-古今荣耻谈"
		    entity.sysId = obj.optString("SysId");						//系统编码
		    entity.coverUrl = obj.optString("CoverUrl");				//封面图片
		    entity.createTime = obj.optString("CreateTime");			//创建时间
			
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
			
			ArrayList<CollectResourceEntity> list = new ArrayList<CollectResourceEntity>();
			
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
