package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.sunteam.library.asynctask.RegisterAsyncTask;
import com.sunteam.library.asynctask.UserUpdatePasswordAsyncTask;
import com.sunteam.library.utils.PublicUtils;

public class AccountPasswdSetting extends BaseActivity implements OnFocusChangeListener, View.OnKeyListener, TextWatcher {
	private String mTitle; // 菜单标题
	private TextView mTvTitle;
	private View mLine = null;
	private TextView mTvUserNameHint; // 用户名
	private TextView mTvUserName; // 用户名
	private TextView mEtPasswdHint; // 密码
	private EditText mEtPasswd; // 密码
	private TextView mEtPasswdConfirmHint; // 确认密码
	private EditText mEtPasswdConfirm; // 确认密码
	private Button mBtConfirm; // 确定按钮
	private Button mBtCancel; // 退出按钮

	private int fontColor, backgroundColor, hightColor;
	private String userName = ""; // 用户名
	private String realName = ""; // 真实姓名
	private String certificateType; // 证件类型
	private String cardNo = ""; // 证件号

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getIntentPara();
		initView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode) {
			return;
		}
		switch (requestCode) {
		case 0: // 
			break;
		case 1: //
			break;
		default:
			break;
		}
	}

	private void getIntentPara() {
		Intent intent = getIntent();
		userName = intent.getStringExtra("user_name");
		realName = intent.getStringExtra("real_name");
		certificateType = intent.getStringExtra("type");
		cardNo = intent.getStringExtra("card_no");
	}

	private void initView() {
		Tools mTools = new Tools(AccountPasswdSetting.this);
		fontColor = mTools.getFontColor();
		backgroundColor = mTools.getBackgroundColor();
		hightColor = mTools.getHighlightColor();
		this.getWindow().setBackgroundDrawable(new ColorDrawable(mTools.getBackgroundColor()));
		setContentView(R.layout.library_account_passwd_setting);

		mTvTitle = (TextView) findViewById(R.id.library_account_passwd_setting_title);
		mTitle = getResources().getString(R.string.library_account_passwd_setting);
		mTvTitle.setText(mTitle);
		mTvTitle.setTextColor(fontColor); // 设置title的文字颜色

		mLine = (View) findViewById(R.id.library_account_passwd_setting_line);
		mLine.setBackgroundColor(fontColor); // 设置分割线的背景色

		// 用户名
		mTvUserNameHint = (TextView) findViewById(R.id.library_account_passwd_setting_username_hint);
		mTvUserNameHint.setTextColor(fontColor);
		mTvUserName = (TextView) findViewById(R.id.library_account_passwd_setting_username_input);
		mTvUserName.setTextColor(fontColor);
		mTvUserName.setText(userName);

		// 密码
		mEtPasswdHint = (TextView) findViewById(R.id.library_account_passwd_setting_passwd_hint);
		mEtPasswdHint.setTextColor(fontColor);
		mEtPasswd = (EditText) findViewById(R.id.library_account_passwd_setting_passwd_input);
		mEtPasswd.setTextColor(fontColor);

		// 确认密码
		mEtPasswdConfirmHint = (TextView) findViewById(R.id.library_account_passwd_setting_passwd_confirm_hint);
		mEtPasswdConfirmHint.setTextColor(fontColor);
		mEtPasswdConfirm = (EditText) findViewById(R.id.library_account_passwd_setting_passwd_confirm_input);
		mEtPasswdConfirm.setTextColor(fontColor);

		// Button
		mBtConfirm = (Button) findViewById(R.id.library_account_passwd_setting_confirm);
		mBtConfirm.setTextColor(fontColor);
		mBtConfirm.setBackgroundColor(mTools.getBackgroundColor());
		mBtCancel = (Button) findViewById(R.id.library_account_passwd_setting_cancel);
		mBtCancel.setTextColor(fontColor);
		mBtCancel.setBackgroundColor(mTools.getBackgroundColor());

		// 设置编辑框按键监听
		mEtPasswd.setOnKeyListener(this);
		mEtPasswdConfirm.setOnKeyListener(this);

		// 添加编辑框文本变化监听
		mEtPasswd.addTextChangedListener(this);
		mEtPasswdConfirm.addTextChangedListener(this);

		// 设置焦点监听
		mEtPasswd.setOnFocusChangeListener(this);
		mEtPasswdConfirm.setOnFocusChangeListener(this);
		mBtConfirm.setOnFocusChangeListener(this);
		mBtCancel.setOnFocusChangeListener(this);

		// TODO 设置测试账号
		mEtPasswd.setText("123");
		mEtPasswdConfirm.setText("123");

		mEtPasswd.requestFocus();

		speak(mTitle + "," + mTvUserNameHint.getText().toString() + userName + "," + getFocusString());
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

	// 确定
	public void onClickForConfirm(View v) {
		String userName = mTvUserName.getText().toString();
		String passwd = mEtPasswd.getText().toString();
		if (checkInfoValid()) {
			TtsUtils.getInstance().speak(((Button) v).getText().toString());
			new UserUpdatePasswordAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "" + certificateType, userName, realName, cardNo, passwd);
		}
	}

	// 退出
	public void onClickForCancel(View v) {
		PublicUtils.showToast(this, mBtCancel.getText().toString(), true);
	}

	// 获取焦点控件上的朗读字符串
	private String getFocusString() {
		View rootview = this.getWindow().getDecorView();
		View v = rootview.findFocus();
		String s= "";
		
		int id = v.getId();
		switch(id){
		case R.id.library_account_passwd_setting_passwd_input:
			s = ((EditText)v).getText().toString();
			if(s.isEmpty()){
				s = ((EditText)v).getHint().toString();
			} else {
				s = mEtPasswdHint.getText().toString() + "," + s;
			}
			break;
		case R.id.library_account_passwd_setting_passwd_confirm_input:
			s = ((EditText)v).getText().toString();
			if(s.isEmpty()){
				s = ((EditText)v).getHint().toString();
			} else {
				s = mEtPasswdConfirmHint.getText().toString() + "," + s;
			}
			break;
		case R.id.library_account_passwd_setting_confirm:
		case R.id.library_account_passwd_setting_cancel:
			s = ((Button)v).getText().toString();
			break;
		default:
			break;
		}				

		return s;
	}

	// 获取焦点控件上提示信息
	private String getFocusHint() {
		View rootview = this.getWindow().getDecorView();
		View v = rootview.findFocus();
		String s= "";
		
		switch(v.getId()){
		case R.id.library_account_passwd_setting_passwd_input:
		case R.id.library_account_passwd_setting_passwd_confirm_input:
			s = ((EditText)v).getHint().toString();
			break;
		default:
			break;
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
		speak(s);
	}

	// 处理【退出】键: 焦点在编辑控件上则删除尾部字符；否则退出当前界面
	private boolean processKeyBack() {
		boolean ret = true;
		View rootview = this.getWindow().getDecorView();
		View v = rootview.findFocus();
		
		switch(v.getId()){
		case R.id.library_account_passwd_setting_passwd_input:
		case R.id.library_account_passwd_setting_passwd_confirm_input:
			delTailCh((EditText) v);
			break;
		default:
			ret = false;
			break;
		}	

		return ret;
	}

	// 焦点变化: 朗读焦点编辑控件中编辑内容；若是Button，则需要设置Button背景色
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			String s = getFocusString();
			speak(s);
		}

		// Button需要设置背景和焦点色
		if (v.getId() == mTvUserName.getId() || v.getId() == mBtConfirm.getId() || v.getId() == mBtCancel.getId()) {
			if (hasFocus) {
				v.setBackgroundColor(hightColor);
			} else {
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
			speak(s1);
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (count <= 0) {
			return;
		}
		String s1 = s.toString().substring(start, start + count);

		speak(s1);
	}

	@Override
	public void afterTextChanged(Editable s) {
		String s1 = s.toString();
		if (null == s1 || 0 == s1.length()) {
			s1 = getFocusHint();
			speak(s1, TtsUtils.TTS_QUEUE_ADD);
		}
	}

	private boolean checkInfoValid() {
		boolean ret = false;
		String passwd = mEtPasswd.getText().toString();
		String passwd2 = mEtPasswdConfirm.getText().toString();
		int id = 0;
		if (passwd.isEmpty()) {
			id = R.string.library_account_passwd_empty;
		} else if (passwd2.isEmpty()) {
			id = R.string.library_account_passwd_empty;
		} else if (!passwd.equals(passwd2)) {
			id = R.string.library_account_passwd_nosame;
		} else {
			ret = true;
		}

		if (0 != id) {
			PublicUtils.showToast(this, getResources().getString(id));
		}

		return ret;
	}

	private void speak(String s) {
		speak(s, TtsUtils.TTS_QUEUE_FLUSH);
	}

	private void speak(String s, int type) {
		if (null != TtsUtils.getInstance()) {
			TtsUtils.getInstance().speak(s);
		}
	}

}
