package com.sunteam.library.parse;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sunteam.library.entity.AudioChapterInfoEntity;
import com.sunteam.library.utils.LogUtils;

//解析有声书章节列表
public class GetAudioChapterParseResponse extends AbsParseResponse 
{
	public static final String TAG = "GetAudioChapterParseResponse";
	
	//直接解析json
	private ArrayList<AudioChapterInfoEntity> parseJson( JSONObject obj )
	{
		if( null == obj )
		{
			return	null;
		}
		
		AudioChapterInfoEntity entity = new AudioChapterInfoEntity();
		
		entity.father = -1;
		entity.level = 0;
		entity.seq = 0;
		
		try
		{
			entity.title = obj.optString("Title");
			entity.detailInfo = obj.optString("DetailInfo");
			entity.abs = obj.optString("Abstract");
			entity.attachmentList = obj.optString("AttachmentList");
			entity.audioUrl = obj.optString("AudioUrl");
			entity.enterPoint = obj.optInt("EnterPoint");
			entity.outPoint = obj.optInt("OutPoint");
			entity.detailUrl = obj.optString("DetailUrl");
			entity.imageUrl = obj.optString("ImageUrl");
			entity.databaseCode = obj.optString("DatabaseCode");
			entity.audioType = obj.optInt("AudioType");
			entity.sysId = obj.optString("SysId");
			entity.chapterIndex = obj.optInt("ChapterIndex");
			entity.downloadUrl = obj.optString("DownloadUrl");
			entity.source = obj.optString("Source");
			entity.columns = obj.optString("Columns");
			entity.uniqueId = obj.optString("UniqueId");
			entity.updateTime = obj.optString("UpdateTime");
			entity.downloadCount = obj.optString("DownloadCount");
			entity.browseCount = obj.optString("BrowseCount");
			entity.categoryName = obj.optString("CategoryName");
			entity.responsible = obj.optString("Responsible");
			entity.keyWords = obj.optString("KeyWords");
			entity.lastChapterIndex = obj.optInt("LastChapterIndex");
			entity.hasReadHistory = obj.optBoolean("HasReadHistory");
			entity.isAuthenticated = obj.optBoolean("IsAuthenticated");
			entity.parentCategroyCode = obj.optString("ParentCategroyCode");
			entity.parentCategroyName = obj.optString("ParentCategroyName");
			entity.pageSize = obj.optInt("PageSize");
			entity.pageIndex = obj.optInt("PageIndex");
			entity.itemCount = obj.optInt("ItemCount");
			entity.isFirstPage = obj.optBoolean("IsFirstPage");
			entity.isLastPage = obj.optBoolean("IsLastPage");
			entity.pageCount = obj.optInt("PageCount");
			
			ArrayList<AudioChapterInfoEntity> list = new ArrayList<AudioChapterInfoEntity>();
			list.add(entity);
			
			return	list;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return	null;
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
	private void parseItems( int father, int level, JSONArray jsonArray, ArrayList<AudioChapterInfoEntity> list )
	{
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			
			AudioChapterInfoEntity entity = new AudioChapterInfoEntity();
			
			entity.father = father;
			entity.level = level;
			entity.seq = list.size();
			
			entity.title = obj.optString("Title");
			entity.detailInfo = obj.optString("DetailInfo");
			entity.abs = obj.optString("Abstract");
			entity.attachmentList = obj.optString("AttachmentList");
			entity.audioUrl = obj.optString("AudioUrl");
			entity.enterPoint = obj.optInt("EnterPoint");
			entity.outPoint = obj.optInt("OutPoint");
			entity.detailUrl = obj.optString("DetailUrl");
			entity.imageUrl = obj.optString("ImageUrl");
			entity.databaseCode = obj.optString("DatabaseCode");
			entity.audioType = obj.optInt("AudioType");
			entity.sysId = obj.optString("SysId");
			entity.chapterIndex = obj.optInt("ChapterIndex");
			entity.downloadUrl = obj.optString("DownloadUrl");
			entity.source = obj.optString("Source");
			entity.columns = obj.optString("Columns");
			entity.uniqueId = obj.optString("UniqueId");
			entity.updateTime = obj.optString("UpdateTime");
			entity.downloadCount = obj.optString("DownloadCount");
			entity.browseCount = obj.optString("BrowseCount");
			entity.categoryName = obj.optString("CategoryName");
			entity.responsible = obj.optString("Responsible");
			entity.keyWords = obj.optString("KeyWords");
			entity.lastChapterIndex = obj.optInt("LastChapterIndex");
			entity.hasReadHistory = obj.optBoolean("HasReadHistory");
			entity.isAuthenticated = obj.optBoolean("IsAuthenticated");
			entity.parentCategroyCode = obj.optString("ParentCategroyCode");
			entity.parentCategroyName = obj.optString("ParentCategroyName");
			entity.pageSize = obj.optInt("PageSize");
			entity.pageIndex = obj.optInt("PageIndex");
			entity.itemCount = obj.optInt("ItemCount");
			entity.isFirstPage = obj.optBoolean("IsFirstPage");
			entity.isLastPage = obj.optBoolean("IsLastPage");
			entity.pageCount = obj.optInt("PageCount");
			
			list.add(entity);
			
			try
			{
				JSONArray itemsArray = obj.optJSONArray("SubItemList");
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
			JSONArray jsonArray = jsonObject.optJSONArray("SubItemList");
			if( (  null == jsonArray ) || ( 0 == jsonArray.length() ) )
			{
				return	parseJson(jsonObject);	//有可能只有一个节点
			}
			
			ArrayList<AudioChapterInfoEntity> list = new ArrayList<AudioChapterInfoEntity>();
			
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
