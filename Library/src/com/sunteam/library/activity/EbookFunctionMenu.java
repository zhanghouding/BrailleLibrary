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
import com.sunteam.library.utils.TTSUtils;

/**
 * @Destryption 电子图书播放界面对应的功能菜单；盲人资讯中的播放界面的功能菜单。
 * @Author Jerry
 * @Date 2017-2-5 下午6:12:19
 * @Note
 */
public class EbookFunctionMenu extends MenuActivity {
	private int pageNo = 1; // 当前页码
	private int pageCount = 10; // 页码总数
	private BookmarkEntity mBookmarkEntity;
	private boolean isNews = false; // 在盲人资讯时，功能菜单项与电子图书不一致；true表示盲人资讯


	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		TtsUtils.getInstance().restoreSettingParameters(); // 在菜单界面使用系统设置朗读
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		TTSUtils.getInstance().init(this);	//恢复电子书中TTS回调; 因为在公用菜单中设置了自己的TtsListener实例
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
		case 4: // 跳至本章页码
			Intent intent = new Intent();
			intent.putExtra("action", EbookConstants.TO_PART_PAGE);
			intent.putExtra("page", data.getIntExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, pageNo));
			setResult(RESULT_OK, intent);
			finish();
			break;
		case 5: // 朗读语音
			finish();
			break;
		case 6: // 背景音乐
			setResult(RESULT_OK, data);
			finish();
			break;
		default:
			break;
		}

	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		if(isNews){
			setResultCodeforNews(resultCode, selectItem, menuItem);
			return;
		}

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
		case 4: // 跳至本章页码
			startPageNumberEdit(PageNumberEdit.class, selectItem, menuItem, pageNo, pageCount);
			break;
		case 5: // 朗读语音
			startNextMenu(VoiceSettings.class, selectItem, menuItem, getResources().getStringArray(R.array.library_array_menu_voice));
			break;
		case 6: // 背景音乐
			startNextMenu(MusicSettings.class, selectItem, menuItem, getResources().getStringArray(R.array.library_array_menu_music));
			break;
		default:
			break;
		}
	}

	private void setResultCodeforNews(int resultCode, int selectItem, String menuItem) {
		Intent intent;
		switch (selectItem) {
		case 0: // 上一条
			intent = new Intent();
			intent.putExtra("action", EbookConstants.TO_PRE_PART);
			setResult(RESULT_OK, intent);
			finish();
			break;
		case 1: // 下一条
			intent = new Intent();
			intent.putExtra("action", EbookConstants.TO_NEXT_PART);
			setResult(RESULT_OK, intent);
			finish();
			break;
		case 2: // 跳至本条开头
			intent = new Intent();
			intent.putExtra("action", EbookConstants.TO_PART_START);
			setResult(RESULT_OK, intent);
			finish();
			break;
		case 3: // 跳至本条页码
			// 与电子图书保持一致：requestCode = 4
			startPageNumberEdit(PageNumberEdit.class, selectItem + 1, menuItem, pageNo, pageCount);
			break;
		case 4: // 朗读语音
			// 与电子图书保持一致：requestCode = 5
			startNextMenu(VoiceSettings.class, selectItem + 1, menuItem, getResources().getStringArray(R.array.library_array_menu_voice));
			break;
		case 5: // 背景音乐
			// 与电子图书保持一致：requestCode = 6
			startNextMenu(MusicSettings.class, selectItem + 1, menuItem, getResources().getStringArray(R.array.library_array_menu_music));
			break;
		default:
			break;
		}
	}

	private void initView() {
		Intent intent = getIntent();
		pageNo = intent.getIntExtra("page_cur", 1);
		pageCount = intent.getIntExtra("page_count", 1);
		mBookmarkEntity = (BookmarkEntity) intent.getSerializableExtra("book_mark");
		isNews = intent.getBooleanExtra("isNews", isNews);
		mTitle = getResources().getString(R.string.common_functionmenu);
		int id = R.array.library_ebook_function_menu_list;
		if(isNews){
			id = R.array.library_news_function_menu_list;
		}
		mMenuList = ArrayUtils.strArray2List(getResources().getStringArray(id));
	}

	// 启动书签管理界面
	public void startBookmarkManager(int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, menuItem);
		intent.putExtra("book_mark", mBookmarkEntity);
		intent.setClass(this, BookmarkManager.class);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);
	}

	// 启动朗读语音设置界面
	public void startNextMenu(Class<?> cls, int selectItem, String menuItem, String[] list) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, menuItem);
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, list);

		intent.setClass(this, cls);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);
	}

	// 启动跳转页码编辑界面
	public void startPageNumberEdit(Class<?> cls, int selectItem, String menuItem, int pageNo, int pageCount) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, menuItem);
		intent.putExtra("page_cur", pageNo);
		intent.putExtra("page_count", pageCount);
		intent.setClass(this, cls);
		startActivityForResult(intent, selectItem);
	}

}
