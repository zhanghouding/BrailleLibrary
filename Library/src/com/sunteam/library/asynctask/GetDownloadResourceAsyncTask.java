package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.activity.DownloadList;
import com.sunteam.library.db.DownloadChapterDBDao;
import com.sunteam.library.db.DownloadResourceDBDao;
import com.sunteam.library.entity.DownloadChapterEntity;
import com.sunteam.library.entity.DownloadResourceEntity;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 得到下载资源列表异步加载类
 * 
 * @author wzp
 * @Created 2017/02/15
 */
public class GetDownloadResourceAsyncTask extends AsyncTask<Integer, Void, Void>
{
	private Context mContext;
	private String mTitle;
	private int type;
	private ArrayList<DownloadResourceEntity> mDownloadResourceEntityList;
	
	public GetDownloadResourceAsyncTask(Context context, String title) 
	{
		mContext = context;
		mTitle = title;
	}

	@Override
	protected Void doInBackground(Integer... params) 
	{
		type = params[0];
		String username = PublicUtils.getUserName(mContext);
		
		DownloadResourceDBDao dao = new DownloadResourceDBDao( mContext );
		if( 0 == type )	//未完成
		{
			mDownloadResourceEntityList = dao.findAllUnCompleted(username);
		}
		else	//已完成
		{
			mDownloadResourceEntityList = dao.findAllCompleted(username);
		}
		dao.closeDb();
		
		if( null == mDownloadResourceEntityList )
		{
			mDownloadResourceEntityList = new ArrayList<DownloadResourceEntity>();
		}
		else
		{
			if( 0 == type )	//未完成
			{
				DownloadChapterDBDao dcDao = new DownloadChapterDBDao( mContext );
				int size = mDownloadResourceEntityList.size();
				for( int i = 0; i < size; i++ )
				{
					if( LibraryConstant.DOWNLOAD_STATUS_GOING == mDownloadResourceEntityList.get(i).status )	//如果当前这本书正在下载
					{
						ArrayList<DownloadChapterEntity> list = dcDao.findAll(mDownloadResourceEntityList.get(i)._id);	//得到所有的章节信息
						if( list != null )
						{
							int count = list.size();
							for( int j = 0; j < count; j++ )
							{
								if( list.get(j).chapterStatus == LibraryConstant.DOWNLOAD_STATUS_GOING )	//正在下载
								{
									mDownloadResourceEntityList.get(i).curDownloadChapterIndex = j;			//保存当前正在下载的章节序号
									break;
								}
							}
						}
					}
				}
				dcDao.closeDb();
			}
		}

		return	null;
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
	protected void onPostExecute(Void result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
		
		if(null != mDownloadResourceEntityList && mDownloadResourceEntityList.size() > 0)
		{
			startNextActivity();
		}
		else
		{
			String s = mContext.getResources().getString(R.string.library_downloading_empty);
			if (0 != type) {
				s = mContext.getResources().getString(R.string.library_downloaded_empty);
			}
			PublicUtils.showToast(mContext, s);
			// TtsUtils.getInstance().speak(s, TtsUtils.TTS_QUEUE_ADD);
		}
	}

	private void startNextActivity() {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, mTitle); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, mDownloadResourceEntityList); // 数据列表
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, type); // 0 下载中，1已下载
		intent.setClass(mContext, DownloadList.class);
		mContext.startActivity(intent);
	}
}
