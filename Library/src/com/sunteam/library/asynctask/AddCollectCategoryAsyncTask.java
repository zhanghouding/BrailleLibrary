package com.sunteam.library.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.entity.CollectCategoryEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 添加收藏分类异步加载类
 * 
 * @author wzp
 * @Created 2017/02/05
 */
public class AddCollectCategoryAsyncTask extends AsyncTask<String, Void, Integer>
{
	private Context mContext;
	private CollectCategoryEntity mCollectCategoryEntity;
	
	public AddCollectCategoryAsyncTask(Context context, CollectCategoryEntity entity) 
	{
		mContext = context;
		mCollectCategoryEntity = entity;
	}

	@Override
	protected Integer doInBackground(String... params) 
	{
		Integer result = HttpDao.addCollectCategory(mCollectCategoryEntity);
		
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
		PublicUtils.showProgress(mContext);
		String s = mContext.getResources().getString(R.string.library_add_collect_category);
		TtsUtils.getInstance().speak(s);
	}
	
	@Override
	protected void onPostExecute(Integer result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
		
		switch( result )
		{
			case LibraryConstant.RESULT_EXCEPTION:	//异常
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_add_collect_category_fail), null);
				break;
			case LibraryConstant.RESULT_SUCCESS:	//成功
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_add_collect_category_success), null);
				break;
			case LibraryConstant.RESULT_FAIL:		//失败
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_add_collect_category_fail), null);
				break;
			default:
				break;
		}
	}
}