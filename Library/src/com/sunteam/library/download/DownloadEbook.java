package com.sunteam.library.download;

import java.util.ArrayList;

import android.os.Process;

import com.sunteam.library.entity.EbookChapterInfoEntity;
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
	private String mFatherPath;	//父目录
	private String identifier;	//电子书identifier
	private ArrayList<EbookChapterInfoEntity> mEbookChapterInfoEntityList;	//电子书章节信息
	
	public DownloadEbook(String fatherPath, String identifier, ArrayList<EbookChapterInfoEntity> list ) 
	{
		this.mFatherPath = fatherPath;
		this.identifier = identifier;
		this.mEbookChapterInfoEntityList = list;
	}

	@Override
	public void run()
	{
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

		for( int i = 0; i < mEbookChapterInfoEntityList.size(); i++ )
		{
			EbookChapterInfoEntity entity = mEbookChapterInfoEntityList.get(i);
			PublicUtils.createCacheDir(mFatherPath, entity.chapterName);	//创建缓存目录
			final String faterPath = mFatherPath+entity.chapterName+"/";
			
			String content = HttpDao.getEbookChapterContent(identifier, entity.chapterIndex);
			if( content != null)
			{
				PublicUtils.saveContent( faterPath, PublicUtils.format(entity.chapterName)+LibraryConstant.CACHE_FILE_SUFFIX, content );
			}
		}
	}
}
