package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.library.asynctask.GetInformationAsyncTask;
import com.sunteam.library.utils.LibraryConstant;

/**
 * @Destryption 图书馆新闻分类列表，是固定菜单
 * @Author Jerry
 * @Date 2017-2-4 下午3:38:19
 * @Note
 */
public class LibraryNewsCategoryList extends MenuActivity {
	private String fatherPath;	//父目录路径
	
	public void onCreate(Bundle savedInstanceState) {
		initView();
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
		new GetInformationAsyncTask(this, fatherPath, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1, LibraryConstant.LIBRARY_INFO_PAGESIZE, selectItem);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}

	private void initView() {
		fatherPath = this.getIntent().getStringExtra(LibraryConstant.INTENT_KEY_FATHER_PATH);
	}

}
