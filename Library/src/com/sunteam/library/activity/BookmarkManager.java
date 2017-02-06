package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.utils.ArrayUtils;
import com.sunteam.library.R;
import com.sunteam.library.utils.LibraryConstant;

/**
 * @Destryption 书签管理：增加、查看、删除、清空书签
 * @Author Jerry
 * @Date 2017-2-6 上午9:35:41
 * @Note 理论上，一本书的书签可以是无穷多个，但实际上我们可以限制在Intent传输的数据量大小之内！
 */
public class BookmarkManager extends MenuActivity {
	private String bookId; // 数目ID
	private int chapterIndex; // 章节号
	private int beginOff; // 起始位置
	private String bookmarkName; // 书签名

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
		switch(selectItem){
		case 0: // 增加书签
			break;
		case 1: // 查看书签
			startNextActivity(BookmarViewkList.class, selectItem, menuItem);
			break;
		case 2: // 删除书签
			break;
		case 3: // 清空书签
			break;
		default:
			break;
		}
	}

	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		bookId = intent.getStringExtra("book_id");
		chapterIndex = intent.getIntExtra("chapter_index", 0);
		beginOff = intent.getIntExtra("begin", 0);
		bookmarkName = intent.getStringExtra("bookmark_name");
		
		mMenuList = ArrayUtils.strArray2List(getResources().getStringArray(R.array.library_bookmark_manager_list));
	}

	// 进入查看书签列表界面；如果需要在异步任务中获取云端数据，则需要在成功获取数据后调用该方法
	public void startNextActivity(Class<?> cls, int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, menuItem);

		intent.setClass(this, cls);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);
	}

}
