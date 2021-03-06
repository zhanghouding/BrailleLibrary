package com.sunteam.library.activity;

import java.util.ArrayList;

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
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.ArrayUtils;
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.dialog.ConfirmListener;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.ClearCollectCategoryAsyncTask;
import com.sunteam.library.asynctask.ClearCollectResourceAsyncTask;
import com.sunteam.library.asynctask.ClearHistoryAsyncTask;
import com.sunteam.library.asynctask.DelCollectCategoryAsyncTask;
import com.sunteam.library.asynctask.DelCollectResourceAsyncTask;
import com.sunteam.library.asynctask.DelHistoryAsyncTask;
import com.sunteam.library.entity.CollectCategoryEntity;
import com.sunteam.library.entity.CollectResourceEntity;
import com.sunteam.library.entity.DownloadResourceEntity;
import com.sunteam.library.entity.HistoryEntity;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 公用的功能菜单：删除、清空; 我收藏的资源、我收藏的分类、我的阅读历史、已下载、正在下载这5个界面下的功能菜单统一处理
 * @Author Jerry
 * @Date 2017-2-19 下午2:08:39
 * @Note
 */
public class CommonFunctionMenu extends MenuActivity {
	private int mType; // 0 我收藏的资源 1 我收藏的分类 2 我的阅读历史 3 已下载 4 正在下载
	private ArrayList<Object> mList;
	private int index; // 要删除的项目序号

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

	@SuppressWarnings("unchecked")
	private void initView() {
		mTitle = getResources().getString(R.string.common_functionmenu); // 功能菜单
		String[] list = getResources().getStringArray(R.array.library_common_function_menu_list);
		mMenuList = ArrayUtils.strArray2List(list);
		Intent intent = getIntent();
		mType = intent.getIntExtra(LibraryConstant.INTENT_KEY_TYPE, 0);
		mList = (ArrayList<Object>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		index = intent.getIntExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, 0);
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
				String s = mTitle + "," + getSelectItemContent();
				TtsUtils.getInstance().speak(s);
			}
		});
		mConfirmDialog.show();
	}

	// 删除当前记录
	private void deleteRecord() {
		switch (mType) {
		case LibraryConstant.MYLIBRARY_FAVARITE_CATEGORY:
			CollectCategoryEntity ce = (CollectCategoryEntity) mList.get(index);
			new DelCollectCategoryAsyncTask(this, mHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ce);
			break;
		case LibraryConstant.MYLIBRARY_FAVARITE_RESOURCE:
			CollectResourceEntity re = (CollectResourceEntity) mList.get(index);
			new DelCollectResourceAsyncTask(this, mHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, re);
			break;
		case LibraryConstant.MYLIBRARY_READING_HISTORY:
			HistoryEntity he = (HistoryEntity) mList.get(index);
			new DelHistoryAsyncTask(this, mHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, he);
			break;
		case LibraryConstant.MYLIBRARY_DOWNLOADING:
		case LibraryConstant.MYLIBRARY_DOWNLOADED:
			// PublicUtils.showToast(this, getResources().getString(R.string.library_del_downloadtask));
			PublicUtils.deleteDownloadTask(this, (DownloadResourceEntity) mList.get(index));
			promptSuccessForDeleting(R.string.library_del_success);
			break;
		default:
			return;
		}
	}

	// 删除所有记录
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void deleteAllRecords() {
		switch (mType) {
		case LibraryConstant.MYLIBRARY_FAVARITE_CATEGORY:
			ArrayList<CollectCategoryEntity> list1 = (ArrayList)mList;
			new ClearCollectCategoryAsyncTask(this, mHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list1);
			break;
		case LibraryConstant.MYLIBRARY_FAVARITE_RESOURCE:
			ArrayList<CollectResourceEntity> list2 = (ArrayList)mList;
			new ClearCollectResourceAsyncTask(this, mHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list2);
			break;
		case LibraryConstant.MYLIBRARY_READING_HISTORY:
			ArrayList<HistoryEntity> list3 = (ArrayList)mList;
			new ClearHistoryAsyncTask(this, mHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list3);
			break;
		case LibraryConstant.MYLIBRARY_DOWNLOADING:
			// PublicUtils.showToast(this, getResources().getString(R.string.library_clear_downloadtask));
			PublicUtils.clearDownloadTask(this, false);
			promptSuccessForDeleting(R.string.library_dialog_clear_su);
			break;
		case LibraryConstant.MYLIBRARY_DOWNLOADED:
			// PublicUtils.showToast(this, getResources().getString(R.string.library_clear_downloadtask));
			PublicUtils.clearDownloadTask(this, true);
			promptSuccessForDeleting(R.string.library_dialog_clear_su);
			break;
		default:
			return;
		}
	}
	
	private void promptSuccessForDeleting(int id) {
		String s = getResources().getString(id);
		PublicUtils.showToast(this, s, new PromptListener() {
			
			@Override
			public void onComplete() {
				mHandler.sendEmptyMessage(0);
			}
		});
	}

}
