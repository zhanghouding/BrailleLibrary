package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.library.entity.VideoChapterInfoEntity;
import com.sunteam.library.utils.EbookConstants;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

public class VideoOnlineChapterList extends MenuActivity {
	private String fatherPath;	//父目录路径
	private ArrayList<VideoChapterInfoEntity> mVideoChapterInfoEntityListt;

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
		intent.putExtra("totalChapter", mVideoChapterInfoEntityListt.size());
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, fatherPath+menuItem+"/");		//父目录
		intent.putExtra(LibraryConstant.INTENT_KEY_URL, mVideoChapterInfoEntityListt.get(selectItem).videoUrl);	//资源路径
		this.startActivityForResult(intent, selectItem);
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mVideoChapterInfoEntityListt = (ArrayList<VideoChapterInfoEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		mMenuList = getListFromChapterInfoEntity(mVideoChapterInfoEntityListt);
		fatherPath = this.getIntent().getStringExtra(LibraryConstant.INTENT_KEY_FATHER_PATH);
	}

	private ArrayList<String> getListFromChapterInfoEntity(ArrayList<VideoChapterInfoEntity> listSrc) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < listSrc.size(); i++) {
			list.add(listSrc.get(i).title);
		}

		return list;
	}
}
