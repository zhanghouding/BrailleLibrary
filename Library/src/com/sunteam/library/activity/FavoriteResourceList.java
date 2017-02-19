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
import com.sunteam.library.utils.LibraryConstant;

/**
 * @Destryption 收藏资源列表，与手机端的收藏资源保持一致
 * @Author Jerry
 * @Date 2017-1-22 下午5:06:24
 * @Note
 */
public class FavoriteResourceList extends MenuActivity {
	private ArrayList<CollectResourceEntity> mCollectResourceEntityList = new ArrayList<CollectResourceEntity>();

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
		startNextActivity(selectItem, menuItem);
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mCollectResourceEntityList = (ArrayList<CollectResourceEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		mMenuList = getListFromCollectResourceEntity(mCollectResourceEntityList);
	}

	private ArrayList<String> getListFromCollectResourceEntity(ArrayList<CollectResourceEntity> listSrc) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < listSrc.size(); i++) {
			list.add(listSrc.get(i).categoryFullName);
		}

		return list;
	}

	private void startNextActivity(int selectItem, String menuItem) 
	{
		CollectResourceEntity entity = mCollectResourceEntityList.get(selectItem);
		int dataType = entity.resType;
		String dbCode;
		String sysId;
		String identifier;
		String[] categoryName = entity.categoryFullName.split("-");
		dbCode = entity.dbCode;
		
		int size = categoryName.length;
		String title = entity.title;
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
