package com.sunteam.library.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.menu.MenuGlobal;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.ArrayUtils;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.GetCategoryAsyncTask;
import com.sunteam.library.asynctask.GetRecommendAsyncTask;
import com.sunteam.library.asynctask.LoginAsyncTask;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.MediaPlayerUtils;
import com.sunteam.library.utils.PublicUtils;
import com.sunteam.library.utils.TTSUtils;
import com.sunteam.library.utils.WifiUtils;

/**
 * @Destryption 进入数字图书馆界面后，会自动连接WIFI，并自动登录数字图书馆。
 * @Author Jerry
 * @Date 2017-1-21 上午11:06:24
 * @Note
 */
public class MainActivity extends MenuActivity {

	public void onCreate(Bundle savedInstanceState) {
		mTitle = getResources().getString(R.string.library_main_title);
		mMenuList = ArrayUtils.strArray2List(getResources().getStringArray(R.array.library_main_list));
		super.onCreate(savedInstanceState);
		MenuGlobal.debug("[Library-MainActivity][onCreate], this = " + this);
		
		MediaPlayerUtils.getInstance().init();	//初始化MediaPlayer
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!WifiUtils.checkWifiState(this)) {
			WifiUtils.openWifi(this);
		} else {
			new LoginAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, PublicUtils.getUserName());
		}
		acquireWakeLock(this);
		MenuGlobal.debug("[Library-MainActivity][onResume], this = " + this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MenuGlobal.debug("[Library-MainActivity][onPause], this = " + this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		MenuGlobal.debug("[Library-MainActivity][onStop], this = " + this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		TTSUtils.getInstance().destroy();
		MediaPlayerUtils.getInstance().destroy();
		if (null != TtsUtils.getInstance()) {
			TtsUtils.getInstance().destroy();
		}
		WifiUtils.closeWifi(this);
		releaseWakeLock();
		/*
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
		*/
		MenuGlobal.debug("[Library-MainActivity][onDestroy], this = " + this);
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
		String[] list = null;
//		Class<?> cls = null;

		switch (selectItem) {
		case 0: // 我的图书馆
			list = getResources().getStringArray(R.array.library_mylibrary_list);
			startNextActivity(MylibraryActivity.class, selectItem, menuItem, list);
//			testCreateFile();
			break;
		case 1: // 资源检索
			startNextActivity(SearchActivity.class, selectItem, menuItem, null);
			break;
		case 2: // 电子书
			new GetCategoryAsyncTask(this, LibraryConstant.LIBRARY_ROOT_PATH, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LibraryConstant.LIBRARY_DATATYPE_EBOOK);
//			testEbook();
			break;
		case 3: // 有声书
			new GetCategoryAsyncTask(this, LibraryConstant.LIBRARY_ROOT_PATH, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LibraryConstant.LIBRARY_DATATYPE_AUDIO);
			break;
		case 4: // 口述影像
			new GetCategoryAsyncTask(this, LibraryConstant.LIBRARY_ROOT_PATH, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LibraryConstant.LIBRARY_DATATYPE_VIDEO);
			break;
//			testLogin("test1");
//			return;
		case 5: // 图书馆新闻
			PublicUtils.createCacheDir(LibraryConstant.LIBRARY_ROOT_PATH, menuItem);	//创建缓存目录
			list = getResources().getStringArray(R.array.library_info_list);
			startNextActivity(LibraryNewsCategoryList.class, selectItem, menuItem, list);
			break;
		case 6:	//个性推荐
			new GetRecommendAsyncTask(this, LibraryConstant.LIBRARY_ROOT_PATH, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LibraryConstant.RECOMMEND_TYPE_GETPERSONALLIST);
			break;
		case 7:	//最新更新
			new GetRecommendAsyncTask(this, LibraryConstant.LIBRARY_ROOT_PATH, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LibraryConstant.RECOMMEND_TYPE_GETLATESTSERIALIZED);
			break;
		case 8:	//精品专区
			new GetRecommendAsyncTask(this, LibraryConstant.LIBRARY_ROOT_PATH, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, LibraryConstant.RECOMMEND_TYPE_GETBOUTIQUEDATA);
			break;
		default:
			break;
		}

		/*if (selectItem <= 1) { // 前两项既可在线也可离线
			startNextActivity(cls, selectItem, menuItem, list);
		} else if (WifiUtils.checkWifiState(this)) { // 后三项只能在线, 必须先连接Wifi
			startNextActivity(cls, selectItem, menuItem, list);
		} else {
			WifiUtils.startWifi(this, mHandler, LibraryConstant.MSG_CONFIRMDIALOG_RETURN);
		}*/
	}

	private void startNextActivity(Class<?> cls, int selectItem, String title, String[] list) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, list); // 菜单列表
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, LibraryConstant.LIBRARY_ROOT_PATH+title+"/");	//父目录

		intent.setClass(this, cls);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LibraryConstant.MSG_CONFIRMDIALOG_RETURN:
				if (0 == msg.arg1) { // 表示确认
					WifiUtils.startWifiSetting(MainActivity.this);
				}
				break;
			case LibraryConstant.MSG_HTTP_USER_AUTH:
