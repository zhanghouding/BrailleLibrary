package com.sunteam.library.utils;

/**
 * 该类定义了数据库基础常量。
 * 
 * @author wzp
 */
public class DatabaseConstants 
{
	public static final int DATABASE_VERSION = 1;					//数据库版本
	public static final String DATABASE_NAME = "library.db";		//数据库名称
	public static final String CATEGORY_TABLE_NAME = "categorys";	//分类表名称
	public static final String RESOURCE_TABLE_NAME = "resources";	//资源表名称
	public static final String CHAPTER_TABLE_NAME = "chapters";		//章节表名称
	
	public static final String RESOURCE_TYPE = "resource_type";		//资源类型
	//分类表字段
	public static final String CATEGORY_FATHER = "father";			//父节点序号
	public static final String CATEGORY_SEQ = "seq";				//节点序号
	public static final String CATEGORY_LEVEL = "level";			//节点等级
	public static final String CATEGORY_NAME = "name";				//分类名称
	public static final String CATEGORY_CODE = "code";				//分类编码
	public static final String CATEGORY_TYPE = "type";				//分类类型

	//资源表字段
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
	public static final String CHAPTER_INDEX = "idx";				//章节索引
	public static final String CHAPTER_NAME = "name";				//章节名称
	public static final String CHAPTER_URL = "url";					//章节URL
}
