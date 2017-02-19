package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.menu.menuview.OnMenuKeyListener;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.GetEbookAsyncTask;
import com.sunteam.library.entity.CollectCategoryEntity;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 收藏分类列表；电子书、有声书、口述影像公用一个界面
 * @Author Jerry
 * @Date 2017-2-4 下午3:27:22
 * @Note
 */
public class FavoriteCategoryList extends MenuActivity implements OnMenuKeyListener {
	private ArrayList<CollectCategoryEntity> mCollectCategoryEntityList = new ArrayList<CollectCategoryEntity>();

	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMenuView.setMenuKeyListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode) { // 在子菜单中回传的标志
			return;
		}

		if (1 == requestCode) { // 清空成功
			setResult(Activity.RESULT_OK, data);
			finish();
			return;
		}

		selectItem = getSelectItem();
		if (selectItem < mMenuList.size()) {
			mCollectCategoryEntityList.remove(selectItem);
			mMenuList.remove(selectItem);
		}
		if (0 == mMenuList.size()) {
			String tips = getResources().getString(R.string.library_favorite_category_null);
			PublicUtils.showToast(this, tips, new PromptListener() {

				@Override
				public void onComplete() {
					setResult(Activity.RESULT_OK);
					finish();
				}
			});
		} else {
			if (selectItem >= mMenuList.size()) {
				selectItem = mMenuList.size() - 1;
			}
			setListData(mMenuList);
			mMenuView.setSelectItem(selectItem, true);
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

	@Override
	public void onMenuKeyCompleted(int selectItem, String menuItem) {
		Intent intent = new Intent();
		String title = getResources().getString(R.string.common_functionmenu); // 功能菜单
		String[] list = getResources().getStringArray(R.array.library_favorite_function_menu_list);
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, list); // 菜单列表
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.MYLIBRARY_FAVARITE_CATEGORY); // 数据类型
		CollectCategoryEntity mCollectCategoryEntity = mCollectCategoryEntityList.get(selectItem);
		intent.putExtra("entity", mCollectCategoryEntity); // 当前收藏分类实体
		intent.setClass(this, CommonFunctionMenu.class);
		startActivityForResult(intent, selectItem);
	}

}
