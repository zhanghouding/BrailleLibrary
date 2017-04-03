package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sunteam.common.menu.BaseActivity;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.CommonUtils;
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.Tools;
import com.sunteam.common.utils.dialog.ConfirmListener;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
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

	@Override
	public void onBackPressed() {
		returnConfirm();
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
		mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel()); // 设置title字号
		mTvTitle.setHeight(mTools.convertSpToPixel(mTools.getFontSize()));
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
		mEtPasswd.setHintTextColor(fontColor);
		mEtPasswd.setTextColor(fontColor);

		// 确认密码
		mEtPasswdConfirmHint = (TextView) findViewById(R.id.library_account_passwd_setting_passwd_confirm_hint);
		mEtPasswdConfirmHint.setTextColor(fontColor);
		mEtPasswdConfirm = (EditText) findViewById(R.id.library_account_passwd_setting_passwd_confirm_input);
		mEtPasswdConfirm.setHintTextColor(fontColor);
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
		// mEtPasswd.setText("123");
		// mEtPasswdConfirm.setText("123");

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
//		PublicUtils.showToast(this, mBtCancel.getText().toString(), true);
		// 在按返回时，要确认是否退出
		returnConfirm();
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
				((EditText)v).setSelection(s.length());
				s = mEtPasswdHint.getText().toString() + "," + s;
			}
			break;
		case R.id.library_account_passwd_setting_passwd_confirm_input:
			s = ((EditText)v).getText().toString();
			if(s.isEmpty()){
				s = ((EditText)v).getHint().toString();
			} else {
				((EditText)v).setSelection(s.length());
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
			int newLen = s.length() - 1; 
			et.setText(s.substring(0, newLen));
			et.setSelection(newLen);
			s = getResources().getString(R.string.common_delete) + ", " + s.substring(newLen);
			if (0 == newLen) { // 朗读提示信息
				s = s + ", " + et.getHint().toString();
			} else { // 朗读剩余字符串
				s = s + ", " + et.getText().toString();
			}
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
//			delTailCh((EditText) v);
			CommonUtils.sendKeyEvent(KeyEvent.KEYCODE_DEL);
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
		View v1;
		if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode) {
			// 截获左方向键, 定位到上一个控件；不要用ACTION_UP，因为系统用ACTION_DOWN切换焦点，如果从其它控件切换过来，此时会收到抬起事件，焦点就切走了
			if (KeyEvent.ACTION_DOWN == event.getAction()) {
				v1 = v.focusSearch(View.FOCUS_UP);
				if (null != v1) {
					v1.requestFocus();
				}
			}
			return true;
		}

		if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode || KeyEvent.KEYCODE_DPAD_CENTER == keyCode || KeyEvent.KEYCODE_ENTER == keyCode) {
			// 截获右方向键, 定位到下一个控件；不要用ACTION_UP，因为系统用ACTION_DOWN切换焦点，如果从其它控件切换过来，此时会收到抬起事件，焦点就切走了
			if (KeyEvent.ACTION_DOWN == event.getAction()) {
				v1 = v.focusSearch(View.FOCUS_DOWN);
				if (null != v1) {
					v1.requestFocus();
				}
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
		// 朗读新增数据；暂不朗读，因为afterTextChanged()中朗读完整字符串
		if (count <= 0) {
			return;
		}
		String s1 = s.toString().substring(start, start + count);
		TtsUtils.getInstance().speak(s1);
	}

	@Override
	public void afterTextChanged(Editable s) {
		// 朗读完整字符串
		String s1 = s.toString();
		if (null == s1 || s1.isEmpty()) {
			s1 = getFocusHint();
		}
		TtsUtils.getInstance().speak(s1, TtsUtils.TTS_QUEUE_ADD);
	}

	private boolean checkInfoValid() {
		boolean ret = false;
		String passwd = mEtPasswd.getText().toString();
		String passwd2 = mEtPasswdConfirm.getText().toString();
		int id = 0;
		EditText mEditText = null; // 用于设置焦点
		if (passwd.isEmpty()) {
			id = R.string.library_account_passwd_empty;
			mEditText = mEtPasswd;
		} else if (!passwd.equals(passwd2)) {
			id = R.string.library_account_passwd_nosame;
			mEditText = mEtPasswdConfirm;
		} else {
			ret = true;
		}

		if (0 != id) {
			final EditText curEditText = mEditText;
			PublicUtils.showToast(this, getResources().getString(id), new PromptListener() {
				
				@Override
				public void onComplete() {
					if (null != curEditText) {
						curEditText.requestFocus();
					}
				}
			});
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
	
	// 退出时要用户确认
	private void returnConfirm() {
		String title = getResources().getString(R.string.common_dialog_confirm_return_title);
		ConfirmDialog mConfirmDialog = new ConfirmDialog(this, title);
		mConfirmDialog.setConfirmListener(new ConfirmListener() {
			
			@Override
			public void doConfirm() {
				finish();
			}
			
			@Override
			public void doCancel() {
				
			}
		});
		mConfirmDialog.show();
	}

}
