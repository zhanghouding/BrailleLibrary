package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.utils.ArrayUtils;
import com.sunteam.library.R;
import com.sunteam.library.entity.EbookChapterInfoEntity;
import com.sunteam.library.entity.EbookInfoEntity;
import com.sunteam.library.utils.LibraryConstant;

/**
 * @Destryption 电子图书播放界面对应的功能菜单
 * @Author Jerry
 * @Date 2017-2-5 下午6:12:19
 * @Note
 */
public class EbookFunctionMenu extends MenuActivity {
	private EbookChapterInfoEntity chapterInfo;

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
		case 0: // 书签管理
			startBookmarkManager(selectItem, menuItem);
			break;
		case 1: // 上一章
			break;
		case 2: // 下一章
			break;
		case 3: // 跳至本章开头
			break;
		case 4: // 跳至本章页码
			break;
		case 5: // 朗读语音
			break;
		case 6: // 背景音乐
			break;
		default:
			break;
		}
	}

	private void initView() {
		Intent intent = getIntent();
		chapterInfo = (EbookChapterInfoEntity) intent.getSerializableExtra("chapter_info");
		mTitle = getResources().getString(R.string.common_functionmenu);
		mMenuList = ArrayUtils.strArray2List(getResources().getStringArray(R.array.library_ebook_function_menu_list));
	}

	// 启动书签管理界面
	public void startBookmarkManager(int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, menuItem);
		
		// TODO 以下内容需要在电子图书播放界面按菜单键启动当前界面时传入，此时，启动书签管理界面时传给书签管理界面！
//		intent.putExtra("book_id", chapterInfo.bookId); // 数目ID
//		intent.putExtra("chapter_index", chapterInfo.chapterIndex); // 当前序号
//		intent.putExtra("begin", 0); // 当前阅读位置
//		intent.putExtra("bookmark_name", chapterInfo.bookId); // 书签名

		intent.setClass(this, BookmarkManager.class);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);
	}

}