package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.activity.EbookOnlineActivity;
import com.sunteam.library.entity.EbookChapterInfoEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 得到电子图书章节异步加载类
 * 
 * @author wzp
 * @Created 2017/01/25
 */
public class GetEbookChapterAsyncTask extends AsyncTask<String, Void, ArrayList<EbookChapterInfoEntity>>
{
	private Context mContext;
	String mTitle;
	private ArrayList<EbookChapterInfoEntity> mEbookChapterInfoEntityList = new ArrayList<EbookChapterInfoEntity>();
	
	public GetEbookChapterAsyncTask(Context context, String title) 
	{
		mContext = context;
		mTitle = title;
	}

	@Override
	protected ArrayList<EbookChapterInfoEntity> doInBackground(String... params) 
	{
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
		String s = mContext.getResources().getString(R.string.library_wait_reading_data);
		TtsUtils.getInstance().speak(s);
	}
	
	@Override
	protected void onPostExecute(ArrayList<EbookChapterInfoEntity> result) 
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
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, mEbookChapterInfoEntityList); // 数据列表
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.LIBRARY_DATATYPE_EBOOK); // 数据类别：电子书、有声书、口述影像
		intent.setClass(mContext, EbookOnlineActivity.class);
		mContext.startActivity(intent);
	}
}
