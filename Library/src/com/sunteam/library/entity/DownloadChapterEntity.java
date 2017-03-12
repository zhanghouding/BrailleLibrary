package com.sunteam.library.entity;

import java.io.Serializable;

/**
 * 下载章节信息类
 * 
 * @author wzp
 * 
 */
public class DownloadChapterEntity implements Serializable
{	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1096193880103029300L;
	
	public int recorcdId;			//对应的下载资源记录ID
  	public String chapterName;		//章节名称
  	public int chapterIndex;		//章节序号
  	public int chapterStatus;		//章节下载状态 (0：等待下载 1：正在下载 2：下载完成)
  	public String chapterPath;		//章节下载路径
  	public String chapterUrl;		//章节下载URL
}
