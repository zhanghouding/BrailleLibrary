package com.sunteam.library.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sunteam.common.menu.BaseActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.Tools;
import com.sunteam.common.utils.dialog.ConfirmListener;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.LoginAsyncTask;
import com.sunteam.library.utils.PublicUtils;

public class AccountLogin extends BaseActivity implements OnFocusChangeListener, View.OnKeyListener, TextWatcher {
	private String mTitle; // 菜单标题
	private TextView mTvTitle;
	private View mLine = null;
	private TextView mTvUserNameHint; // 用户名
	private EditText mEtUserName; // 用户名编辑控件
	private TextView mTvPasswdHint; // 密码
	private EditText mEtPasswd; // 密码编辑控件
	private Button mBtConfirm; // 登录按钮
	private Button mBtCancel; // 取消按钮
	
	private int fontColor, backgroundColor, hightColor;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getIntentPara();
		initView();
	}

	private void getIntentPara() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);

		if (null == mTitle) {
			finish();
			return;
		}
	}

	private void initView() {
		Tools mTools = new Tools(AccountLogin.this);
		fontColor = mTools.getFontColor();
		backgroundColor = mTools.getBackgroundColor();
		hightColor = mTools.getHighlightColor();
		this.getWindow().setBackgroundDrawable(new ColorDrawable(mTools.getBackgroundColor()));
		setContentView(R.layout.library_account_login);

		mTvTitle = (TextView) findViewById(R.id.library_account_login_title);
		mTvTitle.setText(mTitle);
		mTvTitle.setTextColor(fontColor); // 设置title的文字颜色

		mLine = (View) findViewById(R.id.library_account_login_line);
		mLine.setBackgroundColor(fontColor); // 设置分割线的背景色

		// 用户名
		mTvUserNameHint = (TextView) findViewById(R.id.library_account_login_username_hint);
		mTvUserNameHint.setTextColor(fontColor);
		mEtUserName = (EditText) findViewById(R.id.library_account_login_username_input);
		mEtUserName.setTextColor(fontColor);

		// 密码
		mTvPasswdHint = (TextView) findViewById(R.id.library_account_login_passwd_hint);
		mTvPasswdHint.setTextColor(fontColor);
		mEtPasswd = (EditText) findViewById(R.id.library_account_login_passwd_input);
		mEtPasswd.setTextColor(fontColor);

		// Button
		mBtConfirm = (Button) findViewById(R.id.library_account_login_confirm);
		mBtConfirm.setTextColor(fontColor);
		mBtConfirm.setBackgroundColor(mTools.getBackgroundColor());
		mBtCancel = (Button) findViewById(R.id.library_account_login_cancel);
		mBtCancel.setTextColor(fontColor);
		mBtCancel.setBackgroundColor(mTools.getBackgroundColor());

		// 设置编辑框按键监听
		mEtUserName.setOnKeyListener(this);
		mEtPasswd.setOnKeyListener(this);

		// 添加编辑框文本变化监听
		mEtUserName.addTextChangedListener(this);
		mEtPasswd.addTextChangedListener(this);

		// 设置焦点监听
		mEtUserName.setOnFocusChangeListener(this);
		mEtPasswd.setOnFocusChangeListener(this);
		mBtConfirm.setOnFocusChangeListener(this);
		mBtCancel.setOnFocusChangeListener(this);

		// 设置测试账号
		mEtUserName.setText("test1");
		mEtPasswd.setText("123");
		
		mEtUserName.requestFocus();
		
		TtsUtils.getInstance().speak(mTitle + "," + getFocusString());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean ret = true;
		switch (keyCode) {
		case KeyEvent.KEYCODE_STAR: // 屏蔽星号键功能
		case KeyEvent.KEYCODE_POUND: // 屏蔽井号键功能
			break;
		default:
			ret = false;
			break;
		}
		if (!ret) {
			ret = super.onKeyDown(keyCode, event);
		}
		return ret;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean ret = true;
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK: // 删除尾部字符
			ret = processKeyBack();
			break;
		default:
			ret = false;
			break;
		}
		if (!ret) {
			ret = super.onKeyUp(keyCode, event);
		}
		return ret;
	}

	public void onClickForConfirm(View v) {
		TtsUtils.getInstance().speak(mBtConfirm.getText().toString());
		String account = mEtUserName.getText().toString();
		String passwd = mEtPasswd.getText().toString();
		new LoginAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, account, passwd);
	}

	public void onClickForCancel(View v) {
		PublicUtils.showToast(this, mBtCancel.getText().toString(), true);
	}

	private String getFocusString() {
		String s = "";
		if (mEtUserName.isFocused()) {
			s = mEtUserName.getText().toString();
			if (s.isEmpty()) {
				s = mEtUserName.getHint().toString();
			} else {
				s = mTvUserNameHint.getText().toString() + "," + s;
			}
		} else if (mEtPasswd.isFocused()) {
			s = mEtPasswd.getText().toString();
			if (s.isEmpty()) {
				s = mEtPasswd.getHint().toString();
			} else {
				s = mTvPasswdHint.getText().toString() + "," + s;
			}
		} else if (mBtConfirm.isFocused()) {
			s = mBtConfirm.getText().toString();
		} else if (mBtCancel.isFocused()) {
			s = mBtCancel.getText().toString();
		}

		return s;
	}

	private String getFocusHint() {
		String s = "";
		if (mEtUserName.isFocused()) {
			s = mEtUserName.getHint().toString();
		} else if (mEtPasswd.isFocused()) {
			s = mEtPasswd.getHint().toString();
		}

		return s;
	}

	// 处理【退出】键:1.焦点不在编辑框时，直接返回；2.焦点在编辑框且编辑框内容为空时直接返回；3.焦点在编辑框且编辑框内容不为空则删除编辑框尾部字符
	private void delTailCh(EditText et) {
		String s = et.getText().toString();
		if (s.isEmpty()) {
			s = et.getHint().toString();
		} else {
			et.setText(s.substring(0, s.length() - 1));
			et.setSelection(s.length() - 1);
			s = getResources().getString(R.string.common_delete) + ", " + s.substring(s.length() - 1);
		}
		TtsUtils.getInstance().speak(s);
	}

	// 处理【退出】键: 焦点在编辑控件上则删除尾部字符；否则退出当前界面
	private boolean processKeyBack() {
		boolean ret = true;
		if (mEtUserName.isFocused()) {
			delTailCh(mEtUserName);
		} else if (mEtPasswd.isFocused()) {
			delTailCh(mEtPasswd);
		} else {
			ret = false;
		}
//		CommonUtils.sendKeyEvent(KeyEvent.KEYCODE_DEL);

		return ret;
	}

	// 焦点变化
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			String s = getFocusString();
			TtsUtils.getInstance().speak(s);
		}

		// Button需要设置背景和焦点色
		if(v.getId() == R.id.library_account_login_confirm || v.getId() == R.id.library_account_login_cancel){
			if(hasFocus){
				v.setBackgroundColor(hightColor);
				toggleInputmethodWindow(this);
			} else{
				v.setBackgroundColor(backgroundColor);
			}
		}
	}

	// 在编辑控件中截获按键
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_DPAD_CENTER == keyCode || KeyEvent.KEYCODE_ENTER == keyCode) {
			// 截获OK键, 定位到下一个控件
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				onKeyDown(keyCode, event);
			} else {
				onKeyUp(keyCode, event);
			}
			return true;
		}

		// 把其它键都传给下一个控件
		return false;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		if (0 == after && count > 0) {
			String s1 = s.toString().substring(start, start + count);
			s1 = getResources().getString(R.string.common_delete) + " " + s1;
			TtsUtils.getInstance().speak(s1);
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (count <= 0) {
			return;
		}
		String s1 = s.toString().substring(start, start + count);

		TtsUtils.getInstance().speak(s1);
	}

	@Override
	public void afterTextChanged(Editable s) {
		String s1 = s.toString();
		if (null == s1 || 0 == s1.length()) {
			s1 = getFocusHint();
			TtsUtils.getInstance().speak(s1, TtsUtils.TTS_QUEUE_ADD);
		}
	}

	// 显示、隐藏切换
	private void toggleInputmethodWindow(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			// 显示、隐藏切换
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@SuppressWarnings("unused")
	private void startWifi() {
		String s = getResources().getString(R.string.library_startwifi);
		ConfirmDialog mConfirmDialog = new ConfirmDialog(this, s);
		mConfirmDialog.setConfirmListener(new ConfirmListener() {

			@Override
			public void doConfirm() {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						startWifiSetting();
					}
				}, 10);
			}

			@Override
			public void doCancel() {
			}
		});
		mConfirmDialog.show();
	}

	private void startWifiSetting() {
		Intent intent = new Intent();
		String packageName = "com.sunteam.settings";
		String className = "com.sunteam.settings.activity.WifiList";
		intent.setClassName(packageName, className);
		String title = getResources().getString(R.string.library_wifi_setting);
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		startActivity(intent);
	}

}
