package com.sunteam.library.net;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sunteam.library.entity.AudioChapterInfoEntity;
import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.entity.CategoryInfoNodeEntity;
import com.sunteam.library.entity.CollectCategoryEntity;
import com.sunteam.library.entity.CollectResourceEntity;
import com.sunteam.library.entity.EbookChapterInfoEntity;
import com.sunteam.library.entity.EbookInfoEntity;
import com.sunteam.library.entity.HistoryEntity;
import com.sunteam.library.entity.InformationEntity;
import com.sunteam.library.entity.VideoChapterInfoEntity;
import com.sunteam.library.parse.AddBookMarkParseResponse;
import com.sunteam.library.parse.AddCollectCategoryParseResponse;
import com.sunteam.library.parse.AddCollectResourceParseResponse;
import com.sunteam.library.parse.AddHistoryParseResponse;
import com.sunteam.library.parse.DelBookMarkParseResponse;
import com.sunteam.library.parse.GetAudioChapterParseResponse;
import com.sunteam.library.parse.GetBookMarkParseResponse;
import com.sunteam.library.parse.GetCategoryParseResponse;
import com.sunteam.library.parse.GetCollectCategoryParseResponse;
import com.sunteam.library.parse.GetCollectResourceParseResponse;
import com.sunteam.library.parse.GetEbookChapterContentParseResponse;
import com.sunteam.library.parse.GetEbookChapterParseResponse;
import com.sunteam.library.parse.GetEbookParseResponse;
import com.sunteam.library.parse.GetHistoryParseResponse;
import com.sunteam.library.parse.GetInformationParseResponse;
import com.sunteam.library.parse.GetVideoChapterParseResponse;
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
	public static EbookInfoEntity getEbookList( String pageIndex, String pageSize, String categoryCode, int categoryType ) 
	{
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("pageIndex", pageIndex);
		requestParams.put("pageSize", pageSize);
		requestParams.put("categoryCode", categoryCode);
		
		switch( categoryType )
		{
			case LibraryConstant.LIBRARY_DATATYPE_EBOOK:	//电子图书
				requestParams.put("requestType", "GetEbookDataList");
				return (EbookInfoEntity) HttpRequest.get(LibraryConstant.URL_INTERFACE_EBOOK, requestParams, new GetEbookParseResponse() );
			case LibraryConstant.LIBRARY_DATATYPE_AUDIO:	//有声读物
				requestParams.put("requestType", "GetAudioDataList");
				return (EbookInfoEntity) HttpRequest.get(LibraryConstant.URL_INTERFACE_AUDIO, requestParams, new GetEbookParseResponse() );
			case LibraryConstant.LIBRARY_DATATYPE_VIDEO:	//口述影像
				requestParams.put("requestType", "GetVideoDataList");
				return (EbookInfoEntity) HttpRequest.get(LibraryConstant.URL_INTERFACE_VIDEO, requestParams, new GetEbookParseResponse() );
			default:
				return	null;
		}
	}
	
	/**
	 * 得到电子书章节列表
	 * 
	 * @param dbCode
	 * @param identifier
	 * @return
	 * @author wzp
	 * @Created 2017/01/25
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<EbookChapterInfoEntity> getEbookChapterList( String dbCode, String identifier ) 
	{
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "GetEbookDetail");
		requestParams.put("dbCode", dbCode);
		requestParams.put("Identifier", identifier);
		
		return (ArrayList<EbookChapterInfoEntity>) HttpRequest.get(LibraryConstant.URL_INTERFACE_EBOOK, requestParams, new GetEbookChapterParseResponse() );
	}
	
	/**
	 * 得到有声书章节列表
	 * 
	 * @param dbCode
	 * @param sysId
	 * @return
	 * @author wzp
	 * @Created 2017/01/25
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<AudioChapterInfoEntity> getAudioChapterList( String dbCode, String sysId ) 
	{
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "GetAudioDetail");
		requestParams.put("dbCode", dbCode);
		requestParams.put("sysid", sysId);
		
		return (ArrayList<AudioChapterInfoEntity>) HttpRequest.get(LibraryConstant.URL_INTERFACE_AUDIO, requestParams, new GetAudioChapterParseResponse() );
	}	
	
	/**
	 * 得到口述影像章节列表
	 * 
	 * @param dbCode
	 * @param sysId
	 * @return
	 * @author wzp
	 * @Created 2017/01/25
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<VideoChapterInfoEntity> getVideoChapterList( String dbCode, String sysId ) 
	{
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "GetVideoDetail");
		requestParams.put("dbCode", dbCode);
		requestParams.put("sysid", sysId);
		
		return (ArrayList<VideoChapterInfoEntity>) HttpRequest.get(LibraryConstant.URL_INTERFACE_VIDEO, requestParams, new GetVideoChapterParseResponse() );
	}	
	
	/**
	 * 得到盲人咨询列表
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param informationType
	 * @return
	 * @author wzp
	 * @Created 2017/01/25
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<InformationEntity> getInformationList( int pageIndex, int pageSize, int informationType ) 
	{
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "GetDataList");
		requestParams.put("pageIndex", pageIndex+"");
		requestParams.put("pageSize", pageSize+"");
		requestParams.put("orderField", "pubTime");
		requestParams.put("orderDirection", "desc");
		
		switch( informationType )
		{
			case LibraryConstant.LIBRARY_INFOTYPE_NOTICE:		//新闻公告
				requestParams.put("dbCode", "NOTICE");
				break;
			case LibraryConstant.LIBRARY_INFOTYPE_SERVICEINFO: 	// 服务资讯
				requestParams.put("dbCode", "SERVICEINFO");
				break;
			case LibraryConstant.LIBRARY_INFOTYPE_LIBMESSAGE:	//文化活动
				requestParams.put("dbCode", "LIBMESSAGE");
				break;
			default:
				return	null;
		}
		
		return	(ArrayList<InformationEntity>) HttpRequest.get(LibraryConstant.URL_INTERFACE_INFO, requestParams, new GetInformationParseResponse() );
	}
	
	/**
	 * 得到电子书章节内容
	 * 
	 * @param dbCode
	 * @param identifier
	 * @return
	 * @author wzp
	 * @Created 2017/01/29
	 */
	public static String getEbookChapterContent( String identifier, String chapterIndex ) 
	{
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "GetChapterContent");
		requestParams.put("Identifier", identifier);
		requestParams.put("chapterIndex", chapterIndex);
		
		return (String) HttpRequest.get(LibraryConstant.URL_INTERFACE_EBOOK, requestParams, new GetEbookChapterContentParseResponse() );
	}
	
	/**
	 * 得到阅读历史列表
	 * 
	 * @param username
	 * @return
	 * @author wzp
	 * @Created 2017/02/04
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<HistoryEntity> getHistoryList( String username ) 
	{
		String json = "{UserName:\""+username+"\"}";
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "SearchHistory");
		requestParams.put("jsonObj", json);
		
		return (ArrayList<HistoryEntity>) HttpRequest.get(LibraryConstant.URL_INTERFACE_HISTORY, requestParams, new GetHistoryParseResponse() );
	}
	
	/**
	 * 得到收藏分类列表
	 * 
	 * @param username
	 * @return
	 * @author wzp
	 * @Created 2017/02/14
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<CollectCategoryEntity> getCollectCategoryList( String username ) 
	{
		String json = "{UserName:\""+username+"\"}";
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "SearchCategory");
		requestParams.put("jsonObj", json);
		
		return (ArrayList<CollectCategoryEntity>) HttpRequest.get(LibraryConstant.URL_INTERFACE_COLLECT, requestParams, new GetCollectCategoryParseResponse() );
	}
	
	/**
	 * 得到收藏资源列表
	 * 
	 * @param username
	 * @return
	 * @author wzp
	 * @Created 2017/02/14
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<CollectResourceEntity> getCollectResourceList( String username ) 
	{
		String json = "{UserName:\""+username+"\"}";
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "SearchCollect");
		requestParams.put("jsonObj", json);
		
		return (ArrayList<CollectResourceEntity>) HttpRequest.get(LibraryConstant.URL_INTERFACE_COLLECT, requestParams, new GetCollectResourceParseResponse() );
	}
	
	/**
	 * 得到书签列表
	 * 
	 * @param username
	 * @param bookId
	 * @return
	 * @author wzp
	 * @Created 2017/02/14
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<BookmarkEntity> getBookMarkList( String username, String bookId ) 
	{
		String json = 
				"{"
				+ "\"UserName\":\""+username+"\","
				+ "\"BookId\":\""+bookId+"\""
				+ "}";
		
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "GetBookmarks");
		requestParams.put("jsonObj", URLEncoder.encode(json));
		
		return (ArrayList<BookmarkEntity>) HttpRequest.get(LibraryConstant.URL_INTERFACE_BOOKMARK, requestParams, new GetBookMarkParseResponse() );
	}
	
	/**
	 * 添加阅读历史
	 * 
	 * @param username
	 * @return
	 * @author wzp
	 * @Created 2017/02/04
	 */
	public static boolean addHistory( HistoryEntity entity ) 
	{
		String json = 
				"{"
				+ "\"UserName\":\""+entity.userName+"\","
				+ "\"Title\":\""+entity.title+"\","
				+ "\"DbCode\":\""+entity.dbCode+"\","
				+ "\"SysId\":\""+entity.sysId+"\","
				+ "\"ResType\":\""+entity.resType+"\","
				+ "\"CategoryCode\":\""+entity.categoryCode+"\","
				+ "\"LastChapterIndex\":\""+entity.lastChapterIndex+"\","
				+ "\"EnterPoint\":\""+entity.enterPoint+"\","
				+ "\"BookTitle\":\""+entity.bookTitle+"\","
				+ "\"CoverUrl\":\""+entity.coverUrl+"\","
				+ "\"Percent\":\""+entity.percent+"\","
				+ "\"CategoryFullName\":\""+entity.categoryFullName+"\""
				+ "}";
		String encodeJson = null;
		try
		{
			encodeJson = URLEncoder.encode(json,"utf-8");
		}
		catch( Exception e )
		{
			e.printStackTrace();
			
			return	false;
		}
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "AddHistory");
		requestParams.put("jsonObj", encodeJson);
		
		return (Boolean) HttpRequest.get(LibraryConstant.URL_INTERFACE_HISTORY, requestParams, new AddHistoryParseResponse() );
	}
	
	/**
	 * 添加收藏分类
	 * 
	 * @param entity
	 * @return
	 * @author wzp
	 * @Created 2017/02/05
	 */
	public static Integer addCollectCategory( CollectCategoryEntity entity ) 
	{
		String json = 
				"{"
				+ "\"UserName\":\""+entity.userName+"\","
				+ "\"CategoryName\":\""+entity.categoryName+"\","
				+ "\"CategoryCode\":\""+entity.categoryCode+"\","
				+ "\"CategoryFullName\":\""+entity.categoryFullName+"\","
				+ "\"ResType\":\""+entity.resType+"\""
				+ "}";
		String encodeJson = null;
		try
		{
			encodeJson = URLEncoder.encode(json,"utf-8");
		}
		catch( Exception e )
		{
			e.printStackTrace();
			
			return	LibraryConstant.RESULT_EXCEPTION;
		}
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "AddCategory");
		requestParams.put("jsonObj", encodeJson);
		
		return (Integer) HttpRequest.get(LibraryConstant.URL_INTERFACE_COLLECT, requestParams, new AddCollectCategoryParseResponse() );
	}
	
	/**
	 * 添加收藏资源
	 * 
	 * @param entity
	 * @return
	 * @author wzp
	 * @Created 2017/02/05
	 */
	public static Integer addCollectResource( CollectResourceEntity entity ) 
	{
		String json = 
				"{"
				+ "\"UserName\":\""+entity.userName+"\","
				+ "\"Title\":\""+entity.title+"\","
				+ "\"DbCode\":\""+entity.dbCode+"\","
				+ "\"SysId\":\""+entity.sysId+"\","
				+ "\"CategoryFullName\":\""+entity.categoryFullName+"\","
				+ "\"ResType\":\""+entity.resType+"\""
				+ "}";
		String encodeJson = null;
		try
		{
			encodeJson = URLEncoder.encode(json,"utf-8");
		}
		catch( Exception e )
		{
			e.printStackTrace();
			
			return	LibraryConstant.RESULT_EXCEPTION;
		}
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "AddCollect");
		requestParams.put("jsonObj", encodeJson);
		
		return (Integer) HttpRequest.get(LibraryConstant.URL_INTERFACE_COLLECT, requestParams, new AddCollectResourceParseResponse() );
	}
	
	/**
	 * 添加书签
	 * 
	 * @param entity
	 * @return
	 * @author wzp
	 * @Created 2017/02/05
	 */
	public static Integer addBookMark( BookmarkEntity entity ) 
	{
		String json = 
				"{"
				+ "\"UserName\":\""+entity.userName+"\","
				+ "\"BookId\":\""+entity.bookId+"\","
				+ "\"Begin\":\""+entity.begin+"\","
				+ "\"ChapterIndex\":\""+entity.chapterIndex+"\","
				+ "\"ChapterTitle\":\""+entity.chapterTitle+"\","
				+ "\"MarkName\":\""+entity.markName+"\","
				+ "\"Percent\":\""+entity.percent+"\""
				+ "}";
		String encodeJson = null;
		try
		{
			encodeJson = URLEncoder.encode(json,"utf-8");
		}
		catch( Exception e )
		{
			e.printStackTrace();
			
			return	LibraryConstant.RESULT_EXCEPTION;
		}
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "AddBookmark");
		requestParams.put("jsonObj", encodeJson);
		
		return (Integer) HttpRequest.get(LibraryConstant.URL_INTERFACE_BOOKMARK, requestParams, new AddBookMarkParseResponse() );
	}	
	
	/**
	 * 删除书签
	 * 
	 * @param username
	 * @param id
	 * @return
	 * @author wzp
	 * @Created 2017/02/18
	 */
	@SuppressWarnings("unchecked")
	public static Integer delBookMark( String username, String id ) 
	{
		String json = 
				"{"
				+ "\"UserName\":\""+username+"\","
				+ "\"Id\":\""+id+"\""
				+ "}";
		
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "DeleteBookmark");
		requestParams.put("jsonObj", URLEncoder.encode(json));
		
		return (Integer) HttpRequest.get(LibraryConstant.URL_INTERFACE_BOOKMARK, requestParams, new DelBookMarkParseResponse() );
	}
		
}
