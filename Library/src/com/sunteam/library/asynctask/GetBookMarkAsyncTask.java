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
import com.sunteam.library.db.BookMarkDBDao;
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

	//云端数据和本地数据融合同步。(list1是云端数据，list2是本地数据)
	public ArrayList<BookmarkEntity> dataFuse( ArrayList<BookmarkEntity> list1, ArrayList<BookmarkEntity> list2 )
	{
		ArrayList<BookmarkEntity> list = new ArrayList<BookmarkEntity>();
		
		int size2 = list2.size();	//本地数据量
		for( int i = 0; i < size2; i++ )
		{
			BookmarkEntity entity2 = list2.get(i);
			for( int j = 0; j < list1.size(); j++ )
			{
				BookmarkEntity entity1 = list1.get(j);
				if( entity2.id == entity1.id )
				{
					//说明本地数据已经同步到云端了
					list1.remove(j);
					break;
				}
				else if( entity2.bookId.equals(entity1.bookId) && ( entity2.begin == entity1.begin ) )
				{
					//如果云端数据和本地数据都有此资源书签记录，则以本地为准
					HttpDao.delBookMark(entity1.userName, entity1.id+"");	//删除网络端数据
					list1.remove(j);
					break;
				}
			}
			
			if( entity2.id > 0 )	//已经同步过的数据
			{
				list.add(entity2);
			}
			else
			{
				BookmarkEntity entity = HttpDao.addBookMark( entity2 );	//将本地数据同步到网络端
				if( null == entity )
				{
					list.add(entity2);	//添加本地数据
				}
				else
				{
					list.add(entity);	//添加网络数据
				}
			}
		}
		
		list.addAll(list1);	//添加网络端数据
		
		return	list;
	}
	
	@Override
	protected ArrayList<BookmarkEntity> doInBackground(String... params) 
	{
		String username = PublicUtils.getUserName(mContext);
		String bookId = params[0];
		
		ArrayList<BookmarkEntity> list1 = HttpDao.getBookMarkList(username, bookId );	//从云端得到书签记录
		BookMarkDBDao dao = new BookMarkDBDao( mContext );
		ArrayList<BookmarkEntity> list2 = dao.findAll(username, bookId);				//从本地得到书签记录
		
		if( ( list1 != null ) && ( list1.size() > 0 ) )			//如果云端数据不为空
		{
			if( ( list2 != null ) && ( list2.size() > 0 ) )
			{
				//做云端数据和本地数据的数据融合
				ArrayList<BookmarkEntity> list = dataFuse( list1, list2 );	//数据融合
				dao.deleteAll(username);		//先删除所有数据
				dao.insertDescending(list);		//再缓存新的数据
				
				mBookmarkEntityList.addAll(list);
			}
			else
			{
				dao.insertDescending(list1);	//保存云端数据
				mBookmarkEntityList.addAll(list1);
			}
		}
		else if( ( list2 != null ) && ( list2.size() > 0 ) )	//如果本地数据不为空
		{
			mBookmarkEntityList.addAll(list2);
		}
		
		dao.closeDb();			//关闭数据库
		
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
			PublicUtils.showToast(mContext, s, TtsUtils.TTS_QUEUE_ADD); // 有可能没有书签，很快就打断了加载提示信息
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
