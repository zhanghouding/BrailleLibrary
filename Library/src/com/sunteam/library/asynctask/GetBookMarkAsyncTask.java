package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.activity.BookmarViewkList;
import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.PublicUtils;

/**
 * 得到书签列表异步加载类
 * 
 * @author wzp
 * @Created 2017/02/15
 */
public class GetBookMarkAsyncTask extends AsyncTask<String, Void, ArrayList<BookmarkEntity>>
{
	private Context mContext;
	private int mSelectItem;
	private String mTitle;
	private ArrayList<BookmarkEntity> mBookmarkEntityList = new ArrayList<BookmarkEntity>();
	
	public GetBookMarkAsyncTask(Context context, int select, String title) 
	{
		mContext = context;
		mSelectItem = select;
		mTitle = title;
	}

	@Override
	protected ArrayList<BookmarkEntity> doInBackground(String... params) 
	{
		String username = PublicUtils.getUserName(mContext);
		String bookId = params[0];
		ArrayList<BookmarkEntity> list = HttpDao.getBookMarkList(username, bookId);
		
		if( ( list != null ) && ( list.size() > 0 ) )
		{
			mBookmarkEntityList.addAll(list);
			/*
			HistoryDBDao dao = new HistoryDBDao( mContext );
			dao.deleteAll();		//先删除所有数据
			dao.insert(list);		//再缓存新的数据
			dao.closeDb();			//关闭数据库
			*/
		}
		else
		{	/*
			HistoryDBDao dao = new HistoryDBDao( mContext );
			list = dao.findAll();
			dao.closeDb();			//关闭数据库
			*/
			if( ( list != null ) && ( list.size() > 0 ) )
			{
				mBookmarkEntityList.addAll(list);
			}
		}
		
		return	mBookmarkEntityList;
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
	protected void onPostExecute(ArrayList<BookmarkEntity> result) 
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
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, mBookmarkEntityList); // 数据列表
		intent.putExtra("type", mSelectItem); // 区分查看书签、删除书签
		intent.setClass(mContext, BookmarViewkList.class);
		((Activity) mContext).startActivityForResult(intent, mSelectItem);
	}
}
