package com.sunteam.library.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.sunteam.common.menu.BaseActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.utils.Tools;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.utils.PublicUtils;
import com.sunteam.library.utils.TTSUtils;

public class PageNumberEdit extends BaseActivity {
	private String mTitle; // 菜单标题
	private TextView mTvTitle;
	private View mLine = null;
	private TextView mTvNumber;
	private int pageNo = 1; // 当前要跳转的页码
	private int pageCount = 1; // 总页码
//	private int number = 1; // 数字输入
	private boolean isNumericKey = false; // 是否按了数字键，按上下左右键时为false, 按数字键时为true

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getIntentPara();
		initView();
	}

	private void getIntentPara() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		pageNo = intent.getIntExtra("page_cur", 1);
		pageCount = intent.getIntExtra("page_count", 1);

		if (null == mTitle) {
			finish();
			return;
		}
	}

	// 在页码输入中，所有文字字号统一用大字号: 40sp, 已经在布局文件中初始化，不必在此与功能设置中的字号设置挂钩
	private void initView() {
		Tools mTools = new Tools(PageNumberEdit.this);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(mTools.getBackgroundColor()));
		setContentView(R.layout.common_number_edit);

		mTvTitle = (TextView) findViewById(R.id.common_number_edit_title);
		mTvTitle.setText(mTitle);
		mTvTitle.setTextColor(mTools.getFontColor()); // 设置title的文字颜色

		mLine = (View) findViewById(R.id.common_number_edit_line);
		mLine.setBackgroundColor(mTools.getFontColor()); // 设置分割线的背景色

		mTvNumber = (TextView) findViewById(R.id.common_number_edit_digit);
		String tips = getResources().getString(R.string.library_page_read_tips, pageNo, pageCount);
		mTvNumber.setText(tips);
		mTvNumber.setTextColor(mTools.getFontColor()); // 设置文字颜色
		TTSUtils.getInstance().speakMenu(mTitle + "," + tips);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP: // 页码减1
			isNumericKey = false;
			pageNo--;
			if (pageNo < 1) {
				pageNo = pageCount;
			}
			setPage();
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN: // 页码增1
			isNumericKey = false;
			pageNo++;
			if (pageNo > pageCount) {
				pageNo = 1;
			}
			setPage();
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT: // 删除页码尾部数字
			isNumericKey = false;
			pageNo = pageNo / 10;
			setPage();
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT: // 页码尾部添0
			isNumericKey = false;
			pageNo = pageNo * 10;
			if (pageNo > pageCount) {
				pageNo = pageCount;
			}
			setPage();
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
			jumpToPageNumber();
			break;
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;
		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	@SuppressLint("HandlerLeak")
	private Handler mTtsCompletedHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 8:
				finish();
				break;

			default:
				break;
			}
		}
	};

	// 设置当前输入的页码
	private void setPage() {
		if (pageNo < 1) {
			pageNo = 0;
			mTvNumber.setText("");
			PublicUtils.showToast(this, getResources().getString(R.string.library_input_page_num));
		} else if (pageNo > pageCount) {
			pageNo = pageCount;
			mTvNumber.setText("" + pageNo);
			isNumericKey = false;
			PublicUtils.showToast(this, getResources().getString(R.string.library_input_page_max), new PromptListener() {

				@Override
				public void onComplete() {
					TTSUtils.getInstance().speakContent(pageNo + "");
				}
			});
		} else {
			mTvNumber.setText("" + pageNo);
			TTSUtils.getInstance().speakContent(pageNo + "");
		}
	}

	// 通过输入数字键编辑页码，digit 是当前输入的数字键对应数字
	private void changeNumber(int digit) {
		if (isNumericKey) {
			pageNo = pageNo * 10 + digit;
		} else {
			pageNo = digit;
		}
		isNumericKey = true;
		setPage();
	}

	// 跳转到指定页码
	private void jumpToPageNumber() {
		if (pageNo <= 0 || pageNo > pageCount) {
			PublicUtils.showToast(this, getResources().getString(R.string.library_input_page_num));
		} else {
			Intent intent = new Intent();
			intent.putExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, pageNo);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}
	 

}
