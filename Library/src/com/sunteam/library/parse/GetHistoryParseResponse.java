package com.sunteam.library.parse;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sunteam.library.entity.HistoryEntity;
import com.sunteam.library.utils.LogUtils;

//解析阅读历史列表
public class GetHistoryParseResponse extends AbsParseResponse 
{
	public static final String TAG = "GetHistoryParseResponse";
	
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
	 * @Created 2017/02/04
	 */
	private void parseItems( JSONArray jsonArray, ArrayList<HistoryEntity> list )
	{
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			
			HistoryEntity entity = new HistoryEntity();
			
			entity.id = obj.optInt("Id");								//记录id
			entity.userName = obj.optString("UserName");				//用户名
			entity.title = obj.optString("Title");						//标题
			entity.dbCode = obj.optString("DbCode");					//数据编码
			entity.sysId = obj.optString("SysId");						//系统id
			entity.resType = obj.optInt("ResType");						//资源类型 0:电子图书 1:有声读物 3:视频影像
			entity.lastChapterIndex = obj.optInt("LastChapterIndex");	//最后阅读的章节序号
		    entity.enterPoint = obj.optString("EnterPoint");			//最后阅读的音视频时间点，格式"00:00:00"
		    entity.url = obj.optString("Url");
		    entity.createTime = obj.optString("CreateTime");			//创建时间，格式"2017-02-03T19:42:14"
		    entity.updateTime = obj.optString("UpdateTime");			//更新时间，格式"2017-02-03T19:42:14",
		    entity.bookTitle = obj.optString("BookTitle");				//标题
		    entity.coverUrl = obj.optString("CoverUrl");				//封面图片url
		    entity.percent = obj.optString("Percent");					//电子书阅读进度，格式"0.00%"
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
			
			ArrayList<HistoryEntity> list = new ArrayList<HistoryEntity>();
			
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
