package com.sunteam.library.entity;

import java.io.Serializable;

/**
 * 书签实体类
 * 
 * @author wzp
 * 
 */
public class BookmarkEntity implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8872374053931056239L;
	
    public int id;				//标识id
    public String userName;		//用户名
    public String bookId;		//书目ID
    public String addedTime;	//创建时间
    public int begin;			//字节流位置
    public int chapterIndex;	//章节号
    public String chapterTitle;	//章节名称
    public String markName;		//书签名
    public String percent;		//进度
}
