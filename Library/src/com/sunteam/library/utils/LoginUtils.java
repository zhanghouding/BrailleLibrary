package com.sunteam.library.utils;

import com.sunteam.common.utils.CommonUtils;

import android.content.Context;
import android.os.Handler;
import android.provider.Settings;

public class LoginUtils {

	public static boolean autoLogin(Context context, Handler handler) {
		String name = CommonUtils.getSettingSecureString(context, LibraryConstant.ACCESSIBILITY_DEVICEID);
		if (null == name || name.equals("")) {
			return false;
		}

		String url = "http://www.blc.org.cn/API/UserInterface.ashx";
		HttpGetUtils mHttpGetUtils = new HttpGetUtils();
		mHttpGetUtils.addGetParameter("requestType", "UserAuthentication");
		mHttpGetUtils.addGetParameter("timeStr", "2016-01-04$10:54:00");
		mHttpGetUtils.addGetParameter("AuthenticationStr", "MWPlatformAuthentication");
		mHttpGetUtils.addGetParameter("SystemCode", "MWAPP");
		mHttpGetUtils.addGetParameter("userName", name);
		mHttpGetUtils.addGetParameter("EncryptedStr", "6fb7e13bb86e5bddd89f3ef2ba2cb28f");
		mHttpGetUtils.sendGet(url, handler, LibraryConstant.MSG_HTTP_USER_AUTH);

		return true;
	}

}
