package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.activity.EbookChapterList;
import com.sunteam.library.db.ChapterDBDao;
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
	private String mFatherPath;
	private String mTitle;
	private String dbCode;			//数据库编码
	private String sysId;			//系统id
	private String categoryName;	//分类名称
	private String identifier;
	private ArrayList<EbookChapterInfoEntity> mEbookChapterInfoEntityList = new ArrayList<EbookChapterInfoEntity>();
	
	public GetEbookChapterAsyncTask(Context context, String fatherPath, String title) 
	{
		PublicUtils.createCacheDir(fatherPath, title);	//创建缓存目录
		
		mContext = context;
		mFatherPath = fatherPath+title+"/";
		mTitle = title;
	}

	@Override
	protected ArrayList<EbookChapterInfoEntity> doInBackground(String... params) 
	{
		dbCode = params[0];
		sysId = params[1];
		categoryName = params[2];
		identifier = params[3];
		ArrayList<EbookChapterInfoEntity> list = HttpDao.getEbookChapterList(dbCode, identifier);
		
		if( ( list != null ) && ( list.size() > 0 ) )
		{
			mEbookChapterInfoEntityList.addAll(list);
			
			ChapterDBDao dao = new ChapterDBDao( mContext );
			dao.deleteAllEbookChapter(dbCode, identifier);			//先删除缓存的此类型所有数据
			dao.insertEbookChapterInfo(list,dbCode, identifier);		//再缓存新的数据
			dao.closeDb();			//关闭数据库
		}
		else
		{
			ChapterDBDao dao = new ChapterDBDao( mContext );
			list = dao.findAllEbookChapter(dbCode, identifier);
			dao.closeDb();			//关闭数据库
			
			if( ( list != null ) && ( list.size() > 0 ) )
			{
				mEbookChapterInfoEntityList.addAll(list);
			}
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
		intent.putExtra(LibraryConstant.INTENT_KEY_IDENTIFIER, identifier);
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.LIBRARY_DATATYPE_EBOOK); // 数据类别：电子书、有声书、口述影像
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, mFatherPath);	//父目录
		intent.putExtra(LibraryConstant.INTENT_KEY_DBCODE, dbCode);	//数据编码
		intent.putExtra(LibraryConstant.INTENT_KEY_SYSID, sysId);	//系统id
		intent.putExtra(LibraryConstant.INTENT_KEY_CATEGORY_NAME, categoryName);	//分类名称
		intent.setClass(mContext, EbookChapterList.class);
		mContext.startActivity(intent);
	}
}
