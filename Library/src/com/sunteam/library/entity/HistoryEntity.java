package com.sunteam.library.entity;

import java.io.Serializable;

/**
 * 阅读历史信息类
 * 
 * @author wzp
 * 
 */
public class HistoryEntity implements Serializable
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6519721044948456201L;
	
	public int id;					//记录id
	public String userName;			//用户名
    public String title;			//标题
    public String dbCode;			//数据编码
    public String sysId;			//系统id
    public int resType;				//资源类型 0:电子图书 1:有声读物 2:视频影像
    public int lastChapterIndex;	//最后阅读的章节序号
    public String enterPoint;		//最后阅读的音视频时间点，格式"00:00:00"
    public String url;
    public String createTime;		//创建时间，格式"2017-02-03T19:42:14"
    public String updateTime;		//更新时间，格式"2017-02-03T19:42:14",
    public String bookTitle;		//标题
    public String coverUrl;			//封面图片url
    public String percent;			//电子书阅读进度，格式"0.00%"
    public String categoryFullName;	//完整的分类名，格式"有声读物-刘兰芳-古今荣耻谈"
    public String categoryCode;		//分类编码
    
    public int sync;				//数据同步标志，0：未同步 1：已同步
}
