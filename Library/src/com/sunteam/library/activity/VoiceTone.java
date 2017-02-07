package com.sunteam.library.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.iflytek.cloud.SpeechConstant;
import com.sunteam.common.menu.BaseActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.CommonConstant;
import com.sunteam.common.utils.Tools;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.utils.EbookConstants;
import com.sunteam.library.utils.TTSUtils;

public class VoiceTone extends BaseActivity {
	private final int TTS_TONE_MIN = 1; // 语调最小值
	private final int TTS_TONE_STEP = 1; // 语调调整步长
	private final int TTS_TONE_SCALE = CommonConstant.VOICE_TONE_MAX / CommonConstant.VOICE_TONESETTING_MAX; // 语调设置值与实际值的映射比例

	private String mTitle; // 菜单标题
	private TextView mTvTitle;
	private View mLine = null;
	private TextView mTvTone;
	private int ttsTone = CommonConstant.DEFAULT_VOICETONE / TTS_TONE_SCALE; // 功能设置范围：[1, 20]，实际范围: [1, 100]

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getIntentPara();
		initView();
	}

	@Override
	protected void onResume() {
		String s = mTitle + ", " + ttsTone;
		TtsUtils.getInstance().speak(s);
		super.onResume();
	}

	private void getIntentPara() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		ttsTone = intent.getIntExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, ttsTone);
//		ttsTone /= TTS_TONE_SCALE; // 已经除以5了

		if (null == mTitle) {
			finish();
			return;
		}
	}

	// 在语速语调设置中，所有文字字号统一用大字号: 40sp, 已经在布局文件中初始化，不必在此与功能设置中的字号设置挂钩
	private void initView() {
		Tools mTools = new Tools(VoiceTone.this);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(mTools.getBackgroundColor()));
		setContentView(R.layout.library_speed_setting);

		mTvTitle = (TextView) findViewById(R.id.library_speed_title);
		mTvTitle.setText(mTitle);
		mTvTitle.setTextColor(mTools.getFontColor()); // 设置title的文字颜色

		mLine = (View) findViewById(R.id.library_speed_line);
		mLine.setBackgroundColor(mTools.getFontColor()); // 设置分割线的背景色

		mTvTone = (TextView) findViewById(R.id.library_speed_tts_speed);
		mTvTone.setText(String.valueOf(ttsTone));
		mTvTone.setTextColor(mTools.getFontColor()); // 设置文字颜色
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_DPAD_LEFT:
			updateTone(-TTS_TONE_STEP);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			updateTone(TTS_TONE_STEP);
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			setResultCode(Activity.RESULT_OK, ttsTone * TTS_TONE_SCALE, "");
			break;
		case KeyEvent.KEYCODE_BACK: // 恢复上次设置
			ttsTone = TTSUtils.getInstance().getPitch();
			TtsUtils.getInstance().setParameter(SpeechConstant.PITCH, "" + ttsTone * TTS_TONE_SCALE);
			break;
		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	private void updateTone(int step) {
		ttsTone += step;
		if (ttsTone < TTS_TONE_MIN) {
			ttsTone = CommonConstant.VOICE_TONESETTING_MAX;
		} else if (ttsTone > CommonConstant.VOICE_TONESETTING_MAX) {
			ttsTone = TTS_TONE_MIN;
		}
		String s = String.valueOf(ttsTone);
		mTvTone.setText(s);
		TtsUtils.getInstance().setParameter(SpeechConstant.PITCH, "" + ttsTone * TTS_TONE_SCALE);
		
		TtsUtils.getInstance().speak(/*mTitle + */s); // 设置语调时不朗读标题
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

	/**
	 * @Name: setResultCode
	 * @Description: 父类在需要退出时，调用该方法
	 * @param @param
	 *            data 需要传递给子类的数据
	 * @return void
	 * @author Jerry
	 */
	private void setResultCode(int resultCode, int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, selectItem);
		intent.putExtra("selectStr", menuItem);
		setResult(resultCode, intent);

		// 设置成功就有提示对话框
		/*PromptDialog mPromptDialog = new PromptDialog(this, getResources().getString(R.string.library_setting_success));
		mPromptDialog.setHandler(mTtsCompletedHandler, 8);
		mPromptDialog.show();
		saveToneSetting();*/
		TTSUtils.getInstance().setPitch(this, ttsTone, new PromptListener() {
			@Override
			public void onComplete() {
				mTtsCompletedHandler.sendEmptyMessage(8);
			}
			
		});
	}

	// 保存语速设置
	public void saveToneSetting() {
		SharedPreferences mSharedPreferences = getSharedPreferences(EbookConstants.TTS_SETTINGS, Activity.MODE_PRIVATE);
		Editor editor = mSharedPreferences.edit();
		editor.putString(SpeechConstant.PITCH, (ttsTone * TTS_TONE_SCALE) + "");
		editor.commit();
	}

}
