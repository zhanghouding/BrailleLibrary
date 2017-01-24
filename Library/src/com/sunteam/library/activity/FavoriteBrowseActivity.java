package com.sunteam.library.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.menu.MenuGlobal;
import com.sunteam.common.utils.CommonUtils;
import com.sunteam.common.utils.PromptDialog;
import com.sunteam.common.utils.SharedPrefUtils;
import com.sunteam.common.utils.SunteamToast;
import com.sunteam.library.R;
import com.sunteam.library.utils.HttpGetUtils;
import com.sunteam.library.utils.JsonUtils;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.WifiUtils;

/**
 * @Destryption 收藏夹中的新闻、电子书、有声书、口述影像共用一个界面处理程序
 * @Author Jerry
 * @Date 2017-1-22 下午5:06:24
 * @Note
 */
public class FavoriteBrowseActivity extends MenuActivity {
	private int favoriteType = 0; // 收藏类型：新闻、电子书、有声书、口述影像

	public void onCreate(Bundle savedInstanceState) {
		getIntentPara();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (0 == mMenuList.size()) {
			SunteamToast mSunteamToast = new SunteamToast(this);
			mSunteamToast.show(R.string.library_wait_reading_data);
			getFavoriteData(favoriteType);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode || null == data) { // 在子菜单中回传的标志
			return;
		}

	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
	}

	private void getIntentPara() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		favoriteType = intent.getIntExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, 0);
		mMenuList = new ArrayList<String>();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LibraryConstant.MSG_HTTP_FAVROITE_SEARCH:
				parseFavoriteSearchResult((String) msg.obj);
				break;
			case LibraryConstant.MSG_CONFIRMDIALOG_RETURN:
				parseFavoriteSearchResult((String) msg.obj);
				break;

			default:
				break;
			}
		}
	};

	private void getFavoriteData(int type) {
		String userName = CommonUtils.getSettingSecureString(this, LibraryConstant.ACCESSIBILITY_DEVICEID);// Settings.Secure.ACCESSIBILITY_DEVICEID
		switch (type) {
		case 0: // 新闻
			getFavoritNews(userName);
			break;
		case 1: // 电子书
			break;
		case 2: // 有声书
			break;
		case 3: // 口述影像
			break;
		default:
			break;
		}

	}

	private void getFavoritNews(String userName) {
		if (WifiUtils.checkWifiState(this)) {
			HttpGetUtils mHttpGetUtils = new HttpGetUtils();
			String url = "http://www.blc.org.cn/API/CollectInterface.ashx";
			mHttpGetUtils.addGetParameter("requestType", "SearchCollect");
			String jsonObj = "{" + JsonUtils.addQuotation("UserName") + ":" + JsonUtils.addQuotation(userName) + "}";
			mHttpGetUtils.addGetParameter("jsonObj", jsonObj);
			mHttpGetUtils.addGetParameter("pageSize", "1");
			mHttpGetUtils.addGetParameter("pageIndex", "3");
			mHttpGetUtils.sendGet(url, mHandler, LibraryConstant.MSG_HTTP_FAVROITE_SEARCH);
		}
	}

	private void parseFavoriteSearchResult(String mJson) {
		MenuGlobal.debug("[Library-MainActivity][parseUserInfo] mJson = " + mJson);
		if (null == mJson || 0 == mJson.length()) {
			PromptDialog mPromptDialog = new PromptDialog(this, "");
			mPromptDialog.setHandler(mHandler, LibraryConstant.MSG_CONFIRMDIALOG_RETURN);
			return;
		}

		int checkState = JsonUtils.getInt(mJson, "CheckState");
		if (1 == checkState) {
		}
	}
}
