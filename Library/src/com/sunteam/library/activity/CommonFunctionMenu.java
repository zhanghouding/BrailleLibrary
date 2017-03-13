package com.sunteam.library.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.dialog.ConfirmListener;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.DelCollectCategoryAsyncTask;
import com.sunteam.library.asynctask.DelCollectResourceAsyncTask;
import com.sunteam.library.asynctask.DelHistoryAsyncTask;
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

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: // 删除成功
				Intent intent = new Intent();
				intent.putExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, getSelectItem()); // 用于区分当前选择是删除还是清空
				setResult(Activity.RESULT_OK, intent);
				finish();
				break;
			case 1: // 网络未连接
			case 2: // 删除异常
				break;
			default:
				break;
			}
		}
	};

	private void showConfirmDialogue(final Context context, final int resId, final int selectItem) {
		String s = getResources().getString(resId);
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
			CollectCategoryEntity ce = (CollectCategoryEntity) mEntity;
			new DelCollectCategoryAsyncTask(this, mHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ce);
			break;
		case LibraryConstant.MYLIBRARY_FAVARITE_RESOURCE:
			CollectResourceEntity re = (CollectResourceEntity) mEntity;
			new DelCollectResourceAsyncTask(this, mHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, re);
			break;
		case LibraryConstant.MYLIBRARY_READING_HISTORY:
			HistoryEntity he = (HistoryEntity) mEntity;
			new DelHistoryAsyncTask(this, mHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, he);
			break;
		case LibraryConstant.MYLIBRARY_DOWNLOADING:
			// TODO 删除当前
			break;
		case LibraryConstant.MYLIBRARY_DOWNLOADED:
			// TODO 删除当前
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
			// TODO
			break;
		case LibraryConstant.MYLIBRARY_DOWNLOADED:
			// TODO
			break;
		default:
			return;
		}
	}

}
