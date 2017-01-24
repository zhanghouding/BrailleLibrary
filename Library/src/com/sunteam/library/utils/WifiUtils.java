package com.sunteam.library.utils;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.utils.CommonConstant;
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.Tools;
import com.sunteam.library.R;

public class WifiUtils {
	public static boolean checkWifiState(Context context) {
		WifiManager mWiFiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		int state = mWiFiManager.getWifiState();
		if (WifiManager.WIFI_STATE_ENABLED == state || WifiManager.WIFI_STATE_ENABLING == state) {
			return true;
		} else {
			return false;
		}
	}

	public static void openWifi(Context context) {
		WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mWifiManager.setWifiEnabled(true);
	}

	// 数字图书馆也这样处理
	public static void closeWifi(Context context) {
		WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (mWifiManager.isWifiEnabled()) {
			// 如果功能设置中设置为关闭，则退出网络收音机时也关闭，以便省电!
			Tools mTools = new Tools(context);
			if (CommonConstant.DEFAULT_WIFISWITCH_INDEX == mTools.getWifiSwitch()) {
				mWifiManager.setWifiEnabled(false);
			}
		}
	}

	public static void startWifiSetting(Context context) {
		Intent intent = new Intent();
		String packageName = "com.sunteam.settings";
		String className = "com.sunteam.settings.activity.WifiList";
		intent.setClassName(packageName, className);
		String title = context.getResources().getString(R.string.library_wifi_setting);
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		context.startActivity(intent);
	}

	public static void startWifi(Context context, Handler handler, int what) {
		String s = context.getResources().getString(R.string.library_startwifi);
		ConfirmDialog mConfirmDialog = new ConfirmDialog(context, s);
		mConfirmDialog.setHandler(handler, what);
		mConfirmDialog.show();
	}

}
