package com.sunteam.library.asynctask;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.db.CollectResourceDBDao;
import com.sunteam.library.entity.CollectResourceEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.PublicUtils;

/**
 * 添加收藏资源异步加载类
 * 
 * @author wzp
 * @Created 2017/02/05
 */
public class AddCollectResourceAsyncTask extends AsyncTask<CollectResourceEntity, Void, CollectResourceEntity>
{
	private Context mContext;
	
	public AddCollectResourceAsyncTask(Context context) 
	{
		mContext = context;
	}

	@Override
	protected CollectResourceEntity doInBackground(CollectResourceEntity... params) 
	{
		CollectResourceEntity cre = params[0];
		CollectResourceEntity entity = HttpDao.addCollectResource( cre );
		
		if( null == entity )	//添加收藏记录失败则调用更新接口(先删除，后添加。直接调用更新接口总失败)
		{
			HttpDao.delCollectResource(cre.userName, cre.dbCode, cre.sysId);
			entity = HttpDao.addCollectResource( cre );
		}
		
		CollectResourceDBDao dao = new CollectResourceDBDao( mContext );		
		
		if( null == entity )	//如果添加或者更新收藏资源都失败了，则保存原始数据
		{
			dao.delete( cre );
			dao.insert( cre );
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
		
		// 有人建议不显示进度框，因为时间很短！
		// String s = mContext.getResources().getString(R.string.library_add_collect_resource);
		// PublicUtils.showProgress(mContext, s, this);
	}
	
	@Override
	protected void onPostExecute(CollectResourceEntity result) 
	{	
		super.onPostExecute(result);
		// PublicUtils.cancelProgress();
		
		PublicUtils.showToast(mContext, mContext.getString(R.string.library_add_collect_success), new PromptListener() {
			
			@Override
			public void onComplete() {
				((Activity) mContext).finish();
			}
		});
	}
}
