package com.sunteam.library.utils;

public class LibraryConstant {
	// 数字图书馆登录ID保存位置
	public static final String ACCESSIBILITY_DEVICEID = "accessibility_deviceid"; // 保存数字图书馆账号设置名

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

}
