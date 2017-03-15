package com.sunteam.library.asynctask;

import java.util.ArrayList;

import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDetectBigUrlFileListener;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.db.DownloadChapterDBDao;
import com.sunteam.library.db.DownloadResourceDBDao;
import com.sunteam.library.entity.AudioChapterInfoEntity;
import com.sunteam.library.entity.DownloadChapterEntity;
import com.sunteam.library.entity.DownloadResourceEntity;
import com.sunteam.library.entity.EbookChapterInfoEntity;
import com.sunteam.library.entity.VideoChapterInfoEntity;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 下载资源异步加载类
 * 
 * @author wzp
 * @Created 2017/02/05
 */
public class DownloadResourceAsyncTask extends AsyncTask<DownloadResourceEntity, Void, Void>
{
	private Context mContext;
	private String fatherPath;	//父目录路径
	private String identifier;	//电子书identifier
	private ArrayList<EbookChapterInfoEntity> mEbookChapterInfoEntityList;	//电子书章节信息
	private ArrayList<AudioChapterInfoEntity> mAudioChapterInfoEntityList;	//有声书章节信息
	private ArrayList<VideoChapterInfoEntity> mVideoChapterInfoEntityList;	//视频章节信息
	
	public DownloadResourceAsyncTask(Context context, String fatherPath, String identifier, ArrayList<EbookChapterInfoEntity> ebookList, ArrayList<AudioChapterInfoEntity> audioList, ArrayList<VideoChapterInfoEntity> videoList ) 
	{
		this.mContext = context;
		this.fatherPath = fatherPath;
		this.identifier = identifier;
		this.mEbookChapterInfoEntityList = ebookList;
		this.mAudioChapterInfoEntityList = audioList;
		this.mVideoChapterInfoEntityList = videoList;
	}

