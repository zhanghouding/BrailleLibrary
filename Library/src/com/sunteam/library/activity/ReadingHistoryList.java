package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.library.asynctask.GetAudioChapterAsyncTask;
import com.sunteam.library.asynctask.GetEbookChapterAsyncTask;
import com.sunteam.library.asynctask.GetVideoChapterAsyncTask;
import com.sunteam.library.entity.CollectResourceEntity;
import com.sunteam.library.entity.HistoryEntity;
import com.sunteam.library.utils.LibraryConstant;

/**
 * @Destryption 阅读历史列表；阅读历史中只保存了书本
 * @Author Jerry
 * @Date 2017-2-4 下午3:32:25
 * @Note
 */
public class ReadingHistoryList extends MenuActivity {
	private ArrayList<HistoryEntity> mHistoryEntityList = new ArrayList<HistoryEntity>();

	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode) { // 在子菜单中回传的标志
			return;
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

}
