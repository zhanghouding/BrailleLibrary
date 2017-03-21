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
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.CommonUtils;
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.Tools;
import com.sunteam.common.utils.dialog.ConfirmListener;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.RegisterAsyncTask;
import com.sunteam.library.utils.PublicUtils;

public class AccountRegister extends BaseActivity implements OnFocusChangeListener, View.OnKeyListener, TextWatcher {
	private String mTitle; // 菜单标题
	private TextView mTvTitle;
	private View mLine = null;
	private TextView mTvCertificateNoHint;
	private EditText mEtCertificateNo; // 证件号
	private TextView mTvNameHint;
	private EditText mEtName; // 姓名
	private TextView mTvUserNameHint;
	private EditText mEtUserName; // 用户名
	private TextView mTvPasswdHint;
	private EditText mEtPasswd; // 密码
	private TextView mTvPasswdConfirmHint;
	private EditText mEtPasswdConfirm; // 确认密码
	// private Button mBtDetail; // 个人详细信息
	// private EditText mEtPhone; // 联系电话
	// private EditText mEtEmail; // E-mail
	// private EditText mEtCompany; // 单位名称
	// private EditText mEtZip; // 邮政编码
	// private EditText mEtAddr; // 联系地址
	private Button mBtConfirm; // 登录按钮
	private Button mBtCancel; // 取消按钮

	private int fontColor, backgroundColor, hightColor;
	private int certificateType = 0; // 0默认为读者证件号;1二代残疾人证件号
	// private boolean detailExpansion = false;

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

	// 在页码输入中，所有文字字号统一用大字号: 40sp, 已经在布局文件中初始化，不必在此与功能设置中的字号设置挂钩
	private void initView() {
		Tools mTools = new Tools(AccountRegister.this);
		fontColor = mTools.getFontColor();
		backgroundColor = mTools.getBackgroundColor();
		hightColor = mTools.getHighlightColor();
		this.getWindow().setBackgroundDrawable(new ColorDrawable(mTools.getBackgroundColor()));
		setContentView(R.layout.library_account_register);

		mTvTitle = (TextView) findViewById(R.id.library_account_register_title);
		mTvTitle.setText(mTitle);
		mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel()); // 设置title字号
		mTvTitle.setHeight(mTools.convertSpToPixel(mTools.getFontSize()));
		mTvTitle.setTextColor(fontColor); // 设置title的文字颜色

		mLine = (View) findViewById(R.id.library_account_register_line);
		mLine.setBackgroundColor(fontColor); // 设置分割线的背景色

		// 证件号
		mTvCertificateNoHint = (TextView) findViewById(R.id.library_account_register_certificate_no_hint);
		mTvCertificateNoHint.setTextColor(fontColor);
		mEtCertificateNo = (EditText) findViewById(R.id.library_account_register_certificate_no_input);
		mEtCertificateNo.setTextColor(fontColor);

		// 姓名
		mTvNameHint = (TextView) findViewById(R.id.library_account_register_name_hint);
		mTvNameHint.setTextColor(fontColor);
		mEtName = (EditText) findViewById(R.id.library_account_register_name_input);
		mEtName.setTextColor(fontColor);

		// 用户名
		mTvUserNameHint = (TextView) findViewById(R.id.library_account_register_username_hint);
		mTvUserNameHint.setTextColor(fontColor);
		mEtUserName = (EditText) findViewById(R.id.library_account_register_username_input);
		mEtUserName.setTextColor(fontColor);

		// 密码
		mTvPasswdHint = (TextView) findViewById(R.id.library_account_register_passwd_hint);
		mTvPasswdHint.setTextColor(fontColor);
		mEtPasswd = (EditText) findViewById(R.id.library_account_register_passwd_input);
		mEtPasswd.setTextColor(fontColor);

		// 确认密码
		mTvPasswdConfirmHint = (TextView) findViewById(R.id.library_account_register_passwd_confirm_hint);
		mTvPasswdConfirmHint.setTextColor(fontColor);
		mEtPasswdConfirm = (EditText) findViewById(R.id.library_account_register_passwd_confirm_input);
		mEtPasswdConfirm.setTextColor(fontColor);

