package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.db.CategoryDBDao;
import com.sunteam.library.db.HistoryDBDao;
import com.sunteam.library.entity.CategoryInfoNodeEntity;
import com.sunteam.library.entity.HistoryEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.PublicUtils;

/**
 * 添加历史记录异步加载类
 * 
 * @author wzp
 * @Created 2017/02/27
 */
public class AddHistoryAsyncTask extends AsyncTask<HistoryEntity, Void, HistoryEntity>
{
	private Context mContext;
	
	public AddHistoryAsyncTask(Context context) 
	{
		mContext = context;
	}

	@Override
	protected HistoryEntity doInBackground(HistoryEntity... params) 
	{
		return	HttpDao.addHistory( params[0] );
	}
	
	@Override
	protected void onPreExecute() 
	{	
		super.onPreExecute();
		
		//String s = mContext.getResources().getString(R.string.library_add_history);
		//PublicUtils.showProgress(mContext, s);
		//TtsUtils.getInstance().speak(s);
	}
	
	@Override
	protected void onPostExecute(final HistoryEntity result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
		
		/*
		if( null != result )
		{
			PublicUtils.showToast(mContext, mContext.getString(R.string.library_add_history_success), new PromptListener() {
				
				@Override
				public void onComplete() {
					HistoryDBDao dao = new HistoryDBDao( mContext );
					dao.insert(result);		//缓存新的数据
					dao.closeDb();			//关闭数据库
					((Activity) mContext).finish();
				}
			});
		}
		else
		{
			PublicUtils.showToast(mContext, mContext.getString(R.string.library_add_history_fail), new PromptListener() {
				
				@Override
				public void onComplete() {
					//此处应该更新数据库
					((Activity) mContext).finish();
				}
			});
		}
		*/
		
		((Activity) mContext).finish();
	}
}
