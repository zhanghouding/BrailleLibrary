package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.activity.ReadingHistoryList;
import com.sunteam.library.db.HistoryDBDao;
import com.sunteam.library.entity.HistoryEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.PublicUtils;

/**
 * 得到历史列表异步加载类
 * 
 * @author wzp
 * @Created 2017/02/04
 */
public class GetHistoryAsyncTask extends AsyncTask<Integer, Void, ArrayList<HistoryEntity>>
{
	private Context mContext;
	private String mTitle;
	private ArrayList<HistoryEntity> mHistoryEntityList = new ArrayList<HistoryEntity>();
	
	public GetHistoryAsyncTask(Context context, String title) 
	{
		mContext = context;
		mTitle = title;
	}

	//云端数据和本地数据融合同步。(list1是云端数据，list2是本地数据)
	public ArrayList<HistoryEntity> dataFuse( ArrayList<HistoryEntity> list1, ArrayList<HistoryEntity> list2 )
	{
		ArrayList<HistoryEntity> list = new ArrayList<HistoryEntity>();
		
		int size2 = list2.size();	//本地数据量
		for( int i = 0; i < size2; i++ )
		{
			HistoryEntity entity2 = list2.get(i);
			for( int j = 0; j < list1.size(); j++ )
			{
				HistoryEntity entity1 = list1.get(j);
				if( entity2.id == entity1.id )
				{
					//说明本地数据已经同步到云端了
					list1.remove(j);
					break;
				}
				else if( entity2.dbCode.equals(entity1.dbCode) && entity2.sysId.equals(entity1.sysId) )
				{
					//如果云端数据和本地数据都有此资源历史记录，则以本地为准
					HttpDao.delHistory(PublicUtils.getUserName(mContext), entity1.dbCode, entity1.sysId);	//删除网络端数据
					list1.remove(j);
					break;
				}
			}
			
			if( entity2.id > 0 )	//已经同步过的数据
			{
				list.add(entity2);
			}
			else
			{
				HistoryEntity entity = HttpDao.addHistory( entity2 );	//将本地数据同步到网络端
				if( null == entity )
				{
					list.add(entity2);	//添加本地数据
				}
				else
				{
					list.add(entity);	//添加网络数据
				}
			}
		}
		
		list.addAll(list1);	//添加网络端数据
		
		return	list;
	}
	
	@Override
	protected ArrayList<HistoryEntity> doInBackground(Integer... params) 
	{
		String username = PublicUtils.getUserName(mContext);
		
		ArrayList<HistoryEntity> list1 = HttpDao.getHistoryList(username, params[0], params[1] );	//从云端得到历史记录
		HistoryDBDao dao = new HistoryDBDao( mContext );
		ArrayList<HistoryEntity> list2 = dao.findAll(username);				//从本地得到历史记录
		
		if( ( list1 != null ) && ( list1.size() > 0 ) )			//如果云端数据不为空
		{
			if( ( list2 != null ) && ( list2.size() > 0 ) )
			{
				//做云端数据和本地数据的数据融合
				ArrayList<HistoryEntity> list = dataFuse( list1, list2 );	//数据融合
				dao.deleteAll(username);		//先删除所有数据
				dao.insertDescending(list);		//再缓存新的数据
				
				mHistoryEntityList.addAll(list);
			}
			else
			{
				dao.insertDescending(list1);	//保存云端数据
				mHistoryEntityList.addAll(list1);
			}
		}
		else if( ( list2 != null ) && ( list2.size() > 0 ) )	//如果本地数据不为空
		{
			mHistoryEntityList.addAll(list2);
		}
		
		dao.closeDb();			//关闭数据库
		
		return	mHistoryEntityList;
	}
	
	@Override
	protected void onPreExecute() 
	{	
		super.onPreExecute();
		PublicUtils.showProgress(mContext);
		String s = mContext.getResources().getString(R.string.library_wait_reading_data);
		TtsUtils.getInstance().speak(s);
	}
	
	@Override
	protected void onPostExecute(ArrayList<HistoryEntity> result) 
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
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, mHistoryEntityList); // 数据列表
		intent.setClass(mContext, ReadingHistoryList.class);
		mContext.startActivity(intent);
	}
}
