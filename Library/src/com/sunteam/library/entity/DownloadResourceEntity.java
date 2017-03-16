package com.sunteam.library.entity;

import java.io.Serializable;

/**
 * 下载资源信息类
 * 
 * @author wzp
 * 
 */
public class DownloadResourceEntity implements Serializable
{	 
	/**
	 * 
	 */
	private static final long serialVersionUID = -6364243932866381577L;
	
	public int _id;					//数据库自动生成的id
	public String userName;			//用户名
	public int resType;				//资源类型 1:有声读物 2:电子图书  3:视频影像
	public String categoryCode;		//分类编码
	public String categoryFullName;	//完整的分类名，格式"电子图书-古典文学"
	public String title;			//资源名称
	public String dbCode;			//数据库编码
	public String sysId;			//系统id
	public String identifier;		//电子书ID
	public int chapterCount;		//章节总数
	public int status;				//下载状态 (0：等待下载 1：正在下载 2：下载完成)
	
	public int curDownloadChapterIndex;	//当前正在下载的章节序号，只有status==1才有用。
}
