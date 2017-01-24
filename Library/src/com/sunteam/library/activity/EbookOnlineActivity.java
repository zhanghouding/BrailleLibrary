package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.library.asynctask.GetCategoryAsyncTask;
import com.sunteam.library.entity.CategoryInfoNodeEntity;
import com.sunteam.library.utils.LibraryConstant;

public class EbookOnlineActivity extends MenuActivity {
	private static final int LIBRARY_DATA_TYPE_EBOOK = 0; // 表示电子书数据类别
	private int categoryLevel = 0; // 记录电子书当前分类所在的级别
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
		
		
	}

	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		fatherId = intent.getIntExtra(LibraryConstant.INTENT_KEY_FATHER, -1);
		dataType = intent.getIntExtra(LibraryConstant.INTENT_KEY_TYPE, 0);
		mCategoryInfoNodeEntityList = GetCategoryAsyncTask.getChildNodeList(fatherId);
		mMenuList = getListFromCategoryInfoNodeEntity(mCategoryInfoNodeEntityList);
	}

	private ArrayList<String> getListFromCategoryInfoNodeEntity(ArrayList<CategoryInfoNodeEntity> listSrc){
		
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < listSrc.size(); i++)
		{
			list.add( listSrc.get(i).name );
		}
		
		return	list;
	}
}
