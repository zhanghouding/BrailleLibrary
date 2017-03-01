package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.utils.SharedPrefUtils;
import com.sunteam.library.R;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 账号管理：登录、注销、注册、密码找回
 * @Author Jerry
 * @Date 2017-3-1 上午9:51:33
 * @Note 进入数字图书馆后自动用上次用户账号和密码登录。
 */
public class AccountManager extends MenuActivity {

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode) { // 在子菜单中回传的标志
			return;
		}
		String name = data.getStringExtra(LibraryConstant.LIBRARY_LOGIN_USERNAME);
		String passwd = data.getStringExtra(LibraryConstant.LIBRARY_LOGIN_PASSWD);
		switch (requestCode) {
		case 0: // 登录
		case 1: // 注册
			saveLoginInfo(name, passwd);
			setLoginState(1);
			finish();
			break;
		default:
			break;
		}

	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		switch (selectItem) {
		case 0: // 登录
			startNextActivity(AccountLogin.class, selectItem, menuItem);
			break;
		case 1: // 注销
			logoutAccount();
			break;
		case 2: // 注册
			// startNextActivity(AccountRegister.class, selectItem, menuItem);
			break;
		case 3: // 密码找回
			// startNextActivity(AccountPasswdRecovery.class, selectItem, menuItem);
			break;
		default:
			break;
		}
	}

	private void startNextActivity(Class<?> cls, int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, menuItem);
		intent.setClass(this, cls);
		startActivityForResult(intent, selectItem);
	}

	// 缓存登录账号和密码
	@SuppressWarnings("deprecation")
	private void saveLoginInfo(String userName, String passwd) {
		Context context = getApplicationContext();
		String file = LibraryConstant.LIBRARY_CONFIG_FILE;
		SharedPrefUtils.setSharedPrefString(context, file, Context.MODE_WORLD_READABLE, LibraryConstant.LIBRARY_LOGIN_USERNAME, userName);
		SharedPrefUtils.setSharedPrefString(context, file, Context.MODE_WORLD_READABLE, LibraryConstant.LIBRARY_LOGIN_PASSWD, passwd);
	}

	// 缓存登录账号和密码
	@SuppressWarnings("deprecation")
	private void setLoginState(int state) {
		Context context = getApplicationContext();
		String file = LibraryConstant.LIBRARY_CONFIG_FILE;
		SharedPrefUtils.setSharedPrefInt(context, file, Context.MODE_WORLD_READABLE, LibraryConstant.LIBRARY_LOGIN_STATE, state);
	}

	// 注销账号，实际上就是退出登录，只设置登录状态即可
	private void logoutAccount() {
		setLoginState(0);
		PublicUtils.showToast(this, getResources().getString(R.string.library_account_logout_success), true);
	}

}
