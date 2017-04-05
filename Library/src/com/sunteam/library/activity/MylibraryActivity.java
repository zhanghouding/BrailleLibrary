package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.dialog.ConfirmListener;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.GetCollectCategoryAsyncTask;
import com.sunteam.library.asynctask.GetCollectResourceAsyncTask;
import com.sunteam.library.asynctask.GetHistoryAsyncTask;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 我的图书馆浏览界面，是固定的菜单列表
 * @Author Jerry
 * @Date 2017-2-4 下午3:37:32
 * @Note
 */
public class MylibraryActivity extends MenuActivity {

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode) {
			return;
		}
	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		String[] list = null;
		
		switch (selectItem) {
		case 0: // 收藏资源
			new GetCollectResourceAsyncTask(this, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1, LibraryConstant.LIBRARY_COLLECT_RESOURCE_PAGESIZE);
			break;
		case 1: // 收藏分类
			new GetCollectCategoryAsyncTask(this, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1, LibraryConstant.LIBRARY_COLLECT_CATEGORY_PAGESIZE);
			break;
		case 2: // 最近阅读历史
			new GetHistoryAsyncTask(this, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1, LibraryConstant.LIBRARY_HISTORY_PAGESIZE);
			break;
		case 3: // 下载管理
			list = getResources().getStringArray(R.array.library_download_manager_list);
			startNextActivity(DownloadManager.class, selectItem, menuItem, list);
			break;
		case 4: // 退出当前账号
			logout();
			break;
		default:
			break;
		}

		/*if (WifiUtils.checkWifiState(this)) {
			startNextActivity(cls, selectItem, menuItem, list);
		} else {
			startWifi();
		}*/
	}

	private void startNextActivity(Class<?> cls, int selectItem, String title, String[] list){
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, list); // 菜单列表
		intent.setClass(this, cls);
		startActivityForResult(intent, selectItem);
	}

	@SuppressWarnings("unused")
	private void startWifi() {
		String s = getResources().getString(R.string.library_startwifi);
		ConfirmDialog mConfirmDialog = new ConfirmDialog(this, s);
		mConfirmDialog.setConfirmListener(new ConfirmListener() {

			@Override
			public void doConfirm() {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						startWifiSetting();
					}
				}, 10);
			}

			@Override
			public void doCancel() {
			}
		});
		mConfirmDialog.show();
	}

	private void startWifiSetting() {
		Intent intent = new Intent();
		String packageName = "com.sunteam.settings";
		String className = "com.sunteam.settings.activity.WifiList";
		intent.setClassName(packageName, className);
		String title = getResources().getString(R.string.library_wifi_setting);
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		startActivity(intent);
	}

	// 退出当前账号
	private void logout() {
		PublicUtils.saveUserInfo(this, "", ""); // 保存用户信息
		String title = getResources().getString(R.string.library_account_logout_confirm);
		ConfirmDialog mConfirmDialog = new ConfirmDialog(this, title);
		mConfirmDialog.setConfirmListener(new ConfirmListener() {

			@Override
			public void doConfirm() {
				String s = getResources().getString(R.string.library_account_logout_success);
				PublicUtils.showToast(MylibraryActivity.this, s, new PromptListener() {
					
					@Override
					public void onComplete() {
						setResult(Activity.RESULT_OK);
						finish();
					}
				});
			}

			@Override
			public void doCancel() {

			}
		});
		mConfirmDialog.show();
	}

}