		// // 个人详细信息
		// mBtDetail = (Button)
		// findViewById(R.id.library_account_register_person_detail);
		// mBtDetail.setTextColor(fontColor);
		//
		// // 联系电话
		// mTvHint = (TextView)
		// findViewById(R.id.library_account_register_phone_hint);
		// mTvHint.setTextColor(fontColor);
		// mEtPhone = (EditText)
		// findViewById(R.id.library_account_register_phone_input);
		// mEtPhone.setTextColor(fontColor);
		//
		// // E-mail
		// mTvHint = (TextView)
		// findViewById(R.id.library_account_register_email_hint);
		// mTvHint.setTextColor(fontColor);
		// mEtEmail = (EditText)
		// findViewById(R.id.library_account_register_email_input);
		// mEtEmail.setTextColor(fontColor);
		//
		// // 单位名称
		// mTvHint = (TextView)
		// findViewById(R.id.library_account_register_company_hint);
		// mTvHint.setTextColor(fontColor);
		// mEtCompany = (EditText)
		// findViewById(R.id.library_account_register_company_input);
		// mEtCompany.setTextColor(fontColor);
		//
		// // 邮政编码
		// mTvHint = (TextView)
		// findViewById(R.id.library_account_register_zip_hint);
		// mTvHint.setTextColor(fontColor);
		// mEtZip = (EditText)
		// findViewById(R.id.library_account_register_zip_input);
		// mEtZip.setTextColor(fontColor);
		//
		// // 联系地址
		// mTvHint = (TextView)
		// findViewById(R.id.library_account_register_addr_hint);
		// mTvHint.setTextColor(fontColor);
		// mEtAddr = (EditText)
		// findViewById(R.id.library_account_register_addr_input);
		// mEtAddr.setTextColor(fontColor);

		// Button
		mBtConfirm = (Button) findViewById(R.id.library_account_register_confirm);
		mBtConfirm.setTextColor(fontColor);
		mBtConfirm.setBackgroundColor(mTools.getBackgroundColor());
		mBtCancel = (Button) findViewById(R.id.library_account_register_cancel);
		mBtCancel.setTextColor(fontColor);
		mBtCancel.setBackgroundColor(mTools.getBackgroundColor());

		// 设置编辑框按键监听
		mEtCertificateNo.setOnKeyListener(this);
		mEtName.setOnKeyListener(this);
		mEtUserName.setOnKeyListener(this);
		mEtPasswd.setOnKeyListener(this);
		mEtPasswdConfirm.setOnKeyListener(this);

		// 添加编辑框文本变化监听
		mEtCertificateNo.addTextChangedListener(this);
		mEtName.addTextChangedListener(this);
		mEtUserName.addTextChangedListener(this);
		mEtPasswd.addTextChangedListener(this);
		mEtPasswdConfirm.addTextChangedListener(this);

		// 设置焦点监听
		mEtCertificateNo.setOnFocusChangeListener(this);
		mEtName.setOnFocusChangeListener(this);
		mEtUserName.setOnFocusChangeListener(this);
		mEtPasswd.setOnFocusChangeListener(this);
		mEtPasswdConfirm.setOnFocusChangeListener(this);
		mBtConfirm.setOnFocusChangeListener(this);
		mBtCancel.setOnFocusChangeListener(this);

		mEtCertificateNo.requestFocus();

		speak(mTitle + "," + getFocusString());
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

	// 展开或收起
	// public void onClickForDetail(View v) {
	// detailExpansion = !detailExpansion;
	// setDetailInfoState();
	// }

