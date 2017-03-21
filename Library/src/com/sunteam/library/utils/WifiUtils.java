package com.sunteam.library.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.utils.CommonConstant;
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.Tools;
import com.sunteam.common.utils.dialog.ConfirmListener;

public class WifiUtils {

	public boolean checkWifiState(Context context) {
		boolean ret = false;
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (mWiFiNetworkInfo != null) {
			ret = mWiFiNetworkInfo.isAvailable();
		}

		// MenuGlobal.debug("[IpRadio-MainActivity][checkWifiState], ret = " + ret);
		return ret;
	}

	public void openWifi(Context context) {
		WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mWifiManager.setWifiEnabled(true);
	}

	// 数字图书馆也这样处理
	public void closeWifi(Context context) {
		WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (mWifiManager.isWifiEnabled()) {
			// 如果功能设置中设置为关闭，则退出网络收音机时也关闭，以便省电!
			Tools mTools = new Tools(context);
			if (CommonConstant.DEFAULT_WIFISWITCH_INDEX == mTools.getWifiSwitch()) {
				mWifiManager.setWifiEnabled(false);
			}
		}
	}

	public void startWifiConfirm(final Context context, String confirmTitle, final String wifiSettingTitle) {
		ConfirmDialog mConfirmDialog = new ConfirmDialog(context, confirmTitle);
		mConfirmDialog.setConfirmListener(new ConfirmListener() {

			@Override
			public void doConfirm() {
				startWifiSetting(context, wifiSettingTitle);
			}

			@Override
			public void doCancel() {
			}
		});
		mConfirmDialog.show();
	}

	public void startWifiSetting(Context context, String title) {
		Intent intent = new Intent();
		String packageName = "com.sunteam.settings";
		String className = "com.sunteam.settings.activity.WifiList";
		intent.setClassName(packageName, className);
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
		context.startActivity(intent);
	}

	public void startWifiConfirm(Context context, String title, Handler handler, int what) {
		ConfirmDialog mConfirmDialog = new ConfirmDialog(context, title);
		mConfirmDialog.setHandler(handler, what);
		mConfirmDialog.show();
	}

}
