package com.sunteam.library.entity;

import java.io.Serializable;

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
	
	public String uniqueId;
	public String dbCode;
	public String sysId;
	public String url;
	public String downloadUrl;		//下载地址
	public String imageUrl;			//图片地址
	public String title;			//书名
	public String author;			//作者
	public String studio;
	public String keyWords;			//关键字
	public String content;
	public String abs;				//摘要
	public String dataType;
	public String titleHighLighter;
	public String createTime;
	public String boutiqueTime;
	public String browseCount;
	public String publish;			//出版社
	public String chapterList;
	public String copyrightExpirationDate;
	public String categoryCode;
	public String isBoutique;
	public String totalNumber;
	public String identifier;
	public String belongIdentifier;
	public String chapterNumber;
	public String host;
	public String authorAbstract;
	public String duration;
	public String entryPoint;
	public String outPoint;
	public String pubTime;
	public String serial;
	public String resourceType;
	public String detailUrl;
	public String onlineViewUrl;
}
