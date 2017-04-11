package com.sunteam.library.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.menu.MenuGlobal;
import com.sunteam.common.utils.ArrayUtils;
import com.sunteam.common.utils.SharedPrefUtils;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.utils.EbookConstants;
import com.sunteam.library.utils.FileOperateUtils;
import com.sunteam.library.utils.MediaPlayerUtils;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 音乐强度设置，已经把背景音STREAM_TYPE由STRAM_MUSIC改为STREAM_ALARM,以便与TTS区分开。
 * @Author Jerry
 * @Date 2017-2-8 上午10:12:27
 * @Note STREAM_ALARM已经在固件中设置为STRAM_MUSIC的4倍。背景音强度设置为:很强-较强-适中-很弱, 分别是STRAM_MUSIC音的4倍、3倍、2倍和1倍。
 */
public class MusicVolume extends MenuActivity {
	private SharedPreferences musicShared;
	private AudioManager mAudioManager;
	private byte[] volumeScale = {1, 2, 3, 4}; // 音量强度
	private VolumeReceiver mVolumeReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
		registerVolumeReceiver();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterVolumeReceiver();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		// 强制停止背景音乐
		MediaPlayerUtils.getInstance().stop();
		
		// 恢复上次音量
		int index = musicShared.getInt(EbookConstants.MUSIC_INTENSITY, 0);
		setMusicVlolume(index);
	}			

	// 保存背景音乐强度设置
	public void setMusicVlolume(int index) {
		int volume = getMusicVlolume() * volumeScale[index]; 
		mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume, 1);
	}

	// 获取背景音乐强度设置
	public int getMusicVlolume() {
		return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	@SuppressWarnings("deprecation")
	private void playMusic() {
		int mode = Context.MODE_WORLD_READABLE + Context.MODE_MULTI_PROCESS;
		String path = SharedPrefUtils.getSharedPrefString(this, EbookConstants.SETTINGS_TABLE, mode, EbookConstants.MUSICE_PATH, null);
		if (null == path) {
			path = FileOperateUtils.getFirstMusicInDir();
		} else {
			File file = new File(path);
			if (!file.exists()) {
				path = FileOperateUtils.getFirstMusicInDir();
			}
		}
		if (null != path) {
			MediaPlayerUtils.getInstance().play(path, true);
		}
	}

	/*private void stopMusic() {
		boolean isMusic = musicShared.getBoolean(EbookConstants.MUSICE_STATE, false);
		if (!isMusic) {
			MediaPlayerUtils.getInstance().stop();
		}
	}*/

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		if (selectItem >= mMenuList.size()) {
			selectItem = 0;
		}
		
		// 保存设置
		SharedPrefUtils.saveSettings(this, EbookConstants.SETTINGS_TABLE, EbookConstants.MUSIC_INTENSITY, selectItem);

		String tips = getResources().getString(R.string.library_setting_success);
		PublicUtils.showToast(this, tips, new PromptListener() {
			
			@Override
			public void onComplete() {
				returnFatherActivity();
			}
		});
	}

	// 在语速语调设置中，所有文字字号统一用大字号: 40sp, 已经在布局文件中初始化，不必在此与功能设置中的字号设置挂钩
	@SuppressLint("WorldReadableFiles") @SuppressWarnings("deprecation")
	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		String[] list = getResources().getStringArray(R.array.library_array_menu_music_intensity);
		mMenuList = ArrayUtils.strArray2List(list);
		int mode = Context.MODE_WORLD_READABLE + Context.MODE_MULTI_PROCESS;
		musicShared = getSharedPreferences(EbookConstants.SETTINGS_TABLE, mode);
		selectItem = musicShared.getInt(EbookConstants.MUSIC_INTENSITY, 0);
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		setMusicVlolume(selectItem);
		playMusic();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean ret = super.onKeyDown(keyCode, event);

		if (KeyEvent.KEYCODE_DPAD_UP == keyCode || KeyEvent.KEYCODE_DPAD_DOWN == keyCode || KeyEvent.KEYCODE_DPAD_LEFT == keyCode
				|| KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
			selectItem = getSelectItem();
			setMusicVlolume(selectItem);
		}
		return ret;
	}

	// 不能直接返回，需要等发音结束后再返回，否则发音被中断了。
	private void returnFatherActivity() {
		selectItem = getSelectItem();
		String menuItem = getSelectItemContent();
		MusicVolume.super.setResultCode(Activity.RESULT_OK, selectItem, menuItem);
	}

	private void registerVolumeReceiver() {
		if (null == mVolumeReceiver) {
			mVolumeReceiver = new VolumeReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction("android.media.VOLUME_CHANGED_ACTION"); // 音量变化
			registerReceiver(mVolumeReceiver, filter);
		}
	}

	private void unregisterVolumeReceiver() {
		if (null != mVolumeReceiver) {
			unregisterReceiver(mVolumeReceiver);
			mVolumeReceiver = null;
		}
	}

	private class VolumeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (!action.equals("android.media.VOLUME_CHANGED_ACTION")) {
				return;
			}
			// AudioManager.EXTRA_PREV_VOLUME_STREAM_VALUE,EXTRA_VOLUME_STREAM_VALUE 是隐藏属性
			int streamType = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", 0);
			int newVolume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0);
			int oldVolume = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", 0);
			if (AudioManager.STREAM_MUSIC == streamType) {
				setMusicVlolume(selectItem);
				MenuGlobal.debug("[MusicVolume][VolumeReceiver] action = VOLUME_CHANGED_ACTION, streamType = " + streamType + ", newVolume = " + newVolume + ", oldVolume = " + oldVolume);
			}
		}
	}

}
