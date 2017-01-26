package com.sunteam.library.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 电子书信息类
 * 
 * @author wzp
 * 
 */
public class EbookInfoEntity implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8121339564495378442L;
	
	public String moreUrl;
    public String dbName;
    public int pageSize;			//总共页码
    public int pageIndex;			//当前页码，从1开始
    public int itemCount;			//总共有多少本书
    public boolean isFirstPage;		//是否第一页
    public boolean isLastPage;		//是否最后一页
    public int pageCount;			//总共的页数
    public ArrayList<EbookNodeEntity> list = null;	//保存每本书的节点信息
}
