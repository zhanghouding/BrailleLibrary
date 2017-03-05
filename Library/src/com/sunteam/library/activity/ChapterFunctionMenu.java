package com.sunteam.library.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.ArrayUtils;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.AddCollectResourceAsyncTask;
import com.sunteam.library.download.DownloadEbook;
import com.sunteam.library.entity.CollectResourceEntity;
import com.sunteam.library.entity.EbookChapterInfoEntity;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 章节列表时的功能菜单；电子书、有声书、口述影像共用一个功能菜单 
 * @Author Jerry
 * @Date 2017-2-4 下午3:20:42
 * @Note
 */
public class ChapterFunctionMenu extends MenuActivity {
	private String dbCode;			//数据库编码
	private String sysId;			//系统id
	private String categoryName;	//分类名称
	private String resourceName;	//资源名称
	private int dataType = 0; // 数据类别：电子书、有声书、口述影像
	private String identifier;	//电子书identifier
	private ArrayList<EbookChapterInfoEntity> mEbookChapterInfoEntityList;	//电子书章节信息
	private String fatherPath;	//父目录路径
	private DownloadEbook mDownloadEbook;	//电子图书下载线程

	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
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
				
				new AddCollectResourceAsyncTask(this, entity).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
			break;
		case 1: // 下载当前资源
			switch( dataType )
			{
				case LibraryConstant.LIBRARY_DATATYPE_EBOOK:	//电子图书if( null == mDownloadEbook )
					{
						mDownloadEbook = new DownloadEbook(fatherPath, identifier, mEbookChapterInfoEntityList);
						mDownloadEbook.start();
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
						//此次还应该跳转到下载管理界面，或者弹出一个Toast.(先弹出一个Toast吧)
					}
					break;
				case LibraryConstant.LIBRARY_DATATYPE_AUDIO:	//音频
					break;
				case LibraryConstant.LIBRARY_DATATYPE_VIDEO:	//视频
					break;
				default:
					break;
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
		mEbookChapterInfoEntityList = (ArrayList<EbookChapterInfoEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		
		mTitle = getResources().getString(R.string.common_functionmenu);
		mMenuList = ArrayUtils.strArray2List(getResources().getStringArray(R.array.library_chapter_function_menu_list));
	}
}
