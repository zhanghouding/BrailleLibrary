package com.sunteam.library.parse;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sunteam.library.entity.EbookInfoEntity;
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
	private void parseItems( JSONArray jsonArray, ArrayList<EbookInfoEntity> list )
	{
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			
			EbookInfoEntity entity = new EbookInfoEntity();
			
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
			
			JSONArray jsonArray = jsonObject.optJSONArray("Items");
			if( (  null == jsonArray ) || ( 0 == jsonArray.length() ) )
			{
				return	null;
			}
			
			ArrayList<EbookInfoEntity> list = new ArrayList<EbookInfoEntity>();
			
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
