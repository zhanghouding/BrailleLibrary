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
	private int resultType = 0; // 从子界面返回类型：0从播放界面返回；1从功能菜单返回

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

		if (Activity.RESULT_OK != resultCode) {
			// 凡是从子界面返回时，都不会设置resultCode参数
			if(0 == resultType) { // 从播放界面返回时，需要立即更新列表,并重新设置焦点行为第一行！
				freshMenuList();
			}
			return;
		}

		// 从功能菜单返回，需要判断是删除还是清空
		int index = data.getIntExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, 0);
		if (1 == index) { // 清空成功
			setResult(Activity.RESULT_OK);
			finish();
			return;
		}

		// 删除成功
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
		resultType = 0;
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
		String categoryCode = entity.categoryCode;
		String[] categoryName = entity.categoryFullName.split("-");
		dbCode = entity.dbCode;
		
		int size = categoryName.length;
		String title = categoryName[size-1];
		String fatherPath = LibraryConstant.LIBRARY_ROOT_PATH;
		for( int i = 0; i < size-1; i++ )
		{
			fatherPath += (categoryName[i]+"/");
		}
		
		int offset = 0;	//起始位置
		
		switch(dataType)
		{
			case LibraryConstant.LIBRARY_DATATYPE_EBOOK:	
				sysId = "";
				identifier = entity.sysId;
				new GetEbookChapterAsyncTask(this, fatherPath, title, entity.lastChapterIndex, offset).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbCode, sysId, categoryName[size-2], identifier, categoryCode);
				break;
			case LibraryConstant.LIBRARY_DATATYPE_AUDIO:
				sysId = entity.sysId;
				identifier = "";
				new GetAudioChapterAsyncTask(this, fatherPath, title, entity.lastChapterIndex, offset).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbCode, sysId, categoryName[size-2], identifier, categoryCode);
				break;
			case LibraryConstant.LIBRARY_DATATYPE_VIDEO:
				sysId = entity.sysId;
				identifier = "";
				new GetVideoChapterAsyncTask(this, fatherPath, title, entity.lastChapterIndex, offset).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbCode, sysId, categoryName[size-2], identifier, categoryCode);
				break;
			default:
				break;
		}
	}

	@Override
	public void onMenuKeyCompleted(int selectItem, String menuItem) {
		resultType = 1;
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, mHistoryEntityList); // 菜单列表
		intent.putExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, selectItem); // 当前菜单项
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.MYLIBRARY_READING_HISTORY); // 数据类型
		intent.setClass(this, CommonFunctionMenu.class);
		startActivityForResult(intent, selectItem);
	}

	// 从播放界面返回时，要刷新阅读历史列表
	private void freshMenuList() {
		int index = getSelectItem();
		selectItem = 0;
		HistoryEntity mHistoryEntity = mHistoryEntityList.get(index);
		mHistoryEntityList.remove(index);
		mHistoryEntityList.add(selectItem, mHistoryEntity);
		mMenuList = getListFromHistoryEntity(mHistoryEntityList);
		setListData(mMenuList);
		mMenuView.setSelectItem(selectItem);
	}
	
}
