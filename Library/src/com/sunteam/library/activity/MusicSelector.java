package com.sunteam.library.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsCompletedListener;
import com.sunteam.common.utils.SharedPrefUtils;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.entity.FileInfo;
import com.sunteam.library.utils.EbookConstants;
import com.sunteam.library.utils.FileOperateUtils;
import com.sunteam.library.utils.MediaPlayerUtils;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 背景音乐选择
 * @Author Jerry
 * @Date 2017-2-8 下午2:09:49
 * @Note
 */
public class MusicSelector extends MenuActivity implements TtsCompletedListener {
	private ArrayList<FileInfo> fileList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MediaPlayerUtils.getInstance().stop();
	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		saveMusicFile(selectItem);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mMenuList = new ArrayList<String>();
		fileList = new ArrayList<FileInfo>();
		int mode = Context.MODE_WORLD_READABLE + Context.MODE_MULTI_PROCESS;
		String path = SharedPrefUtils.getSharedPrefString(this, EbookConstants.SETTINGS_TABLE, mode, EbookConstants.MUSICE_PATH, null);
		ArrayList<File> filesList = FileOperateUtils.getMusicInDir();
		if (null != filesList && 0 < filesList.size()) {
			for (int i = 0; i < filesList.size(); i++) {
				File f = filesList.get(i);
				FileInfo info = new FileInfo();
				info.name = f.getName();
				info.path = f.getPath();
				fileList.add(info);
				mMenuList.add(info.name);
				if (f.getPath().equals(path)) {
					selectItem = i;
				}
			}
		}
		if( ( selectItem >= 0 ) && ( selectItem < fileList.size() ) )
		{
			MediaPlayerUtils.getInstance().play(fileList.get(selectItem).path, true);
		}
		mTtsCompletedListener = this;
	}

	private void saveMusicFile(final int index) {
		SharedPrefUtils.saveSettings(this, EbookConstants.SETTINGS_TABLE, EbookConstants.MUSICE_PATH, fileList.get(index).path);

		// 提示设定成功
		PublicUtils.showToast(this, getResources().getString(R.string.library_setting_success), new PromptListener() {

			@Override
			public void onComplete() {
				MediaPlayerUtils.getInstance().stop();
				setResult(Activity.RESULT_OK);
				finish();
			}
		});
	}

	@Override
	public void onCompleted(String error) {
		int position = getSelectItem();
		if (selectItem != position) {
			selectItem = position;
			MediaPlayerUtils.getInstance().stop();
			MediaPlayerUtils.getInstance().play(fileList.get(selectItem).path, true);
		}
	}

}
