package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.ArrayUtils;
import com.sunteam.library.R;
import com.sunteam.library.utils.EbookConstants;

/**
 * @Destryption 电子图书播放界面对应的功能菜单
 * @Author Jerry
 * @Date 2017-2-5 下午6:12:19
 * @Note
 */
public class EbookFunctionMenu extends MenuActivity {
	private int pageNo = 1; // 当前页码
	private int pageCount = 10; // 页码总数

	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode || null == data) { // 在子菜单中回传的标志
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

	private void initView() {
		// TODO 需要传递书签管理需要的信息、当前页码、页码总数
		Intent intent = getIntent();
		pageNo = intent.getIntExtra("page_cur", 1);
		pageCount = intent.getIntExtra("page_count", 1);
		mTitle = getResources().getString(R.string.common_functionmenu);
		mMenuList = ArrayUtils.strArray2List(getResources().getStringArray(R.array.library_ebook_function_menu_list));
		TtsUtils.getInstance().restoreSettingParameters(); // 在菜单界面使用系统设置朗读
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
