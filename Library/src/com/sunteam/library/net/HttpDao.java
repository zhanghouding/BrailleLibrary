package com.sunteam.library.net;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

import com.sunteam.library.entity.AudioChapterInfoEntity;
import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.entity.CategoryInfoNodeEntity;
import com.sunteam.library.entity.CollectCategoryEntity;
import com.sunteam.library.entity.CollectResourceEntity;
import com.sunteam.library.entity.EbookChapterInfoEntity;
import com.sunteam.library.entity.EbookInfoEntity;
import com.sunteam.library.entity.HistoryEntity;
import com.sunteam.library.entity.InformationEntity;
import com.sunteam.library.entity.UserInfoEntity;
import com.sunteam.library.entity.VideoChapterInfoEntity;
import com.sunteam.library.parse.AddBookMarkParseResponse;
import com.sunteam.library.parse.AddCollectCategoryParseResponse;
import com.sunteam.library.parse.AddCollectResourceParseResponse;
import com.sunteam.library.parse.AddHistoryParseResponse;
import com.sunteam.library.parse.DelBookMarkParseResponse;
import com.sunteam.library.parse.DelCollectCategoryParseResponse;
import com.sunteam.library.parse.DelCollectResourceParseResponse;
import com.sunteam.library.parse.DelHistoryParseResponse;
import com.sunteam.library.parse.GetAudioChapterParseResponse;
import com.sunteam.library.parse.GetBookMarkParseResponse;
import com.sunteam.library.parse.GetCategoryNameParseResponse;
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
import com.sunteam.library.parse.RegisterParseResponse;
import com.sunteam.library.parse.UserGetPasswordParseResponse;
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
	 * @return password
	 * @author wzp
	 * @Created 2017/01/24
	 */
	public static String login( String username ) 
	{
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "UserAuthentication");
		requestParams.put("timeStr", "2016-01-04$10:54:00");
		requestParams.put("AuthenticationStr", "MWPlatformAuthentication");
		requestParams.put("SystemCode", "MWAPP");
		requestParams.put("userName", username);
		requestParams.put("EncryptedStr", "6fb7e13bb86e5bddd89f3ef2ba2cb28f");
		
		return (String) HttpRequest.get(LibraryConstant.URL_INTERFACE_USER, requestParams, new LoginParseResponse() );
	}
	
	/**
	 * 得到用户密码信息(找回密码第一步)
	 * 
	 * @param authenticType ：验证类型：1 读者卡号 2：残疾人证号
	 * @param realName	: 真实姓名
	 * @param cardNo	: 号码(验证类型为1，号码就是读者卡号；验证类型为2，号码就是残疾人证号)
	 * @return
	 * @author wzp
	 * @Created 2017/03/01
	 */
	public static UserInfoEntity userGetPassword( int authenticType, String realName, String cardNo ) 
	{
		String userInfo = null;
		switch( authenticType )
		{
			case 1:	//读者卡号
				userInfo = 
						"{"
						+ "\"RealName\":\""+realName+"\","
						+ "\"ReaderCardNo\":\""+cardNo+"\""
						+ "}";
				break;
			case 2:	//残疾人证号
				userInfo = 
						"{"
						+ "\"RealName\":\""+realName+"\","
						+ "\"DisableCardNo\":\""+cardNo+"\""
						+ "}";
				break;
			default:
				return	null;
		}
		
		String encodeJson = null;
		try
		{
			encodeJson = URLEncoder.encode(userInfo,"utf-8");
		}
		catch( Exception e )
		{
			e.printStackTrace();
			
			return	null;
		}
		
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "UserGetPassword");
		requestParams.put("AuthenticType", authenticType+"");
		requestParams.put("UserInfo", encodeJson);
		
		return (UserInfoEntity) HttpRequest.get(LibraryConstant.URL_INTERFACE_USER, requestParams, new UserGetPasswordParseResponse() );
	}
	
	/**
	 * 更新用户密码信息(找回密码第二步)
	 * 
	 * @param authenticType ：验证类型：1 读者卡号 2：残疾人证号
	 * @param username	: 用户名
	 * @param realName	: 真实姓名
	 * @param cardNo	: 号码(验证类型为1，号码就是读者卡号；验证类型为2，号码就是残疾人证号)
	 * @return
	 * @author wzp
	 * @Created 2017/03/01
	 */
	public static Integer updateGetPassword( int authenticType, String userName, String realName, String cardNo, String password ) 
	{
		String userInfo = null;
		switch( authenticType )
		{
			case 1:	//读者卡号
				userInfo = 
						"{"
						+ "\"UserName\":\""+userName+"\","
						+ "\"RealName\":\""+realName+"\","
						+ "\"ReaderCardNo\":\""+cardNo+"\","
						+ "\"Password\":\""+password+"\""
						+ "}";
				break;
			case 2:	//残疾人证号
				userInfo = 
						"{"
						+ "\"UserName\":\""+userName+"\","
						+ "\"RealName\":\""+realName+"\","
						+ "\"DisableCardNo\":\""+cardNo+"\","
						+ "\"Password\":\""+password+"\""
						+ "}";
				break;
			default:
				return	LibraryConstant.RESULT_EXCEPTION;
		}
		
		String encodeJson = null;
		try
		{
			encodeJson = URLEncoder.encode(userInfo,"utf-8");
		}
		catch( Exception e )
		{
			e.printStackTrace();
			
			return	LibraryConstant.RESULT_EXCEPTION;
		}
		
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "UpdatePassword");
		requestParams.put("AuthenticType", authenticType+"");
		requestParams.put("UserInfo", encodeJson);
		
		if( HttpRequest.get(LibraryConstant.URL_INTERFACE_USER, requestParams, new UserGetPasswordParseResponse() ) == null )
		{
			return	LibraryConstant.RESULT_FAIL;	
		}
		
		return	LibraryConstant.RESULT_SUCCESS;
	}
	
	/**
	 * 注册
	 * 
	 * @param authenticType ：验证类型：1 读者卡号 2：残疾人证号
	 * @param username	: 用户名
	 * @param realName	: 真实姓名
	 * @param cardNo	: 号码(验证类型为1，号码就是读者卡号；验证类型为2，号码就是残疾人证号)
	 * @return
	 * @author wzp
	 * @Created 2017/03/01
	 */
	public static Integer register( int authenticType, String userName, String realName, String cardNo, String password ) 
	{
		Map<String, String> requestParams = new HashMap<String, String>();
		
		String userInfo = null;
		switch( authenticType )
		{
			case 1:	//读者卡号
				userInfo = 
						"{"
						+ "\"ReaderCardNo\":\""+cardNo+"\","
						+ "\"RealName\":\""+realName+"\","
						+ "\"UserName\":\""+userName+"\","
						+ "\"Password\":\""+password+"\""
						+ "}";
				requestParams.put("requestType", "UserAddByReaderCard");
				break;
			case 2:	//残疾人证号
				userInfo = 
						"{"
						+ "\"DisableCardNo\":\""+cardNo+"\","
						+ "\"RealName\":\""+realName+"\","
						+ "\"UserName\":\""+userName+"\","
						+ "\"Password\":\""+password+"\""
						+ "}";
				requestParams.put("requestType", "UserAddByDisabledCardNo");
				break;
			default:
				return	LibraryConstant.RESULT_EXCEPTION;
		}
		
		String encodeJson = null;
		try
		{
			encodeJson = URLEncoder.encode(userInfo,"utf-8");
		}
		catch( Exception e )
		{
			e.printStackTrace();
			
			return	LibraryConstant.RESULT_EXCEPTION;
		}
		
		requestParams.put("UserInfo", encodeJson);
		
		return (Integer) HttpRequest.get(LibraryConstant.URL_INTERFACE_USER, requestParams, new RegisterParseResponse() );
	}
		
	/**
	 * 得到分类名称
	 * 
	 * @param categoryType
	 * @return
	 * @author wzp
	 * @Created 2017/01/24
	 */
	@SuppressWarnings("unchecked")
	public static String getCategoryName( int categoryType, String categoryCode ) 
	{
		Map<String, String> requestParams = new HashMap<String, String>();
		if( categoryCode.contains(";") )
		{
			String[] code = categoryCode.split(";");
			for( int i = code.length-1; i >= 0; i-- )
			{
				if( !TextUtils.isEmpty(code[i]) )
				{
					requestParams.put("categoryCode", code[i]);
					break;
				}
			}
		}
		else
		{
			requestParams.put("categoryCode", "categoryCode");
		}
		switch( categoryType )
		{
			case LibraryConstant.LIBRARY_DATATYPE_EBOOK:	//电子图书
				requestParams.put("requestType", "GetEbookCategoryName");
				return (String) HttpRequest.get(LibraryConstant.URL_INTERFACE_EBOOK, requestParams, new GetCategoryNameParseResponse() );
			case LibraryConstant.LIBRARY_DATATYPE_AUDIO:	//有声读物
				requestParams.put("requestType", "GetAudioCategoryName");
				return (String) HttpRequest.get(LibraryConstant.URL_INTERFACE_AUDIO, requestParams, new GetCategoryNameParseResponse() );
			case LibraryConstant.LIBRARY_DATATYPE_VIDEO:	//口述影像
				requestParams.put("requestType", "GetVideoCategoryName");
				return (String) HttpRequest.get(LibraryConstant.URL_INTERFACE_VIDEO, requestParams, new GetCategoryNameParseResponse() );
			default:
				return	null;
		}
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
	public static ArrayList<HistoryEntity> getHistoryList( String userName, int pageIndex, int pageSize ) 
	{
		String json = 
				"{"
				+ "\"UserName\":\""+userName+"\","
				+ "\"PageIndex\":\""+pageIndex+"\","
				+ "\"PageSize\":\""+pageSize+"\""
				+ "}";
		String encodeJson = null;
		try
		{
			encodeJson = URLEncoder.encode(json,"utf-8");
		}
		catch( Exception e )
		{
			e.printStackTrace();
			
			return	null;
		}
		
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "SearchHistory");
		requestParams.put("jsonObj", encodeJson);
		
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
	public static ArrayList<CollectCategoryEntity> getCollectCategoryList( String userName, int pageIndex, int pageSize ) 
	{
		String json = 
				"{"
				+ "\"UserName\":\""+userName+"\","
				+ "\"PageIndex\":\""+pageIndex+"\","
				+ "\"PageSize\":\""+pageSize+"\""
				+ "}";
		String encodeJson = null;
		try
		{
			encodeJson = URLEncoder.encode(json,"utf-8");
		}
		catch( Exception e )
		{
			e.printStackTrace();
			
			return	null;
		}
		
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "SearchCategory");
		requestParams.put("jsonObj", encodeJson);
		
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
	public static ArrayList<CollectResourceEntity> getCollectResourceList( String userName, int pageIndex, int pageSize ) 
	{
		String json = 
				"{"
				+ "\"UserName\":\""+userName+"\","
				+ "\"PageIndex\":\""+pageIndex+"\","
				+ "\"PageSize\":\""+pageSize+"\""
				+ "}";
		String encodeJson = null;
		try
		{
			encodeJson = URLEncoder.encode(json,"utf-8");
		}
		catch( Exception e )
		{
			e.printStackTrace();
			
			return	null;
		}
		
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "SearchCollect");
		requestParams.put("jsonObj", encodeJson);
		
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
		
		String encodeJson = null;
		try
		{
			encodeJson = URLEncoder.encode(json,"utf-8");
		}
		catch( Exception e )
		{
			e.printStackTrace();
			
			return	null;
		}
		
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "GetBookmarks");
		requestParams.put("jsonObj", encodeJson);
		
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
	public static HistoryEntity addHistory( HistoryEntity entity ) 
	{
		if( null == entity )
		{
			return	null;
		}
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
			
			return	entity;
		}
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "AddHistory");
		requestParams.put("jsonObj", encodeJson);
		
		return (HistoryEntity) HttpRequest.get(LibraryConstant.URL_INTERFACE_HISTORY, requestParams, new AddHistoryParseResponse() );
	}
	
	/**
	 * 添加收藏分类
	 * 
	 * @param entity
	 * @return
	 * @author wzp
	 * @Created 2017/02/05
	 */
	public static CollectCategoryEntity addCollectCategory( CollectCategoryEntity entity ) 
	{
		if( null == entity )
		{
			return	null;
		}
		
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
			
			return	entity;
		}
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "AddCategory");
		requestParams.put("jsonObj", encodeJson);
		
		return (CollectCategoryEntity) HttpRequest.get(LibraryConstant.URL_INTERFACE_COLLECT, requestParams, new AddCollectCategoryParseResponse() );
	}
	
	/**
	 * 添加收藏资源
	 * 
	 * @param entity
	 * @return
	 * @author wzp
	 * @Created 2017/02/05
	 */
	public static CollectResourceEntity addCollectResource( CollectResourceEntity entity ) 
	{
		if( null == entity )
		{
			return	null;
		}
		
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
			
			return	entity;
		}
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "AddCollect");
		requestParams.put("jsonObj", encodeJson);
		
		return (CollectResourceEntity) HttpRequest.get(LibraryConstant.URL_INTERFACE_COLLECT, requestParams, new AddCollectResourceParseResponse() );
	}
	
	/**
	 * 添加书签
	 * 
	 * @param entity
	 * @return
	 * @author wzp
	 * @Created 2017/02/05
	 */
	public static BookmarkEntity addBookMark( BookmarkEntity entity ) 
	{
		if( null == entity )
		{
			return	null;
		}
		
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
			
			return	entity;
		}
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "AddBookmark");
		requestParams.put("jsonObj", encodeJson);
		
		return (BookmarkEntity) HttpRequest.get(LibraryConstant.URL_INTERFACE_BOOKMARK, requestParams, new AddBookMarkParseResponse() );
	}	
	
	/**
	 * 删除历史
	 * 
	 * @param username
	 * @param dbCode
	 * @param sysId
	 * @return
	 * @author wzp
	 * @Created 2017/02/19
	 */
	public static Integer delHistory( String username, String dbCode, String sysId ) 
	{
		String json = 
				"{"
				+ "\"UserName\":\""+username+"\","
				+ "\"DbCode\":\""+dbCode+"\","
				+ "\"SysId\":\""+sysId+"\""
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
		requestParams.put("requestType", "DeleteHistory");
		requestParams.put("jsonObj", encodeJson);
		
		return (Integer) HttpRequest.get(LibraryConstant.URL_INTERFACE_HISTORY, requestParams, new DelHistoryParseResponse() );
	}
	
	/**
	 * 删除收藏分类
	 * 
	 * @param username
	 * @param categoryCode
	 * @return
	 * @author wzp
	 * @Created 2017/02/19
	 */
	public static Integer delCollectCategory( String username, String categoryCode ) 
	{
		String json = 
				"{"
				+ "\"UserName\":\""+username+"\","
				+ "\"CategoryCode\":\""+categoryCode+"\""
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
		requestParams.put("requestType", "DeleteCategory");
		requestParams.put("jsonObj", encodeJson);
		
		return (Integer) HttpRequest.get(LibraryConstant.URL_INTERFACE_COLLECT, requestParams, new DelCollectCategoryParseResponse() );
	}
	
	/**
	 * 删除收藏资源
	 * 
	 * @param username
	 * @param dbCode
	 * @param sysId
	 * @return
	 * @author wzp
	 * @Created 2017/02/19
	 */
	public static Integer delCollectResource( String username, String dbCode, String sysId ) 
	{
		String json = 
				"{"
				+ "\"UserName\":\""+username+"\","
				+ "\"DbCode\":\""+dbCode+"\","
				+ "\"SysId\":\""+sysId+"\""
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
		requestParams.put("requestType", "DeleteCollect");
		requestParams.put("jsonObj", encodeJson);
		
		return (Integer) HttpRequest.get(LibraryConstant.URL_INTERFACE_COLLECT, requestParams, new DelCollectResourceParseResponse() );
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
	public static Integer delBookMark( String username, String id ) 
	{
		String json = 
				"{"
				+ "\"UserName\":\""+username+"\","
				+ "\"Id\":\""+id+"\""
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
		requestParams.put("requestType", "DeleteBookmark");
		requestParams.put("jsonObj", encodeJson);
		
		return (Integer) HttpRequest.get(LibraryConstant.URL_INTERFACE_BOOKMARK, requestParams, new DelBookMarkParseResponse() );
	}	
	
	/**
	 * 得到推荐列表
	 * 
	 * @param username
	 * @return
	 * @author wzp
	 * @Created 2017/02/22
	 */
	public static EbookInfoEntity getRecommendList( int type, String username ) 
	{
		Map<String, String> requestParams = new HashMap<String, String>();
		switch( type )
		{
			case LibraryConstant.RECOMMEND_TYPE_GETPERSONALLIST:		//个性推荐
				requestParams.put("requestType", "GetPersonalList");
				requestParams.put("UserName", username);
				return (EbookInfoEntity) HttpRequest.get(LibraryConstant.URL_INTERFACE_RECOMMEND, requestParams, new GetEbookParseResponse() );
			case LibraryConstant.RECOMMEND_TYPE_GETLATESTSERIALIZED:	//最新更新
				requestParams.put("requestType", "GetLatestSerialized");
				return (EbookInfoEntity) HttpRequest.get(LibraryConstant.URL_INTERFACE_RECOMMEND, requestParams, new GetEbookParseResponse() );
			case LibraryConstant.RECOMMEND_TYPE_GETBOUTIQUEDATA:		//精品专区
				requestParams.put("requestType", "GetBoutiqueData");
				return (EbookInfoEntity) HttpRequest.get(LibraryConstant.URL_INTERFACE_RECOMMEND, requestParams, new GetEbookParseResponse() );
			default:
				return	null;
		}
	}	
	
	/**
	 * 得到查找结果列表
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param searchWord
	 * @param categoryType
	 * @return
	 * @author wzp
	 * @Created 2017/02/25
	 */
	public static EbookInfoEntity getSearchList( String pageIndex, String pageSize, String searchWord, int categoryType ) 
	{
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("pageIndex", pageIndex);
		requestParams.put("pageSize", pageSize);
		requestParams.put("searchField", "Title");
		requestParams.put("searchWord", searchWord);
		
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
	 * 更新阅读历史
	 * 
	 * @param username
	 * @return
	 * @author wzp
	 * @Created 2017/03/04
	 */
	public static HistoryEntity updateHistory( HistoryEntity entity ) 
	{
		if( null == entity )
		{
			return	null;
		}
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
			
			return	entity;
		}
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "UpdateHistory");
		requestParams.put("jsonObj", encodeJson);
		
		return (HistoryEntity) HttpRequest.get(LibraryConstant.URL_INTERFACE_HISTORY, requestParams, new AddHistoryParseResponse() );
	}
	
	/**
	 * 更新收藏分类
	 * 
	 * @param username
	 * @return
	 * @author wzp
	 * @Created 2017/03/04
	 */
	public static CollectCategoryEntity updateCollectCategory( CollectCategoryEntity entity ) 
	{
		if( null == entity )
		{
			return	null;
		}
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
			
			return	entity;
		}
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("requestType", "UpdateCollect");
		requestParams.put("jsonObj", encodeJson);
		
		return (CollectCategoryEntity) HttpRequest.get(LibraryConstant.URL_INTERFACE_COLLECT, requestParams, new AddCollectCategoryParseResponse() );
	}	
}
