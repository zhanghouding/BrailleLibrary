package com.sunteam.library.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.sunteam.common.menu.BaseActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.utils.Tools;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.AddBookMarkAsyncTask;
import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.utils.PublicUtils;
import com.sunteam.library.utils.TTSUtils;

/**
 * 增加书签界面
 * 
 * @author sylar
 */
public class BookmarkNameEdit extends BaseActivity {
	private String mTitle; // 菜单标题
	private TextView mTvTitle;
	private View mLine = null;
	private TextView mTvBookmarkName;
	private BookmarkEntity mBookmarkEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getIntentPara();
		initViews();
	}

	private void getIntentPara() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mBookmarkEntity = (BookmarkEntity) intent.getSerializableExtra("book_mark");

		if (null == mTitle) {
			finish();
			return;
		}
	}

	private void initViews() {
		Tools mTools = new Tools(this);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(mTools.getBackgroundColor()));
		setContentView(R.layout.library_bookmark_edit);
		
		int fontSize = mTools.getFontSize();
		mTvTitle = (TextView) findViewById(R.id.library_addbookmark_title);
		mTvTitle.setText(mTitle);
		mTvTitle.setTextColor(mTools.getFontColor()); // 设置title的文字颜色
		mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel()); // 设置title字号
		mTvTitle.setHeight(mTools.convertSpToPixel(fontSize));

		mLine = (View) findViewById(R.id.library_addbookmark_line);
		mLine.setBackgroundColor(mTools.getFontColor()); // 设置分割线的背景色

		mTvBookmarkName = (TextView) findViewById(R.id.library_addbookmark_content);
		mTvBookmarkName.setText(mBookmarkEntity.markName);
		mTvBookmarkName.setTextColor(mTools.getFontColor()); // 设置文字颜色
		mTvBookmarkName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel());
//		mTvBookmarkName.setHeight(mTools.convertSpToPixel(fontSize));

		TTSUtils.getInstance().speakMenu(mTitle + "，" + mTvBookmarkName.getText().toString());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:// 返回
			PublicUtils.showToast(this, getResources().getString(R.string.library_add_mark_cancel), true);
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER: // 确定
		case KeyEvent.KEYCODE_ENTER:
			new AddBookMarkAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mBookmarkEntity);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/*public void setResultCode() {
		setResult(Activity.RESULT_OK);
		finish();
	}*/
}
