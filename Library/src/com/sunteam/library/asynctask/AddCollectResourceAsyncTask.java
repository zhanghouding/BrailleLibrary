package com.sunteam.library.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.sunteam.library.R;
import com.sunteam.library.entity.CollectResourceEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 添加收藏资源异步加载类
 * 
 * @author wzp
 * @Created 2017/02/05
 */
public class AddCollectResourceAsyncTask extends AsyncTask<String, Void, Integer>
{
	private Context mContext;
	private CollectResourceEntity mCollectResourceEntity;
	
	public AddCollectResourceAsyncTask(Context context, CollectResourceEntity entity) 
	{
		mContext = context;
		mCollectResourceEntity = entity;
	}

	@Override
	protected Integer doInBackground(String... params) 
	{
		Integer result = HttpDao.addCollectResource(mCollectResourceEntity);
		
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
		
		String s = mContext.getResources().getString(R.string.library_add_collect_resource);
		PublicUtils.showProgress(mContext, s, this);
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
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_net_error), null);
				//如果出现网络异常，应该收藏到本地数据库中。
				break;
			case LibraryConstant.RESULT_SUCCESS:	//成功
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_add_collect_success), null);
				break;
			case LibraryConstant.RESULT_FAIL:		//失败
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_add_collect_fail), null);
				break;
			default:
				break;
		}
	}
}
