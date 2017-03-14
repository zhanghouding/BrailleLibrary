package com.sunteam.library.asynctask;

import com.sunteam.library.entity.UserInfoEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.PublicUtils;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 找回密码第一步异步加载类
 * 
 * @author wzp
 * @Created 2017/03/14
 */
public class UserGetPasswordAsyncTask extends AsyncTask<String, Void, UserInfoEntity>
{
	private Context mContext;
	
	public UserGetPasswordAsyncTask( Context context )
	{
		mContext = context;
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
		int authenticType = Integer.parseInt(params[0]);
		String realName = params[1];
		String cardNo = params[2];
		
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
		
	}
}
