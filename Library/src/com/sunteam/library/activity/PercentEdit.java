package com.sunteam.library.activity;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.sunteam.common.menu.BaseActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.Tools;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

public class PercentEdit extends BaseActivity {
	private String mTitle; // 菜单标题
	private TextView mTvTitle;
	private View mLine = null;
	private TextView mTvNumber;
	private String mPercentStr; // 百分比字符串; 把字符串作为编辑对象
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
		if (0.0f == mPercentFloat) {
			mPercentStr = "0";
		} else {
			DecimalFormat decimalFormat = new DecimalFormat("0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
			mPercentStr = decimalFormat.format(mPercentFloat);
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
		mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel()); // 设置title字号
		mTvTitle.setTextColor(mTools.getFontColor()); // 设置title的文字颜色

		mLine = (View) findViewById(R.id.common_number_edit_line);
		mLine.setBackgroundColor(mTools.getFontColor()); // 设置分割线的背景色

		mTvNumber = (TextView) findViewById(R.id.common_number_edit_digit);
		mTvNumber.setText(mPercentStr);
		mTvNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel());
		mTvNumber.setTextColor(mTools.getFontColor()); // 设置文字颜色
		TtsUtils.getInstance().speak(mTitle + "," + mPercentStr);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean ret = true;
		switch (keyCode) {
		case KeyEvent.KEYCODE_STAR: // 屏蔽星号键功能
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
		case KeyEvent.KEYCODE_DPAD_UP: // 数字减1
			isNumericKey = false;
			incDecNumber(false);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN: // 数字增1
			isNumericKey = false;
			incDecNumber(true);
			break;
		case KeyEvent.KEYCODE_STAR: // 星号键也实现删除功能
			readingPromptInfo(R.string.library_input_percent_error);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT: // 删除尾部数字
			isNumericKey = false;
			deleteTailCh();
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT: // 尾部添0
			isNumericKey = false;
			appendZero();
			break;
		case KeyEvent.KEYCODE_POUND: // '#'输入小数点
			inputDot();
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
			ret = false;
			break;
		}
		if (!ret) {
			ret = super.onKeyUp(keyCode, event);
		}
		return ret;
	}

	// 设置当前输入的百分比
	private void setPercent() {
		mTvNumber.setText(mPercentStr);
		TtsUtils.getInstance().speakSymbol(mPercentStr, TtsUtils.TTS_QUEUE_FLUSH);
	}

	// 数字尾部增减1；state true 增1；false 减1
	private void incDecNumber(boolean state) {
		String format = "###.00"; // 使用"#"号会删除前导0和尾部多余的0;使用"0",则前后补0
		int index = mPercentStr.indexOf('.'); // 小数点位置
		float step = 0.0f;
		if (-1 == index) { // 没有小数点
			step = 1;
			format = "###";
		} else if (mPercentStr.length() - index >= 3) { // 说明有两位小数位
			step = 0.01f;
			format = "##0.00";
		} else { // 没有小数位或只有一位小数位
			step = 0.1f;
			format = "##0.0";
		}
		mPercentFloat = str2FloatNumber(mPercentStr);
		if (!state) {
			step = -step;
		}
		mPercentFloat += step;

		if (mPercentFloat > 100) {
			mPercentFloat = 0;
			format = "###";
		} else if (mPercentFloat < 0) {
			mPercentFloat = 100;
			format = "###";
		}

		DecimalFormat df = new DecimalFormat(format);
		mPercentStr = df.format(mPercentFloat);

		setPercent();
	}

	// 删除尾部字符，已经删空后提示输入数字
	private void deleteTailCh() {
		if (0 != mPercentStr.length()) {
			mPercentStr = mPercentStr.substring(0, mPercentStr.length() - 1);
			setPercent();
		}
		if (0 == mPercentStr.length()) {
			readingPromptInfo(R.string.library_input_percent_error);
		}
	}

	// 数字尾部添0;需要判断超出有效范围
	private void appendZero() {
		if (0 == mPercentStr.length()) {
			readingPromptInfo(R.string.library_input_percent_error);
		} else {
			if (mPercentStr.equals("0")) {
				mPercentStr = mPercentStr.concat(".");
			}
			mPercentStr = mPercentStr.concat("0");
			
			checkValidValue();
		}
	}

	// 数字尾部添加小数点：如果已经有小数点，则提示错误; 通过方向键输入时也允许输入小数点!
	private void inputDot() {
		mPercentFloat = str2FloatNumber(mPercentStr);
		int index = mPercentStr.indexOf('.'); // 小数点位置
		if (-1 != index) {
			readingPromptInfo(R.string.library_input_percent_error);
			return;
		}
		if (mPercentStr.equals("100") || mPercentStr.isEmpty()) {
			// 认为是数字输入
			isNumericKey = true;
			if (isNumericKey) {
				mPercentStr = "0.";
			}
		} else {
			mPercentStr = mPercentStr + ".";
		}

		setPercent();
	}

	// 通过输入数字键编辑百分比，digit 是当前输入的数字键对应数字
	private void changeNumber(int digit) {
		if (!isNumericKey) {
			isNumericKey = true;
			mPercentFloat = digit;
			mPercentStr = "" + digit;
			setPercent();
		} else {
			mPercentStr = mPercentStr + digit;
			checkValidValue();
		}
	}

	// 判断是否超出有效范围
	private void checkValidValue() {
		// 前导0只保留一个
		mPercentStr = mPercentStr.replaceAll("^0+", "0");

		// 小数点后面只保留两位有效位；
		int dotIndex = mPercentStr.indexOf('.'); // 小数点位置,没有小数点时为-1
		if (-1 == dotIndex) {
			dotIndex = mPercentStr.length();
		}

		// 1. 总长度最大为5："xx.xx";2.整数长度最大为3:"100";3.小数有效位最大为2;4.整数长度为3而字符串大于"100"
		if (mPercentStr.length() > 5 || dotIndex > 3 || mPercentStr.length() - dotIndex - 1 > 2 || (dotIndex == 3 && mPercentStr.compareTo("100") > 0)) {
			readingOutOfRange(R.string.library_input_percent_max, "100");
			isNumericKey = false; // 超出最大值后，再次按数字要清空
		} else{
			setPercent();
		}
	}

	// 跳转到指定百分比
	private void jumpToPercent() {
		mPercentFloat = str2FloatNumber(mPercentStr);
		if (mPercentFloat <= 0 || mPercentFloat > 100) {
			PublicUtils.showToast(this, getResources().getString(R.string.library_input_percent_num));
		} else {
			Intent intent = new Intent();
			intent.putExtra("percent", mPercentFloat);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}

	// 有可能为空字符串
	private float str2FloatNumber(String percent) {
		float number = 0;
		try {
			number = Float.parseFloat(percent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return number;
	}

	// 提示超出范围
	private void readingOutOfRange(int id, String value) {
		mPercentStr = value;
		mTvNumber.setText(mPercentStr);
		readingPromptInfo(id);
	}

	// 朗读提示信息
	private void readingPromptInfo(int id) {
		PublicUtils.showToast(this, getResources().getString(id), new PromptListener() {

			@Override
			public void onComplete() {
				TtsUtils.getInstance().speakSymbol(mPercentStr, TtsUtils.TTS_QUEUE_FLUSH);
			}
		});
	}

}
