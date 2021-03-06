package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.library.asynctask.GetDownloadResourceAsyncTask;

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
		case 1: // 已下载
			new GetDownloadResourceAsyncTask(this, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, selectItem);
			break;
		default:
			break;
		}
	}

}
