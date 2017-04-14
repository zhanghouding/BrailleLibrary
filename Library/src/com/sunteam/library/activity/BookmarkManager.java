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
import com.sunteam.common.utils.ArrayUtils;
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.dialog.ConfirmListener;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.ClearBookMarkAsyncTask;
import com.sunteam.library.asynctask.GetBookMarkAsyncTask;
import com.sunteam.library.entity.BookmarkEntity;

/**
 * @Destryption 书签管理：增加、查看、删除、清空书签
 * @Author Jerry
 * @Date 2017-2-6 上午9:35:41
 * @Note 理论上，一本书的书签可以是无穷多个，但实际上我们可以限制在Intent传输的数据量大小之内！
 */
public class BookmarkManager extends MenuActivity {
	private BookmarkEntity mBookmarkEntity;

	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK == resultCode) {
			setResult(Activity.RESULT_OK, data);
			finish();
		}
	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		switch (selectItem) {
		case 0: // 增加书签
			startAddBookmarkActivity(selectItem, menuItem);
			break;
		case 1: // 查看书签
			new GetBookMarkAsyncTask(this, selectItem, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mBookmarkEntity.bookId);
			break;
		case 2: // 删除书签
			new GetBookMarkAsyncTask(this, selectItem, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mBookmarkEntity.bookId);
			break;
		case 3: // 清空书签
			confirmClearBookmark(this);
			break;
		default:
			break;
		}
	}

	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mBookmarkEntity = (BookmarkEntity) intent.getSerializableExtra("book_mark");
		mMenuList = ArrayUtils.strArray2List(getResources().getStringArray(R.array.library_bookmark_manager_list));
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

	// 启动添加书签界面
	private void startAddBookmarkActivity(int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, menuItem);
		intent.putExtra("book_mark", mBookmarkEntity);
		intent.setClass(this, BookmarkNameEdit.class);

		startActivityForResult(intent, selectItem);
	}

	// 进入查看书签列表界面；如果需要在异步任务中获取云端数据，则需要在成功获取数据后调用该方法
	public void startNextActivity(Class<?> cls, int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, menuItem);
		intent.setClass(this, cls);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		startActivityForResult(intent, selectItem);
	}

	private void confirmClearBookmark(final Context context) {
		String s = getResources().getString(R.string.library_dialog_clear);
		ConfirmDialog mConfirmDialog = new ConfirmDialog(this, s);
		mConfirmDialog.setConfirmListener(new ConfirmListener() {

			@Override
			public void doConfirm() {
				new ClearBookMarkAsyncTask(context, mHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mBookmarkEntity.bookId);
			}

			@Override
			public void doCancel() {
				/*String s = mTitle + "," + getSelectItemContent();
				TtsUtils.getInstance().speak(s);*/
				BookmarkManager.this.onResume();
			}
		});
		mConfirmDialog.show();
	}	

}
