package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.sunteam.common.utils.Tools;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.UserGetPasswordAsyncTask;
import com.sunteam.library.utils.PublicUtils;

public class AccountPasswdRecovery extends BaseActivity implements OnFocusChangeListener, View.OnKeyListener, TextWatcher {
	private String mTitle; // 菜单标题
	private int resquestCode; // 启动当前Activity时使用的resquestCode值
	private TextView mTvTitle;
	private View mLine = null;
	private TextView mTvCertificateNoHint; // 用户名
	private EditText mEtCertificateNo; // 用户名编辑控件
	private TextView mTvNameHint; // 姓名
	private EditText mEtName; // 姓名编辑控件
	private Button mBtConfirm; // 密码找回分两步: 先输入证件号和姓名，然后再设置新密码
	private Button mBtCancel; // 退出密码找回界面
	
	private int fontColor, backgroundColor, hightColor;
	private int certificateType = 1; // 1默认为读者证件号;2二代残疾人证件号

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
		case 0: // 证件类型选择
			certificateType = data.getIntExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, 0);
			updateCertificateHint(); // 刷新证件号输入框中的提示信息
			break;
		case 1: //
			break;
		default:
			break;
		}
	}

	private void getIntentPara() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		resquestCode = intent.getIntExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, 0);
	}

	// 在页码输入中，所有文字字号统一用大字号: 40sp, 已经在布局文件中初始化，不必在此与功能设置中的字号设置挂钩
	private void initView() {
		Tools mTools = new Tools(AccountPasswdRecovery.this);
		fontColor = mTools.getFontColor();
		backgroundColor = mTools.getBackgroundColor();
		hightColor = mTools.getHighlightColor();
		this.getWindow().setBackgroundDrawable(new ColorDrawable(mTools.getBackgroundColor()));
		setContentView(R.layout.library_account_passwd_recovery);

		mTvTitle = (TextView) findViewById(R.id.library_account_passwd_recovery_title);
		mTvTitle.setText(mTitle);
		mTvTitle.setTextColor(fontColor); // 设置title的文字颜色

		mLine = (View) findViewById(R.id.library_account_passwd_recovery_line);
		mLine.setBackgroundColor(fontColor); // 设置分割线的背景色

		// 证件号
		mTvCertificateNoHint = (TextView) findViewById(R.id.library_account_passwd_recovery_certificate_no_hint);
		mTvCertificateNoHint.setTextColor(fontColor);
		mEtCertificateNo = (EditText) findViewById(R.id.library_account_passwd_recovery_certificate_no_input);
		mEtCertificateNo.setTextColor(fontColor);

		// 姓名
		mTvNameHint = (TextView) findViewById(R.id.library_account_passwd_recovery_name_hint);
		mTvNameHint.setTextColor(fontColor);
		mEtName = (EditText) findViewById(R.id.library_account_passwd_recovery_name_input);
		mEtName.setTextColor(fontColor);

		// Button
		mBtConfirm = (Button) findViewById(R.id.library_account_passwd_recovery_confirm);
		mBtConfirm.setTextColor(fontColor);
		mBtConfirm.setBackgroundColor(mTools.getBackgroundColor());
		mBtCancel = (Button) findViewById(R.id.library_account_passwd_recovery_cancel);
		mBtCancel.setTextColor(fontColor);
		mBtCancel.setBackgroundColor(mTools.getBackgroundColor());

		// 设置编辑框按键监听
		mEtCertificateNo.setOnKeyListener(this);
		mEtName.setOnKeyListener(this);

		// 添加编辑框文本变化监听
		mEtCertificateNo.addTextChangedListener(this);
		mEtName.addTextChangedListener(this);

		// 设置焦点监听
		mEtCertificateNo.setOnFocusChangeListener(this);
		mEtName.setOnFocusChangeListener(this);
		mBtConfirm.setOnFocusChangeListener(this);
		mBtCancel.setOnFocusChangeListener(this);

		// TODO 设置测试账号
		certificateType = 2; // 二代残疾人证号
		mEtCertificateNo.setText("130182198609215753120");
		mEtName.setText("测试");
		
		mEtCertificateNo.requestFocus();
		
		TtsUtils.getInstance().speak(mTitle + "," + getFocusString());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean ret = true;
		switch (keyCode) {
		case KeyEvent.KEYCODE_STAR: // 屏蔽星号键功能
		case KeyEvent.KEYCODE_POUND: // 屏蔽井号键功能
			break;
		case KeyEvent.KEYCODE_MENU: // 菜单键选择证件类型
			selectCertificateType();
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

	// 下一步
	public void onClickForConfirm(View v) {
		String cardNo = mEtCertificateNo.getText().toString();
		String realName = mEtName.getText().toString();
		if (checkInfoValid()) {
			TtsUtils.getInstance().speak(((Button) v).getText().toString());
			new UserGetPasswordAsyncTask(this, resquestCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "" + certificateType, realName, cardNo);
		}
	}

	public void onClickForCancel(View v) {
		PublicUtils.showToast(this, mBtCancel.getText().toString(), true);
	}

	private String getFocusString() {
		String s = "";
		if (mEtCertificateNo.isFocused()) {
			s = mEtCertificateNo.getText().toString();
			if (s.isEmpty()) {
				s = mEtCertificateNo.getHint().toString();
			} else {
				s = mTvCertificateNoHint.getText().toString() + "," + s;
			}
		} else if (mEtName.isFocused()) {
			s = mEtName.getText().toString();
			if (s.isEmpty()) {
				s = mEtName.getHint().toString();
			} else {
				s = mTvNameHint.getText().toString() + "," + s;
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
		if (mEtCertificateNo.isFocused()) {
			s = mEtCertificateNo.getHint().toString();
		} else if (mEtName.isFocused()) {
			s = mEtName.getHint().toString();
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
		if (mEtCertificateNo.isFocused()) {
			delTailCh(mEtCertificateNo);
		} else if (mEtName.isFocused()) {
			delTailCh(mEtName);
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
		if(v.getId() == R.id.library_account_passwd_recovery_confirm || v.getId() == R.id.library_account_passwd_recovery_cancel){
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

	// 选择证件类型：启动一个菜单
	private void selectCertificateType() {
		String title = getResources().getString(R.string.library_account_certificate_type_select);
		String[] list = getResources().getStringArray(R.array.library_certificate_type_menu_list);
		Intent intent = new Intent();
		intent.setClass(this, AccountRegisterFunctionMenu.class);
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, list); // 菜单列表
		startActivityForResult(intent, 0);
	}

	// 刷新证件号输入框中的提示信息
	private void updateCertificateHint() {
		String s = getResources().getString(R.string.library_account_certificate_no_hint);
		if (1 == certificateType) {
			s = getResources().getString(R.string.library_account_disabled_id_number_hint);
		}
		mEtCertificateNo.setText("");
		mEtCertificateNo.setHint(s);
		speak(s);
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

	private boolean checkInfoValid() {
		boolean ret = false;
		String cardNo = mEtCertificateNo.getText().toString();
		String name = mEtName.getText().toString();
		int id = 0;
		if (cardNo.isEmpty()) {
			id = R.string.library_account_certificateno_empty;
		} else if (name.isEmpty()) {
			id = R.string.library_account_name_empty;
		} else {
			ret = true;
		}

		if (0 != id) {
			PublicUtils.showToast(this, getResources().getString(id));
		}

		return ret;
	}

	// 显示、隐藏切换
	private void toggleInputmethodWindow(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			// 显示、隐藏切换
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
		}
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
