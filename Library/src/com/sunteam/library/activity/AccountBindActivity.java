package com.sunteam.library.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sunteam.common.menu.BaseActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.Tools;
import com.sunteam.library.R;

/**
 * @Destryption 图书馆账号绑定；需要服务器端提供接口
 * @Author Jerry
 * @Date 2017-2-4 下午3:22:05
 * @Note
 */
@SuppressWarnings("unused")
public class AccountBindActivity extends BaseActivity {
	private TextView tvTitle; // 标题
	private View mLine; // 标题下的分隔线
	private TextView tvAccount; // 图书馆账号
	private TextView tvPasswd; // 账号密码
	private EditText etAccount; // 图书馆账号
	private EditText etPasswd; // 账号密码
	private String mTitle; // 界面标题
	private String mAccount; // 图书馆账号
	private String mPasswd; // 账号密码

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();

		String accountStr = etAccount.getText().toString();
		if (null == accountStr || 0 == accountStr.length()) {
			accountStr = etAccount.getHint().toString();
		}
		String s = mTitle + ", " + tvAccount.getText().toString() + ", " + accountStr;
		TtsUtils.getInstance().speak(s);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (processKeyEvent(keyCode, event)) {
			return true;
		}

		return super.onKeyUp(keyCode, event);
	}

	// 在日期与时间设置中，所有文字字号统一用大字号: 40sp, 已经在布局文件中初始化，不必在此与功能设置中的字号设置挂钩
	private void initView() {
		Tools mTools = new Tools(this);
		int backgroundColor = mTools.getBackgroundColor();
		int fontColor = mTools.getFontColor();

		this.getWindow().setBackgroundDrawable(new ColorDrawable(backgroundColor)); // 设置View的背景色
		setContentView(R.layout.library_account_bind);

		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);

		tvTitle = (TextView) findViewById(R.id.library_account_bind_title);
		mLine = (TextView) findViewById(R.id.library_account_bind_line);
		tvAccount = (TextView) findViewById(R.id.library_account_bind_account_hint);
		tvPasswd = (TextView) findViewById(R.id.library_account_bind_account_passwd_hint);
		etAccount = (EditText) findViewById(R.id.library_account_bind_account_input);
		etPasswd = (EditText) findViewById(R.id.library_account_bind_account_passwd_input);

		tvTitle.setTextColor(fontColor); // 设置title的文字颜色

		int fontSize = mTools.getFontSize();
		tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel()); // 设置title字号
		tvTitle.setHeight(mTools.convertSpToPixel(fontSize));
		mLine.setBackgroundColor(mTools.getFontColor()); // 设置分隔线的背景色
		tvAccount.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel()); // 设置title字号
		tvPasswd.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel()); // 设置title字号
	}

	public boolean processKeyEvent(int keyCode, KeyEvent event) {
		boolean ret = true;

		if (KeyEvent.ACTION_UP != event.getAction()) {
			return false;
		}

		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			break;
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			break;
		default:
			ret = false;
			break;
		}

		return ret;
	}

	@SuppressLint("HandlerLeak")
	Handler mHandle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: // 确认
				finish();
				break;
			case 1:
				finish();
				break;
			default:
				break;
			}
		}

	};

}
