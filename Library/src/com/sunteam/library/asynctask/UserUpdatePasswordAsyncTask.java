package com.sunteam.library.asynctask;

import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

/**
 * 找回密码第二步异步加载类
 * 
 * @author wzp
 * @Created 2017/03/14
 */
public class UserUpdatePasswordAsyncTask extends AsyncTask<String, Void, Integer>
{
	private Context mContext;
	
	public UserUpdatePasswordAsyncTask( Context context )
	{
		mContext = context;
	}
	
	/**
	 * 更新用户密码信息(找回密码第二步)
	 * 
	 * @param authenticType ：验证类型：1 读者卡号 2：残疾人证号
	 * @param username	: 用户名
	 * @param realName	: 真实姓名
	 * @param cardNo	: 号码(验证类型为1，号码就是读者卡号；验证类型为2，号码就是残疾人证号)
	 * @return
	 * @author wzp
	 * @Created 2017/03/01
	 */
	@Override
	protected Integer doInBackground(String... params) 
	{
		int authenticType = Integer.parseInt(params[0]);
		String userName = params[1];
		String realName = params[2];
		String cardNo = params[3];
		String password = params[4];
		
		return	HttpDao.updateGetPassword( authenticType, userName, realName, cardNo, password );
	}
	
	@Override
	protected void onPreExecute() 
	{	
		super.onPreExecute();
		PublicUtils.showProgress(mContext, this);
	}
	
	@Override
	protected void onPostExecute(Integer result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
		
		switch( result )
		{
			case LibraryConstant.RESULT_EXCEPTION:	//异常
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_net_error), new PromptListener() {
					
					@Override
					public void onComplete() {
						
					}
				});
				break;
			case LibraryConstant.RESULT_SUCCESS:	//成功
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_update_password_success), new PromptListener() {
					
					@Override
					public void onComplete() {
						((Activity) mContext).setResult(Activity.RESULT_OK);
						((Activity) mContext).finish();
					}
				});
				break;
			case LibraryConstant.RESULT_FAIL:		//失败
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_update_password_fail), new PromptListener() {
					
					@Override
					public void onComplete() {
						
					}
				});
				break;
			default:
				break;
		}
	}
}
