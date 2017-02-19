package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.activity.VideoChapterList;
import com.sunteam.library.db.ChapterDBDao;
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
	private String dbCode;			//数据库编码
	private String sysId;			//系统id
	private String identifier;		//书本id
	private String categoryName;	//分类名称
	private ArrayList<VideoChapterInfoEntity> mVideoChapterInfoEntityList = new ArrayList<VideoChapterInfoEntity>();
	private boolean isHistory = false;	//是否是从历史记录进入
	private int lastChapterIndex = 0;
	private int offset = 0;
	
	private void init(Context context, String fatherPath, String title)
	{
		PublicUtils.createCacheDir(fatherPath, title);	//创建缓存目录
		
		mContext = context;
		mFatherPath = fatherPath+title+"/";
		mTitle = title;
	}
	
	public GetVideoChapterAsyncTask(Context context, String fatherPath, String title) 
	{
		init( context, fatherPath, title );
	}
	
	public GetVideoChapterAsyncTask(Context context, String fatherPath, String title, int chapterIndex, int begin) 
	{
		init( context, fatherPath, title );
		lastChapterIndex = chapterIndex;
		offset = begin;
		isHistory = true;
	}

	@Override
	protected ArrayList<VideoChapterInfoEntity> doInBackground(String... params) 
	{
		dbCode = params[0];
		sysId = params[1];
		categoryName = params[2];
		identifier = params[3];
		ArrayList<VideoChapterInfoEntity> list = HttpDao.getVideoChapterList(dbCode, sysId);
		
		if( ( list != null ) && ( list.size() > 0 ) )
		{
			mVideoChapterInfoEntityList.addAll(list);
			
			ChapterDBDao dao = new ChapterDBDao( mContext );
			dao.deleteAllVideoChapter(dbCode, sysId);			//先删除缓存的此类型所有数据
			dao.insertVideoChapterInfo(list,dbCode, sysId);		//再缓存新的数据
			dao.closeDb();			//关闭数据库
		}
		else
		{
			ChapterDBDao dao = new ChapterDBDao( mContext );
			list = dao.findAllVideoChapter(dbCode, sysId);
			dao.closeDb();			//关闭数据库
			
			if( ( list != null ) && ( list.size() > 0 ) )
			{
				mVideoChapterInfoEntityList.addAll(list);
			}
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
		intent.putExtra(LibraryConstant.INTENT_KEY_DBCODE, dbCode);	//数据编码
		intent.putExtra(LibraryConstant.INTENT_KEY_SYSID, sysId);	//系统id
		intent.putExtra(LibraryConstant.INTENT_KEY_IDENTIFIER, identifier);	//书本id
		intent.putExtra(LibraryConstant.INTENT_KEY_CATEGORY_NAME, categoryName);	//分类名称
		intent.putExtra("isHistory", isHistory);
		intent.putExtra("lastChapterIndex", lastChapterIndex);
		intent.putExtra("offset", offset);
		intent.setClass(mContext, VideoChapterList.class);
		mContext.startActivity(intent);
	}
}
