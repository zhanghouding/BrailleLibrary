package com.sunteam.library.activity;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sunteam.common.menu.BaseActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.CommonUtils;
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.Tools;
import com.sunteam.common.utils.dialog.ConfirmListener;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.LoginAsyncTask;
import com.sunteam.library.utils.PublicUtils;
import com.sunteam.library.utils.TTSUtils;

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

	@Override
	public void onBackPressed() {
		returnConfirm();
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
		mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel()); // 设置title字号
		mTvTitle.setHeight(mTools.convertSpToPixel(mTools.getFontSize()));
		mTvTitle.setTextColor(fontColor); // 设置title的文字颜色

		mLine = (View) findViewById(R.id.library_account_login_line);
		mLine.setBackgroundColor(fontColor); // 设置分割线的背景色

		// 用户名
		mTvUserNameHint = (TextView) findViewById(R.id.library_account_login_username_hint);
		mTvUserNameHint.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel());
		mTvUserNameHint.setTextColor(fontColor);
		mEtUserName = (EditText) findViewById(R.id.library_account_login_username_input);
		mEtUserName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel());
		mEtUserName.setHintTextColor(fontColor);
		mEtUserName.setTextColor(fontColor);

		// 密码
		mTvPasswdHint = (TextView) findViewById(R.id.library_account_login_passwd_hint);
		mTvPasswdHint.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel());
		mTvPasswdHint.setTextColor(fontColor);
		mEtPasswd = (EditText) findViewById(R.id.library_account_login_passwd_input);
		mEtPasswd.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel());
		mEtPasswd.setHintTextColor(fontColor);
		mEtPasswd.setTextColor(fontColor);

		// Button
		mBtConfirm = (Button) findViewById(R.id.library_account_login_confirm);
		mBtConfirm.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel());
		mBtConfirm.setTextColor(fontColor);
		mBtConfirm.setBackgroundColor(mTools.getBackgroundColor());
		mBtCancel = (Button) findViewById(R.id.library_account_login_cancel);
		mBtCancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel());
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

		// TODO 设置测试账号
		// 设置测试账号
		// mEtUserName.setText("test1");
		// mEtPasswd.setText("123");
		
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
		case KeyEvent.KEYCODE_MENU: // 无效键要提示
			invalidKey();
			break;
//		case KeyEvent.KEYCODE_0:
//		case KeyEvent.KEYCODE_1:
//		case KeyEvent.KEYCODE_2:
//		case KeyEvent.KEYCODE_3:
//		case KeyEvent.KEYCODE_4:
//		case KeyEvent.KEYCODE_5:
//		case KeyEvent.KEYCODE_6:
//		case KeyEvent.KEYCODE_7:
//		case KeyEvent.KEYCODE_8:
//		case KeyEvent.KEYCODE_9:
//		case KeyEvent.KEYCODE_STAR:
//		case KeyEvent.KEYCODE_POUND:
//			processInvalid();
//			break;
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
		if (checkInfoValid()) {
			new LoginAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, account, passwd);
		}
	}

	public void onClickForCancel(View v) {
//		PublicUtils.showToast(this, mBtCancel.getText().toString(), true);
		// 在按返回时，要确认是否退出
		returnConfirm();
	}

	private String getFocusString() {
		String s = "";
		if (mEtUserName.isFocused()) {
			s = mEtUserName.getText().toString();
			if (s.isEmpty()) {
				s = mEtUserName.getHint().toString();
			} else {
				mEtUserName.setSelection(s.length());
				s = mTvUserNameHint.getText().toString() + "," + s;
			}
		} else if (mEtPasswd.isFocused()) {
			s = mEtPasswd.getText().toString();
			if (s.isEmpty()) {
				s = mEtPasswd.getHint().toString();
			} else {
				mEtPasswd.setSelection(s.length());
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
	@SuppressWarnings("unused")
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
		TtsUtils.getInstance().speak(s);
	}

	// 处理【退出】键: 焦点在编辑控件上则删除尾部字符；否则退出当前界面
	private boolean processKeyBack() {
		boolean ret = true;
		EditText mEditText = null;

		if (mEtUserName.isFocused()) {
			mEditText = mEtUserName;
		} else if (mEtPasswd.isFocused()) {
			mEditText = mEtPasswd;
		}

		if (null == mEditText) {
			ret = false;
		} else {
			if (mEditText.getText().toString().isEmpty()) {
				// 已经为空时再按【返回】键，退出当前界面
				// returnConfirm();
				invalidKey(mEditText);
			} else {
//				delTailCh(mEditText);
				CommonUtils.sendKeyEvent(KeyEvent.KEYCODE_DEL);
			}
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
				toggleInputmethodWindow(this);
			} else{
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
			TtsUtils.getInstance().speak(s1);
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

	// 显示、隐藏切换
	private void toggleInputmethodWindow(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			// 显示、隐藏切换
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
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

	private boolean checkInfoValid() {
		boolean ret = false;
		String account = mEtUserName.getText().toString();
		String passwd = mEtPasswd.getText().toString();
		EditText mEditText = null; // 用于设置焦点
		int id = 0;
		if (account.isEmpty()) {
			id = R.string.library_account_username_empty;
			mEditText = mEtUserName;
		} else if (passwd.isEmpty()) {
			id = R.string.library_account_passwd_empty;
			mEditText = mEtPasswd;
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

	// 非法键提示信息:请输入页码，同时，播报当前页码。
	private void invalidKey(EditText mEditText) {
		String s;
		final String etStr;
		if (null != mEditText) {
			s = mEditText.getHint().toString();
			etStr = mEditText.getText().toString();
		} else {
			s = getResources().getString(R.string.library_account_invalidkey_hint);
			etStr = "";
		}
		PublicUtils.showToast(this, s, new PromptListener() {

			@Override
			public void onComplete() {
				TTSUtils.getInstance().speakMenu(etStr);
			}
		});
	}

	// 非法键提示信息:请输入页码，同时，播报当前页码。
	private void invalidKey() {
		EditText mEditText = null;
		if (mEtUserName.isFocused()) {
			mEditText = mEtUserName;
		} else if (mEtPasswd.isFocused()) {
			mEditText = mEtPasswd;
		} else {
			return;
		}
		invalidKey(mEditText);
	}

	// 判断是否为无效键，若是则提示“请输入”
//	private void processInvalid() {
//		if (mEtUserName.isFocused() || mEtPasswd.isFocused()) {
//			return;
//		}
//		final String s = getResources().getString(R.string.library_account_invalidkey_hint);
//		PublicUtils.showToast(this, s);
//	}

}
