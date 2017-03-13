package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.menu.menuview.OnMenuKeyListener;
import com.sunteam.library.R;
import com.sunteam.library.entity.DownloadResourceEntity;
import com.sunteam.library.utils.LibraryConstant;

/**
 * @Destryption 正在下载列表 
 * @Author Jerry
 * @Date 2017-2-21 上午10:23:06
 * @Note
 */
public class DownloadingList extends MenuActivity implements OnMenuKeyListener {
	private ArrayList<DownloadResourceEntity> mDownloadResourceEntityList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (null != mMenuView) {
			mMenuView.setMenuKeyListener(this);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode) {
			return;
		}

	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		// TODO 暂停下载、正在下载
	}

	@Override
	public void onMenuKeyCompleted(int selectItem, String menuItem) {
		Intent intent = new Intent();
		String title = getResources().getString(R.string.common_functionmenu); // 功能菜单
		String[] list = getResources().getStringArray(R.array.library_favorite_function_menu_list);
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, list); // 菜单列表
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.MYLIBRARY_DOWNLOADING); // 数据类型
		intent.setClass(this, CommonFunctionMenu.class);
		startActivityForResult(intent, selectItem);
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mDownloadResourceEntityList = (ArrayList<DownloadResourceEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		mMenuList = getListFromDownloadResourceEntity(mDownloadResourceEntityList);
	}

	private ArrayList<String> getListFromDownloadResourceEntity(ArrayList<DownloadResourceEntity> listSrc) {
		ArrayList<String> list = new ArrayList<String>();
		String[] downloadState = getResources().getStringArray(R.array.library_download_state_list);
		String s;
		for (int i = 0; i < listSrc.size(); i++) {
			s = "";
			if(listSrc.get(i).status < downloadState.length){
				s = downloadState[listSrc.get(i).status];
			}
			s = s + " " + listSrc.get(i).categoryFullName;
			if( LibraryConstant.DOWNLOAD_STATUS_GOING == listSrc.get(i).status )
			{
				s += String.format(this.getString(R.string.library_download_chapter_index), listSrc.get(i).curDownloadChapterIndex);
			}
			list.add(s);
		}

		return list;
	}

}
