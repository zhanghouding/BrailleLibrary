package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.library.asynctask.GetAudioChapterAsyncTask;
import com.sunteam.library.asynctask.GetCategoryAsyncTask;
import com.sunteam.library.asynctask.GetEbookAsyncTask;
import com.sunteam.library.asynctask.GetEbookChapterAsyncTask;
import com.sunteam.library.asynctask.GetInformationAsyncTask;
import com.sunteam.library.asynctask.GetVideoChapterAsyncTask;
import com.sunteam.library.entity.CategoryInfoNodeEntity;
import com.sunteam.library.entity.EbookNodeEntity;
import com.sunteam.library.entity.InformationEntity;
import com.sunteam.library.utils.LibraryConstant;

public class LibraryNewsCategoryList extends MenuActivity {

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
		new GetInformationAsyncTask(this, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1, LibraryConstant.LIBRARY_RESOURCE_PAGESIZE, selectItem);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		/*Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mEbookNodeEntityList = (ArrayList<EbookNodeEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		dataType = intent.getIntExtra(LibraryConstant.INTENT_KEY_TYPE, 0);
		mMenuList = getListFromEbookNodeEntity(mEbookNodeEntityList);
		bookCount = mMenuList.size();
		bookCount = intent.getIntExtra(LibraryConstant.INTENT_KEY_BOOKCOUNT, bookCount);*/
	}

}
