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
import com.sunteam.library.entity.CollectResourceEntity;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 收藏资源列表，与手机端的收藏资源保持一致
 * @Author Jerry
 * @Date 2017-1-22 下午5:06:24
 * @Note
 */
public class FavoriteResourceList extends MenuActivity implements OnMenuKeyListener {
	private ArrayList<CollectResourceEntity> mCollectResourceEntityList = new ArrayList<CollectResourceEntity>();

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
			mCollectResourceEntityList.remove(selectItem);
			mMenuList.remove(selectItem);
		}
		if (0 == mMenuList.size()) {
			String tips = getResources().getString(R.string.library_favorite_resource_null);
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
		String categoryCode = "";
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
				new GetEbookChapterAsyncTask(this, fatherPath, title).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbCode, sysId, categoryName[size-2], identifier, categoryCode);
				break;
			case LibraryConstant.LIBRARY_DATATYPE_AUDIO:
				sysId = entity.sysId;
				identifier = "";
				new GetAudioChapterAsyncTask(this, fatherPath, title).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbCode, sysId, categoryName[size-2], identifier, categoryCode);
				break;
			case LibraryConstant.LIBRARY_DATATYPE_VIDEO:
				sysId = entity.sysId;
				identifier = "";
				new GetVideoChapterAsyncTask(this, fatherPath, title).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbCode, sysId, categoryName[size-2], identifier, categoryCode);
				break;
			default:
				break;
		}
	}

	@Override
	public void onMenuKeyCompleted(int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, mCollectResourceEntityList); // 菜单列表
		intent.putExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, selectItem); // 当前菜单项
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.MYLIBRARY_FAVARITE_RESOURCE); // 数据类型
		intent.setClass(this, CommonFunctionMenu.class);
		startActivityForResult(intent, selectItem);
	}
	
}
