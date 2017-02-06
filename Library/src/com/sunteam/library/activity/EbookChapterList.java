package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.menu.menuview.OnMenuKeyListener;
import com.sunteam.library.asynctask.GetEbookChapterContentAsyncTask;
import com.sunteam.library.entity.EbookChapterInfoEntity;
import com.sunteam.library.utils.LibraryConstant;

/**
 * @Destryption 电子书章节列表
 * @Author Jerry
 * @Date 2017-2-4 下午3:20:17
 * @Note
 */
public class EbookChapterList extends MenuActivity implements OnMenuKeyListener {
	private String identifier;	//电子书identifier
	private String fatherPath;	//父目录路径
	private String dbCode;		//数据编码
	private String sysId;		//系统id
	private String categoryName;//分类名称
	private ArrayList<EbookChapterInfoEntity> mEbookChapterInfoEntityList;

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
		mMenuView.setMenuKeyListener(this);
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
		String chapterIndex = mEbookChapterInfoEntityList.get(selectItem).chapterIndex;
		new GetEbookChapterContentAsyncTask(this, fatherPath, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, identifier, chapterIndex);
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mEbookChapterInfoEntityList = (ArrayList<EbookChapterInfoEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		mMenuList = getListFromChapterInfoEntity(mEbookChapterInfoEntityList);
		fatherPath = this.getIntent().getStringExtra(LibraryConstant.INTENT_KEY_FATHER_PATH);
		identifier = this.getIntent().getStringExtra(LibraryConstant.INTENT_KEY_IDENTIFIER);
		dbCode = intent.getStringExtra(LibraryConstant.INTENT_KEY_DBCODE);
		sysId = intent.getStringExtra(LibraryConstant.INTENT_KEY_SYSID);
		categoryName = intent.getStringExtra(LibraryConstant.INTENT_KEY_CATEGORY_NAME);
	}

	private ArrayList<String> getListFromChapterInfoEntity(ArrayList<EbookChapterInfoEntity> listSrc) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < listSrc.size(); i++) {
			list.add(listSrc.get(i).chapterName);
		}

		return list;
	}

	@Override
	public void onMenuKeyCompleted(int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.LIBRARY_DATATYPE_EBOOK); // 数据类别：电子书、有声书、口述影像
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, fatherPath); // 父目录
		intent.putExtra(LibraryConstant.INTENT_KEY_CATEGORY_NAME, categoryName); // 分类名称
		intent.putExtra(LibraryConstant.INTENT_KEY_RESOURCE, mTitle);
		intent.putExtra(LibraryConstant.INTENT_KEY_DBCODE, dbCode);	//数据编码
		intent.putExtra(LibraryConstant.INTENT_KEY_SYSID, sysId);	//系统id

		intent.setClass(this, ChapterFunctionMenu.class);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);
	}

}
