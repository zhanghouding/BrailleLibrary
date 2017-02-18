package com.sunteam.library.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.sunteam.common.menu.BaseActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.utils.PromptDialog;
import com.sunteam.common.utils.Tools;
import com.sunteam.library.R;
import com.sunteam.library.utils.EbookConstants;
import com.sunteam.library.utils.MediaPlayerUtils;
import com.sunteam.library.utils.TTSUtils;

/**
 * @Destryption 音乐强度设置，实际上就是北京音乐音量，目前看来与系统音量一致！
 * @Author Jerry
 * @Date 2017-2-8 上午10:12:27
 * @Note
 */
public class MusicVolume extends BaseActivity {
	private final int MUSIC_VOLUME_MIN = 1; // 背景音强度设置最小值
	public final int MUSIC_VOLUME_MAX = 10; // 背景音强度设置最大值 
	private final int MUSIC_VOLUME_STEP = 1; // 背景音强度调整步长
	public final int SYSTEM_VOLUME_MAX = 15; // 系统音量最大值
	private final float MUSIC_VOLUME_SCALE = ((float) SYSTEM_VOLUME_MAX / MUSIC_VOLUME_MAX); // 设置值与实际值的映射比例

	private String mTitle; // 菜单标题
	private TextView mTvTitle;
	private View mLine = null;
	private TextView mTvVolume;
	private int musicVolume = MUSIC_VOLUME_MIN; // 设置范围：[1,10]，实际范围:[1,15]
	private SharedPreferences musicShared;
	private AudioManager mAudioManager;
	private int currentVolume; // 当前音量

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getIntentPara();
		initView();
	}

	@Override
	protected void onResume() {
		String s = mTitle + ", " + musicVolume;
		TTSUtils.getInstance().speakMenu(s);
		super.onResume();
	}

	private void getIntentPara() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);

		if (null == mTitle) {
			finish();
			return;
		}
	}

	// 在语速语调设置中，所有文字字号统一用大字号: 40sp, 已经在布局文件中初始化，不必在此与功能设置中的字号设置挂钩
	private void initView() {
		Tools mTools = new Tools(MusicVolume.this);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(mTools.getBackgroundColor()));
		setContentView(R.layout.common_number_edit);
		
		musicShared = getSharedPreferences(EbookConstants.SETTINGS_TABLE, Context.MODE_PRIVATE);
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		currentVolume = getMusicVlolume();
		musicVolume = (int) ((currentVolume + MUSIC_VOLUME_SCALE / 2)/ MUSIC_VOLUME_SCALE);
		playMusic();

		mTvTitle = (TextView) findViewById(R.id.common_number_edit_title);
		mTvTitle.setText(mTitle);
		mTvTitle.setTextColor(mTools.getFontColor()); // 设置title的文字颜色

		mLine = (View) findViewById(R.id.common_number_edit_line);
		mLine.setBackgroundColor(mTools.getFontColor()); // 设置分割线的背景色

		mTvVolume = (TextView) findViewById(R.id.common_number_edit_digit);
		mTvVolume.setText(String.valueOf(musicVolume));
		mTvVolume.setTextColor(mTools.getFontColor()); // 设置文字颜色
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_DPAD_LEFT:
			updateMusicVolume(-MUSIC_VOLUME_STEP);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			updateMusicVolume(MUSIC_VOLUME_STEP);
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			showPromptDialog();
			break;
		case KeyEvent.KEYCODE_BACK: // 恢复上次设置
			stopMusic();
			setMusicVlolume(currentVolume);
			break;
		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	private void updateMusicVolume(int step) {
		musicVolume += step;
		if (musicVolume < MUSIC_VOLUME_MIN) {
			musicVolume = MUSIC_VOLUME_MAX;
		} else if (musicVolume > MUSIC_VOLUME_MAX) {
			musicVolume = MUSIC_VOLUME_MIN;
		}
		String s = String.valueOf(musicVolume);
		mTvVolume.setText(s);

		TTSUtils.getInstance().speakMenu(s); // 不要把该行放到设置音量后面!因为音量变化后在主菜单中会收到广播，也会发音，但若已经在播音就不播音量值了。
		setMusicVlolume((int) (musicVolume * MUSIC_VOLUME_SCALE));
	}

	@SuppressLint("HandlerLeak")
	private Handler mTtsCompletedHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 8:
				Intent intent = new Intent();
				intent.putExtra("action", EbookConstants.TO_PLAY_MUSIC);
				setResult(Activity.RESULT_OK, intent);
				MediaPlayerUtils.getInstance().stop();
				finish();
				break;

			default:
				break;
			}
		}
	};

	private void showPromptDialog() {
		stopMusic();
		setMusicVlolume((int) (musicVolume * MUSIC_VOLUME_SCALE));
		// 设置成功就有提示对话框
		PromptDialog mPromptDialog = new PromptDialog(this, getResources().getString(R.string.library_setting_success));
		mPromptDialog.setHandler(mTtsCompletedHandler, 8);
		mPromptDialog.show();
	}

	// 保存背景音乐强度设置
	public void setMusicVlolume(int volume) {
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 1);
	}

	// 获取背景音乐强度设置
	public int getMusicVlolume() {
		return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	private void playMusic() {
		// TODO to create class FileOperateUtils 
		String path = musicShared.getString(EbookConstants.MUSICE_PATH, null);
		if (null == path) {
//			path = FileOperateUtils.getFirstMusicInDir();
		} else {
			File file = new File(path);
			if (!file.exists()) {
//				path = FileOperateUtils.getFirstMusicInDir();
			}
		}
		if (null != path) {
			MediaPlayerUtils.getInstance().play(path, true);
		}
	}

	private void stopMusic() {
		boolean isMusic = musicShared.getBoolean(EbookConstants.MUSICE_STATE, false);
		if (!isMusic) {
			MediaPlayerUtils.getInstance().stop();
		}
	}

}
