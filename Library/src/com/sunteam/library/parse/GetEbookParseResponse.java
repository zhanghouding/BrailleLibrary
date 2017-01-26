package com.sunteam.library.parse;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sunteam.library.entity.EbookInfoEntity;
import com.sunteam.library.entity.EbookNodeEntity;
import com.sunteam.library.utils.LogUtils;

//解析电子图书列表
public class GetEbookParseResponse extends AbsParseResponse 
{
	public static final String TAG = "GetEbookParseResponse";
	
	/**
	 * 方法(解析items)
	 * 
	 * @param jsonArray
	 * @param list
	 * @return
	 * @author wzp
	 * @Created 2017/01/24
	 */
	private void parseItems( JSONArray jsonArray, ArrayList<EbookNodeEntity> list )
	{
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			
			EbookNodeEntity entity = new EbookNodeEntity();
			
			entity.uniqueId = obj.optString("UniqueId");
			entity.dbCode = obj.optString("DbCode");
			entity.sysId = obj.optString("SysId");
			entity.url = obj.optString("Url");
			entity.downloadUrl = obj.optString("DownloadUrl");
			entity.imageUrl = obj.optString("ImageUrl");
			entity.title = obj.optString("Title");
			entity.author = obj.optString("Author");
			entity.studio = obj.optString("Studio");
			entity.keyWords = obj.optString("KeyWords");
			entity.content = obj.optString("Content");
			entity.abs = obj.optString("Abstract");
			entity.dataType = obj.optString("DataType");
			entity.titleHighLighter = obj.optString("TitleHighLighter");
			entity.createTime = obj.optString("CreateTime");
			entity.boutiqueTime = obj.optString("BoutiqueTime");
			entity.browseCount = obj.optString("BrowseCount");
			entity.publish = obj.optString("Publish");
			entity.chapterList = obj.optString("ChapterList");
			entity.copyrightExpirationDate = obj.optString("CopyrightExpirationDate");
			entity.categoryCode = obj.optString("CategoryCode");
			entity.isBoutique = obj.optString("IsBoutique");
			entity.totalNumber = obj.optString("TotalNumber");
			entity.identifier = obj.optString("Identifier");
			entity.belongIdentifier = obj.optString("BelongIdentifier");
			entity.chapterNumber = obj.optString("ChapterNumber");
			entity.host = obj.optString("Host");
			entity.authorAbstract = obj.optString("AuthorAbstract");
			entity.duration = obj.optString("Duration");
			entity.entryPoint = obj.optString("EntryPoint");
			entity.outPoint = obj.optString("OutPoint");
			entity.pubTime = obj.optString("PubTime");
			entity.serial = obj.optString("Serial");
			entity.resourceType = obj.optString("ResourceType");
			entity.detailUrl = obj.optString("DetailUrl");
			entity.onlineViewUrl = obj.optString("OnlineViewUrl");
			
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
			
			EbookInfoEntity entity = new EbookInfoEntity();
			entity.moreUrl = jsonObject.optString("MoreUrl");
			entity.dbName = jsonObject.optString("dbName");
			entity.pageSize = jsonObject.optInt("PageSize");			//总共页码
			entity.pageIndex = jsonObject.optInt("PageIndex");			//当前页码，从1开始
			entity.itemCount = jsonObject.optInt("ItemCount");			//总共有多少本书
			entity.isFirstPage = jsonObject.optBoolean("IsFirstPage");	//是否第一页
			entity.isLastPage = jsonObject.optBoolean("IsLastPage");	//是否最后一页
		    entity.pageCount = jsonObject.optInt("PageCount");			//总共的页数
			
			JSONArray jsonArray = jsonObject.optJSONArray("Items");
			if( (  null == jsonArray ) || ( 0 == jsonArray.length() ) )
			{
				return	null;
			}
			
			entity.list = new ArrayList<EbookNodeEntity>();
			
			parseItems( jsonArray, entity.list );
			
			return	entity;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
