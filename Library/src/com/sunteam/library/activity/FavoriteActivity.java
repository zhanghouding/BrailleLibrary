package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;

public class FavoriteActivity extends MenuActivity {

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
		/*String[] list = null;
		Class<?> cls = null;

		switch (selectItem) {
		case 0: // 新闻
			cls = FavoriteBrowseActivity.class;
			break;
		case 1: // 电子书
			cls = FavoriteBrowseActivity.class;
			break;
		case 2: // 有声书
			cls = FavoriteBrowseActivity.class;
			break;
		case 3: // 口述影像
			cls = FavoriteBrowseActivity.class;
			break;
		default:
			break;
		}*/

		startNextActivity(selectItem, menuItem);
	}

	private void startNextActivity(int selectItem, String title) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, selectItem); // 选中的选项

		intent.setClass(this, FavoriteActivity.class);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);
	}

}
