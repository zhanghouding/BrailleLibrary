package com.sunteam.library.asynctask;

import java.util.ArrayList;

import com.sunteam.library.entity.EbookChapterInfoEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.PublicUtils;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 得到电子图书章节异步加载类
 * 
 * @author wzp
 * @Created 2017/01/25
 */
public class GetEbookChapterAsyncTask extends AsyncTask<String, Void, ArrayList<EbookChapterInfoEntity>>
{
	private Context mContext;
	private ArrayList<EbookChapterInfoEntity> mEbookChapterInfoEntityList = new ArrayList<EbookChapterInfoEntity>();
	
	public GetEbookChapterAsyncTask(Context context) 
	{
		mContext = context;
	}

	@Override
	protected ArrayList<EbookChapterInfoEntity> doInBackground(String... params) 
	{
		// TODO Auto-generated method stub
		ArrayList<EbookChapterInfoEntity> list = HttpDao.getEbookChapterList(params[0]);
		
		if( ( list != null ) && ( list.size() > 0 ) )
		{
			mEbookChapterInfoEntityList.addAll(list);
		}
		
		return	mEbookChapterInfoEntityList;
	}
	
	@Override
	protected void onPreExecute() 
	{	
		super.onPreExecute();
		PublicUtils.showProgress(mContext);
	}
	
	@Override
	protected void onPostExecute(ArrayList<EbookChapterInfoEntity> result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
	}
}