//				parseUserInfo((String) msg.obj);
				break;
			case LibraryConstant.MSG_HTTP_EBOOK_CATEGORY_LIST:
//				parseEbookCategoryList((String) msg.obj);
				break;

			default:
				break;
			}
		}
	};

	// 电子图书馆界面禁止休眠
	private WakeLock mWakeLock = null; // 唤醒锁

	@SuppressWarnings("deprecation")
	private void acquireWakeLock(Context context) {
		if (null == mWakeLock) {
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, context.getClass().getName());
			mWakeLock.acquire();
		}
	}

	private void releaseWakeLock() {
		if (null != mWakeLock && mWakeLock.isHeld()) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

//	private void testCreateFile() {
//		boolean ret = new LibraryOfflineFile().createLibraryDir(this, 0, "课外读物/少儿");
//		MenuGlobal.debug("[Library-MainActivity][testCreateFile] ret = " + ret);
//		
//	}

//	private void testLogin(String userName) {
//		String url = "http://www.blc.org.cn/API/UserInterface.ashx";
//		HttpGetUtils mHttpGetUtils = new HttpGetUtils();
//		mHttpGetUtils.addGetParameter("requestType", "UserAuthentication");
//		mHttpGetUtils.addGetParameter("timeStr", "2016-01-04$10:54:00");
//		mHttpGetUtils.addGetParameter("AuthenticationStr", "MWPlatformAuthentication");
//		mHttpGetUtils.addGetParameter("SystemCode", "MWAPP");
//		mHttpGetUtils.addGetParameter("userName", userName);
//		mHttpGetUtils.addGetParameter("EncryptedStr", "6fb7e13bb86e5bddd89f3ef2ba2cb28f");
//		mHttpGetUtils.sendGet(url, mHandler, LibraryConstant.MSG_HTTP_USER_AUTH);
//	}

//	@SuppressWarnings("deprecation")
//	private void parseUserInfo(String mJson) {
//		MenuGlobal.debug("[Library-MainActivity][parseUserInfo] mJson = " + mJson);
//		if (null == mJson || 0 == mJson.length()) {
//			return;
//		}
//
//		int checkState = JsonUtils.getInt(mJson, "CheckState");
//		if (1 == checkState) {
//			SharedPrefUtils.setSharedPrefString(MainActivity.this, LibraryConstant.LIBRARY_CONFIG_FILE, Context.MODE_WORLD_READABLE,
//					LibraryConstant.LIBRARY_LOGIN_STATE, "1");
//		}
//	}
//
//	private void testEbook() {
//		MenuGlobal.debug("[Library-MainActivity][testEbook]");
//		String url = "http://www.blc.org.cn/API/EbookInterface.ashx?requestType=GetEbookCategory";
//		HttpGetUtils mHttpGetUtils = new HttpGetUtils();
//		mHttpGetUtils.sendGet(url, mHandler, LibraryConstant.MSG_HTTP_EBOOK_CATEGORY_LIST);
//	}
//
//	private void parseEbookCategoryList(String mJson) {
//		MenuGlobal.debug("[Library-MainActivity][parseEbookCategoryList] mJson = " + mJson);
//		if (null == mJson || 0 == mJson.length()) {
//			return;
//		}
//
//	}

}
