package com.sunteam.library.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.utils.PromptDialog;
import com.sunteam.common.utils.SharedPrefUtils;
import com.sunteam.library.R;
import com.sunteam.library.utils.EbookConstants;

/**
 * @Destryption 音乐开关设置界面
 * @Author Jerry
 * @Date 2017-2-8 上午9:08:29
 * @Note
 */
public class MusicSwitch extends MenuActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		selectItem = getMusicSwitch();
	}

	@SuppressLint("HandlerLeak")
	private Handler mTtsCompletedHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 8:
				setResult(Activity.RESULT_OK);
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
			Intent intent = new Intent();
			intent.putExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, selectItem);
			intent.putExtra("selectStr", menuItem);
			saveMusicSwitch(selectItem);
			setResult(Activity.RESULT_OK, intent);
		}
	}

	private void saveMusicSwitch(int index) {
		Intent intent = new Intent(EbookConstants.MENU_PAGE_EDIT);
		intent.putExtra("result_flag", 1);
		SharedPrefUtils.saveSettings(this, EbookConstants.SETTINGS_TABLE, EbookConstants.MUSICE_STATE, index);
		sendBroadcast(intent);
		
		// 提示设定成功
		PromptDialog mPromptDialog = new PromptDialog(this, getResources().getString(R.string.library_setting_success));
		mPromptDialog.setHandler(mTtsCompletedHandler, 8);
		mPromptDialog.show();
	}

	@SuppressWarnings("deprecation")
	private int getMusicSwitch() {
		int mode = Context.MODE_WORLD_READABLE + Context.MODE_MULTI_PROCESS;
		return SharedPrefUtils.getSharedPrefInt(this, EbookConstants.SETTINGS_TABLE, mode, EbookConstants.MUSICE_STATE, 0);
	}

}
