package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;

/**
 * @Destryption 精品专区列表
 * @Author Jerry
 * @Date 2017-2-19 下午7:08:27
 * @Note
 */
public class BoutiqueArea extends MenuActivity {

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
//		mHistoryEntityList = (ArrayList<HistoryEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
//		mMenuList = getListFromHistoryEntity(mHistoryEntityList);
	}

//	private ArrayList<String> getListFromHistoryEntity(ArrayList<HistoryEntity> listSrc) {
//		ArrayList<String> list = new ArrayList<String>();
//		for (int i = 0; i < listSrc.size(); i++) {
//			list.add(listSrc.get(i).categoryFullName);
//		}
//
//		return list;
//	}

	private void startNextActivity(Class<?> cls, int selectItem, String menuItem, String[] list){
	}

}
