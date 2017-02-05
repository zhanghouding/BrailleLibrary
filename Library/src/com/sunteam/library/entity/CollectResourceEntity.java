package com.sunteam.library.entity;

import java.io.Serializable;

/**
 * 收藏资源信息类
 * 
 * @author wzp
 * 
 */
public class CollectResourceEntity implements Serializable
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 438081827045572686L;
  	 
	public int id;					//记录id
	public String userName;			//用户名
	public String title;			//资源名称
	public String dbCode;			//数据库编码
	public String sysId;			//系统id
	public int resType;				//资源类型 1:有声读物 2:电子图书  3:视频影像
	public String categoryFullName;	//完整的分类名，格式"电子图书-古典文学"
	public String coverUrl;			//封面url
	public String createTime;		//创建时间
    public int sync;				//数据同步标志，0：未同步 1：已同步
}
