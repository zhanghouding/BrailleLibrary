package com.sunteam.library.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.iflytek.cloud.SpeechConstant;
import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.Tools;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.utils.TTSUtils;

/**
 * @Destryption 中文角色设置界面
 * @Author Jerry
 * @Date 2017-2-7 上午9:55:09
 * @Note
 */
public class VoiceChineseSpeaker extends MenuActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 用上次设置的发音人进行发音
		TtsUtils.getInstance().setParameter(SpeechConstant.VOICE_NAME, Tools.getSpeaker(selectItem));
	}

	@SuppressLint("HandlerLeak")
	private Handler mTtsCompletedHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 8:
				returnFatherActivity();
				break;

			default:
				break;
			}
		}
	};

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		if (selectItem >= mMenuList.size()) {
			selectItem = 0;
		}

		// 设置成功就有提示对话框
		/*PromptDialog mPromptDialog = new PromptDialog(this, getResources().getString(R.string.library_setting_success));
		mPromptDialog.setHandler(mTtsCompletedHandler, 8);
		mPromptDialog.show();
		saveRoleCnSetting(selectItem);*/
		TTSUtils.getInstance().setRoleCn(this, selectItem, new PromptListener() {
			@Override
			public void onComplete() {
				mTtsCompletedHandler.sendEmptyMessage(8);
			}
			
		});
	}

	@Override
	// 国语男声、国语女声、粤语女声、童声
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 先设置发音人再发声
		selectItem = getSelectItem();
		if (KeyEvent.KEYCODE_DPAD_UP == keyCode) {
			selectItem--;
			if (selectItem < 0) {
				selectItem = mMenuList.size() - 1;
			}
			TtsUtils.getInstance().setParameter(SpeechConstant.VOICE_NAME, Tools.getSpeaker(selectItem));
		} else if (KeyEvent.KEYCODE_DPAD_DOWN == keyCode) {
			selectItem++;
			if (selectItem >= mMenuList.size()) {
				selectItem = 0;
			}
			TtsUtils.getInstance().setParameter(SpeechConstant.VOICE_NAME, Tools.getSpeaker(selectItem));
		} else if (KeyEvent.KEYCODE_BACK == keyCode) {
			// 恢复上次设置
			selectItem = TTSUtils.getInstance().getCurRoleCnIndex();
			TtsUtils.getInstance().setParameter(SpeechConstant.VOICE_NAME, Tools.getSpeaker(selectItem));
		}

		return super.onKeyDown(keyCode, event);
	}

	// 不能直接返回，需要等发音结束后再返回，否则发音被中断了。
	private void returnFatherActivity() {
		selectItem = mMenuView.getSelectItem();
		String menuItem = mMenuView.getSelectItemContent(selectItem);
		VoiceChineseSpeaker.super.setResultCode(Activity.RESULT_OK, selectItem, menuItem);
	}

	/*// 保存中文发音人设置
	public void saveRoleCnSetting(int index) {
		String role = Tools.getSpeaker(index);
		SharedPreferences mSharedPreferences = getSharedPreferences(EbookConstants.TTS_SETTINGS, Activity.MODE_PRIVATE);
		Editor editor = mSharedPreferences.edit();
		editor.putString(SpeechConstant.VOICE_NAME, role);
		editor.commit();
	}*/

}
