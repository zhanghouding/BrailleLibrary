package com.sunteam.library.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.sunteam.library.R;
import com.sunteam.library.db.CollectCategoryDBDao;
import com.sunteam.library.entity.CollectCategoryEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.PublicUtils;

/**
 * 添加收藏分类异步加载类
 * 
 * @author wzp
 * @Created 2017/02/05
 */
public class AddCollectCategoryAsyncTask extends AsyncTask<CollectCategoryEntity, Void, CollectCategoryEntity>
{
	private Context mContext;
	
	public AddCollectCategoryAsyncTask(Context context) 
	{
		mContext = context;
	}

	@Override
	protected CollectCategoryEntity doInBackground(CollectCategoryEntity... params) 
	{
		CollectCategoryEntity cce = params[0];
		CollectCategoryEntity entity = HttpDao.addCollectCategory( cce );
		if( null == entity )	//添加收藏记录失败则调用更新接口(先删除，后添加。直接调用更新接口总失败)
		{
			HttpDao.delCollectCategory(cce.userName, cce.categoryCode);
			entity = HttpDao.addCollectCategory( cce );
		}
		
		CollectCategoryDBDao dao = new CollectCategoryDBDao( mContext );		
		
		if( null == entity )	//如果添加或者更新收藏分类都失败了，则保存原始数据
		{
			dao.delete( cce );
			dao.insert( cce );
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
		
		String s = mContext.getResources().getString(R.string.library_add_collect_category);
		PublicUtils.showProgress(mContext, s, this);
		//TtsUtils.getInstance().speak(s);
	}
	
	@Override
	protected void onPostExecute(CollectCategoryEntity result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
		
		PublicUtils.showToast(mContext, mContext.getString(R.string.library_add_collect_success), null);
	}
}
