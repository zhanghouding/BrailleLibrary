package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.menu.menuview.OnMenuKeyListener;
import com.sunteam.library.entity.AudioChapterInfoEntity;
import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.utils.EbookConstants;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 有声书章节列表
 * @Author Jerry
 * @Date 2017-2-4 下午3:25:36
 * @Note
 */
public class AudioChapterList extends MenuActivity implements OnMenuKeyListener {
	private String fatherPath;	//父目录路径
	private String dbCode;		//数据编码
	private String sysId;		//系统id
	private String identifier;	//书本id
	private String categoryName;//分类名称
	private ArrayList<AudioChapterInfoEntity> mAudioChapterInfoEntityList;
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
		mMenuView.setMenuKeyListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode || null == data) { // 在子菜单中回传的标志
			return;
		}

		int action = data.getIntExtra("action", EbookConstants.TO_NEXT_PART);
		switch (action) {
		case EbookConstants.TO_NEXT_PART:
			mMenuView.down();
			mMenuView.enter();
			break;
		case EbookConstants.TO_PRE_PART:
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
		PublicUtils.createCacheDir(fatherPath, menuItem);	//创建缓存目录
		
		Intent intent = new Intent( this, PlayAudioVedioActivity.class );
		intent.putExtra("dbCode", mAudioChapterInfoEntityList.get(selectItem).databaseCode); // 数据库代码
		intent.putExtra("sysId", mAudioChapterInfoEntityList.get(selectItem).sysId); // 记录标识号
		intent.putExtra("identifier", identifier);	//书本id
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.LIBRARY_DATATYPE_AUDIO); // 数据类别：电子书、有声书、口述影像
		intent.putExtra("categoryCode", mAudioChapterInfoEntityList.get(selectItem).categoryName); // 分类代码
		intent.putExtra("filename", menuItem); // 书名
		intent.putExtra("curChapter", selectItem); // 当前章节序号
		intent.putExtra("totalChapter", mAudioChapterInfoEntityList.size()); // 总章节数
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, fatherPath+menuItem+"/");		//父目录
		intent.putExtra(LibraryConstant.INTENT_KEY_URL, mAudioChapterInfoEntityList.get(selectItem).audioUrl);	//资源路径
		if( mBookmarkEntity != null )
		{
			intent.putExtra("book_mark", mBookmarkEntity);	//书签
		}
		this.startActivityForResult(intent, selectItem);
		
		mBookmarkEntity = null;
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mAudioChapterInfoEntityList = (ArrayList<AudioChapterInfoEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		mMenuList = getListFromChapterInfoEntity(mAudioChapterInfoEntityList);
		fatherPath = this.getIntent().getStringExtra(LibraryConstant.INTENT_KEY_FATHER_PATH);
		dbCode = intent.getStringExtra(LibraryConstant.INTENT_KEY_DBCODE);
		sysId = intent.getStringExtra(LibraryConstant.INTENT_KEY_SYSID);
		identifier = intent.getStringExtra(LibraryConstant.INTENT_KEY_IDENTIFIER);
		categoryName = intent.getStringExtra(LibraryConstant.INTENT_KEY_CATEGORY_NAME);
		isHistory = intent.getBooleanExtra("isHistory", false);
		lastChapterIndex = intent.getIntExtra("lastChapterIndex", 0);
		offset = intent.getIntExtra("offset", 0);
	}

	private ArrayList<String> getListFromChapterInfoEntity(ArrayList<AudioChapterInfoEntity> listSrc) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < listSrc.size(); i++) {
			list.add(listSrc.get(i).title);
		}

		return list;
	}

	@Override
	public void onMenuKeyCompleted(int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.LIBRARY_DATATYPE_AUDIO); // 数据类别：电子书、有声书、口述影像
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, fatherPath); // 父目录
		intent.putExtra(LibraryConstant.INTENT_KEY_CATEGORY_NAME, categoryName); // 分类名称
		intent.putExtra(LibraryConstant.INTENT_KEY_RESOURCE, mTitle);
		intent.putExtra(LibraryConstant.INTENT_KEY_DBCODE, dbCode);	//数据编码
		intent.putExtra(LibraryConstant.INTENT_KEY_SYSID, sysId);	//系统id
		
		intent.setClass(this, ChapterFunctionMenu.class);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);
	}

}
