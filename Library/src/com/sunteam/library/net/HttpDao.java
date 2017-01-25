package com.sunteam.library.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.sunteam.library.entity.CategoryInfoNodeEntity;
import com.sunteam.library.entity.EbookInfoEntity;
import com.sunteam.library.parse.GetCategoryParseResponse;
import com.sunteam.library.parse.GetEbookParseResponse;
import com.sunteam.library.parse.LoginParseResponse;
import com.sunteam.library.utils.LibraryConstant;

/**
 * 有关网络操作的相关的接口
 * 
 * @author wzp
 * @Created 2017/01/24
 */
public class HttpDao 
{
	/**
	 * 登录
	 * 
	 * @param username
	 * @return
	 * @author wzp
	 * @Created 2017/01/24
	 */
	public static Boolean login( String username ) 
	{
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "UserAuthentication");
		requestParams.put("timeStr", "2016-01-04$10:54:00");
		requestParams.put("AuthenticationStr", "MWPlatformAuthentication");
		requestParams.put("SystemCode", "MWAPP");
		requestParams.put("userName", username);
		requestParams.put("EncryptedStr", "6fb7e13bb86e5bddd89f3ef2ba2cb28f");
		
		return (Boolean) HttpRequest.get(LibraryConstant.URL_USER_INTERFACE, requestParams, new LoginParseResponse() );
	}
	
	/**
	 * 得到所有的分类信息
	 * 
	 * @param categoryType
	 * @return
	 * @author wzp
	 * @Created 2017/01/24
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<CategoryInfoNodeEntity> getCategoryInfoList( int categoryType ) 
	{
		Map<String, String> requestParams = new HashMap<String, String>();
		switch( categoryType )
		{
			case LibraryConstant.LIBRARY_EBOOK_TYPE:	//电子图书
				requestParams.put("requestType", "GetEbookCategory");
				return (ArrayList<CategoryInfoNodeEntity>) HttpRequest.get(LibraryConstant.URL_EBOOK_INTERFACE, requestParams, new GetCategoryParseResponse() );
			case LibraryConstant.LIBRARY_AUDIO_TYPE:	//有声读物
				requestParams.put("requestType", "GetAudioCategory");
				return (ArrayList<CategoryInfoNodeEntity>) HttpRequest.get(LibraryConstant.URL_AUDIO_INTERFACE, requestParams, new GetCategoryParseResponse() );
			case LibraryConstant.LIBRARY_VIDEO_TYPE:	//口述影像
				requestParams.put("requestType", "GetVideoCategory");
				return (ArrayList<CategoryInfoNodeEntity>) HttpRequest.get(LibraryConstant.URL_VIDEO_INTERFACE, requestParams, new GetCategoryParseResponse() );
			default:
				return	null;
		}
	}
	
	/**
	 * 得到电子书列表
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param categoryCode
	 * @return
	 * @author wzp
	 * @Created 2017/01/25
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<EbookInfoEntity> getEbookList( String pageIndex, String pageSize, String categoryCode ) 
	{
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "GetEbookDataList");
		requestParams.put("pageIndex", pageIndex);
		requestParams.put("pageSize", pageSize);
		requestParams.put("CategoryCode", categoryCode);
		
		return (ArrayList<EbookInfoEntity>) HttpRequest.get(LibraryConstant.URL_EBOOK_INTERFACE, requestParams, new GetEbookParseResponse() );
	}
}
