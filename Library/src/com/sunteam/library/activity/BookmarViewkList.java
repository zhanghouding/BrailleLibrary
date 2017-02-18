package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.dialog.ConfirmListener;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.AddBookMarkAsyncTask;
import com.sunteam.library.asynctask.DelBookMarkAsyncTask;
import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 查看书签
 * @Author Jerry
 * @Date 2017-2-6 上午9:35:41
 * @Note 理论上，一本书的书签可以是无穷多个，但实际上我们可以限制在Intent传输的数据量大小之内！
 */
public class BookmarViewkList extends MenuActivity {
	private int type = 1; // 查看书签和删除书签共用一个界面： 1 查看书签，2
							// 删除书签；该值就是书签管理菜单中查看书签或删除书签的序号
	private ArrayList<BookmarkEntity> mBookmarkEntityList; // 书签列表：数目ID, 章节号,
															// 起始位置, 书签名等

	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
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
		if (selectItem >= mBookmarkEntityList.size()) {
			return;
		}

		BookmarkEntity mBookmarkEntity = mBookmarkEntityList.get(selectItem);
		if (1 == type) { // 查看书签，点击后跳转到该书签浏览
			// TODO 跳转到指定位置
			Intent intent = new Intent();
			intent.putExtra("book_mark", mBookmarkEntity);
			setResult(Activity.RESULT_OK, intent);
			finish();
		} else { // 删除指定书签
			new DelBookMarkAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mBookmarkEntity.id);
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

}
