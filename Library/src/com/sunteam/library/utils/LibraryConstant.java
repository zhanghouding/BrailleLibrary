package com.sunteam.library.utils;

import android.os.Environment;

public class LibraryConstant {
	// 数字图书馆登录ID保存位置
	public static final String ACCESSIBILITY_DEVICEID = "accessibility_deviceid"; // 保存数字图书馆账号设置名
	
	// 数字图书馆本地数据路径名
	public static final String LIBRARY_ROOT_PATH = Environment.getExternalStorageDirectory()+"/s918p/library/"; // 数字图书馆要保存的数据Home目录

	// 云端访问URL
	public static final String URL_INTERFACE_USER = "UserInterface.ashx"; // 用户相关接口
	public static final String URL_INTERFACE_EBOOK = "EbookInterface.ashx"; // 图书相关接口
	public static final String URL_INTERFACE_AUDIO = "AudioInterface.ashx"; // 音频相关接口
	public static final String URL_INTERFACE_VIDEO = "VideoInterface.ashx"; // 视频相关接口
	public static final String URL_INTERFACE_INFO = "InformationInterface.ashx";	//咨询相关接口

	// 类别常量
	public static final int LIBRARY_CATEGORY_ROOT_ID = -1; // 类别树形结构中的根节点ID
	public static final int LIBRARY_DATATYPE_EBOOK = 0; // 电子书
	public static final int LIBRARY_DATATYPE_AUDIO = 1; // 有声书
	public static final int LIBRARY_DATATYPE_VIDEO = 2; // 口述影像

	// 咨询类别常量
	public static final int LIBRARY_INFOTYPE_NOTICE = 0; 		// 新闻公告
	public static final int LIBRARY_INFOTYPE_SERVICEINFO = 1; 	// 服务资讯
	public static final int LIBRARY_INFOTYPE_LIBMESSAGE = 2; 	// 文化活动

	// Intent传递属性
	public static final String INTENT_KEY_TYPE = "library_type"; // 数据分类
	public static final String INTENT_KEY_BOOKCOUNT = "library_resource_count"; // 资源总数
	public static final String INTENT_KEY_FATHER = "library_father_id"; // 父节点ID
	public static final String INTENT_KEY_FATHER_PATH = "library_father_path"; // 父节点路径
	public static final String INTENT_KEY_IDENTIFIER = "library_ebook_identifier";	//电子书ID
	public static final String INTENT_KEY_URL = "library_resource_url";	//资源路径

	// 资源列表常量
	public static final int LIBRARY_RESOURCE_PAGESIZE = 10; // 资源列表时一页的资源数
	
	// SharePref文件名，KEY类型，如：登录状态，有些界面需要根据登录状态获取不同的数据源
	public static final String LIBRARY_CONFIG_FILE = "config";
	public static final String LIBRARY_LOGIN_STATE = "login_state";

	// Handler处理的消息类型
	public static final int MSG_CONFIRMDIALOG_RETURN = 0; // 确认对话框返回;arg1:0确认,1取消
	public static final int MSG_PROMPTDIALOG_RETURN = 0; // 确认对话框返回;arg1:0确认,1取消
	public static final int MSG_HTTP_USER_AUTH = 1; // 用户信息认证
	public static final int MSG_HTTP_USER_UPDATE = 2; // 用户信息更新
	public static final int MSG_HTTP_USER_GETPWD = 3; // 找回密码
	public static final int MSG_HTTP_AUDIO_CATEGORY_LIST = 4; // 获取音频分类列表
	public static final int MSG_HTTP_AUDIO_CATEGORY_NAME = 5; // 获取音频分类名称
	public static final int MSG_HTTP_AUDIO_QUERY_LIST = 6; // 获取音频资源检索列表
	public static final int MSG_HTTP_AUDIO_DETAIL = 7; // 获取音频详细信息
	public static final int MSG_HTTP_EBOOK_CATEGORY_LIST = 4; // 获取电子书分类列表
	public static final int MSG_HTTP_EBOOK_CATEGORY_NAME = 5; // 获取电子书分类名称
	public static final int MSG_HTTP_EBOOK_QUERY_LIST = 6; // 获取电子书资源检索列表
	public static final int MSG_HTTP_EBOOK_DETAIL = 7; // 获取电子书详细信息
	public static final int MSG_HTTP_EBOOK_CHAPTER_CONTENT = 8; // 获取电子书指定章节内容
	public static final int MSG_HTTP_VIDEO_CATEGORY_LIST = 9; // 获取视频分类列表
	public static final int MSG_HTTP_VIDEO_CATEGORY_NAME = 10; // 获取视频分类名称
	public static final int MSG_HTTP_VIDEO_QUERY_LIST = 11; // 获取视频资源检索列表
	public static final int MSG_HTTP_VIDEO_DETAIL = 12; // 获取视频详细信息
	public static final int MSG_HTTP_FAVROITE_SEARCH = 13; // 查询收藏夹

	public static final String CACHE_FILE_SUFFIX = ".lib";	//缓存文件后缀
}
