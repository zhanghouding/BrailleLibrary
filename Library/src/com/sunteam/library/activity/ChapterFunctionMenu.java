package com.sunteam.library.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.utils.ArrayUtils;
import com.sunteam.library.R;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 章节列表时的功能菜单；电子书、有声书、口述影像共用一个功能菜单 
 * @Author Jerry
 * @Date 2017-2-4 下午3:20:42
 * @Note
 */
public class ChapterFunctionMenu extends MenuActivity {
	private int dataType = 0; // 数据类别：电子书、有声书、口述影像
	private String fatherPath;	//父目录路径

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
		case 0: // 收藏当前资源
			break;
		case 1: // 下载当前资源
			break;
		case 2: // 删除当前资源
			{
				String path = fatherPath;
				File file = new File( path );
				PublicUtils.deleteFiles(file);
				PublicUtils.createCacheDir(fatherPath, "");	//创建缓存目录(因为用deleteFiles会连fatherPath也给删除了，所以必须重建)
				String tips = menuItem+this.getString(R.string.library_success);
				PublicUtils.showToast(this, tips, null);
			}
			break;
		default:
			break;
		}
	}

	private void initView() {
		Intent intent = getIntent();
		dataType = intent.getIntExtra(LibraryConstant.INTENT_KEY_TYPE, 0);
		fatherPath = intent.getStringExtra(LibraryConstant.INTENT_KEY_FATHER_PATH);
		mTitle = getResources().getString(R.string.common_functionmenu);
		mMenuList = ArrayUtils.strArray2List(getResources().getStringArray(R.array.library_chapter_function_menu_list));
	}

}
