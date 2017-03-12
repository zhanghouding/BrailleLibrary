package com.sunteam.library.activity;

import java.io.File;
import java.util.ArrayList;

import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDetectBigUrlFileListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.ArrayUtils;
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.dialog.ConfirmListener;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.AddCollectResourceAsyncTask;
import com.sunteam.library.db.DownloadChapterDBDao;
import com.sunteam.library.db.DownloadResourceDBDao;
import com.sunteam.library.entity.AudioChapterInfoEntity;
import com.sunteam.library.entity.CollectResourceEntity;
import com.sunteam.library.entity.DownloadChapterEntity;
import com.sunteam.library.entity.DownloadResourceEntity;
import com.sunteam.library.entity.EbookChapterInfoEntity;
import com.sunteam.library.entity.VideoChapterInfoEntity;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 章节列表时的功能菜单；电子书、有声书、口述影像共用一个功能菜单 
 * @Author Jerry
 * @Date 2017-2-4 下午3:20:42
 * @Note
 */
public class ChapterFunctionMenu extends MenuActivity {
	private Context mContext;
	private String dbCode;			//数据库编码
	private String sysId;			//系统id
	private String categoryName;	//分类名称
	private String resourceName;	//资源名称
	private int dataType = 0; // 数据类别：电子书、有声书、口述影像
	private String identifier;	//电子书identifier
	private ArrayList<EbookChapterInfoEntity> mEbookChapterInfoEntityList;	//电子书章节信息
	private ArrayList<AudioChapterInfoEntity> mAudioChapterInfoEntityList;	//有声书章节信息
	private ArrayList<VideoChapterInfoEntity> mVideoChapterInfoEntityList;	//视频章节信息
	private String fatherPath;	//父目录路径

	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
		mContext = this;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onResume() {
		TtsUtils.getInstance().restoreSettingParameters();
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode || null == data) { // 在子菜单中回传的标志
			return;
		}

	}

	//开始下载
	private void startDownload( DownloadResourceEntity entity )
	{
		DownloadResourceDBDao dao = new DownloadResourceDBDao(this);
		dao.insert(entity);
		dao.closeDb();
		
		DownloadChapterDBDao dcDao = new DownloadChapterDBDao( mContext );
		switch( dataType )
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
			default:
				break;
		}
		
		dcDao.closeDb();
		
		String tips = this.getString(R.string.library_downloading);
		PublicUtils.showToast(this, tips, new PromptListener() {
			@Override
			public void onComplete() 
			{
				// TODO Auto-generated method stub
				{
					finish();
				}
			}
		});
	}
	
	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		switch(selectItem){
		case 0: // 收藏当前资源
			{
				CollectResourceEntity entity = new CollectResourceEntity();
	
				entity.title = resourceName;
				entity.dbCode = dbCode;
				entity.sysId = sysId;
				if (LibraryConstant.LIBRARY_DATATYPE_EBOOK == dataType) {
					entity.sysId = identifier;
				}
				entity.userName = PublicUtils.getUserName(this);
				entity.resType = dataType;
				entity.categoryFullName = PublicUtils.getCategoryName(this, dataType) + "-" + categoryName + "-" + resourceName;
				
				new AddCollectResourceAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, entity);
			}
			break;
		case 1: // 下载当前资源
			String userName = PublicUtils.getUserName(this);
			DownloadResourceEntity entity = new DownloadResourceEntity();
			entity.userName = userName;			//用户名
			entity.resType = dataType;			//资源类型 1:有声读物 2:电子图书  3:视频影像
			entity.categoryFullName = PublicUtils.getCategoryName(this, dataType) + "-" + categoryName + "-" + resourceName;	//完整的分类名，格式"电子图书-古典文学"
			entity.title = resourceName;		//资源名称
			entity.dbCode = dbCode;				//数据库编码
			entity.sysId = sysId;				//系统id
			entity.identifier = identifier;		//电子书ID
			entity.status = LibraryConstant.DOWNLOAD_STATUS_WAIT;					//下载状态 (0：等待下载 1：正在下载 2：下载完成)
			
			switch( dataType )
			{
				case LibraryConstant.LIBRARY_DATATYPE_EBOOK:
					entity.chapterCount = mEbookChapterInfoEntityList.size();		//章节总数
					break;
				case LibraryConstant.LIBRARY_DATATYPE_AUDIO:	//音频
					entity.chapterCount = mAudioChapterInfoEntityList.size();		//章节总数
					break;
				case LibraryConstant.LIBRARY_DATATYPE_VIDEO:	//视频
					entity.chapterCount = mVideoChapterInfoEntityList.size();		//章节总数
					break;
				default:
					break;
			}			
			
			DownloadResourceDBDao dao = new DownloadResourceDBDao(this);
			final DownloadResourceEntity dre = dao.find(userName, entity);	//查找下载资源任务是否已经存在
			dao.closeDb();
			
			if( dre != null )
			{
				//此任务已经存在，则提示是否重新下载
				String s = getResources().getString(R.string.library_downloading_tips);
				ConfirmDialog mConfirmDialog = new ConfirmDialog(this, s);
				mConfirmDialog.setConfirmListener(new ConfirmListener() {
					@Override
					public void doConfirm() 
					{
						DownloadResourceDBDao DrDao = new DownloadResourceDBDao(mContext);
						DrDao.delete(dre);
						DrDao.closeDb();
						
						DownloadChapterDBDao dcDao = new DownloadChapterDBDao( mContext);
						dcDao.deleteAll(dre._id);
						dcDao.closeDb();
						
						startDownload(dre);
					}
					@Override
					public void doCancel() 
					{
					}
				});
				mConfirmDialog.show();
			}
			else
			{
				startDownload(entity);
			}
			break;
		case 2: // 删除当前资源
			{
				String path = fatherPath;
				File file = new File( path );
				PublicUtils.deleteFiles(file);
				PublicUtils.createCacheDir(fatherPath, "");	//创建缓存目录(因为用deleteFiles会连fatherPath也给删除了，所以必须重建)
				String tips = menuItem+this.getString(R.string.library_success);
				PublicUtils.showToast(this, tips, null);
			}
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		Intent intent = getIntent();
		categoryName = intent.getStringExtra(LibraryConstant.INTENT_KEY_CATEGORY_NAME);
		resourceName = intent.getStringExtra(LibraryConstant.INTENT_KEY_RESOURCE);
		dataType = intent.getIntExtra(LibraryConstant.INTENT_KEY_TYPE, 0);
		fatherPath = intent.getStringExtra(LibraryConstant.INTENT_KEY_FATHER_PATH);
		dbCode = intent.getStringExtra(LibraryConstant.INTENT_KEY_DBCODE);
		sysId = intent.getStringExtra(LibraryConstant.INTENT_KEY_SYSID);
		identifier = intent.getStringExtra(LibraryConstant.INTENT_KEY_IDENTIFIER);
		
		switch( dataType )
		{
			case LibraryConstant.LIBRARY_DATATYPE_EBOOK:
				mEbookChapterInfoEntityList = (ArrayList<EbookChapterInfoEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
				break;
			case LibraryConstant.LIBRARY_DATATYPE_AUDIO:	//音频
				mAudioChapterInfoEntityList = (ArrayList<AudioChapterInfoEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
				break;
			case LibraryConstant.LIBRARY_DATATYPE_VIDEO:	//视频
				mVideoChapterInfoEntityList = (ArrayList<VideoChapterInfoEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
				break;
			default:
				break;
		}
		
		mTitle = getResources().getString(R.string.common_functionmenu);
		mMenuList = ArrayUtils.strArray2List(getResources().getStringArray(R.array.library_chapter_function_menu_list));
	}
}
