package com.sunteam.library.utils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;

/**
 * @Destryption 发送get请求工具类
 * @Author Jerry
 * @Date 2017-1-19 下午5:44:45
 * @Note
 */
public class HttpGetUtils {

	private HttpClient client;
	private HttpResponse response;
	private HttpGet httpGet;
	private StringBuilder parameterStr = new StringBuilder(); // 保存参数

	public HttpGetUtils() {
		client = new DefaultHttpClient();
	}

	/**
	 * 添加get请求参数
	 * 
	 * @param key
	 * @param value
	 */
	public void addGetParameter(String key, String value) {
		if (0 == parameterStr.length()) {
			parameterStr.append("?");
		} else {
			parameterStr.append("&");
		}
		parameterStr.append(key);
		parameterStr.append("=");
		parameterStr.append(value);
	}

	/**
	 * 发送Get请求,把结果通过handler发送出去，若失败则结果为空字符串。
	 * 
	 * @param url
	 * @param handler
	 */
	public void sendGet(final String url, final Handler handler, final int what) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String result = "";
				try {
					StringBuffer requestUrl = new StringBuffer(url);
					requestUrl.append(parameterStr);
					httpGet = new HttpGet(requestUrl.toString());
					// MenuGlobal.debug(requestUrl.toString());
					response = client.execute(httpGet);
					// MenuGlobal.debug("[HttpGetUtils] StatusCode = " + response.getStatusLine().getStatusCode());
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
						// MenuGlobal.debug("[HttpGetUtils] response = " + response.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// MenuGlobal.debug("[HttpGetUtils] result = " + result);
				Message msg = handler.obtainMessage(what, result);
				handler.sendMessage(msg);
			}
		}, "HttpGet").start();
	}
}
