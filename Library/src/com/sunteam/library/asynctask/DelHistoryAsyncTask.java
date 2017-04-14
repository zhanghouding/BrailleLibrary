package com.sunteam.library.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.db.HistoryDBDao;
import com.sunteam.library.entity.HistoryEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 删除阅读历史异步加载类
 * 
 * @author wzp
 * @Created 2017/02/19
 */
public class DelHistoryAsyncTask extends AsyncTask<HistoryEntity, Void, Integer>
{
	private Context mContext;
	private Handler mHandler;
	
	public DelHistoryAsyncTask(Context context, Handler h) 
	{
		mContext = context;
		mHandler =  h;
	}

	@Override
	protected Integer doInBackground(HistoryEntity... params) 
	{
		HistoryEntity entity = (HistoryEntity)params[0];
		String userName = entity.userName;
		String dbCode = entity.dbCode;
		String sysId = entity.sysId;
		
		HistoryDBDao dao = new HistoryDBDao( mContext );
		dao.delete(userName, dbCode, sysId);
		dao.closeDb();
		
		if( 0 == entity.id )	//本地历史记录
		{
			return	LibraryConstant.RESULT_SUCCESS;
		}
		else					//网络历史记录
		{
			Integer result = HttpDao.delHistory(userName, dbCode, sysId);
			
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
		
		// 因为删除时间很短，不必提示“正在删除阅读历史”，否则，会立即被“删除成功”打断！
//		String s = mContext.getResources().getString(R.string.library_del_history);
//		PublicUtils.showProgress(mContext, s, this); //TtsUtils.getInstance().speak(s);
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
				}, TtsUtils.TTS_QUEUE_ADD);
				break;
			case LibraryConstant.RESULT_SUCCESS:	//成功
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_del_success), new PromptListener() {
					
					@Override
					public void onComplete() {
						sendEmptyMessage(0);
					}
				}, TtsUtils.TTS_QUEUE_ADD);
				break;
			case LibraryConstant.RESULT_FAIL:		//失败
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_del_fail), new PromptListener() {
					
					@Override
					public void onComplete() {
						sendEmptyMessage(2);
					}
				}, TtsUtils.TTS_QUEUE_ADD);
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
