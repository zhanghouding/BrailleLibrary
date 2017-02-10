package com.sunteam.library.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.iflytek.cloud.SpeechConstant;
import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.tts.TtsUtils;
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
	private final int[] mEffect = {
		0,	//原声	
		2,	//回声
		3,	//机器人
		7,	//阴阳怪气
	};	//音效
	

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
	}

	@Override
	// 原声、回声、机器人、阴阳怪气
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		TtsUtils.getInstance().setMuteFlag(true);
		boolean ret = super.onKeyDown(keyCode, event);
		if (KeyEvent.KEYCODE_DPAD_UP == keyCode || KeyEvent.KEYCODE_DPAD_DOWN == keyCode || KeyEvent.KEYCODE_DPAD_LEFT == keyCode
				|| KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
			TtsUtils.getInstance().setMuteFlag(false);
//			TtsUtils.getInstance().setParameter("effect", "" + mEffect[selectItem]);
			String s = getSelectItemContent();
//			TtsUtils.getInstance().speak(s);
			TTSUtils.getInstance().testEffect(s, s);
		}
		return ret;

		// 先设置发音人再发声
		/*selectItem = getSelectItem();
		if (KeyEvent.KEYCODE_DPAD_UP == keyCode) {
			selectItem--;
			if (selectItem < 0) {
				selectItem = mMenuList.size() - 1;
			}
			TtsUtils.getInstance().setParameter("effect", "" + mEffect[selectItem]);
		} else if (KeyEvent.KEYCODE_DPAD_DOWN == keyCode) {
			selectItem++;
			if (selectItem >= mMenuList.size()) {
				selectItem = 0;
			}
			
			// 在使用公用菜单时，以下音效测试，会被公用菜单中的发音中断，因此，只能在此直接音效值
			TtsUtils.getInstance().setParameter("effect", "" + mEffect[selectItem]);
//			String s = getSelectItemContent();
//			TTSUtils.getInstance().testEffect(selectItem, s);
		} else if (KeyEvent.KEYCODE_BACK == keyCode) {
			// 恢复上次设置; 暂时不必处理
		}

		return super.onKeyDown(keyCode, event);*/
	}

	// 不能直接返回，需要等发音结束后再返回，否则发音被中断了。
	private void returnFatherActivity() {
		selectItem = mMenuView.getSelectItem();
		String menuItem = mMenuView.getSelectItemContent(selectItem);
		VoiceEffect.super.setResultCode(Activity.RESULT_OK, selectItem, menuItem);
	}

}
