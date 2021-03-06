package com.sunteam.library.asynctask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.activity.EbookChapterList;
import com.sunteam.library.activity.ReadTxtActivity;
import com.sunteam.library.entity.BookmarkEntity;
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
public class GetEbookChapterContentAsyncTask extends AsyncTask<String, Void, Boolean>
{
	private Context mContext;
	private String mFatherPath;
	private String mTitle;
	private int mCurChapter;
	private int mTotalChapter;
	private String mContent;
	
	private String identifier;	//电子书identifier
	private String dbCode;		//数据编码
	private String sysId;		//系统id
	private String categoryName;//分类名称
	private String resourceName;//资源名称
	private String categoryCode;

	private boolean isHistory = false;	//是否是从历史记录进入
	private int offset = 0;
	
	private BookmarkEntity mBookmarkEntity = null;
	
	public GetEbookChapterContentAsyncTask(Context context, String fatherPath, String title, int curChapter, int totalChapter, BookmarkEntity entity, boolean historyFlag, int off ) 
	{
		mTitle = PublicUtils.format(title);
		
		PublicUtils.createCacheDir(fatherPath, mTitle);	//创建缓存目录
		
		mContext = context;
		mFatherPath = fatherPath+mTitle+"/";
		mCurChapter = curChapter;
		mTotalChapter = totalChapter;
		mBookmarkEntity = entity;
		isHistory = historyFlag;
		offset = off;
	}

	@Override
	protected Boolean doInBackground(String... params) 
	{
		identifier = params[0];
		String chapterIndex = params[1];
		dbCode = params[2];
		sysId = params[3];
		categoryName = params[4];
		resourceName = params[5];
		categoryCode = params[6];
		
		mContent = HttpDao.getEbookChapterContent(identifier, chapterIndex);
		if( null == mContent )
		{
			mContent = PublicUtils.readContent( mFatherPath, mTitle+LibraryConstant.CACHE_FILE_SUFFIX );
		}
		else if( TextUtils.isEmpty(mContent) )
		{
			PublicUtils.saveContent( mFatherPath, mTitle+LibraryConstant.CACHE_FILE_SUFFIX, "" );
		}
		else
		{
			PublicUtils.saveContent( mFatherPath, mTitle+LibraryConstant.CACHE_FILE_SUFFIX, mContent );
		}
		
		if(TextUtils.isEmpty(mContent))
		{
			return	false;
		}
		
		return	true;
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
	protected void onPostExecute(Boolean result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
		
		if(result)
		{
			startNextActivity();
		}
		else
		{
			String s = mContext.getResources().getString(R.string.library_reading_data_error);
			PublicUtils.showToast(mContext, s);
		}
	}

	private void startNextActivity() {
		try 
		{
			String fullpath = mFatherPath+mTitle+LibraryConstant.CACHE_FILE_SUFFIX;
			TextFileReaderUtils.getInstance().init(fullpath);
			
			Intent intent = new Intent( mContext, ReadTxtActivity.class );
			intent.putExtra("dbCode", dbCode); // 数据库代码
			intent.putExtra("sysId", sysId); // 记录标识号
			intent.putExtra(LibraryConstant.INTENT_KEY_IDENTIFIER, identifier);	//书本id
			intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.LIBRARY_DATATYPE_EBOOK); // 数据类别：电子书、有声书、口述影像
			intent.putExtra("categoryName", categoryName);	// 分类名称
			intent.putExtra("resourceName", resourceName);	// 资源名称
			intent.putExtra("categoryCode", categoryCode);
			intent.putExtra("chapterName", mTitle); 		// 章节名
			intent.putExtra("curChapter", mCurChapter); 	// 当前章节序号
			intent.putExtra("totalChapter", mTotalChapter); // 总章节数
			if( mBookmarkEntity != null )
			{
				intent.putExtra("book_mark", mBookmarkEntity);	//书签
			}
			else if( isHistory )
			{
				BookmarkEntity entity = new BookmarkEntity();
				entity.begin = offset;
				intent.putExtra("book_mark", entity);			//把历史记录变成临时书签
			}
			((EbookChapterList) mContext).startActivityForResult(intent, mCurChapter);

			mBookmarkEntity = null;
			isHistory = false;
			offset = 0;
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
