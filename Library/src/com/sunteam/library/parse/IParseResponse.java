package com.sunteam.library.parse;

import java.io.InputStream; 

/**
 * 解析Response接口
 * @author wzp
 * @date 2017/01/24
 */
public interface IParseResponse 
{
	public Object parseResponse(InputStream inputStream) throws Exception;
}
