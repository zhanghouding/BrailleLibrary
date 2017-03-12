package com.sunteam.library.download;

import java.util.ArrayList;

import android.content.Context;
import android.os.Process;

import com.sunteam.library.db.DownloadChapterDBDao;
import com.sunteam.library.entity.DownloadChapterEntity;
import com.sunteam.library.entity.DownloadResourceEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 下载电子图书
 * 
 * @author wzp
 * @Created 2017/01/29
 */
public class DownloadEbook extends Thread
{
	private static OnDownloadEbookStatusListener mOnDownloadEbookStatusListener;	//监听器
	private Context mContext;
	private DownloadResourceEntity mDownloadResourceEntity;
	
	public interface OnDownloadEbookStatusListener 
	{
		public void onDownloadEbookCompleted();		//下载电子书完成
		public void onDownloadEbookError();			//下载电子书错误
		public void onDownloadEbookProgress(int index, int status);	//下载进度
	}
	
	public static void registerDownloadStatusListener( OnDownloadEbookStatusListener listener )
	{
		mOnDownloadEbookStatusListener = listener;
	}
	
	public static void unregisterDownloadStatusListener()
	{
		mOnDownloadEbookStatusListener = null;
	}
	
	public DownloadEbook( Context context, DownloadResourceEntity entity )
	{
		mContext = context;
		mDownloadResourceEntity = entity;
	}
	
	@Override
	public void run()
	{
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

		DownloadChapterDBDao dao = new DownloadChapterDBDao( mContext );
		ArrayList<DownloadChapterEntity> list = dao.findAll(mDownloadResourceEntity._id);
		
		if( list != null )
		{
			for( int i = 0; i < list.size(); i++ )
			{
				DownloadChapterEntity entity = list.get(i);
				if( mOnDownloadEbookStatusListener != null )
				{
					mOnDownloadEbookStatusListener.onDownloadEbookProgress(i, LibraryConstant.DOWNLOAD_STATUS_GOING);
				}
				
				PublicUtils.createCacheDir(entity.chapterPath, entity.chapterName);	//创建缓存目录
				final String faterPath = entity.chapterPath + entity.chapterName+"/";
				
				String content = HttpDao.getEbookChapterContent(mDownloadResourceEntity.identifier, entity.chapterIndex+"");
				if( content != null)
				{
					PublicUtils.saveContent( faterPath, PublicUtils.format(entity.chapterName)+LibraryConstant.CACHE_FILE_SUFFIX, content );
					
					if( mOnDownloadEbookStatusListener != null )
					{
						mOnDownloadEbookStatusListener.onDownloadEbookProgress(i, LibraryConstant.DOWNLOAD_STATUS_FINISH);
					}
				}
				else
				{
					if( mOnDownloadEbookStatusListener != null )
					{
						mOnDownloadEbookStatusListener.onDownloadEbookError();
					}
					
					return;
				}
			}
			
			if( mOnDownloadEbookStatusListener != null )
			{
				mOnDownloadEbookStatusListener.onDownloadEbookCompleted();
			}
		}
		else
		{
			if( mOnDownloadEbookStatusListener != null )
			{
				mOnDownloadEbookStatusListener.onDownloadEbookError();
			}
		}
	}
}
