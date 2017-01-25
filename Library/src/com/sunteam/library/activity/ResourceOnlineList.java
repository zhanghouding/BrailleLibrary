package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.library.asynctask.GetEbookChapterAsyncTask;
import com.sunteam.library.entity.EbookInfoEntity;
import com.sunteam.library.utils.LibraryConstant;

public class ResourceOnlineList extends MenuActivity {
	private int dataType = 0; // 数据类别：电子书、有声书、口述影像
	private ArrayList<EbookInfoEntity> mEbookInfoEntityList = new ArrayList<EbookInfoEntity>();

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
		switch(dataType){
		case LibraryConstant.LIBRARY_DATATYPE_EBOOK:
			String dbCode = mEbookInfoEntityList.get(selectItem).dbCode;
			String identifier = mEbookInfoEntityList.get(selectItem).identifier;
			new GetEbookChapterAsyncTask(this, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbCode, identifier);
		case LibraryConstant.LIBRARY_DATATYPE_AUDIO:
			break;
		case LibraryConstant.LIBRARY_DATATYPE_VIDEO:
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mEbookInfoEntityList = (ArrayList<EbookInfoEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		dataType = intent.getIntExtra(LibraryConstant.INTENT_KEY_TYPE, 0);
		mMenuList = getListFromEbookInfoEntity(mEbookInfoEntityList);
	}

	private ArrayList<String> getListFromEbookInfoEntity(ArrayList<EbookInfoEntity> listSrc) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < listSrc.size(); i++) {
			list.add(listSrc.get(i).title);
		}

		return list;
	}

}
