package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.utils.TTSUtils;

/**
 * @Destryption 电子图书中朗读界面需要设置文本朗读的一些设置，如：中文角色、语速、语调、语音音效
 * @Author Jerry
 * @Date 2017-2-7 上午9:55:09
 * @Note
 */
public class VoiceSettings extends MenuActivity {

	@Override
	protected void onResume() {
		TtsUtils.getInstance().restoreSettingParameters();
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		TtsUtils.getInstance().restoreSettingParameters(); // 在菜单界面使用系统设置朗读

		if (Activity.RESULT_OK != resultCode || null == data) { // 在子菜单中回传的标志
			return;
		}
		// 设置成功后，销毁当前界面，返回到父窗口
		super.setResultCode(Activity.RESULT_OK, requestCode, getSelectItemContent());
	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		String[] list;
		int defaultItem = 0;
	
		switch(selectItem){
		case 0: // 中文角色
			list = getResources().getStringArray(R.array.library_array_menu_voice_china);
			defaultItem = TTSUtils.getInstance().getRoleIndex();
			startNextActivity(VoiceSpeaker.class, selectItem, menuItem, list, defaultItem);
			break;
		case 1: // 语速
			defaultItem = TTSUtils.getInstance().getSpeed();
			startNextActivity(VoiceSpeed.class, selectItem, menuItem, null, defaultItem);
			break;
		case 2: // 语调
			defaultItem = TTSUtils.getInstance().getPitch();
			startNextActivity(VoiceTone.class, selectItem, menuItem, null, defaultItem);
			break;
		case 3: // 语音音效
			list = getResources().getStringArray(R.array.library_array_menu_voice_effect);
			defaultItem = TTSUtils.getInstance().getCurEffectIndex();
			startNextActivity(VoiceEffect.class, selectItem, menuItem, list, defaultItem);
			break;
		default:
			break;
		}
	}

	private void startNextActivity(Class<?> cls, int selectItem, String title, String[] list, int defaultItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, list); // 菜单列表
		intent.putExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, defaultItem); // 子菜单默认值

		intent.setClass(this, cls);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);
	}

}
