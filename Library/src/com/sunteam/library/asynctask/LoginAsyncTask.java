package com.sunteam.library.asynctask;

import com.sunteam.library.net.HttpDao;

import android.os.AsyncTask;

/**
 * 登录异步加载类
 * 
 * @author wzp
 * @Created 2017/01/24
 */
public class LoginAsyncTask extends AsyncTask<String, Void, Boolean>
{
	@Override
	protected Boolean doInBackground(String... params) 
	{
		// TODO Auto-generated method stub
		return	HttpDao.login(params[0]);
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
	}
}
