package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.activity.VideoOnlineChapterList;
import com.sunteam.library.entity.VideoChapterInfoEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 得到口述影像章节异步加载类
 * 
 * @author wzp
 * @Created 2017/01/25
 */
public class GetVideoChapterAsyncTask extends AsyncTask<String, Void, ArrayList<VideoChapterInfoEntity>>
{
	private Context mContext;
	private String mFatherPath;
	private String mTitle;
	private ArrayList<VideoChapterInfoEntity> mVideoChapterInfoEntityList = new ArrayList<VideoChapterInfoEntity>();
	
	public GetVideoChapterAsyncTask(Context context, String fatherPath, String title) 
	{
		PublicUtils.createCacheDir(fatherPath, title);	//创建缓存目录
		
		mContext = context;
		mFatherPath = fatherPath+title+"/";
		mTitle = title;
	}

	@Override
	protected ArrayList<VideoChapterInfoEntity> doInBackground(String... params) 
	{
		ArrayList<VideoChapterInfoEntity> list = HttpDao.getVideoChapterList(params[0], params[1]);
		
		if( ( list != null ) && ( list.size() > 0 ) )
		{
			mVideoChapterInfoEntityList.addAll(list);
		}
		
		return	mVideoChapterInfoEntityList;
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
	protected void onPostExecute(ArrayList<VideoChapterInfoEntity> result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
		
		if(null != result && result.size() > 0)
		{
			startNextActivity();
		}
	}

	private void startNextActivity() {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, mTitle); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, mVideoChapterInfoEntityList); // 数据列表
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.LIBRARY_DATATYPE_VIDEO); // 数据类别：电子书、有声书、口述影像
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, mFatherPath);	//父目录
		intent.setClass(mContext, VideoOnlineChapterList.class);
		mContext.startActivity(intent);
	}
}
