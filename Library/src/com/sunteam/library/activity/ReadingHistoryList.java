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
import com.sunteam.library.asynctask.GetAudioChapterAsyncTask;
import com.sunteam.library.asynctask.GetEbookChapterAsyncTask;
import com.sunteam.library.asynctask.GetVideoChapterAsyncTask;
import com.sunteam.library.entity.HistoryEntity;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 阅读历史列表；阅读历史中只保存了书本
 * @Author Jerry
 * @Date 2017-2-4 下午3:32:25
 * @Note
 */
public class ReadingHistoryList extends MenuActivity implements OnMenuKeyListener {
	private ArrayList<HistoryEntity> mHistoryEntityList = new ArrayList<HistoryEntity>();

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
			setResult(Activity.RESULT_OK);
			finish();
			return;
		}

		selectItem = getSelectItem();
		if (selectItem < mMenuList.size()) {
			mHistoryEntityList.remove(selectItem);
			mMenuList.remove(selectItem);
		}
		if (0 == mMenuList.size()) {
			String tips = getResources().getString(R.string.library_reading_history_null);
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
		String[] list = null;
		Class<?> cls = null;

		startNextActivity(cls, selectItem, menuItem, list);
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mHistoryEntityList = (ArrayList<HistoryEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		mMenuList = getListFromHistoryEntity(mHistoryEntityList);
	}

	private ArrayList<String> getListFromHistoryEntity(ArrayList<HistoryEntity> listSrc) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < listSrc.size(); i++) {
			list.add(listSrc.get(i).categoryFullName);
		}

		return list;
	}

	private void startNextActivity(Class<?> cls, int selectItem, String menuItem, String[] list){
		HistoryEntity entity = mHistoryEntityList.get(selectItem);
		int dataType = entity.resType;
		String dbCode;
		String sysId;
		String identifier;
		String[] categoryName = entity.categoryFullName.split("-");
		dbCode = entity.dbCode;
		
		int size = categoryName.length;
		String title = categoryName[size-1];
		String fatherPath = LibraryConstant.LIBRARY_ROOT_PATH;
		for( int i = 0; i < size-1; i++ )
		{
			fatherPath += (categoryName[i]+"/");
		}
		
		switch(dataType)
		{
			case LibraryConstant.LIBRARY_DATATYPE_EBOOK:	
				sysId = "";
				identifier = entity.sysId;
				new GetEbookChapterAsyncTask(this, fatherPath, title).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbCode, sysId, categoryName[size-2], identifier);
				break;
			case LibraryConstant.LIBRARY_DATATYPE_AUDIO:
				sysId = entity.sysId;
				identifier = "";
				new GetAudioChapterAsyncTask(this, fatherPath, title).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbCode, sysId, categoryName[size-2], identifier);
				break;
			case LibraryConstant.LIBRARY_DATATYPE_VIDEO:
				sysId = entity.sysId;
				identifier = "";
				new GetVideoChapterAsyncTask(this, fatherPath, title).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbCode, sysId, categoryName[size-2], identifier);
				break;
			default:
				break;
		}
	}

	@Override
	public void onMenuKeyCompleted(int selectItem, String menuItem) {
		Intent intent = new Intent();
		String title = getResources().getString(R.string.common_functionmenu); // 功能菜单
		String[] list = getResources().getStringArray(R.array.library_favorite_function_menu_list);
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, list); // 菜单列表
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.MYLIBRARY_READING_HISTORY); // 数据类型
		HistoryEntity mHistoryEntity = mHistoryEntityList.get(selectItem);
		intent.putExtra("entity", mHistoryEntity); // 当前收藏分类实体
		intent.setClass(this, CommonFunctionMenu.class);
		startActivityForResult(intent, selectItem);
	}

}
