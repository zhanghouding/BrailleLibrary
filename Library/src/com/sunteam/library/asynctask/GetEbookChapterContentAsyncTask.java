package com.sunteam.library.asynctask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.activity.ReadTxtActivity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;
import com.sunteam.library.utils.TextFileReaderUtils;

/**
 * 得到电子图书章节内容异步加载类
 * 
 * @author wzp
 * @Created 2017/01/29
 */
public class GetEbookChapterContentAsyncTask extends AsyncTask<String, Void, String>
{
	private Context mContext;
	private String mFatherPath;
	private String mTitle;
	private String mContent;
	
	public GetEbookChapterContentAsyncTask(Context context, String fatherPath, String title) 
	{
		PublicUtils.createCacheDir(fatherPath, title);	//创建缓存目录
		
		mContext = context;
		mFatherPath = fatherPath+title+"/";
		mTitle = title;
	}

	@Override
	protected String doInBackground(String... params) 
	{
		mContent = HttpDao.getEbookChapterContent(params[0], params[1]);
		if( TextUtils.isEmpty(mContent) )
		{
			PublicUtils.saveContent( mFatherPath, mTitle+LibraryConstant.CACHE_FILE_SUFFIX, "" );
		}
		else
		{
			PublicUtils.saveContent( mFatherPath, mTitle+LibraryConstant.CACHE_FILE_SUFFIX, mContent );
		}
		
		return	mContent;
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
	protected void onPostExecute(String result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
		
		if(!TextUtils.isEmpty(mContent))
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
		try 
		{
			String fullpath = mFatherPath+mTitle+LibraryConstant.CACHE_FILE_SUFFIX;
			TextFileReaderUtils.getInstance().init(fullpath);
			
			Intent intent = new Intent( mContext, ReadTxtActivity.class );
			intent.putExtra("filename", mTitle);
			mContext.startActivity(intent);
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
