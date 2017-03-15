package com.sunteam.library.asynctask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sunteam.library.R;
import com.sunteam.library.activity.AccountPasswdSetting;
import com.sunteam.library.entity.UserInfoEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.PublicUtils;

/**
 * 找回密码第一步异步加载类
 * 
 * @author wzp
 * @Created 2017/03/14
 */
public class UserGetPasswordAsyncTask extends AsyncTask<String, Void, UserInfoEntity>
{
	private Context mContext;
	private int resquestCode; // 启动当前Activity时使用的resquestCode值
	private int authenticType; // 证件类型
	private String realName; // 真实姓名
	private String cardNo; // 证件号
	
	public UserGetPasswordAsyncTask( Context context, int code )
	{
		mContext = context;
		resquestCode = code;
	}
	
	/**
	 * 得到用户密码信息(找回密码第一步)
	 * 
	 * @param authenticType ：验证类型：1 读者卡号 2：残疾人证号
	 * @param realName	: 真实姓名
	 * @param cardNo	: 号码(验证类型为1，号码就是读者卡号；验证类型为2，号码就是残疾人证号)
	 * @return
	 * @author wzp
	 * @Created 2017/03/01
	 */
	@Override
	protected UserInfoEntity doInBackground(String... params) 
	{
		authenticType = Integer.parseInt(params[0]);
		realName = params[1];
		cardNo = params[2];
		
		return	HttpDao.userGetPassword( authenticType, realName, cardNo );
	}
	
	@Override
	protected void onPreExecute() 
	{	
		super.onPreExecute();
		PublicUtils.showProgress(mContext, this);
	}
	
	@Override
	protected void onPostExecute(UserInfoEntity result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
		
		if (null == result) {
			// 失败
			PublicUtils.showToast(mContext, mContext.getString(R.string.library_get_password_fail));
		} else {
			Intent intent = new Intent();
			intent.putExtra("user_name", result.userName); // 用户名
			intent.putExtra("type", "" + authenticType); // 证件类型
			intent.putExtra("card_no", cardNo); // 证件号
			intent.putExtra("real_name", realName); // 真实姓名
			intent.setClass(mContext, AccountPasswdSetting.class);
			((Activity) mContext).startActivityForResult(intent, resquestCode);
			((Activity) mContext).finish(); // 销毁当前Activity
		}
	}

}
