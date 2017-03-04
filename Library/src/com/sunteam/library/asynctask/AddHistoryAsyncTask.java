package com.sunteam.library.asynctask;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.sunteam.library.db.HistoryDBDao;
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
		HistoryEntity he = params[0];
		HistoryEntity entity = HttpDao.addHistory( he );
		if( null == entity )	//添加历史记录失败则调用更新接口
		{
			entity = HttpDao.updateHistory( he );
		}
		
		HistoryDBDao dao = new HistoryDBDao( mContext );
		if( null == entity )	//如果添加或者更新历史记录都失败了，则保存原始数据
		{
			dao.delete( he );
			dao.insert( he );
		}
		else
		{
			dao.delete( entity );
			dao.insert( entity );
		}
		dao.closeDb();			//关闭数据库
		
		return	entity;
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
