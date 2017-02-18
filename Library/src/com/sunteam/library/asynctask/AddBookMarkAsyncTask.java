package com.sunteam.library.asynctask;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 添加书签异步加载类
 * 
 * @author wzp
 * @Created 2017/02/05
 */
public class AddBookMarkAsyncTask extends AsyncTask<String, Void, Integer>
{
	private Context mContext;
	private BookmarkEntity mBookmarkEntity;
	
	public AddBookMarkAsyncTask(Context context, BookmarkEntity entity) 
	{
		mContext = context;
		mBookmarkEntity = entity;
	}

	@Override
	protected Integer doInBackground(String... params) 
	{
		Integer result = HttpDao.addBookMark(mBookmarkEntity);
		
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
		
		String s = mContext.getResources().getString(R.string.library_add_bookmark);
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
						setResultCode((Activity)mContext);
					}
				});
				break;
			case LibraryConstant.RESULT_SUCCESS:	//成功
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_add_mark_su), new PromptListener() {
					
					@Override
					public void onComplete() {
						setResultCode((Activity)mContext);
					}
				});
				break;
			case LibraryConstant.RESULT_FAIL:		//失败
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_add_mark_has), new PromptListener() {
					
					@Override
					public void onComplete() {
						setResultCode((Activity)mContext);
					}
				});
				break;
			default:
				break;
		}
	}
	
	public void setResultCode(Activity activity) {
		activity.setResult(Activity.RESULT_OK);
		activity.finish();
	}
}
