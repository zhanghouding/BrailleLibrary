package com.sunteam.library.entity;

import java.io.Serializable;

/**
 * 分类信息节点
 * 
 * @author wzp
 * 
 */
public class CategoryInfoNodeEntity implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6918726873033133846L;
	
	public int father;			// 父节点序号
	public int seq;				// 节点序号
	public int level;			// 节点等级
	public String name;		// 分类名称
	public String code;		// 分类编码
	public String type;		// 分类类型
	public int recordCount;
}
