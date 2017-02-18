package com.sunteam.library.asynctask;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 删除书签异步加载类
 * 
 * @author wzp
 * @Created 2017/02/18
 */
public class DelBookMarkAsyncTask extends AsyncTask<Integer, Void, Integer>
{
	private Context mContext;
	private Handler mHandler;
	
	public DelBookMarkAsyncTask(Context context, Handler h) 
	{
		mContext = context;
		mHandler =  h;
	}

	@Override
	protected Integer doInBackground(Integer... params) 
	{
		int id = params[0];
		Integer result = HttpDao.delBookMark(PublicUtils.getUserName(), id+"");
		
		if( null == result )
		{
			return	LibraryConstant.RESULT_EXCEPTION;
		}
		
		return	result;
	}
	
	@Override
	protected void onPreExecute() 
	{	
		super.onPreExecute();
		
		String s = mContext.getResources().getString(R.string.library_del_bookmark);
		PublicUtils.showProgress(mContext, s);
		//TtsUtils.getInstance().speak(s);
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
						sendEmptyMessage(1);
					}
				});
				break;
			case LibraryConstant.RESULT_SUCCESS:	//成功
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_del_bookmark_success), new PromptListener() {
					
					@Override
					public void onComplete() {
						sendEmptyMessage(0);
					}
				});
				break;
			case LibraryConstant.RESULT_FAIL:		//失败
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_del_bookmark_fail), new PromptListener() {
					
					@Override
					public void onComplete() {
						sendEmptyMessage(2);
					}
				});
				break;
			default:
				break;
		}
	}
	
	private void sendEmptyMessage(int what){
		if(null != mHandler){
			mHandler.sendEmptyMessage(what);
		}
	}
}
