package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.db.HistoryDBDao;
import com.sunteam.library.entity.HistoryEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 清空阅读历史异步加载类
 * 
 * @author wzp
 * @Created 2017/02/19
 */
public class ClearHistoryAsyncTask extends AsyncTask<ArrayList<HistoryEntity>, Void, Integer>
{
	private Context mContext;
	private Handler mHandler;
	
	public ClearHistoryAsyncTask(Context context, Handler h) 
	{
		mContext = context;
		mHandler =  h;
	}

	@Override
	protected Integer doInBackground(ArrayList<HistoryEntity>... params) 
	{
		ArrayList<HistoryEntity> list = (ArrayList<HistoryEntity>)params[0];
		if( null == list )
		{
			return	LibraryConstant.RESULT_EXCEPTION;	
		}
		String userName = PublicUtils.getUserName(mContext);
		
		HistoryDBDao dao = new HistoryDBDao( mContext );
		dao.deleteAll(userName);	//清空此人的所有本地历史记录
		dao.closeDb();
		
		ArrayList<Integer> ids = new ArrayList<Integer>();
		int size = list.size();
		for( int i = 0; i < size; i++ )
		{			
			if( list.get(i).id > 0 )	//网络历史记录
			{
				ids.add(list.get(i).id);
			}
		}
		
		size = ids.size();
		if( size > 0 )	//有网络历史记录
		{
			String recordIds = "";
			for( int i = 0; i < size; i++ )
			{			
				if( i > 0 )
				{
					recordIds += ",";
				}
			
				recordIds += ids.get(i);
			}
			
			Integer result = HttpDao.clearHistory(userName, recordIds);
			
			if( null == result )
			{
				return	LibraryConstant.RESULT_EXCEPTION;
			}
			
			return	result;
		}
		
		return	LibraryConstant.RESULT_SUCCESS;
	}
	
	@Override
	protected void onPreExecute() 
	{	
		super.onPreExecute();

		// 因为删除时间很短，不必提示“正在清空阅读历史”，否则，会立即被“清空成功”或“清空失败”打断！
//		String s = mContext.getResources().getString(R.string.library_clear_history);
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
				});
				break;
			case LibraryConstant.RESULT_SUCCESS:	//成功
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_clear_success), new PromptListener() {
					
					@Override
					public void onComplete() {
						sendEmptyMessage(0);
					}
				});
				break;
			case LibraryConstant.RESULT_FAIL:		//失败
				PublicUtils.showToast(mContext, mContext.getString(R.string.library_clear_fail), new PromptListener() {
					
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
