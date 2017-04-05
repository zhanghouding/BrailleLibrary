package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.ArrayUtils;
import com.sunteam.library.R;
import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.utils.EbookConstants;

/**
 * @Destryption 有声读物播放界面对应的功能菜单
 * @Author Jerry
 * @Date 2017-2-11 下午1:30:56
 * @Note
 */
public class AudioFunctionMenu extends MenuActivity {
	private float percent = 0.0f;
	private BookmarkEntity mBookmarkEntity = null;	//书签实体类

	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		TtsUtils.getInstance().restoreSettingParameters();
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode) {
			return;
		}
		switch(requestCode){
		case 0: // 书签管理
			setResult(resultCode, data); // 需要把跳转的书签返回给朗读界面
			finish();
			break;
		case 4: // 跳至本章百分比
			Intent intent = new Intent();
			intent.putExtra("action", EbookConstants.TO_PART_PAGE);
			intent.putExtra("percent", data.getFloatExtra("percent", 0.0f));
			setResult(RESULT_OK, intent);
			finish();
			break;
		default:
			break;
		}

	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		switch(selectItem){
		case 0: // 书签管理
			startBookmarkManager(selectItem, menuItem);
			break;
		case 1: // 上一章
			{
				Intent intent = new Intent();
				intent.putExtra("action", EbookConstants.TO_PRE_PART);
				setResult(RESULT_OK, intent);
				finish();
			}
			break;
		case 2: // 下一章
			{
				Intent intent = new Intent();
				intent.putExtra("action", EbookConstants.TO_NEXT_PART);
				setResult(RESULT_OK, intent);
				finish();
			}
			break;
		case 3: // 跳至本章开头
			{
				Intent intent = new Intent();
				intent.putExtra("action", EbookConstants.TO_PART_START);
				setResult(RESULT_OK, intent);
				finish();
			}
			break;
		case 4: // 跳至本章百分比
			startPercentEdit(selectItem, menuItem);
			break;
		default:
			break;
		}
	}

	private void initView() {
		// TODO 需要传递书签管理需要的信息、当前页码、页码总数
		Intent intent = getIntent();
		percent = intent.getFloatExtra("percent", 0.0f);
		mBookmarkEntity = (BookmarkEntity) intent.getSerializableExtra("book_mark");
		mTitle = getResources().getString(R.string.common_functionmenu);
		mMenuList = ArrayUtils.strArray2List(getResources().getStringArray(R.array.library_audio_function_menu_list));
	}

	// 启动书签管理界面
	public void startBookmarkManager(int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, menuItem);
		
		// TODO 以下内容需要在播放界面按菜单键启动当前界面时传入，此时，启动书签管理界面时传给书签管理界面！
//		intent.putExtra("book_id", chapterInfo.bookId); // 数目ID
//		intent.putExtra("chapter_index", chapterInfo.chapterIndex); // 当前序号
//		intent.putExtra("begin", 0); // 当前阅读位置
//		intent.putExtra("bookmark_name", chapterInfo.bookId); // 书签名

		intent.putExtra("book_mark", mBookmarkEntity);
		intent.setClass(this, BookmarkManager.class);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);
	}

	// 启动跳转页码编辑界面
	public void startPercentEdit(int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, menuItem);
		intent.putExtra("percent", percent);
		intent.setClass(this, PercentEdit.class);
		startActivityForResult(intent, selectItem);
	}

}
