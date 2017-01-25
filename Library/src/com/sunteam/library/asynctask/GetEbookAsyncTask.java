package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.activity.ResourceOnlineList;
import com.sunteam.library.entity.EbookInfoEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 得到电子图书异步加载类
 * 
 * @author wzp
 * @Created 2017/01/25
 */
public class GetEbookAsyncTask extends AsyncTask<String, Void, ArrayList<EbookInfoEntity>>
{
	private Context mContext;
	private String mTitle;
	private int dataType;
	private ArrayList<EbookInfoEntity> mEbookInfoEntityList = new ArrayList<EbookInfoEntity>();
	
	public GetEbookAsyncTask(Context context, String title) 
	{
		mContext = context;
		mTitle = title;
	}

	@Override
	protected ArrayList<EbookInfoEntity> doInBackground(String... params) 
	{
		dataType = Integer.parseInt(params[3]);
		ArrayList<EbookInfoEntity> list = HttpDao.getEbookList(params[0], params[1], params[2], dataType);
		
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
		String s = mContext.getResources().getString(R.string.library_wait_reading_data);
		TtsUtils.getInstance().speak(s);
	}
	
	@Override
	protected void onPostExecute(ArrayList<EbookInfoEntity> result) 
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
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, mEbookInfoEntityList); // 菜单名称
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, dataType); // 数据类别：电子书、有声书、口述影像

		intent.setClass(mContext, ResourceOnlineList.class);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		mContext.startActivity(intent);
	}
}
