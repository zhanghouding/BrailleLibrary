package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.menu.menuview.OnMenuKeyListener;
import com.sunteam.library.entity.AudioChapterInfoEntity;
import com.sunteam.library.utils.EbookConstants;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 有声书章节列表
 * @Author Jerry
 * @Date 2017-2-4 下午3:25:36
 * @Note
 */
public class AudioChapterList extends MenuActivity implements OnMenuKeyListener {
	private String fatherPath;	//父目录路径
	private ArrayList<AudioChapterInfoEntity> mAudioChapterInfoEntityList;

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
		mMenuView.setMenuKeyListener(this);
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

		int action = data.getIntExtra("action", EbookConstants.TO_NEXT_PART);
		switch (action) {
		case EbookConstants.TO_NEXT_PART:
			mMenuView.down();
			mMenuView.enter();
			break;
		case EbookConstants.TO_PRE_PART:
			mMenuView.up();
			mMenuView.enter();
			break;
		case EbookConstants.TO_BOOK_START:
			break;
		default:
			break;
		}

	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		PublicUtils.createCacheDir(fatherPath, menuItem);	//创建缓存目录
		
		Intent intent = new Intent( this, PlayAudioVedioActivity.class );
		intent.putExtra("filename", menuItem);
		intent.putExtra("curChapter", selectItem);
		intent.putExtra("totalChapter", mAudioChapterInfoEntityList.size());
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, fatherPath+menuItem+"/");		//父目录
		intent.putExtra(LibraryConstant.INTENT_KEY_URL, mAudioChapterInfoEntityList.get(selectItem).audioUrl);	//资源路径
		this.startActivityForResult(intent, selectItem);
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mAudioChapterInfoEntityList = (ArrayList<AudioChapterInfoEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		mMenuList = getListFromChapterInfoEntity(mAudioChapterInfoEntityList);
		fatherPath = this.getIntent().getStringExtra(LibraryConstant.INTENT_KEY_FATHER_PATH);
	}

	private ArrayList<String> getListFromChapterInfoEntity(ArrayList<AudioChapterInfoEntity> listSrc) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < listSrc.size(); i++) {
			list.add(listSrc.get(i).title);
		}

		return list;
	}

	@Override
	public void onMenuKeyCompleted(int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.LIBRARY_DATATYPE_AUDIO); // 数据类别：电子书、有声书、口述影像
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, fatherPath); // 父目录

		intent.setClass(this, ChapterFunctionMenu.class);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);
	}

}
