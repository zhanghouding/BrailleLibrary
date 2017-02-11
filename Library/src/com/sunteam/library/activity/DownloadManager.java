package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;

/**
 * @Destryption 下载管理器：正在下载和已下载
 * @Author Jerry
 * @Date 2017-2-10 上午9:03:35
 * @Note
 */
public class DownloadManager extends MenuActivity {

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode || null == data) { // 在子菜单中回传的标志
			return;
		}
		switch(requestCode){
		case 0: // 正在下载
			break;
		case 1: // 已下载
			break;
		default:
			break;
		}

	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		switch(selectItem){
		case 0: // 正在下载
			break;
		case 1: // 已下载
			break;
		default:
			break;
		}
	}

	// 进入查看书签列表界面；如果需要在异步任务中获取云端数据，则需要在成功获取数据后调用该方法
	public void startNextActivity(Class<?> cls, int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, menuItem);
		intent.setClass(this, cls);

		// 第二个参数requestCode用于区分谁启动了Activity，或者说是从哪一个Acitivty返回的。
		startActivityForResult(intent, selectItem);
	}

}
