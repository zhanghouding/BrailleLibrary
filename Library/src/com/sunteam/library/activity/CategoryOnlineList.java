package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.library.asynctask.GetCategoryAsyncTask;
import com.sunteam.library.asynctask.GetEbookAsyncTask;
import com.sunteam.library.entity.CategoryInfoNodeEntity;
import com.sunteam.library.utils.LibraryConstant;

public class CategoryOnlineList extends MenuActivity {
	private int fatherId = -1;
	private int dataType = 0; // 数据类别：电子书、有声书、口述影像
	private ArrayList<CategoryInfoNodeEntity> mCategoryInfoNodeEntityList;

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
		ArrayList<CategoryInfoNodeEntity> list = GetCategoryAsyncTask.getChildNodeList(fatherId + 1);
		if(0 == list.size()) {
			String pageIndex = "1";
			String pageSize = "" + LibraryConstant.LIBRARY_RESOURCE_PAGESIZE;
			String categoryCode = mCategoryInfoNodeEntityList.get(selectItem).code;
			new GetEbookAsyncTask(this, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pageIndex, pageSize, categoryCode, "" + dataType);
		} else {
			startNextActivity(selectItem, menuItem);
		}
	}

	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		fatherId = intent.getIntExtra(LibraryConstant.INTENT_KEY_FATHER, -1);
		dataType = intent.getIntExtra(LibraryConstant.INTENT_KEY_TYPE, 0);
		mCategoryInfoNodeEntityList = GetCategoryAsyncTask.getChildNodeList(fatherId);
		mMenuList = getListFromCategoryInfoNodeEntity(mCategoryInfoNodeEntityList);
	}

	private ArrayList<String> getListFromCategoryInfoNodeEntity(ArrayList<CategoryInfoNodeEntity> listSrc) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < listSrc.size(); i++) {
			list.add(listSrc.get(i).name);
		}

		return list;
	}

	private void startNextActivity(int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, menuItem); // 菜单名称
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER, fatherId + 1); // 父节点ID
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, dataType); // 数据类别：电子书、有声书、口述影像
		intent.setClass(this, CategoryOnlineList.class);

		startActivity(intent);
	}

	/*private void startResourceList(int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, menuItem); // 菜单名称
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, dataType); // 数据类别：电子书、有声书、口述影像
		intent.setClass(this, ResourceOlineList.class);

		startActivity(intent);
	}*/

}
