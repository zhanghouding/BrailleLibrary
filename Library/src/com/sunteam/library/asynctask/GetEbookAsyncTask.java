package com.sunteam.library.asynctask;

import java.util.ArrayList;

import com.sunteam.library.entity.EbookInfoEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.PublicUtils;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 得到电子图书异步加载类
 * 
 * @author wzp
 * @Created 2017/01/25
 */
public class GetEbookAsyncTask extends AsyncTask<String, Void, ArrayList<EbookInfoEntity>>
{
	private Context mContext;
	private ArrayList<EbookInfoEntity> mEbookInfoEntityList = new ArrayList<EbookInfoEntity>();
	
	public GetEbookAsyncTask(Context context) 
	{
		mContext = context;
	}

	@Override
	protected ArrayList<EbookInfoEntity> doInBackground(String... params) 
	{
		// TODO Auto-generated method stub
		ArrayList<EbookInfoEntity> list = HttpDao.getEbookList(params[0], params[1], params[2]);
		
		if( ( list != null ) && ( list.size() > 0 ) )
		{
			mEbookInfoEntityList.addAll(list);
		}
		
		return	mEbookInfoEntityList;
	}
	
	@Override
	protected void onPreExecute() 
	{	
		super.onPreExecute();
		PublicUtils.showProgress(mContext);
	}
	
	@Override
	protected void onPostExecute(ArrayList<EbookInfoEntity> result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
	}
}
