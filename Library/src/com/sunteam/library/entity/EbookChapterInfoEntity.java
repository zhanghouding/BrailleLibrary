package com.sunteam.library.entity;

import java.io.Serializable;

/**
 * 电子书章节信息类
 * 
 * @author wzp
 * 
 */
public class EbookChapterInfoEntity implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2132913121556021352L;
	
	public String chapterName;	//章节名称
	public String chapterIndex;	//章节索引
	public String content;		//章节内容
}