	@Override
	protected Void doInBackground(DownloadResourceEntity... params) 
	{
		DownloadResourceEntity entity = params[0];		
		
		DownloadResourceDBDao dao = new DownloadResourceDBDao(mContext);
		dao.insert(entity);
		entity = dao.find(entity.userName, entity);	//为了得到记录的_id
		dao.closeDb();
		
		DownloadChapterDBDao dcDao = new DownloadChapterDBDao( mContext );
		switch( entity.resType )
		{
			case LibraryConstant.LIBRARY_DATATYPE_EBOOK:
				for( int i = 0; i < mEbookChapterInfoEntityList.size(); i++ )
				{
					final DownloadChapterEntity dce = new DownloadChapterEntity();
					dce.recorcdId = entity._id;			//对应的下载资源记录ID
				  	dce.chapterName = mEbookChapterInfoEntityList.get(i).chapterName;		//章节名称
				  	dce.chapterIndex = i;				//章节序号
				  	dce.chapterStatus = LibraryConstant.DOWNLOAD_STATUS_WAIT;				//章节下载状态 (0：等待下载 1：正在下载 2：下载完成)
				  	dce.chapterPath = fatherPath+PublicUtils.format(dce.chapterName);		//章节下载路径
				  	dce.chapterUrl = LibraryConstant.API_URL + LibraryConstant.URL_INTERFACE_EBOOK + "?chapterIndex="+i+"&Identifier="+identifier+"&requestType=GetChapterContent";				//章节下载URL
				  	
				  	dcDao.insert(dce);
				  	
				  	FileDownloader.detect(dce.chapterUrl, new OnDetectBigUrlFileListener() {
			    		@Override
			    		public void onDetectNewDownloadFile(String url, String fileName, String saveDir, long fileSize) 
			    		{
			    			// 如果有必要，可以改变文件名称fileName和下载保存的目录saveDir
			    			FileDownloader.createAndStart(url, dce.chapterPath, PublicUtils.format(dce.chapterName)+LibraryConstant.CACHE_FILE_SUFFIX);
			    		}
			    		
			    		@Override
			    		public void onDetectUrlFileExist(String url) 
			    		{
			    			FileDownloader.start(url);	
			    			//如果文件没被下载过，将创建并开启下载，否则继续下载，自动会断点续传（如果服务器无法支持断点续传将从头开始下载）
			    		}
			    		
			    		@Override
			    		public void onDetectUrlFileFailed(String url, DetectBigUrlFileFailReason failReason) 
			    		{
			    			// 探测一个网络文件失败了，具体查看failReason
			    		}
			    	});
				}				
				break;
			case LibraryConstant.LIBRARY_DATATYPE_AUDIO:	//音频
				for( int i = 0; i < mAudioChapterInfoEntityList.size(); i++ )
				{
					final DownloadChapterEntity dce = new DownloadChapterEntity();
					dce.recorcdId = entity._id;			//对应的下载资源记录ID
				  	dce.chapterName = mAudioChapterInfoEntityList.get(i).title;		//章节名称
				  	dce.chapterIndex = i;				//章节序号
				  	dce.chapterStatus = LibraryConstant.DOWNLOAD_STATUS_WAIT;		//章节下载状态 (0：等待下载 1：正在下载 2：下载完成)
				  	dce.chapterPath = fatherPath+PublicUtils.format(dce.chapterName);		//章节下载路径
				  	dce.chapterUrl = mAudioChapterInfoEntityList.get(i).audioUrl;	//章节下载URL
				  	
				  	dcDao.insert(dce);
				  	
				  	FileDownloader.detect(dce.chapterUrl, new OnDetectBigUrlFileListener() {
			    		@Override
			    		public void onDetectNewDownloadFile(String url, String fileName, String saveDir, long fileSize) 
			    		{
			    			// 如果有必要，可以改变文件名称fileName和下载保存的目录saveDir
			    			FileDownloader.createAndStart(url, dce.chapterPath, fileName);
			    		}
			    		
			    		@Override
			    		public void onDetectUrlFileExist(String url) 
			    		{
			    			FileDownloader.start(url);	
			    			//如果文件没被下载过，将创建并开启下载，否则继续下载，自动会断点续传（如果服务器无法支持断点续传将从头开始下载）
			    		}
			    		
			    		@Override
			    		public void onDetectUrlFileFailed(String url, DetectBigUrlFileFailReason failReason) 
			    		{
			    			// 探测一个网络文件失败了，具体查看failReason
			    		}
			    	});
				}
				break;
			case LibraryConstant.LIBRARY_DATATYPE_VIDEO:	//视频
				for( int i = 0; i < mVideoChapterInfoEntityList.size(); i++ )
				{
					final DownloadChapterEntity dce = new DownloadChapterEntity();
					dce.recorcdId = entity._id;			//对应的下载资源记录ID
				  	dce.chapterName = mVideoChapterInfoEntityList.get(i).title;		//章节名称
				  	dce.chapterIndex = i;				//章节序号
				  	dce.chapterStatus = LibraryConstant.DOWNLOAD_STATUS_WAIT;		//章节下载状态 (0：等待下载 1：正在下载 2：下载完成)
				  	dce.chapterPath = fatherPath+PublicUtils.format(dce.chapterName);		//章节下载路径
				  	dce.chapterUrl = mVideoChapterInfoEntityList.get(i).videoUrl;	//章节下载URL
				  	
				  	dcDao.insert(dce);
				  	
				  	FileDownloader.detect(dce.chapterUrl, new OnDetectBigUrlFileListener() {
			    		@Override
			    		public void onDetectNewDownloadFile(String url, String fileName, String saveDir, long fileSize) 
			    		{
			    			// 如果有必要，可以改变文件名称fileName和下载保存的目录saveDir
			    			FileDownloader.createAndStart(url, dce.chapterPath, fileName);
			    		}
			    		
			    		@Override
			    		public void onDetectUrlFileExist(String url) 
			    		{
			    			FileDownloader.start(url);	
			    			//如果文件没被下载过，将创建并开启下载，否则继续下载，自动会断点续传（如果服务器无法支持断点续传将从头开始下载）
			    		}
			    		
			    		@Override
			    		public void onDetectUrlFileFailed(String url, DetectBigUrlFileFailReason failReason) 
			    		{
			    			// 探测一个网络文件失败了，具体查看failReason
			    		}
			    	});
				}
				break;
			default:
				break;
		}
		
		dcDao.closeDb();
		
		return	null;
	}
	
	@Override
	protected void onPreExecute() 
	{	
		super.onPreExecute();
		
		String s = mContext.getResources().getString(R.string.library_downloading_addresource);
		PublicUtils.showProgress(mContext, s, this);
		//TtsUtils.getInstance().speak(s);
	}
	
	@Override
	protected void onPostExecute(Void result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
		
		String tips = mContext.getString(R.string.library_downloading_start);
		PublicUtils.showToast(mContext, tips, new PromptListener() {
			@Override
			public void onComplete() 
			{
				Activity activity = (Activity)mContext;
				activity.finish();
			}
		});
	}
}
