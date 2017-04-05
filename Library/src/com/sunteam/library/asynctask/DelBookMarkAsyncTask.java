package com.sunteam.library.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.db.BookMarkDBDao;
import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 删除书签异步加载类
 * 
 * @author wzp
 * @Created 2017/02/18
 */
public class DelBookMarkAsyncTask extends AsyncTask<BookmarkEntity, Void, Integer>
{
	private Context mContext;
	private Handler mHandler;
	
	public DelBookMarkAsyncTask(Context context, Handler h) 
	{
		mContext = context;
		mHandler =  h;
	}

	@Override
	protected Integer doInBackground(BookmarkEntity... params) 
	{
		BookmarkEntity entity = (BookmarkEntity)params[0];
		String userName = entity.userName;
		String bookId = entity.bookId;
		int begin = entity.begin;
		BookMarkDBDao dao = new BookMarkDBDao( mContext );
		dao.delete(userName, bookId, begin);
		dao.closeDb();
		
		if( 0 == entity.id )	//本地历史记录
		{
			return	LibraryConstant.RESULT_SUCCESS;
		}
		else					//网络历史记录
		{
			Integer result = HttpDao.delBookMark(userName, entity.id+"");
			
			if( null == result )
			{
				return	LibraryConstant.RESULT_EXCEPTION;
			}
			
			return	result;
		}
	}
	
	@Override
	protected void onPreExecute() 
	{	
		super.onPreExecute();
		
		String s = mContext.getResources().getString(R.string.library_del_bookmark);
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
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_net_error), new PromptListener() {
					
					@Override
					public void onComplete() {
						sendEmptyMessage(1);
					}
				});
				break;
			case LibraryConstant.RESULT_SUCCESS:	//成功
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_del_success), new PromptListener() {
					
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
