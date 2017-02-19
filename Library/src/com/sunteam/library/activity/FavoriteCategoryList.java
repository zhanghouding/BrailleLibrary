package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.library.asynctask.GetEbookAsyncTask;
import com.sunteam.library.entity.CollectCategoryEntity;
import com.sunteam.library.utils.LibraryConstant;

/**
 * @Destryption 收藏分类列表；电子书、有声书、口述影像公用一个界面
 * @Author Jerry
 * @Date 2017-2-4 下午3:27:22
 * @Note
 */
public class FavoriteCategoryList extends MenuActivity {
	private ArrayList<CollectCategoryEntity> mCollectCategoryEntityList = new ArrayList<CollectCategoryEntity>();

	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
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
		startNextActivity(selectItem, menuItem);
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mCollectCategoryEntityList = (ArrayList<CollectCategoryEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		mMenuList = getListFromCollectCategoryEntity(mCollectCategoryEntityList);
	}

	private ArrayList<String> getListFromCollectCategoryEntity(ArrayList<CollectCategoryEntity> listSrc) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < listSrc.size(); i++) {
			list.add(listSrc.get(i).categoryFullName);
		}

		return list;
	}

	private void startNextActivity(int selectItem, String menuItem) {
		CollectCategoryEntity entity = mCollectCategoryEntityList.get(selectItem);
		String pageIndex = "1";
		String pageSize = "" + LibraryConstant.LIBRARY_RESOURCE_PAGESIZE;
		String categoryCode = entity.categoryCode;
		int dataType = entity.resType;
		
		String[] categoryName = entity.categoryFullName.split("-");
		int size = categoryName.length;
		String title = entity.categoryName;
		String fatherPath = LibraryConstant.LIBRARY_ROOT_PATH;
		for( int i = 0; i < size-1; i++ )
		{
			fatherPath += (categoryName[i]+"/");
		}
		
		new GetEbookAsyncTask(this, fatherPath, title).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pageIndex, pageSize, categoryCode, "" + dataType);
	}

}
