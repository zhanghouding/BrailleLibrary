package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.activity.ResourceListForRecommend;
import com.sunteam.library.entity.EbookInfoEntity;
import com.sunteam.library.entity.EbookNodeEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 得到推荐资源异步加载类
 * 
 * @author wzp
 * @Created 2017/01/25
 */
public class GetRecommendAsyncTask extends AsyncTask<Integer, Void, Boolean>
{
	private Context mContext;
	private String mFatherPath;
	private String mTitle;
	private int type; // 个性推荐、最新更新、精品专区
	private int bookCount = 0; // 资源总数，即当前分类下的书本总数
	public static ArrayList<EbookNodeEntity> mEbookNodeEntityList = new ArrayList<EbookNodeEntity>();
	
	public GetRecommendAsyncTask(Context context, String fatherPath, String title) 
	{
		PublicUtils.createCacheDir(fatherPath, title);	//创建缓存目录
		
		mContext = context;
		mFatherPath = fatherPath+title+"/";
		mTitle = title;
	}

	@Override
	protected Boolean doInBackground(Integer... params) 
	{
		type = params[0];
		String username = PublicUtils.getUserName();
		EbookInfoEntity entity = HttpDao.getRecommendList(params[0], username);
	
		if( ( null == entity ) || ( ( null == entity.list ) || ( 0 == entity.list.size() ) ) )
		{
			/*
			ResourceDBDao dao = new ResourceDBDao( mContext );
			ArrayList<EbookNodeEntity> list = dao.findAll(categoryCode, dataType);
			dao.closeDb();			//关闭数据库
			
			if( ( list != null ) && ( list.size() > 0 ) )
			{
				bookCount = list.size();
				mEbookNodeEntityList.addAll(list);
				
				return	true;
			}
			*/
			return	false;
		}
		
		bookCount = entity.itemCount;
		
		if( ( entity.list != null ) && ( entity.list.size() > 0 ) )
		{
			mEbookNodeEntityList.addAll(entity.list);
			/*
			ResourceDBDao dao = new ResourceDBDao( mContext );
			dao.deleteAll(categoryCode, dataType);			//先删除缓存的此类型所有数据
			dao.insert(entity.list, categoryCode, dataType);	//再缓存新的数据
			dao.closeDb();			//关闭数据库
			*/
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
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, mTitle); // 菜单名称
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, mEbookNodeEntityList); // 菜单名称
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, type); // 数据类别：个性推荐、最新更新、精品专区
		intent.putExtra(LibraryConstant.INTENT_KEY_BOOKCOUNT, bookCount); // 资源总数
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, mFatherPath);	//父目录
		intent.setClass(mContext, ResourceListForRecommend.class);

		mContext.startActivity(intent);
	}
}
