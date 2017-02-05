package com.sunteam.library.entity;

import java.io.Serializable;

/**
 * 收藏分类信息类
 * 
 * @author wzp
 * 
 */
public class CollectCategoryEntity implements Serializable
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3179387371554575390L;
	
	public int id;					//记录id
	public String userName;			//用户名
	public String categoryName;		//分类名称
	public String categoryCode;		//分类编码
	public String categoryFullName;	//完整的分类名，格式"电子图书-古典文学"
    public int resType;				//资源类型 1:有声读物 2:电子图书  3:视频影像
    public int sync;				//数据同步标志，0：未同步 1：已同步
}
