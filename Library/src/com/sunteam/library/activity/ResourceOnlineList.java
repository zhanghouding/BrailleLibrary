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
import com.sunteam.library.asynctask.GetEbookChapterAsyncTask;
import com.sunteam.library.asynctask.GetVideoChapterAsyncTask;
import com.sunteam.library.entity.EbookNodeEntity;
import com.sunteam.library.utils.LibraryConstant;

public class ResourceOnlineList extends MenuActivity {
	private int dataType = 0; // 数据类别：电子书、有声书、口述影像
	private int bookCount = 0; // 当前类资源总数，在分页加载时，需要使用该值
	private String fatherPath;	//父目录路径
	private ArrayList<EbookNodeEntity> mEbookNodeEntityList = new ArrayList<EbookNodeEntity>();

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
		String dbCode;
		String sysId;

		switch(dataType){
		case LibraryConstant.LIBRARY_DATATYPE_EBOOK:
			dbCode = mEbookNodeEntityList.get(selectItem).dbCode;
			String identifier = mEbookNodeEntityList.get(selectItem).identifier;
			new GetEbookChapterAsyncTask(this, fatherPath, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbCode, identifier);
		case LibraryConstant.LIBRARY_DATATYPE_AUDIO:
			dbCode = mEbookNodeEntityList.get(selectItem).dbCode;
			sysId = mEbookNodeEntityList.get(selectItem).sysId;
			new GetAudioChapterAsyncTask(this, fatherPath, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbCode, sysId);
			break;
		case LibraryConstant.LIBRARY_DATATYPE_VIDEO:
			dbCode = mEbookNodeEntityList.get(selectItem).dbCode;
			sysId = mEbookNodeEntityList.get(selectItem).sysId;
			new GetVideoChapterAsyncTask(this, fatherPath, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbCode, sysId);
			break;
		default:
			break;
		}
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
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mEbookNodeEntityList = (ArrayList<EbookNodeEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		dataType = intent.getIntExtra(LibraryConstant.INTENT_KEY_TYPE, 0);
		mMenuList = getListFromEbookNodeEntity(mEbookNodeEntityList);
		bookCount = mMenuList.size();
		bookCount = intent.getIntExtra(LibraryConstant.INTENT_KEY_BOOKCOUNT, bookCount);
		fatherPath = this.getIntent().getStringExtra(LibraryConstant.INTENT_KEY_FATHER_PATH);
	}

	private ArrayList<String> getListFromEbookNodeEntity(ArrayList<EbookNodeEntity> listSrc) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < listSrc.size(); i++) {
			list.add(listSrc.get(i).title);
		}

		return list;
	}

}
