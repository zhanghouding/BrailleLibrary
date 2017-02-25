package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;

import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.entity.EbookInfoEntity;
import com.sunteam.library.entity.EbookNodeEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 得到查找结果异步加载类
 * 
 * @author wzp
 * @Created 2017/01/25
 */
public class GetSearchResultAsyncTask extends AsyncTask<String, Void, Boolean>
{
	private Context mContext;
	private String mFatherPath;
	private String mTitle;
	private int type;
	public static ArrayList<EbookNodeEntity> mEbookNodeEntityList = new ArrayList<EbookNodeEntity>();
	
	public GetSearchResultAsyncTask(Context context, String fatherPath, String title) 
	{
		PublicUtils.createCacheDir(fatherPath, title);	//创建缓存目录
		
		mContext = context;
		mFatherPath = fatherPath+title+"/";
		mTitle = title;
	}

	@Override
	protected Boolean doInBackground(String... params) 
	{
		String pageIndex = params[0];
		String pageSize = params[1];
		String searchWord = params[2]; 
		
		EbookInfoEntity entity = HttpDao.getSearchList(pageIndex, pageSize, searchWord, LibraryConstant.LIBRARY_DATATYPE_EBOOK);
		if( ( null == entity ) || ( ( null == entity.list ) || ( 0 == entity.list.size() ) ) )
		{
			mEbookNodeEntityList.addAll(entity.list);
		}

		entity = HttpDao.getSearchList(pageIndex, pageSize, searchWord, LibraryConstant.LIBRARY_DATATYPE_AUDIO);
		if( ( null == entity ) || ( ( null == entity.list ) || ( 0 == entity.list.size() ) ) )
		{
			mEbookNodeEntityList.addAll(entity.list);
		}
		
		entity = HttpDao.getSearchList(pageIndex, pageSize, searchWord, LibraryConstant.LIBRARY_DATATYPE_VIDEO);
		if( ( null == entity ) || ( ( null == entity.list ) || ( 0 == entity.list.size() ) ) )
		{
			mEbookNodeEntityList.addAll(entity.list);
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
		
		mEbookNodeEntityList.clear();
	}
	
	@Override
	protected void onPostExecute(Boolean result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
		
		if(null != mEbookNodeEntityList && mEbookNodeEntityList.size() > 0)
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
		/*
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, mTitle); // 菜单名称
		//intent.putExtra(MenuConstant.INTENT_KEY_LIST, mEbookNodeEntityList); // 菜单名称
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, dataType); // 数据类别：电子书、有声书、口述影像
		intent.putExtra(LibraryConstant.INTENT_KEY_BOOKCOUNT, bookCount); // 资源总数
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, mFatherPath);	//父目录
		intent.putExtra(LibraryConstant.INTENT_KEY_CATEGORY_CODE, categoryCode);	//分类编码
		intent.setClass(mContext, ResourceList.class);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		mContext.startActivity(intent);
		*/
	}
}
