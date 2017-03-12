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
import com.sunteam.library.asynctask.DownloadResourceAsyncTask;
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
			{
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
							
							new DownloadResourceAsyncTask(mContext, fatherPath, identifier,  mEbookChapterInfoEntityList, mAudioChapterInfoEntityList, mVideoChapterInfoEntityList ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dre);
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
					new DownloadResourceAsyncTask(mContext, fatherPath, identifier,  mEbookChapterInfoEntityList, mAudioChapterInfoEntityList, mVideoChapterInfoEntityList ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, entity);
				}
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
