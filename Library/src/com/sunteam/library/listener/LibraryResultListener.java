package com.sunteam.library.listener;

import java.util.ArrayList;

/**
 * @Destryption 调用数字图书馆API返回结果后的公共回调。通过回调传递数据，避免把异步任务类放到界面程序中，保持程序模块的独立性。
 * @Author Jerry
 * @Date 2017-3-31 下午4:40:51
 * @Note
 */
public interface LibraryResultListener {

	public void onResult(ArrayList<Object> list);

	public void onFail(String error);
}
