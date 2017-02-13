package com.sunteam.library.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import com.sunteam.common.utils.CommonConstant;
import com.sunteam.common.utils.Tools;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.utils.TTSUtils;

public class VoiceSpeed extends BaseActivity {
	private final int TTS_SPEED_MIN = 1; // 语速最小值
	private final int TTS_SPEED_STEP = 1; // 语速调整步长
	private final int TTS_SPEED_SCALE = CommonConstant.VOICE_SPEED_MAX / CommonConstant.VOICE_SPEEDSETTING_MAX; // 语速设置值与实际值的映射比例

	private String mTitle; // 菜单标题
	private TextView mTvTitle;
	private View mLine = null;
	private TextView mTvSpeed;
	private int ttsSpeed = CommonConstant.DEFAULT_VOICESPEED / TTS_SPEED_SCALE; // 功能设置范围：[1,20]，实际范围:[1,200]

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getIntentPara();
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		String s = mTitle + ", " + ttsSpeed;
		TTSUtils.getInstance().speakTest(s, SpeechConstant.SPEED, "" + ttsSpeed * TTS_SPEED_SCALE);
	}

	private void getIntentPara() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		ttsSpeed = intent.getIntExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, ttsSpeed);

		if (null == mTitle) {
			finish();
			return;
		}
	}

	// 在语速语调设置中，所有文字字号统一用大字号: 40sp, 已经在布局文件中初始化，不必在此与功能设置中的字号设置挂钩
	private void initView() {
		Tools mTools = new Tools(VoiceSpeed.this);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(mTools.getBackgroundColor()));
		setContentView(R.layout.common_number_edit);

		mTvTitle = (TextView) findViewById(R.id.common_number_edit_title);
		mTvTitle.setText(mTitle);
		mTvTitle.setTextColor(mTools.getFontColor()); // 设置title的文字颜色

		mLine = (View) findViewById(R.id.common_number_edit_line);
		mLine.setBackgroundColor(mTools.getFontColor()); // 设置分割线的背景色

		mTvSpeed = (TextView) findViewById(R.id.common_number_edit_digit);
		mTvSpeed.setText(String.valueOf(ttsSpeed));
		mTvSpeed.setTextColor(mTools.getFontColor()); // 设置文字颜色
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_DPAD_LEFT:
			updateSpeed(-TTS_SPEED_STEP);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			updateSpeed(TTS_SPEED_STEP);
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			setResultCode(Activity.RESULT_OK, ttsSpeed * TTS_SPEED_SCALE, "");
			break;
		default:
			return super.onKeyUp(keyCode, event);
		}
		return true;
	}

	private void updateSpeed(int step) {
		ttsSpeed += step;
		if (ttsSpeed < TTS_SPEED_MIN) {
			ttsSpeed = CommonConstant.VOICE_SPEEDSETTING_MAX;
		} else if (ttsSpeed > CommonConstant.VOICE_SPEEDSETTING_MAX) {
			ttsSpeed = TTS_SPEED_MIN;
		}
		String s = String.valueOf(ttsSpeed);
		mTvSpeed.setText(s);
		TTSUtils.getInstance().speakTest(s, SpeechConstant.SPEED, "" + ttsSpeed * TTS_SPEED_SCALE);
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
	 * @param @param data 需要传递给子类的数据
	 * @return void
	 * @author Jerry
	 */
	private void setResultCode(int resultCode, int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, selectItem);
		intent.putExtra("selectStr", menuItem);
		setResult(resultCode, intent);

		TTSUtils.getInstance().setSpeed(this, ttsSpeed, new PromptListener() {
			@Override
			public void onComplete() {
				mTtsCompletedHandler.sendEmptyMessage(8);
			}
			
		});
	}

}
