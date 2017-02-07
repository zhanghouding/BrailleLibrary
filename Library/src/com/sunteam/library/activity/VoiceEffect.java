package com.sunteam.library.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.utils.PromptDialog;
import com.sunteam.library.R;
import com.sunteam.library.utils.TTSUtils;

/**
 * @Destryption 电子图书中朗读界面需要设置文本朗读的一些设置，如：中文角色、语速、语调、语音音效
 * @Author Jerry
 * @Date 2017-2-7 上午9:55:09
 * @Note
 */
public class VoiceEffect extends MenuActivity {

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
		PromptDialog mPromptDialog = new PromptDialog(this, getResources().getString(R.string.library_setting_success));
		mPromptDialog.setHandler(mTtsCompletedHandler, 8);
		mPromptDialog.show();
		/*TTSUtils.getInstance().setEffect(this, selectItem, new PromptListener() {
			@Override
			public void onComplete() {
				mTtsCompletedHandler.sendEmptyMessage(8);
			}
			
		});*/
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
//			TtsUtils.getInstance().setParameter(SpeechConstant.VOICE_EFFECT, Tools.getSpeaker(selectItem));
		} else if (KeyEvent.KEYCODE_DPAD_DOWN == keyCode) {
			selectItem++;
			if (selectItem >= mMenuList.size()) {
				selectItem = 0;
			}
//			TtsUtils.getInstance().setParameter(SpeechConstant.VOICE_EFFECT, Tools.getSpeaker(selectItem));
		} else if (KeyEvent.KEYCODE_BACK == keyCode) {
			// 恢复上次设置
			selectItem = TTSUtils.getInstance().getCurEffectIndex();
//			TtsUtils.getInstance().setParameter(SpeechConstant.VOICE_EFFECT, Tools.getSpeaker(selectItem));
		}

		return super.onKeyDown(keyCode, event);
	}

	// 不能直接返回，需要等发音结束后再返回，否则发音被中断了。
	private void returnFatherActivity() {
		selectItem = mMenuView.getSelectItem();
		String menuItem = mMenuView.getSelectItemContent(selectItem);
		VoiceEffect.super.setResultCode(Activity.RESULT_OK, selectItem, menuItem);
	}

}
