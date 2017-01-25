package com.sunteam.library.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sunteam.library.entity.CategoryInfoNodeEntity;
import com.sunteam.library.entity.EbookChapterInfoEntity;
import com.sunteam.library.entity.EbookInfoEntity;
import com.sunteam.library.parse.GetCategoryParseResponse;
import com.sunteam.library.parse.GetEbookChapterParseResponse;
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
		
		return (Boolean) HttpRequest.get(LibraryConstant.URL_INTERFACE_USER, requestParams, new LoginParseResponse() );
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
			case LibraryConstant.LIBRARY_DATATYPE_EBOOK:	//电子图书
				requestParams.put("requestType", "GetEbookCategory");
				return (ArrayList<CategoryInfoNodeEntity>) HttpRequest.get(LibraryConstant.URL_INTERFACE_EBOOK, requestParams, new GetCategoryParseResponse() );
			case LibraryConstant.LIBRARY_DATATYPE_AUDIO:	//有声读物
				requestParams.put("requestType", "GetAudioCategory");
				return (ArrayList<CategoryInfoNodeEntity>) HttpRequest.get(LibraryConstant.URL_INTERFACE_AUDIO, requestParams, new GetCategoryParseResponse() );
			case LibraryConstant.LIBRARY_DATATYPE_VIDEO:	//口述影像
				requestParams.put("requestType", "GetVideoCategory");
				return (ArrayList<CategoryInfoNodeEntity>) HttpRequest.get(LibraryConstant.URL_INTERFACE_VIDEO, requestParams, new GetCategoryParseResponse() );
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
	 * @param categoryType
	 * @return
	 * @author wzp
	 * @Created 2017/01/25
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<EbookInfoEntity> getEbookList( String pageIndex, String pageSize, String categoryCode, int categoryType ) 
	{
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("pageIndex", pageIndex);
		requestParams.put("pageSize", pageSize);
		requestParams.put("categoryCode", categoryCode);
		
		switch( categoryType )
		{
			case LibraryConstant.LIBRARY_DATATYPE_EBOOK:	//电子图书
				requestParams.put("requestType", "GetEbookDataList");
				return (ArrayList<EbookInfoEntity>) HttpRequest.get(LibraryConstant.URL_INTERFACE_EBOOK, requestParams, new GetEbookParseResponse() );
			case LibraryConstant.LIBRARY_DATATYPE_AUDIO:	//有声读物
				requestParams.put("requestType", "GetAudioDataList");
				return (ArrayList<EbookInfoEntity>) HttpRequest.get(LibraryConstant.URL_INTERFACE_AUDIO, requestParams, new GetEbookParseResponse() );
			case LibraryConstant.LIBRARY_DATATYPE_VIDEO:	//口述影像
				requestParams.put("requestType", "GetVideoDataList");
				return (ArrayList<EbookInfoEntity>) HttpRequest.get(LibraryConstant.URL_INTERFACE_VIDEO, requestParams, new GetEbookParseResponse() );
			default:
				return	null;
		}
	}
	
	/**
	 * 得到电子书章节列表
	 * 
	 * @param identifier
	 * @return
	 * @author wzp
	 * @Created 2017/01/25
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<EbookChapterInfoEntity> getEbookChapterList( String identifier ) 
	{
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "GetEbookDetail");
		requestParams.put("dbCode", "Ebook");
		requestParams.put("Identifier", identifier);
		
		return (ArrayList<EbookChapterInfoEntity>) HttpRequest.get(LibraryConstant.URL_INTERFACE_EBOOK, requestParams, new GetEbookChapterParseResponse() );
	}	
}
