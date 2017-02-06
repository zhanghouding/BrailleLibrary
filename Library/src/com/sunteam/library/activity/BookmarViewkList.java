package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;

/**
 * @Destryption 查看书签
 * @Author Jerry
 * @Date 2017-2-6 上午9:35:41
 * @Note 理论上，一本书的书签可以是无穷多个，但实际上我们可以限制在Intent传输的数据量大小之内！
 */
public class BookmarViewkList extends MenuActivity {
	// TODO 需要定义书签实体类
//	private ArrayList<BookmarkEntity> mBookmarkEntityList; // 书签列表：数目ID, 章节号, 起始位置, 书签名等

	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode || null == data) { // 在子菜单中回传的标志
			return;
		}

	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		// TODO 跳转到指定位置
	}

	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
//		mBookmarkEntityList = (ArrayList<BookmarkEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
//		mMenuList = getListFromChapterInfoEntity(mBookmarkEntityList);
		mMenuList = new ArrayList<String>();
	}

	// 从书签列表中获取书签名列表
//	private ArrayList<String> getListFromBookmarkEntity(ArrayList<BookmarkEntity> listSrc) {
//		ArrayList<String> list = new ArrayList<String>();
//		for (int i = 0; i < listSrc.size(); i++) {
//			list.add(listSrc.get(i).bookmarkName);
//		}
//
//		return list;
//	}

}
