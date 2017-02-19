package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.library.entity.HistoryEntity;

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

	private void startNextActivity(Class<?> cls, int selectItem, String title, String[] list){
		/*Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, list); // 菜单列表

		intent.setClass(this, cls);
		 
        // 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);*/
	}

}
