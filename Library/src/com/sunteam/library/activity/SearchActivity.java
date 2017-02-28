package com.sunteam.library.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.sunteam.common.menu.BaseActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsListener;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.utils.LogUtils;
import com.sunteam.library.view.LibrarySearchView;

public class SearchActivity extends BaseActivity {
	private FrameLayout mFlContainer = null;
	private LibrarySearchView mMainView = null;
	private Boolean hasCreated = false; 

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.common_menu_activity);
		initView();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			if(!hasCreated){
				hasCreated = true;
				// 在平板电脑上运行时，若在onCreate()中，会出现java.lang.NullPointerException异常
				mFlContainer.removeAllViews();
				mFlContainer.addView(mMainView.getView());
				// mMainView.calcItemCount();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null != mMainView) {
			mMainView.onResume();
		}

		TtsUtils.getInstance(this, mTtsListener);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initView() {
//		this.getWindow().setBackgroundDrawable(new ColorDrawable(new Tools(this).getBackgroundColor()));

		mFlContainer = (FrameLayout) this.findViewById(R.id.common_menu_fl_container);
		Intent intent = getIntent();
		String mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		try {
			mMainView = new LibrarySearchView(this, mTitle);
		} catch (Exception e) {
			e.printStackTrace();
			finish();
		}

		/*// 在平板电脑上运行时，会出现java.lang.NullPointerException异常，可把这两行放到onWindowFocusChanged()中
		mFlContainer.removeAllViews();
		mFlContainer.addView(mMainView.getView());*/

		/*Global.setHandler(mHandler);
		Global.setContext(this);*/
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean ret = false;
		try {
			ret = mMainView.onKeyDown(keyCode, event);
			LogUtils.d("[MainActivity] onKeyDown(): keyCode = " + keyCode + ", ret = " + ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!ret) {
			ret = super.onKeyDown(keyCode, event);
		}

		return ret;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean ret = false;
		try {
			ret = mMainView.onKeyUp(keyCode, event);
			LogUtils.d("[MainActivity] onKeyDown(): onKeyUp = " + keyCode + ", ret = " + ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!ret) {
			ret = super.onKeyUp(keyCode, event);
		}

		return ret;
	}

	private TtsListener mTtsListener = new TtsListener() {
		
		@Override
		public void onSpeakResumed() {
		}
		
		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
		}
		
		@Override
		public void onSpeakPaused() {
		}
		
		@Override
		public void onSpeakBegin() {
		}
		
		@Override
		public void onInit(int code) {
		}
		
		@Override
		public void onCompleted(String error) {
			mMainView.processLongKey();
		}
		
		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
			
		}
	};

}
