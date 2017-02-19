package com.sunteam.library.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.dialog.ConfirmListener;
import com.sunteam.library.R;
import com.sunteam.library.entity.CollectCategoryEntity;
import com.sunteam.library.entity.CollectResourceEntity;
import com.sunteam.library.entity.HistoryEntity;
import com.sunteam.library.utils.LibraryConstant;

/**
 * @Destryption 公用的功能菜单：删除、清空; 我收藏的资源、我收藏的分类、我的阅读历史、已下载、正在下载这5个界面下的功能菜单统一处理
 * @Author Jerry
 * @Date 2017-2-19 下午2:08:39
 * @Note
 */
public class CommonFunctionMenu extends MenuActivity {
	private Object mEntity;
	private int mType; // 0 我收藏的资源 1 我收藏的分类 2 我的阅读历史 3 已下载 4 正在下载

	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		int id;
		switch (selectItem) {
		case 0: // 删除
			id = R.string.library_dialog_delete;
			break;
		case 1: // 清空
			id = R.string.library_dialog_clear;
			break;
		default:
			return;
		}
		showConfirmDialogue(this, id, selectItem);
	}

	private void initView() {
		Intent intent = getIntent();
		mEntity = intent.getSerializableExtra("entity");
		mType = intent.getIntExtra(LibraryConstant.INTENT_KEY_TYPE, 0);
	}

	private void showConfirmDialogue(final Context context, final int id, final int selectItem) {
		String s = getResources().getString(id);
		ConfirmDialog mConfirmDialog = new ConfirmDialog(this, s);
		mConfirmDialog.setConfirmListener(new ConfirmListener() {

			@Override
			public void doConfirm() {
				if (0 == selectItem) { // 删除
					deleteRecord();
				}
				if (1 == selectItem) { // 清空
					deleteAllRecords();
				}
			}

			@Override
			public void doCancel() {
			}
		});
		mConfirmDialog.show();
	}

	// 删除当前记录
	private void deleteRecord() {
		switch(mType){
		case LibraryConstant.MYLIBRARY_FAVARITE_CATEGORY:
			// TODO 删除收藏分类
			CollectCategoryEntity mCollectCategoryEntity = (CollectCategoryEntity) mEntity;
			break;
		case LibraryConstant.MYLIBRARY_FAVARITE_RESOURCE:
			// TODO 删除收藏资源
			CollectResourceEntity mCollectResourceEntity = (CollectResourceEntity) mEntity;
			break;
		case LibraryConstant.MYLIBRARY_READING_HISTORY:
			// TODO 删除阅读历史
			HistoryEntity mHistoryEntity = (HistoryEntity) mEntity;
			break;
		case LibraryConstant.MYLIBRARY_DOWNLOADING:
			break;
		case LibraryConstant.MYLIBRARY_DOWNLOADED:
			break;
		default:
			return;
		}
	}

	// 删除所有记录
	private void deleteAllRecords() {
		switch(mType){
		case LibraryConstant.MYLIBRARY_FAVARITE_CATEGORY:
			break;
		case LibraryConstant.MYLIBRARY_FAVARITE_RESOURCE:
			break;
		case LibraryConstant.MYLIBRARY_READING_HISTORY:
			break;
		case LibraryConstant.MYLIBRARY_DOWNLOADING:
			break;
		case LibraryConstant.MYLIBRARY_DOWNLOADED:
			break;
		default:
			return;
		}
	}

}
