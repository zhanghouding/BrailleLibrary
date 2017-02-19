package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.library.entity.CollectResourceEntity;

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

	private void startNextActivity(int selectItem, String title) {
		Intent intent = new Intent();
/*		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, selectItem); // 选中的选项

		intent.setClass(this, FavoriteActivity.class);*/

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);
	}
	
}
