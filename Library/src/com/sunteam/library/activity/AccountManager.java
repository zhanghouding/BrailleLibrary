package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.utils.ArrayUtils;
import com.sunteam.library.R;

/**
 * @Destryption 账号管理：登录、注销、注册、密码找回
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
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode) {
			return;
		}
		setResult(Activity.RESULT_OK);
		finish();
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
