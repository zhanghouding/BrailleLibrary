package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.activity.ResourceOnlineList;
import com.sunteam.library.db.ResourceDBDao;
import com.sunteam.library.entity.EbookInfoEntity;
import com.sunteam.library.entity.EbookNodeEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 得到电子图书异步加载类
 * 
 * @author wzp
 * @Created 2017/01/25
 */
public class GetEbookAsyncTask extends AsyncTask<String, Void, Boolean>
{
	private Context mContext;
	private String mFatherPath;
	private String mTitle;
	private int dataType;
	private int bookCount = 0; // 资源总数，即当前分类下的书本总数
	private ArrayList<EbookNodeEntity> mEbookNodeEntityList = new ArrayList<EbookNodeEntity>();
	
	public GetEbookAsyncTask(Context context, String fatherPath, String title) 
	{
		PublicUtils.createCacheDir(fatherPath, title);	//创建缓存目录
		
		mContext = context;
		mFatherPath = fatherPath+title+"/";
		mTitle = title;
	}

	@Override
	protected Boolean doInBackground(String... params) 
	{
		dataType = Integer.parseInt(params[3]);
		EbookInfoEntity entity = HttpDao.getEbookList(params[0], params[1], params[2], dataType);
	
		if( ( null == entity ) || ( ( null == entity.list ) || ( 0 == entity.list.size() ) ) )
		{
			ResourceDBDao dao = new ResourceDBDao( mContext );
			ArrayList<EbookNodeEntity> list = dao.findAll(params[2], dataType);
			dao.closeDb();			//关闭数据库
			
			if( ( list != null ) && ( list.size() > 0 ) )
			{
				bookCount = list.size();
				mEbookNodeEntityList.addAll(list);
				
				return	true;
			}
			
			return	false;
		}
		
		bookCount = entity.itemCount;
		
		if( ( entity.list != null ) && ( entity.list.size() > 0 ) )
		{
			mEbookNodeEntityList.addAll(entity.list);
			
			ResourceDBDao dao = new ResourceDBDao( mContext );
			dao.deleteAll(params[2], dataType);			//先删除缓存的此类型所有数据
			dao.insert(entity.list, params[2], dataType);	//再缓存新的数据
			dao.closeDb();			//关闭数据库
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
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, mTitle); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, mEbookNodeEntityList); // 菜单名称
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, dataType); // 数据类别：电子书、有声书、口述影像
		intent.putExtra(LibraryConstant.INTENT_KEY_BOOKCOUNT, bookCount); // 资源总数
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, mFatherPath);	//父目录
		intent.setClass(mContext, ResourceOnlineList.class);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		mContext.startActivity(intent);
	}
}
