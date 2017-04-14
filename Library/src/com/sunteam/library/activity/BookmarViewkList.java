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
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.dialog.ConfirmListener;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.DelBookMarkAsyncTask;
import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.utils.EbookConstants;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 查看书签
 * @Author Jerry
 * @Date 2017-2-6 上午9:35:41
 * @Note 理论上，一本书的书签可以是无穷多个，但实际上我们可以限制在Intent传输的数据量大小之内！
 */
public class BookmarViewkList extends MenuActivity {
	private int type = 1; // 查看书签和删除书签共用一个界面： 1 查看书签，2删除书签；该值就是书签管理菜单中查看书签或删除书签的序号
	private ArrayList<BookmarkEntity> mBookmarkEntityList; // 书签列表：数目ID, 章节号,起始位置, 书签名等

	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: // 删除成功
				selectItem = getSelectItem();
				if (selectItem < mMenuList.size()) {
					mBookmarkEntityList.remove(selectItem);
					mMenuList.remove(selectItem);
				}
				if (0 == mMenuList.size()) {
					setResult(Activity.RESULT_OK);
					String s = getResources().getString(R.string.library_menu_mark_null);
					PublicUtils.showToast(BookmarViewkList.this, s, true);
				} else {
					if (selectItem >= mMenuList.size()) {
						selectItem = mMenuList.size() - 1;
					}
					setListData(mMenuList);
					mMenuView.setSelectItem(selectItem, true);
				}
				break;
			case 1: // 网络未连接
			case 2: // 删除异常
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		if (selectItem >= mBookmarkEntityList.size()) {
			return;
		}

		BookmarkEntity mBookmarkEntity = mBookmarkEntityList.get(selectItem);
		if (1 == type) { // 查看书签，点击后跳转到该书签浏览
			Intent intent = new Intent();
			intent.putExtra("action", EbookConstants.TO_BOOK_MARK);
			intent.putExtra("book_mark", mBookmarkEntity);
			setResult(Activity.RESULT_OK, intent);
			finish();
		} else { // 删除指定书签
			confirmDeleteBookmark(this, mBookmarkEntity);
		}
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		type = intent.getIntExtra("type", type);
		mBookmarkEntityList = (ArrayList<BookmarkEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		mMenuList = getListFromBookmarkEntity(mBookmarkEntityList);
	}

	// 从书签列表中获取书签名列表
	private ArrayList<String> getListFromBookmarkEntity(ArrayList<BookmarkEntity> listSrc) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < listSrc.size(); i++) {
			list.add(listSrc.get(i).markName);
		}

		return list;
	}

	private void confirmDeleteBookmark(final Context context, final BookmarkEntity entity) {
		String s = getResources().getString(R.string.library_dialog_delete);
		ConfirmDialog mConfirmDialog = new ConfirmDialog(this, s);
		mConfirmDialog.setConfirmListener(new ConfirmListener() {

			@Override
			public void doConfirm() {
				new DelBookMarkAsyncTask(context, mHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, entity);
			}

			@Override
			public void doCancel() {
				/*String s = mTitle + "," + getSelectItemContent();
				TtsUtils.getInstance().speak(s);*/
				BookmarViewkList.this.onResume();
			}
		});
		mConfirmDialog.show();
	}
	
}
