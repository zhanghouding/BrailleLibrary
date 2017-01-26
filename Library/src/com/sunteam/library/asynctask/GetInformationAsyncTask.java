package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.activity.LibraryNewsList;
import com.sunteam.library.activity.ResourceOnlineList;
import com.sunteam.library.entity.InformationEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 得到盲人咨询异步加载类
 * 
 * @author wzp
 * @Created 2017/01/25
 */
public class GetInformationAsyncTask extends AsyncTask<Integer, Void, Boolean>
{
	private Context mContext;
	private String mTitle;
	private int infoType;
	private ArrayList<InformationEntity> mInformationEntityList;
	
	public GetInformationAsyncTask(Context context, String title) 
	{
		mContext = context;
		mTitle = title;
	}

	@Override
	protected Boolean doInBackground(Integer... params) 
	{
		infoType = params[2];
		mInformationEntityList = HttpDao.getInformationList(params[0], params[1], infoType);
	
		if( ( null == mInformationEntityList ) || ( 0 == mInformationEntityList.size() ) )
		{
			return	false;
		}
		
		return	true;
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
	protected void onPostExecute(Boolean result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
		
		if(null != mInformationEntityList && mInformationEntityList.size() > 0)
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
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, mInformationEntityList); // 数据列表
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, infoType); // 盲人资讯数据类别：新闻公告、服务资讯、文化活动
		intent.setClass(mContext, LibraryNewsList.class);

		mContext.startActivity(intent);
	}
}
