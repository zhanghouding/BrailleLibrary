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
	
	public int father;			// 父节点序号
	public int seq;				// 节点序号
	public int level;			// 节点等级
	
	public String chapterName;	//章节名称
	public String chapterIndex;	//章节索引
	public String content;		//章节内容
}
