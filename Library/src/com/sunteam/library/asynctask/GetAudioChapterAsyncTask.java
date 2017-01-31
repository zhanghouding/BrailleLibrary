package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.activity.AudioOnlineChapterList;
import com.sunteam.library.db.ChapterDBDao;
import com.sunteam.library.entity.AudioChapterInfoEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 得到有声书章节异步加载类
 * 
 * @author wzp
 * @Created 2017/01/25
 */
public class GetAudioChapterAsyncTask extends AsyncTask<String, Void, ArrayList<AudioChapterInfoEntity>>
{
	private Context mContext;
	private String mFatherPath;
	private String mTitle;
	private ArrayList<AudioChapterInfoEntity> mAudioChapterInfoEntityList = new ArrayList<AudioChapterInfoEntity>();
	
	public GetAudioChapterAsyncTask(Context context, String fatherPath, String title) 
	{
		PublicUtils.createCacheDir(fatherPath, title);	//创建缓存目录
		
		mContext = context;
		mFatherPath = fatherPath+title+"/";
		mTitle = title;
	}

	@Override
	protected ArrayList<AudioChapterInfoEntity> doInBackground(String... params) 
	{
		ArrayList<AudioChapterInfoEntity> list = HttpDao.getAudioChapterList(params[0], params[1]);
		
		if( ( list != null ) && ( list.size() > 0 ) )
		{
			mAudioChapterInfoEntityList.addAll(list);
			
			ChapterDBDao dao = new ChapterDBDao( mContext );
			dao.deleteAll(LibraryConstant.LIBRARY_DATATYPE_AUDIO);			//先删除缓存的此类型所有数据
			dao.insert(list, LibraryConstant.LIBRARY_DATATYPE_AUDIO);		//再缓存新的数据
			dao.closeDb();			//关闭数据库
		}
		
		return	mAudioChapterInfoEntityList;
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
	protected void onPostExecute(ArrayList<AudioChapterInfoEntity> result) 
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
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, mAudioChapterInfoEntityList); // 数据列表
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.LIBRARY_DATATYPE_AUDIO); // 数据类别：电子书、有声书、口述影像
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, mFatherPath);	//父目录
		intent.setClass(mContext, AudioOnlineChapterList.class);
		mContext.startActivity(intent);
	}
}
