package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;

public class DownloadDirActivity extends MenuActivity {

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
		case 0: // 电子书
//			list = getResources().getStringArray(R.array.settings_voice_list);
//			cls = EbookOfflineActivity.class;
			break;
		case 1: // 有声书
//			list = getResources().getStringArray(R.array.settings_language_list);
			cls = AudioOfflineActivity.class;
			break;
		case 2: // 口述影像
//			list = getResources().getStringArray(R.array.settings_power_saving_list);
			cls = VideoOfflineActivity.class;
			break;
		default:
			break;
		}

		startNextActivity(cls, selectItem, menuItem, list);
	}

	private void startNextActivity(Class<?> cls, int selectItem, String title, String[] list){
		/*Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, list); // 菜单列表

		intent.setClass(this, cls);
		 
        // 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);*/
	}

}
