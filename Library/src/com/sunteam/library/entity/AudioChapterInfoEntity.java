package com.sunteam.library.entity;

import java.io.Serializable;

/**
 * 有声书章节信息类
 * 
 * @author wzp
 * 
 */
public class AudioChapterInfoEntity implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8376565224600070970L;
	
	public String title;
	public String detailInfo;
	public String abs;
	public String attachmentList;
	public String audioUrl;
	public String enterPoint;
	public int outPoint;
	public int detailUrl;
	public String imageUrl;
	public String databaseCode;
	public int audioType;
	public String sysId;
	public int chapterIndex;
	public String downloadUrl;
	public String source;
	public String columns;
	public String subItemList;
	public String uniqueId;
	public String updateTime;
	public String downloadCount;
	public String browseCount;
	public String categoryName;
	public String responsible;
	public String keyWords;
	public int lastChapterIndex;
	public boolean hasReadHistory;
	public boolean isAuthenticated;
	public String parentCategroyCode;
	public String parentCategroyName;
	public int pageSize;
	public int pageIndex;
	public int itemCount;
	public boolean isFirstPage;
	public boolean isLastPage;
	public int pageCount;
}
