package com.sunteam.library.activity;

import android.app.Activity;
import android.view.KeyEvent;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.utils.TTSUtils;

/**
 * @Destryption 语音音效设置界面
 * @Author Jerry
 * @Date 2017-2-7 上午9:55:09
 * @Note
 */
public class VoiceEffect extends MenuActivity {

	@Override
	protected void onResume() {
		// 禁止父类朗读菜单焦点行
		TtsUtils.getInstance().setMuteFlag(true);

		super.onResume();

		// 允许朗读
		TtsUtils.getInstance().setMuteFlag(false);
		if (!wakeupFlag) { // 不是休眠唤醒时才朗读焦点行内容
			String s = mTitle + "," + mMenuList.get(selectItem);
			TTSUtils.getInstance().testEffect(selectItem, s);
		}
	}	

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		if (selectItem >= mMenuList.size()) {
			selectItem = 0;
		}

		// 设置成功就有提示对话框
		TTSUtils.getInstance().setEffect(this, selectItem, new PromptListener() {
			@Override
			public void onComplete() {
				returnFatherActivity();
			}
			
		});
	}

	@Override
	// 原声、回声、机器人、阴阳怪气
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		TtsUtils.getInstance().setMuteFlag(true);
		boolean ret = super.onKeyDown(keyCode, event);
		TtsUtils.getInstance().setMuteFlag(false);
		if (KeyEvent.KEYCODE_DPAD_UP == keyCode || KeyEvent.KEYCODE_DPAD_DOWN == keyCode || KeyEvent.KEYCODE_DPAD_LEFT == keyCode
				|| KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
			int selectItem = getSelectItem();
			String menuItem = getSelectItemContent();
			TTSUtils.getInstance().testEffect(selectItem, menuItem);
		}
		return ret;
	}

	// 不能直接返回，需要等发音结束后再返回，否则发音被中断了。
	private void returnFatherActivity() {
		selectItem = getSelectItem();
		String menuItem = getSelectItemContent();
		VoiceEffect.super.setResultCode(Activity.RESULT_OK, selectItem, menuItem);
	}

}
