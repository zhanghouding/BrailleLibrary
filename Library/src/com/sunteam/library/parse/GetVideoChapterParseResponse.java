package com.sunteam.library.parse;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sunteam.library.entity.VideoChapterInfoEntity;
import com.sunteam.library.utils.LogUtils;

//解析口述影像章节列表
public class GetVideoChapterParseResponse extends AbsParseResponse 
{
	public static final String TAG = "GetVideoChapterParseResponse";
	
	/**
	 * 方法(解析ChapterList)
	 * 
	 * @param jsonArray
	 * @param list
	 * @return
	 * @author wzp
	 * @Created 2017/01/24
	 */
	private void parseChapterList( JSONArray jsonArray, ArrayList<VideoChapterInfoEntity> list )
	{
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			
			VideoChapterInfoEntity entity = new VideoChapterInfoEntity();
			
			entity.title = obj.optString("Title");
			entity.abs = obj.optString("Abstract");
			entity.videoUrl = obj.optString("VideoUrl");
			entity.srtUrl = obj.optString("SrtUrl");
			entity.srtTextUrl = obj.optString("SrtTextUrl");
			entity.enterPoint = obj.optInt("EnterPoint");
			entity.outPoint = obj.optInt("OutPoint");
			entity.detailUrl = obj.optString("DetailUrl");
			entity.imageUrl = obj.optString("ImageUrl");
			entity.databaseCode = obj.optString("DatabaseCode");
			entity.videoType = obj.optInt("VideoType");
			entity.sysId = obj.optString("SysId");
			entity.chapterIndex = obj.optInt("ChapterIndex");
			entity.downloadUrl = obj.optString("DownloadUrl");
			entity.source = obj.optString("Source");
			entity.columns = obj.optString("Columns");
			entity.subItemList = obj.optString("SubItemList");
			entity.uniqueId = obj.optString("UniqueId");
			entity.updateTime = obj.optString("UpdateTime");
			entity.downloadCount = obj.optString("DownloadCount");
			entity.browseCount = obj.optString("BrowseCount");
			entity.categoryName = obj.optString("CategoryName");
			entity.speaker = obj.optString("Speaker");
			entity.speakerUnit = obj.optString("SpeakerUnit");
			entity.speakerSummary = obj.optString("SpeakerSummary");
			entity.keyWords = obj.optString("KeyWords");
			entity.lastChapterIndex = obj.optInt("LastChapterIndex");
			entity.hasReadHistory = obj.optBoolean("HasReadHistory");
			entity.isAuthenticated = obj.optBoolean("IsAuthenticated");
			entity.pageSize = obj.optInt("PageSize");
			entity.pageIndex = obj.optInt("PageIndex");
			entity.itemCount = obj.optInt("ItemCount");
			entity.isFirstPage = obj.optBoolean("IsFirstPage");
			entity.isLastPage = obj.optBoolean("IsLastPage");
			entity.pageCount = obj.optInt("PageCount");
			
			list.add(entity);
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
	private void parseItems( JSONArray jsonArray, ArrayList<VideoChapterInfoEntity> list )
	{
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			JSONArray array = obj.optJSONArray("SubItemList");
			
			parseChapterList( array, list );
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
			
			ArrayList<VideoChapterInfoEntity> list = new ArrayList<VideoChapterInfoEntity>();
			
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
