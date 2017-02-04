package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.dialog.ConfirmListener;
import com.sunteam.library.R;
import com.sunteam.library.utils.WifiUtils;

public class MylibraryActivity extends MenuActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode || null == data) { // 在子菜单中回传的标志
			return;
		}

	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		String[] list = null;
		Class<?> cls = null;
		
		switch (selectItem) {
		case 0: // 收藏夹
			list = getResources().getStringArray(R.array.library_favorite_list);
			cls = FavoriteActivity.class;
			break;
		case 1: // 最近阅读历史
			cls = RecentBrowseActivity.class;
			break;
		case 2: //下载文件夹
			list = getResources().getStringArray(R.array.library_download_list);
			cls = DownloadDirActivity.class;
			break;
		case 3: // 图书馆账号绑定
			cls = AccountBindActivity.class;
			break;
		case 4: // 图书馆新闻
			cls = LibraryNewsActivity.class;
			break;
		default:
			break;
		}

		/*if (WifiUtils.checkWifiState(this)) {
			startNextActivity(cls, selectItem, menuItem, list);
		} else {
			startWifi();
		}*/
	}

	private void startNextActivity(Class<?> cls, int selectItem, String title, String[] list){
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, list); // 菜单列表

		intent.setClass(this, cls);
		 
        // 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);
	}

	private void startWifi() {
		String s = getResources().getString(R.string.library_startwifi);
		ConfirmDialog mConfirmDialog = new ConfirmDialog(this, s);
		mConfirmDialog.setConfirmListener(new ConfirmListener() {

			@Override
			public void doConfirm() {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						startWifiSetting();
					}
				}, 10);
			}

			@Override
			public void doCancel() {
			}
		});
		mConfirmDialog.show();
	}

	private void startWifiSetting() {
		Intent intent = new Intent();
		String packageName = "com.sunteam.settings";
		String className = "com.sunteam.settings.activity.WifiList";
		intent.setClassName(packageName, className);
		String title = getResources().getString(R.string.library_wifi_setting);
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		startActivity(intent);
	}

}
