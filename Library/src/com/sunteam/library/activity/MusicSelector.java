package com.sunteam.library.activity;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.utils.PromptDialog;
import com.sunteam.library.R;
import com.sunteam.library.entity.FileInfo;
import com.sunteam.library.utils.EbookConstants;
import com.sunteam.library.utils.FileOperateUtils;

/**
 * @Destryption 背景音乐选择
 * @Author Jerry
 * @Date 2017-2-8 下午2:09:49
 * @Note
 */
public class MusicSelector extends MenuActivity {
	private ArrayList<FileInfo> fileList = null;
	private int musicPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("HandlerLeak")
	private Handler mTtsCompletedHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 8:
				finish();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		if (0 == selectItem || 1 == selectItem) {
			saveMusicFile(selectItem);
			setResult(Activity.RESULT_OK);
		}
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		SharedPreferences shared = getSharedPreferences(EbookConstants.SETTINGS_TABLE, Context.MODE_PRIVATE);
		String path = shared.getString(EbookConstants.MUSICE_PATH, null);
		ArrayList<File> filesList = FileOperateUtils.getMusicInDir();
		if (null != filesList && 0 < filesList.size()) {
			for (int i=0; i< filesList.size(); i++) {
				File f = filesList.get(i);
				FileInfo info = new FileInfo();
				info.name = f.getName();
				info.path = f.getPath();
				mMenuList.add(info.name);
				fileList.add(info);
				if(f.getPath().equals(path)){
					musicPosition = i;
				}
			}
		}
		
		mMenuView.setSelectItem(musicPosition);
	}

	private void saveMusicFile(int index) {
		SharedPreferences shared = getSharedPreferences(EbookConstants.SETTINGS_TABLE, Context.MODE_PRIVATE);
		Intent intent = new Intent(EbookConstants.MENU_PAGE_EDIT);
		Editor edit = shared.edit();
		intent.putExtra("result_flag", 2);
		edit.putString(EbookConstants.MUSICE_PATH, fileList.get(selectItem).path);
		edit.commit();
		sendBroadcast(intent);

		// 提示设定成功
		PromptDialog mPromptDialog = new PromptDialog(this, getResources().getString(R.string.library_setting_success));
		mPromptDialog.setHandler(mTtsCompletedHandler, 8);
		mPromptDialog.show();
	}

}
