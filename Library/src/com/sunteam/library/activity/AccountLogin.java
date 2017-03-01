package com.sunteam.library.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sunteam.common.menu.BaseActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.Tools;
import com.sunteam.library.R;

public class AccountLogin extends BaseActivity implements OnFocusChangeListener, View.OnKeyListener {
	private String mTitle; // 菜单标题
	private TextView mTvTitle;
	private View mLine = null;
	private EditText mEtUserName; // 用户名编辑控件
	private EditText mEtPasswd; // 密码编辑控件
	private Button mBtLogin; // 登录按钮
	private Button mBtLogout; // 取消按钮
	
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

	// 在页码输入中，所有文字字号统一用大字号: 40sp, 已经在布局文件中初始化，不必在此与功能设置中的字号设置挂钩
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
		TextView mTvUsernameHint = (TextView) findViewById(R.id.library_account_login_username_hint);
		mTvUsernameHint.setTextColor(fontColor);
		mEtUserName = (EditText) findViewById(R.id.library_account_login_username_input);
		mEtUserName.setTextColor(fontColor);

		// 密码
		TextView mTvPasswdHint = (TextView) findViewById(R.id.library_account_login_passwd_hint);
		mTvPasswdHint.setTextColor(fontColor);
		mEtPasswd = (EditText) findViewById(R.id.library_account_login_passwd_input);
		mEtPasswd.setTextColor(fontColor);
		
		// Button
		mBtLogin = (Button) findViewById(R.id.library_account_login_confirm);
		mBtLogin.setTextColor(fontColor);
		mBtLogin.setBackgroundColor(mTools.getBackgroundColor());
		mBtLogout = (Button) findViewById(R.id.library_account_login_cancel);
		mBtLogout.setTextColor(fontColor);
		mBtLogout.setBackgroundColor(mTools.getBackgroundColor());

		// 设置编辑框按键监听
		mEtUserName.setOnKeyListener(this);
		mEtPasswd.setOnKeyListener(this);

		// 设置焦点监听
		mEtUserName.setOnFocusChangeListener(this);
		mEtPasswd.setOnFocusChangeListener(this);
		mBtLogin.setOnFocusChangeListener(this);
		mBtLogout.setOnFocusChangeListener(this);

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

	public void onClickForLogin(View v) {
		finish();
	}

	public void onClickForCancel(View v) {
		finish();
	}

	private String getFocusString() {
		String s = "";
		if (mEtUserName.isFocused()) {
			s = mEtUserName.getText().toString();
			if (s.isEmpty()) {
				s = mEtUserName.getHint().toString();
			}
		} else if (mEtPasswd.isFocused()) {
			s = mEtPasswd.getText().toString();
			if (s.isEmpty()) {
				s = mEtPasswd.getHint().toString();
			}
		} else if (mBtLogin.isFocused()) {
			s = mBtLogin.getText().toString();
		} else if (mBtLogout.isFocused()) {
			s = mBtLogout.getText().toString();
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

}
