package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.activity.FavoriteResourceList;
import com.sunteam.library.entity.CollectResourceEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.PublicUtils;

/**
 * 得到收藏资源列表异步加载类
 * 
 * @author wzp
 * @Created 2017/02/15
 */
public class GetCollectResourceAsyncTask extends AsyncTask<String, Void, ArrayList<CollectResourceEntity>>
{
	private Context mContext;
	private String mTitle;
	private ArrayList<CollectResourceEntity> mCollectResourceEntityList = new ArrayList<CollectResourceEntity>();
	
	public GetCollectResourceAsyncTask(Context context, String title) 
	{
		mContext = context;
		mTitle = title;
	}

	@Override
	protected ArrayList<CollectResourceEntity> doInBackground(String... params) 
	{
		String username = PublicUtils.getUserName(mContext);
		ArrayList<CollectResourceEntity> list = HttpDao.getCollectResourceList(username);
		
		if( ( list != null ) && ( list.size() > 0 ) )
		{
			mCollectResourceEntityList.addAll(list);
			/*
			HistoryDBDao dao = new HistoryDBDao( mContext );
			dao.deleteAll();		//先删除所有数据
			dao.insert(list);		//再缓存新的数据
			dao.closeDb();			//关闭数据库
			*/
		}
		else
		{	/*
			HistoryDBDao dao = new HistoryDBDao( mContext );
			list = dao.findAll();
			dao.closeDb();			//关闭数据库
			*/
			if( ( list != null ) && ( list.size() > 0 ) )
			{
				mCollectResourceEntityList.addAll(list);
			}
		}
		
		return	mCollectResourceEntityList;
	}
	
	@Override
	protected void onPreExecute() 
	{	
		super.onPreExecute();
		PublicUtils.showProgress(mContext, this);
		String s = mContext.getResources().getString(R.string.library_wait_reading_data);
		TtsUtils.getInstance().speak(s);
	}
	
	@Override
	protected void onPostExecute(ArrayList<CollectResourceEntity> result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
		
		if(null != result && result.size() > 0)
		{
			startNextActivity();
		}
		else
		{
			String s = mContext.getResources().getString(R.string.library_reading_data_error);
			TtsUtils.getInstance().speak(s);
		}
	}

	private void startNextActivity() {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, mTitle); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, mCollectResourceEntityList); // 数据列表
		intent.setClass(mContext, FavoriteResourceList.class);
		mContext.startActivity(intent);
	}
}
