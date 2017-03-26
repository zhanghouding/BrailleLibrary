package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.utils.ArrayUtils;
import com.sunteam.library.R;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 账号管理：登录、注册、密码找回; 注销功能只有在登录后才起作用。进入数字图书馆后，若尚未登录则进入账号管理界面。
 * @Author Jerry
 * @Date 2017-3-1 上午9:51:33
 * @Note 进入数字图书馆后自动用上次用户账号和密码登录。
 */
public class AccountManager extends MenuActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mTitle = getResources().getString(R.string.library_account_manage);
		String[] list = getResources().getStringArray(R.array.library_account_manager_list);
		mMenuList = ArrayUtils.strArray2List(list);
		super.onCreate(savedInstanceState);
		// 不显示提示框，而且也不进入Wifi设置界面
		// WifiUtils mWifiUtils = new WifiUtils();
		// if(!mWifiUtils.checkWifiState(this)){
		// String confirmTitle = getResources().getString(R.string.library_startwifi);
		// String wifiSettingTitle = getResources().getString(R.string.library_wifi_setting);
		// mWifiUtils.startWifiConfirm(this, confirmTitle, wifiSettingTitle);
		// }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK == resultCode) {
			setResult(Activity.RESULT_OK);
			finish();
		}
	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		switch (selectItem) {
		case 0: // 登录
			startNextActivity(AccountLogin.class, selectItem, menuItem);
			break;
		case 1: // 注册
			startNextActivity(AccountRegister.class, selectItem, menuItem);
			break;
		case 2: // 密码找回
			startNextActivity(AccountPasswdRecovery.class, selectItem, menuItem);
			break;
		case 3: // 使用默认账号登录
			String username = android.provider.Settings.Secure.getString(getContentResolver(), "accessibility_deviceid");
			if (null == username || username.length() <= 6) {
				PublicUtils.showToast(this, getResources().getString(R.string.library_login_fail));
			} else {
				String password = "S918P" + username.substring(username.length() - 6);
				PublicUtils.saveUserInfo(this, username, password); // 保存用户信息
				PublicUtils.showToast(this, getResources().getString(R.string.library_login_success));
				setResult(Activity.RESULT_OK);
				finish();
			}
			break;
		default:
			break;
		}
	}

	private void startNextActivity(Class<?> cls, int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, menuItem);
		intent.putExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, selectItem);
		intent.setClass(this, cls);
		startActivityForResult(intent, selectItem);
	}

}
