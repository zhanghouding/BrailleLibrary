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
		mContext = context;
		mFatherPath = fatherPath+title+"/";
		mTitle = title;
	}

	@Override
	protected Boolean doInBackground(Integer... params) 
	{
		type = params[0];
		String username = PublicUtils.getUserName(mContext);
		EbookInfoEntity entity = HttpDao.getRecommendList(params[0], username);
	
		if( ( null == entity ) || ( ( null == entity.list ) || ( 0 == entity.list.size() ) ) )
		{
			return	false;
		}
		
		bookCount = entity.itemCount;
		
		if( ( entity.list != null ) && ( entity.list.size() > 0 ) )
		{
			mEbookNodeEntityList.addAll(entity.list);
			
			int size1 = entity.list.size();
			for( int i = 0; i < size1; i++ )
			{
				String type = entity.list.get(i).dbCode.toLowerCase();
				int resType = LibraryConstant.LIBRARY_DATATYPE_EBOOK;
				if(type.contains(LibraryConstant.LIBRARY_DBCODE_EBOOK))
				{
					resType = LibraryConstant.LIBRARY_DATATYPE_EBOOK;
				} 
				else if(type.contains(LibraryConstant.LIBRARY_DBCODE_AUDIO))
				{
					resType = LibraryConstant.LIBRARY_DATATYPE_AUDIO;
				} 
				else if(type.contains(LibraryConstant.LIBRARY_DBCODE_VIDEO))
				{
					resType = LibraryConstant.LIBRARY_DATATYPE_VIDEO;
				}
				
				entity.list.get(i).resType = resType;
				entity.list.get(i).categoryName = HttpDao.getCategoryName( resType, entity.list.get(i).categoryCode );
				entity.list.get(i).categoryFullName = PublicUtils.getCategoryName(mContext, resType) + "-" + entity.list.get(i).categoryName + "-" + entity.list.get(i).title;
			}
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
			PublicUtils.showToast(mContext, s);
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
