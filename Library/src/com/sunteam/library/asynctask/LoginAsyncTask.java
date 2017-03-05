package com.sunteam.library.asynctask;

import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.PublicUtils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

/**
 * 登录异步加载类
 * 
 * @author wzp
 * @Created 2017/01/24
 */
public class LoginAsyncTask extends AsyncTask<String, Void, Boolean>
{
	private Context mContext;
	
	public LoginAsyncTask( Context context )
	{
		mContext = context;
	}
	
	@Override
	protected Boolean doInBackground(String... params) 
	{
		String username = params[0];
		String password = params[1];
		
		String pw = HttpDao.login(username);
		if( ( null == pw ) || !pw.equals(password) )
		{
			return	false;
		}
		
		PublicUtils.saveUserInfo( mContext, username, password );	// 保存用户信息
		
		return	true;
	}
	
	@Override
	protected void onPreExecute() 
	{	
		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(Boolean result) 
	{	
		super.onPostExecute(result);
		
		if( result )
		{
			PublicUtils.showToast(mContext, mContext.getString(R.string.library_login_success), new PromptListener() {
				
				@Override
				public void onComplete() {
					((Activity) mContext).setResult(Activity.RESULT_OK);
					((Activity) mContext).finish();
				}
			});
		}
		else
		{
			PublicUtils.showToast(mContext, mContext.getString(R.string.library_login_fail), new PromptListener() {
				
				@Override
				public void onComplete() {
					
				}
			});
		}
	}
}
