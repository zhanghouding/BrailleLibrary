package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;

import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.entity.CategoryInfoNodeEntity;
import com.sunteam.library.entity.EbookInfoEntity;
import com.sunteam.library.entity.EbookNodeEntity;
import com.sunteam.library.listener.LibraryResultListener;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 得到查找结果异步加载类
 * 
 * @author wzp
 * @Created 2017/01/25
 */
public class GetSearchResultAsyncTask extends AsyncTask<String, Void, Boolean>
{
	private Context mContext;
	private ArrayList<EbookNodeEntity> mEbookNodeEntityList = new ArrayList<EbookNodeEntity>();
	private LibraryResultListener mListener;
	
	public GetSearchResultAsyncTask(Context context, LibraryResultListener listener ) 
	{
		mContext = context;
		mListener = listener;
	}

	private void search( String pageIndex, String pageSize, String searchWord, int resType )
	{
		EbookInfoEntity entity = HttpDao.getSearchList(pageIndex, pageSize, searchWord, resType);
		if( ( null != entity ) && ( ( null != entity.list ) && ( 0 != entity.list.size() ) ) )
		{
			mEbookNodeEntityList.addAll(entity.list);
			
			ArrayList<CategoryInfoNodeEntity> list = HttpDao.getCategoryInfoList(resType);
			if( ( list != null) && ( list.size() > 0 ) )
			{
				int size1 = entity.list.size();
				int size2 = list.size();
				String categoryName = PublicUtils.getCategoryName(mContext, resType);
				for( int i = 0; i < size1; i++ )
				{
					entity.list.get(i).resType = resType;
					for( int j = 0; j < size2; j++ )
					{
						if( entity.list.get(i).categoryCode.contains(list.get(j).code) )
						{
							entity.list.get(i).categoryName = list.get(j).name;
							entity.list.get(i).categoryFullName = categoryName + "-" + entity.list.get(i).categoryName + "-" + entity.list.get(i).title;
						}
					}
				}
			}
		}
	}
	
	@Override
	protected Boolean doInBackground(String... params) 
	{
		String pageIndex = params[0];
		String pageSize = params[1];
		String searchWord = params[2]; 
		
		search(pageIndex, pageSize, searchWord, LibraryConstant.LIBRARY_DATATYPE_EBOOK);
		search(pageIndex, pageSize, searchWord, LibraryConstant.LIBRARY_DATATYPE_AUDIO);
		search(pageIndex, pageSize, searchWord, LibraryConstant.LIBRARY_DATATYPE_VIDEO);
		
		return	true;
	}
	
	@Override
	protected void onPreExecute() 
	{	
		super.onPreExecute();
		PublicUtils.showProgress(mContext, this);
		String s = mContext.getResources().getString(R.string.library_wait_reading_data);
		TtsUtils.getInstance().speak(s);
		
		mEbookNodeEntityList.clear();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void onPostExecute(Boolean result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
		
		if(null != mEbookNodeEntityList && mEbookNodeEntityList.size() > 0)
		{
			if(null != mListener){
				mListener.onResult((ArrayList)mEbookNodeEntityList);
			}
		}
		else
		{
			if(null != mListener){
				mListener.onFail(null);
			}
		}
	}

}
