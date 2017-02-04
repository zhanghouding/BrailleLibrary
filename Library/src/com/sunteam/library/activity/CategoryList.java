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

/**
 * @Destryption 浏览所有在线分类；同时在本地创建分类文件夹。
 * @Author Jerry
 * @Date 2017-1-26 上午10:43:02
 * @Note
 */
public class CategoryList extends MenuActivity {
	private int fatherId = LibraryConstant.LIBRARY_CATEGORY_ROOT_ID; // 当前分类列表的父节点ID
	private int dataType = 0; // 数据类别：电子书、有声书、口述影像
	private ArrayList<CategoryInfoNodeEntity> mCategoryInfoNodeEntityList;
	
	// 当前分类列表父节点路径，如："/s918p/library/" + "有声书/" +"课外读物/少儿/", 其中第二个字段根据属性dataType确定
	private String fatherPath = "";

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
		ArrayList<CategoryInfoNodeEntity> list = GetCategoryAsyncTask.getChildNodeList(mCategoryInfoNodeEntityList.get(selectItem).seq);
		if(0 == list.size()) {
			String pageIndex = "1";
			String pageSize = "" + LibraryConstant.LIBRARY_RESOURCE_PAGESIZE;
			String categoryCode = mCategoryInfoNodeEntityList.get(selectItem).code;
			new GetEbookAsyncTask(this, fatherPath, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pageIndex, pageSize, categoryCode, "" + dataType);
		} else {
			startNextActivity(selectItem, menuItem);
		}
	}

	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		fatherId = intent.getIntExtra(LibraryConstant.INTENT_KEY_FATHER, -1);
		dataType = intent.getIntExtra(LibraryConstant.INTENT_KEY_TYPE, 0);
		fatherPath = intent.getStringExtra(LibraryConstant.INTENT_KEY_FATHER_PATH);			
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
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER, mCategoryInfoNodeEntityList.get(selectItem).seq); // 父节点ID
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, dataType); // 数据类别：电子书、有声书、口述影像
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, fatherPath+menuItem+"/");	//父目录
		intent.setClass(this, CategoryList.class);

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
