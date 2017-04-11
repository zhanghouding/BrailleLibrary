package com.sunteam.library.utils;

import android.os.Environment;

public class LibraryConstant {
	// 数字图书馆登录ID保存位置
	public static final String ACCESSIBILITY_DEVICEID = "accessibility_deviceid"; // 保存数字图书馆账号设置名
	
	// 数字图书馆本地数据路径名
	public static final String LIBRARY_ROOT_PATH = Environment.getExternalStorageDirectory()+"/s918p/library/"; // 数字图书馆要保存的数据Home目录
	public static final String BACKGROUND_MUSIC_PATH = Environment.getExternalStorageDirectory()+"/背景音乐/"; // 背景音乐路径，与电子书保持一致

	// 云端访问URL
	public static final String API_URL = "http://www.blc.org.cn/API/";
	public static final String URL_INTERFACE_USER = "UserInterface.ashx"; // 用户相关接口
	public static final String URL_INTERFACE_EBOOK = "EbookInterface.ashx"; // 图书相关接口
	public static final String URL_INTERFACE_AUDIO = "AudioInterface.ashx"; // 音频相关接口
	public static final String URL_INTERFACE_VIDEO = "VideoInterface.ashx"; // 视频相关接口
	public static final String URL_INTERFACE_INFO = "InformationInterface.ashx";	//咨询相关接口
	public static final String URL_INTERFACE_HISTORY = "ReadHistoryInterface.ashx";	// 阅读历史相关接口
	public static final String URL_INTERFACE_COLLECT = "CollectInterface.ashx";	// 收藏相关接口
	public static final String URL_INTERFACE_BOOKMARK = "BookmarkInterface.ashx";	// 书签相关接口
	public static final String URL_INTERFACE_RECOMMEND = "RecommendInterface.ashx";	// 推荐相关接口
	
	public static final int RECOMMEND_TYPE_GETPERSONALLIST = 0;		//个性推荐
	public static final int RECOMMEND_TYPE_GETLATESTSERIALIZED = 1;	//最新更新
	public static final int RECOMMEND_TYPE_GETBOUTIQUEDATA = 2;		//精品专区

	// 类别常量
	public static final int LIBRARY_CATEGORY_ROOT_ID = -1; // 类别树形结构中的根节点ID
	public static final int LIBRARY_DATATYPE_AUDIO = 1; // 有声书
	public static final int LIBRARY_DATATYPE_EBOOK = 2; // 电子书
	public static final int LIBRARY_DATATYPE_VIDEO = 3; // 口述影像

	// 通过DbCode 来区分资源类型；从推荐模块查询得到的资源列表可能会混合三种类型的数据
	public static final String LIBRARY_DBCODE_AUDIO = "sound"; // 有声书
	public static final String LIBRARY_DBCODE_EBOOK = "ebook"; // 电子书
	public static final String LIBRARY_DBCODE_VIDEO = "video"; // 口述影像

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
	public static final String INTENT_KEY_RESOURCE = "library_resource_name";	//资源名称
	public static final String INTENT_KEY_CATEGORY_CODE = "library_category_code";	//分类编码
	public static final String INTENT_KEY_CATEGORY_NAME = "library_category_name";	//分类名称
	public static final String INTENT_KEY_DBCODE = "library_dbCode";	//数据编码
	public static final String INTENT_KEY_SYSID = "library_sysId";		//系统id
	public static final String INTENT_KEY_PERCENT = "percent"; // 百分比
	
	// 资讯列表常量
	public static final int LIBRARY_INFO_PAGESIZE = 100;		// 盲人资讯列表时一页的数量
	// 资源列表常量
	public static final int LIBRARY_RESOURCE_PAGESIZE = 1000;	// 资源列表时一页的数量
	// 历史列表常量
	public static final int LIBRARY_HISTORY_PAGESIZE = 200;		// 历史记录列表时一页的数量
	// 收藏分类列表常量
	public static final int LIBRARY_COLLECT_CATEGORY_PAGESIZE = 200;	// 收藏分类记录列表时一页的数量
	// 收藏资源列表常量
	public static final int LIBRARY_COLLECT_RESOURCE_PAGESIZE = 200;	// 收藏资源记录列表时一页的数量
	
	// SharePref文件名，KEY类型，如：登录状态，有些界面需要根据登录状态获取不同的数据源
	public static final String LIBRARY_CONFIG_FILE = "config";
	public static final String LIBRARY_LOGIN_STATE = "login_state";
	public static final String LIBRARY_LOGIN_USERNAME = "login_username";
	public static final String LIBRARY_LOGIN_PASSWD = "login_passwd";

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
	
	public static final int RESULT_SUCCESS = 0;		//成功
	public static final int RESULT_FAIL = -1;		//失败
	public static final int RESULT_EXCEPTION = -2;	//异常
	
	// 公用的删除、清空菜单界面
	public static final int MYLIBRARY_FAVARITE_CATEGORY = 0; // 我收藏的分类
	public static final int MYLIBRARY_FAVARITE_RESOURCE = 1; // 我收藏的资源
	public static final int MYLIBRARY_READING_HISTORY = 2; // 我的阅读历史
	public static final int MYLIBRARY_DOWNLOADING = 3; // 正在下载
	public static final int MYLIBRARY_DOWNLOADED = 4; // 已下载
	
	public static final String USER_INFO = "user_info";	//保持用户信息的SharedPreferences
	public static final String USER_NAME = "user_name";
	public static final String USER_PASSWORD = "user_password";

	public static final int DOWNLOAD_STATUS_WAIT = 0;	//等待下载
	public static final int DOWNLOAD_STATUS_GOING = 1;	//下载中
	public static final int DOWNLOAD_STATUS_FINISH = 2;	//已完成
	
	public static final String ACTION_DOWNLOAD_STATUS = "android.intent.action.ACTION_DOWNLOAD_STATUS_CHANGE";
	
	public static final int ENCRYPT_FLAGS_LENGTH = 16;	//加密标记长度。
}