	// 注册
	public void onClickForConfirm(View v) {
		String account = mEtUserName.getText().toString();
		String name = mEtName.getText().toString();
		String cardNo = mEtCertificateNo.getText().toString();
		String passwd = mEtPasswd.getText().toString();
		if (checkInfoValid()) {
			TtsUtils.getInstance().speak(((Button) v).getText().toString());
			new RegisterAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "" + certificateType, account, name, cardNo, passwd);
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
		String s = "";

		int id = v.getId();
		switch (id) {
		case R.id.library_account_register_certificate_no_input:
			s = ((EditText) v).getText().toString();
			if (s.isEmpty()) {
				s = ((EditText) v).getHint().toString();
			} else {
				((EditText)v).setSelection(s.length());
				s = mTvCertificateNoHint.getText().toString() + "," + s;
			}
			break;
		case R.id.library_account_register_name_input:
			s = ((EditText) v).getText().toString();
			if (s.isEmpty()) {
				s = ((EditText) v).getHint().toString();
			} else {
				((EditText)v).setSelection(s.length());
				s = mTvNameHint.getText().toString() + "," + s;
			}
			break;
		case R.id.library_account_register_username_input:
			s = ((EditText) v).getText().toString();
			if (s.isEmpty()) {
				s = ((EditText) v).getHint().toString();
			} else {
				((EditText)v).setSelection(s.length());
				s = mTvUserNameHint.getText().toString() + "," + s;
			}
			break;
		case R.id.library_account_register_passwd_input:
			s = ((EditText) v).getText().toString();
			if (s.isEmpty()) {
				s = ((EditText) v).getHint().toString();
			} else {
				((EditText)v).setSelection(s.length());
				s = mTvPasswdHint.getText().toString() + "," + s;
			}
			break;
		case R.id.library_account_register_passwd_confirm_input:
			s = ((EditText) v).getText().toString();
			if (s.isEmpty()) {
				s = ((EditText) v).getHint().toString();
			} else {
				((EditText)v).setSelection(s.length());
				s = mTvPasswdHint.getText().toString() + "," + s;
			}
			break;
			// case R.id.library_account_register_phone_input:
			// case R.id.library_account_register_email_input:
			// case R.id.library_account_register_company_input:
			// case R.id.library_account_register_zip_input:
			// case R.id.library_account_register_addr_input:
		// case R.id.library_account_register_person_detail:
		case R.id.library_account_register_confirm:
		case R.id.library_account_register_cancel:
			s = ((Button) v).getText().toString();
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
		String s = "";

		switch (v.getId()) {
		case R.id.library_account_register_certificate_no_input:
		case R.id.library_account_register_name_input:
		case R.id.library_account_register_username_input:
		case R.id.library_account_register_passwd_input:
		case R.id.library_account_register_passwd_confirm_input:
			// case R.id.library_account_register_phone_input:
			// case R.id.library_account_register_email_input:
			// case R.id.library_account_register_company_input:
			// case R.id.library_account_register_zip_input:
			// case R.id.library_account_register_addr_input:
			s = ((EditText) v).getHint().toString();
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

		switch (v.getId()) {
		case R.id.library_account_register_certificate_no_input:
		case R.id.library_account_register_name_input:
		case R.id.library_account_register_username_input:
		case R.id.library_account_register_passwd_input:
		case R.id.library_account_register_passwd_confirm_input:
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
		if (v.getId() == mBtConfirm.getId() || v.getId() == mBtCancel.getId()) {
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

	// 根据展开和收起状态，设置详细信息控件可见还是隐藏
	// private void setDetailInfoState() {
	// int id = R.string.library_account_personal_detail_expansion;
	// // 修改mTvDetail上的提示文字；展开时，设置详细信息对应的控件为可见; 收起时，设置详细信息对应的控件为隐藏
	// if (detailExpansion) {
	// id = R.string.library_account_personal_detail_contraction;
	// mEtPhone.setVisibility(View.VISIBLE);
	// mEtEmail.setVisibility(View.VISIBLE);
	// mEtCompany.setVisibility(View.VISIBLE);
	// mEtZip.setVisibility(View.VISIBLE);
	// mEtAddr.setVisibility(View.VISIBLE);
	// } else {
	// mEtPhone.setVisibility(View.GONE);
	// mEtEmail.setVisibility(View.GONE);
	// mEtCompany.setVisibility(View.GONE);
	// mEtZip.setVisibility(View.GONE);
	// mEtAddr.setVisibility(View.GONE);
	// }
	// mBtDetail.setText(getResources().getString(id));
	// }

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
		}
		speak(s1, TtsUtils.TTS_QUEUE_ADD);
	}

	private boolean checkInfoValid() {
		boolean ret = false;
		String cardNo = mEtCertificateNo.getText().toString();
		String name = mEtName.getText().toString();
		String account = mEtUserName.getText().toString();
		String passwd = mEtPasswd.getText().toString();
		String passwd2 = mEtPasswdConfirm.getText().toString();
		EditText mEditText = null; // 用于设置焦点
		int id = 0;
		if (cardNo.isEmpty()) {
			id = R.string.library_account_certificateno_empty;
			mEditText = mEtCertificateNo;
		} else if (name.isEmpty()) {
			id = R.string.library_account_name_empty;
			mEditText = mEtName;
		} else if (account.isEmpty()) {
			id = R.string.library_account_username_empty;
			mEditText = mEtUserName;
		} else if (passwd.isEmpty()) {
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
