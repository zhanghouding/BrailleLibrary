package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.sunteam.common.menu.BaseActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.utils.Tools;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;
import com.sunteam.library.utils.TTSUtils;

public class PercentEdit extends BaseActivity {
	private final float maxPercent = 99.99f; // 最大百分比s
	private final int numberWidth = 5; // 百分比数字位数
	private String mTitle; // 菜单标题
	private TextView mTvTitle;
	private View mLine = null;
	private TextView mTvNumber;
	private String mPercentStr; // 百分比字符串
	private float mPercentFloat = (float) 0.0; // 百分比数字
	private boolean isNumericKey = false; // 是否按了数字键，按上下左右键时为false, 按数字键时为true

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getIntentPara();
		initView();
	}

	private void getIntentPara() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mPercentFloat = intent.getFloatExtra(LibraryConstant.INTENT_KEY_PERCENT, mPercentFloat);
		if(0.0f == mPercentFloat){
			mPercentStr = "0";
		} else {
			mPercentStr = String.format("%##.##f", mPercentFloat);
		}

		if (null == mTitle) {
			finish();
			return;
		}
	}

	// 在页码输入中，所有文字字号统一用大字号: 40sp, 已经在布局文件中初始化，不必在此与功能设置中的字号设置挂钩
	private void initView() {
		Tools mTools = new Tools(PercentEdit.this);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(mTools.getBackgroundColor()));
		setContentView(R.layout.common_number_edit);

		mTvTitle = (TextView) findViewById(R.id.common_number_edit_title);
		mTvTitle.setText(mTitle);
		mTvTitle.setTextColor(mTools.getFontColor()); // 设置title的文字颜色

		mLine = (View) findViewById(R.id.common_number_edit_line);
		mLine.setBackgroundColor(mTools.getFontColor()); // 设置分割线的背景色

		mTvNumber = (TextView) findViewById(R.id.common_number_edit_digit);
		mTvNumber.setText(mPercentStr);
		mTvNumber.setTextColor(mTools.getFontColor()); // 设置文字颜色
		TTSUtils.getInstance().speakMenu(mTitle + "," + mPercentStr);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP: // 数字减1
			isNumericKey = false;
			decNumber();
			setPercent();
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN: // 数字增1
			isNumericKey = false;
			incNumber();
			setPercent();
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT: // 删除尾部数字
			isNumericKey = false;
			mPercentStr = mPercentStr.substring(0, mPercentStr.length() - 1);
			setPercent();
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT: // 尾部添0
			isNumericKey = false;
			tenTimesNumber();
			setPercent();
			break;
		case KeyEvent.KEYCODE_POUND: // '#'输入小数点
			inputDot();
			setPercent();
			break;
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_NUMPAD_0:
			changeNumber(0);
			break;
		case KeyEvent.KEYCODE_1:
		case KeyEvent.KEYCODE_NUMPAD_1:
			changeNumber(1);
			break;
		case KeyEvent.KEYCODE_2:
		case KeyEvent.KEYCODE_NUMPAD_2:
			changeNumber(2);
			break;
		case KeyEvent.KEYCODE_3:
		case KeyEvent.KEYCODE_NUMPAD_3:
			changeNumber(3);
			break;
		case KeyEvent.KEYCODE_4:
		case KeyEvent.KEYCODE_NUMPAD_4:
			changeNumber(4);
			break;
		case KeyEvent.KEYCODE_5:
		case KeyEvent.KEYCODE_NUMPAD_5:
			changeNumber(5);
			break;
		case KeyEvent.KEYCODE_6:
		case KeyEvent.KEYCODE_NUMPAD_6:
			changeNumber(6);
			break;
		case KeyEvent.KEYCODE_7:
		case KeyEvent.KEYCODE_NUMPAD_7:
			changeNumber(7);
			break;
		case KeyEvent.KEYCODE_8:
		case KeyEvent.KEYCODE_NUMPAD_8:
			changeNumber(8);
			break;
		case KeyEvent.KEYCODE_9:
		case KeyEvent.KEYCODE_NUMPAD_9:
			changeNumber(9);
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			jumpToPercent();
			break;
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;
		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	// 设置当前输入的百分比
	private void setPercent() {
		mTvNumber.setText(mPercentStr);
		TTSUtils.getInstance().speakMenu(mPercentStr);
	}
	
	// 数字尾部减1
	private void decNumber() {
		int index = mPercentStr.indexOf('.'); // 小数点位置
		float step = 0.0f;
		if(-1 == index){
			step = 1;
		} else if(1 == mPercentStr.length() - index){
			step= 0.1f;
		} else{
			step = 0.01f;
		}
		mPercentFloat = str2FloatNumber(mPercentStr);
		mPercentFloat -= step;
		if(mPercentFloat < 0){
			mPercentFloat = maxPercent;
		}
	}
	
	// 数字尾部减1
	private void incNumber() {
		int index = mPercentStr.indexOf('.'); // 小数点位置
		float step = 0.0f;
		if(-1 == index){
			step = 1;
		} else if(1 == mPercentStr.length() - index){
			step= 0.1f;
		} else{
			step = 0.01f;
		}
		mPercentFloat = str2FloatNumber(mPercentStr);
		mPercentFloat += step;
		if(mPercentFloat > maxPercent){
			mPercentFloat = 0;
		}
	}
	
	// 数字尾部添0
	private void tenTimesNumber() {
		mPercentStr = mPercentStr + "0";
		mPercentFloat = str2FloatNumber(mPercentStr);
		if(mPercentFloat > maxPercent){
			mPercentFloat = maxPercent;
			mPercentStr = "" + mPercentFloat;
		}
	}
	
	// 数字尾部添加小数点：如果已经有小数点，则提示错误
	private void inputDot() {
		int index = mPercentStr.indexOf('.'); // 小数点位置
		if(-1 != index){
			readingOutOfRange(R.string.library_input_percent_error);
			return;
		}
		mPercentStr = mPercentStr + ".0";
	}

	// 通过输入数字键编辑百分比，digit 是当前输入的数字键对应数字
	private void changeNumber(int digit) {
		if (!isNumericKey) {
			isNumericKey = true;
			mPercentFloat = digit;
			mPercentStr = "" + digit;
		} else {
			mPercentStr = mPercentStr + digit;
			mPercentFloat = str2FloatNumber(mPercentStr);
			if (mPercentFloat > maxPercent) {
				int index = mPercentStr.indexOf('.'); // 小数点位置
				if (-1 == index) {
					mPercentFloat = mPercentFloat / 10;
				} else if (mPercentStr.length() > numberWidth) {
					mPercentFloat = maxPercent;
					mPercentStr = "" + mPercentStr;
					setPercent();
					readingOutOfRange(R.string.library_input_percent_max);
					return;
				}
			}
		}
		setPercent();
	}

	// 跳转到指定百分比
	private void jumpToPercent() {
		mPercentFloat = str2FloatNumber(mPercentStr);
		if (mPercentFloat <= 0 || mPercentFloat > maxPercent) {
			PublicUtils.showToast(this, getResources().getString(R.string.library_input_percent_num));
		} else {
			Intent intent = new Intent();
			intent.putExtra("percent", mPercentFloat);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}

	private float str2FloatNumber(String percent) {
		float number = 0;
		try {
			number = Float.parseFloat(percent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return number;
	}
	
	private String floatNumber2Str(float number){
		String str;
		if(0.0f == number){
			str = "0";
		} else {
			str = String.format("%##.##f", number);
		}
		return str;
	}

	// 提示超出范围，
	private void readingOutOfRange(int id) {
		PublicUtils.showToast(this, getResources().getString(id), new PromptListener() {

			@Override
			public void onComplete() {
				TTSUtils.getInstance().speakMenu(mPercentStr);
			}
		});
	}

}
