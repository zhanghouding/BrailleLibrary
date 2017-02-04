package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;

import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
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
public class GetHistoryAsyncTask extends AsyncTask<String, Void, ArrayList<HistoryEntity>>
{
	private Context mContext;
	private String mTitle;
	private ArrayList<HistoryEntity> mHistoryEntityList = new ArrayList<HistoryEntity>();
	
	public GetHistoryAsyncTask(Context context, String title) 
	{
		mContext = context;
		mTitle = title;
	}

	@Override
	protected ArrayList<HistoryEntity> doInBackground(String... params) 
	{
		ArrayList<HistoryEntity> list = HttpDao.getHistoryList(params[0]);
		
		if( ( list != null ) && ( list.size() > 0 ) )
		{
			mHistoryEntityList.addAll(list);
			
			HistoryDBDao dao = new HistoryDBDao( mContext );
			dao.deleteAll();		//先删除所有数据
			dao.insert(list);		//再缓存新的数据
			dao.closeDb();			//关闭数据库
		}
		else
		{
			HistoryDBDao dao = new HistoryDBDao( mContext );
			list = dao.findAll();
			dao.closeDb();			//关闭数据库
			
			if( ( list != null ) && ( list.size() > 0 ) )
			{
				mHistoryEntityList.addAll(list);
			}
		}
		
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
		/*
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, mTitle); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, mHistoryEntityList); // 数据列表
		intent.putExtra(LibraryConstant.INTENT_KEY_IDENTIFIER, identifier);
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.LIBRARY_DATATYPE_EBOOK); // 数据类别：电子书、有声书、口述影像
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, mFatherPath);	//父目录
		intent.setClass(mContext, EbookOnlineChapterList.class);
		mContext.startActivity(intent);
		*/
	}
}