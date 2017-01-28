package com.sunteam.library.entity;

import java.io.Serializable;

/**
 * 盲人咨询信息类
 * 
 * @author wzp
 * 
 */
public class InformationEntity implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6346472193474030745L;
	
	public String title;	//标题
    public String date;		//日期
    public String fullpath;	//缓存文件全路径
}
