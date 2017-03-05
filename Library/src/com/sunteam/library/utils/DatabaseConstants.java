package com.sunteam.library.utils;

/**
 * 该类定义了数据库基础常量。
 * 
 * @author wzp
 */
public class DatabaseConstants 
{
	public static final int DATABASE_VERSION = 1;					//数据库版本
	public static final String LIBRARY_DATABASE_NAME = "library.db";//图书馆数据库名称
	
	public static final String CATEGORY_TABLE_NAME = "categorys";	//分类表名称
	public static final String RESOURCE_TABLE_NAME = "resources";	//资源表名称
	public static final String CHAPTER_TABLE_NAME = "chapters";		//章节表名称
	public static final String INFO_TABLE_NAME = "infos";			//资讯表名称	
	public static final String HISTORY_TABLE_NAME = "historys";		//历史表名称
	public static final String BOOKMARK_TABLE_NAME = "bookmarks";	//书签表名称
	public static final String COLLECT_CATEGORY_TABLE_NAME = "collect_categorys";	//收藏分类表名称
	public static final String COLLECT_RESOURCE_TABLE_NAME = "collect_resources";	//收藏资源表名称
	
	public static final String RESOURCE_TYPE = "resource_type";		//资源类型
	//分类表字段
	public static final String CATEGORY_FATHER = "father";			//父节点序号
	public static final String CATEGORY_SEQ = "seq";				//节点序号
	public static final String CATEGORY_LEVEL = "level";			//节点等级
	public static final String CATEGORY_NAME = "name";				//分类名称
	public static final String CATEGORY_CODE = "code";				//分类编码
	public static final String CATEGORY_TYPE = "type";				//分类类型

	//资源表字段
	public static final String RESOURCE_CATEGORYCODE = "categoryCode";	//分类编码
	public static final String RESOURCE_DBCODE = "dbCode";			//数据编码
	public static final String RESOURCE_SYSID = "sysId";			//系统ID
	public static final String RESOURCE_TITLE = "title";			//书名
	public static final String RESOURCE_AUTHOR = "author";			//作者
	public static final String RESOURCE_KEYWORDS = "keyWords";		//关键字
	public static final String RESOURCE_ABS = "abs";				//摘要
	public static final String RESOURCE_PUBLISH = "publish";		//出版社
	public static final String RESOURCE_IDENTIFIER = "identifier";	//电子书ID
	
	//章节表字段
	public static final String CHAPTER_FATHER = "father";			//父节点序号
	public static final String CHAPTER_SEQ = "seq";					//节点序号
	public static final String CHAPTER_LEVEL = "level";				//节点等级
	public static final String CHAPTER_DBCODE = "dbCode";			//数据编码
	public static final String CHAPTER_IDENTIFIER = "identifier";	//电子书ID
	public static final String CHAPTER_SYSID = "sysId";				//系统ID
	public static final String CHAPTER_INDEX = "idx";				//章节索引
	public static final String CHAPTER_NAME = "name";				//章节名称
	public static final String CHAPTER_URL = "url";					//章节URL
	
	//资讯表字段
	public static final String INFO_TITLE = "title";				//资讯标题
	public static final String INFO_DATE = "date";					//日期
	
	//历史表字段
	public static final String HISTORY_ID = "id";						//记录id
	public static final String HISTORY_USERNAME = "userName";			//用户名
    public static final String HISTORY_TITLE = "title";					//标题
    public static final String HISTORY_DBCODE = "dbCode";				//数据编码
    public static final String HISTORY_SYSID = "sysId";					//系统id
    public static final String HISTORY_RESTYPE = "resType";				//资源类型 1:有声读物 2:电子图书  3:视频影像
    public static final String HISTORY_LCINDEX = "lastChapterIndex";	//最后阅读的章节序号
    public static final String HISTORY_ENTERPOINT = "enterPoint";		//最后阅读的音视频时间点，格式"00:00:00"
    public static final String HISTORY_URL = "url";
    public static final String HISTORY_CTIME = "createTime";			//创建时间，格式"2017-02-03T19:42:14"
    public static final String HISTORY_UTIME = "updateTime";			//更新时间，格式"2017-02-03T19:42:14",
    public static final String HISTORY_BOOKTITLE = "bookTitle";			//标题
    public static final String HISTORY_COVERURL = "coverUrl";			//封面图片url
    public static final String HISTORY_PERCENT = "percent";				//电子书阅读进度，格式"0.00%"
    public static final String HISTORY_CFULLNAME = "categoryFullName";	//完整的分类名，格式"有声读物-刘兰芳-古今荣耻谈"
    public static final String HISTORY_CATEGORYCODE = "categoryCode";	//分类编码
    
    //收藏分类表字段
  	public static final String COLLECT_CATEGORY_ID = "id";				//记录id
  	public static final String COLLECT_CATEGORY_USERNAME = "userName";	//用户名
  	public static final String COLLECT_CATEGORY_NAME = "categoryName";	//分类名称
  	public static final String COLLECT_CATEGORY_FULLNAME = "categoryFullName";	//完整的分类名，格式"有声读物-刘兰芳-古今荣耻谈"
  	public static final String COLLECT_CATEGORY_CODE = "categoryCode";	//分类名称
  	public static final String COLLECT_CATEGORY_RESTYPE = "resType";	//分类类型
  	
  	//收藏资源表字段
  	public static final String COLLECT_RESOURCE_ID = "id";				//记录id
  	public static final String COLLECT_RESOURCE_USERNAME = "userName";	//用户名
  	public static final String COLLECT_RESOURCE_TITLE = "title";		//书名
  	public static final String COLLECT_RESOURCE_DBCODE = "dbCode";		//数据编码
  	public static final String COLLECT_RESOURCE_SYSID = "sysId";		//系统id
  	public static final String COLLECT_RESOURCE_RESTYPE = "resType";	//分类类型
  	public static final String COLLECT_RESOURCE_FULLNAME = "categoryFullName";	//完整的分类名，格式"有声读物-刘兰芳-古今荣耻谈"
  	public static final String COLLECT_RESOURCE_COVERURL = "coverUrl";		//封面地址
  	public static final String COLLECT_RESOURCE_CREATETIME = "createTime";	//创建时间
}
