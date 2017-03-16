package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.menu.menuview.OnMenuKeyListener;
import com.sunteam.library.asynctask.GetEbookChapterContentAsyncTask;
import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.entity.EbookChapterInfoEntity;
import com.sunteam.library.utils.EbookConstants;
import com.sunteam.library.utils.LibraryConstant;

/**
 * @Destryption 电子书章节列表：在线、离线从资源列表进入；从下载管理中的已下载列表进入。
 * @Author Jerry
 * @Date 2017-2-4 下午3:20:17
 * @Note 从下载管理中的已下载列表进入后，再按菜单键无效
 */
public class EbookChapterList extends MenuActivity implements OnMenuKeyListener {
	private int fatherWindowType = 0; //父窗口类型：0 从资源列表进入；1从下载管理进入，此时菜单键无效
	private String identifier;	//电子书identifier
	private String fatherPath;	//父目录路径
	private String dbCode;		//数据编码
	private String sysId;		//系统id
	private String categoryName;//分类名称
	private String categoryCode;
	private ArrayList<EbookChapterInfoEntity> mEbookChapterInfoEntityList;
	private BookmarkEntity mBookmarkEntity;
	private boolean isHistory = false;	//是否是从历史记录进入
	private int lastChapterIndex = 0;
	private int offset = 0;
	
	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (0 == fatherWindowType) {
			mMenuView.setMenuKeyListener(this);
		}
		if (isHistory) {
			if(null != mMenuView){
				mMenuView.setSelectItem(selectItem);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode || null == data) { // 在子菜单中回传的标志
			return;
		}

		int action = data.getIntExtra("action", EbookConstants.TO_NEXT_PART);
		switch (action) {
			case EbookConstants.TO_NEXT_PART:	//上一章
				mMenuView.down();
				mMenuView.enter();
				break;
			case EbookConstants.TO_PRE_PART:	//下一章
				mMenuView.up();
				mMenuView.enter();
				break;
			case EbookConstants.TO_BOOK_MARK:	//书签
				mBookmarkEntity = (BookmarkEntity) data.getSerializableExtra("book_mark");
				if( mBookmarkEntity != null )
				{
					int chapterIndex = mBookmarkEntity.chapterIndex;
					int selectItem = mMenuView.getSelectItem();
					if( chapterIndex == selectItem )
					{
						
					}
					else if( chapterIndex > selectItem )
					{
						for( int i = selectItem; i < chapterIndex; i++ )
						{
							mMenuView.down();
						}
					}
					else
					{
						for( int i = selectItem; i > chapterIndex; i-- )
						{
							mMenuView.up();
						}
					}
					
					mMenuView.enter();
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		String chapterIndex = mEbookChapterInfoEntityList.get(selectItem).chapterIndex;
		new GetEbookChapterContentAsyncTask(this, fatherPath, menuItem,selectItem,mEbookChapterInfoEntityList.size(), mBookmarkEntity,isHistory, offset).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, identifier, chapterIndex, dbCode, sysId, categoryName, mTitle, categoryCode);
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mEbookChapterInfoEntityList = (ArrayList<EbookChapterInfoEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		mMenuList = getListFromChapterInfoEntity(mEbookChapterInfoEntityList);
		fatherPath = this.getIntent().getStringExtra(LibraryConstant.INTENT_KEY_FATHER_PATH);
		identifier = this.getIntent().getStringExtra(LibraryConstant.INTENT_KEY_IDENTIFIER);
		dbCode = intent.getStringExtra(LibraryConstant.INTENT_KEY_DBCODE);
		sysId = intent.getStringExtra(LibraryConstant.INTENT_KEY_SYSID);
		categoryName = intent.getStringExtra(LibraryConstant.INTENT_KEY_CATEGORY_NAME);
		categoryCode = intent.getStringExtra(LibraryConstant.INTENT_KEY_CATEGORY_CODE);
		isHistory = intent.getBooleanExtra("isHistory", false);
		lastChapterIndex = intent.getIntExtra("lastChapterIndex", 0);
		offset = intent.getIntExtra("offset", 0);
		if (isHistory) {
			if (lastChapterIndex < mMenuList.size()) {
				selectItem = lastChapterIndex;
			}
			setResultCode(Activity.RESULT_OK, selectItem, (String) mMenuList.get(selectItem));
		}
	}

	private ArrayList<String> getListFromChapterInfoEntity(ArrayList<EbookChapterInfoEntity> listSrc) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < listSrc.size(); i++) {
			list.add(listSrc.get(i).chapterName);
		}

		return list;
	}

	@Override
	public void onMenuKeyCompleted(int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.LIBRARY_DATATYPE_EBOOK); // 数据类别：电子书、有声书、口述影像
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, fatherPath); // 父目录
		intent.putExtra(LibraryConstant.INTENT_KEY_CATEGORY_CODE, categoryCode); // 分类编码
		intent.putExtra(LibraryConstant.INTENT_KEY_CATEGORY_NAME, categoryName); // 分类名称
		intent.putExtra(LibraryConstant.INTENT_KEY_RESOURCE, mTitle);
		intent.putExtra(LibraryConstant.INTENT_KEY_DBCODE, dbCode);	//数据编码
		intent.putExtra(LibraryConstant.INTENT_KEY_SYSID, sysId);	//系统id
		intent.putExtra(LibraryConstant.INTENT_KEY_IDENTIFIER, identifier);
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, mEbookChapterInfoEntityList);

		intent.setClass(this, ChapterFunctionMenu.class);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);
	}

}
