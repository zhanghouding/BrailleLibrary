package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.menu.MenuGlobal;
import com.sunteam.common.menu.menuview.OnMenuKeyListener;
import com.sunteam.library.R;
import com.sunteam.library.db.DownloadChapterDBDao;
import com.sunteam.library.db.DownloadResourceDBDao;
import com.sunteam.library.entity.DownloadChapterEntity;
import com.sunteam.library.entity.DownloadResourceEntity;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 正在下载和已下载列表公用界面；
 * @Author Jerry
 * @Date 2017-3-14 下午3:43:46
 * @Note
 */
public class DownloadList extends MenuActivity implements OnMenuKeyListener {
	private ArrayList<DownloadResourceEntity> mDownloadResourceEntityList;
	private int type = 0; // 0下载任务列表；1已下载列表
	private DownloadReceiver mDownloadReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
		registerMyReceiver();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (null != mMenuView) {
			mMenuView.setMenuKeyListener(this);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterMyReceiver();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK != resultCode) {
			return;
		}
	}

	@Override
	public void setResultCode(int resultCode, int selectItem, String menuItem) {
		// TODO 暂停下载、正在下载
	}

	@Override
	public void onMenuKeyCompleted(int selectItem, String menuItem) {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_LIST, mDownloadResourceEntityList); // 菜单列表
		intent.putExtra(MenuConstant.INTENT_KEY_SELECTEDITEM, selectItem); // 当前菜单项
		int mType = LibraryConstant.MYLIBRARY_DOWNLOADING + type;
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, mType); // 数据类型
		intent.setClass(this, CommonFunctionMenu.class);
		startActivityForResult(intent, selectItem);
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		type = intent.getIntExtra(LibraryConstant.INTENT_KEY_TYPE, 0);
		mDownloadResourceEntityList = (ArrayList<DownloadResourceEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		mMenuList = getListFromDownloadResourceEntity(mDownloadResourceEntityList);
	}

	private ArrayList<String> getListFromDownloadResourceEntity(ArrayList<DownloadResourceEntity> listSrc) {
		ArrayList<String> list = new ArrayList<String>();
		if (null == listSrc) {
			return list;
		}
		String[] downloadState = getResources().getStringArray(R.array.library_download_state_list);
		String s;
		for (int i = 0; i < listSrc.size(); i++) {
			s = "";
			if (listSrc.get(i).status < downloadState.length) {
				s = downloadState[listSrc.get(i).status];
			}
			s = s + " " + listSrc.get(i).categoryFullName;
			if (LibraryConstant.DOWNLOAD_STATUS_GOING == listSrc.get(i).status) {
				s += String.format(this.getString(R.string.library_download_chapter_index), listSrc.get(i).curDownloadChapterIndex);
			}
			list.add(s);
		}

		return list;
	}

	// 必须在主线程中执行
	private void updateMenuList() {
		selectItem = getSelectItem();
		mMenuList = getListFromDownloadResourceEntity(mDownloadResourceEntityList);

		if (null == mMenuList || mMenuList.isEmpty()) {
			String s = getResources().getString(R.string.library_download_empty);
			PublicUtils.showToast(this, s, true);
			return;
		}

		setListData(mMenuList);
		if (selectItem >= mMenuList.size()) {
			selectItem = mMenuList.size() - 1;
		}
		mMenuView.setSelectItem(selectItem, true);
	}

	// 在子线程中执行
	private void getDownloadResourceEntity(Context mContext) {
		String username = PublicUtils.getUserName(DownloadList.this);
		DownloadResourceDBDao dao = new DownloadResourceDBDao(mContext);
		if (0 == type) {
			mDownloadResourceEntityList = dao.findAllUnCompleted(username);
		} else {
			mDownloadResourceEntityList = dao.findAllCompleted(username);
		}
		dao.closeDb();

		// 无下载任务
		if (null == mDownloadResourceEntityList) {
			mDownloadResourceEntityList = new ArrayList<DownloadResourceEntity>();
		} else if (0 == type) {
			DownloadChapterDBDao dcDao = new DownloadChapterDBDao(mContext);
			int size = mDownloadResourceEntityList.size();
			for (int i = 0; i < size; i++) {
				// 如果当前这本书正在下载
				if (LibraryConstant.DOWNLOAD_STATUS_GOING == mDownloadResourceEntityList.get(i).status) {
					// 得到所有的章节信息
					ArrayList<DownloadChapterEntity> list = dcDao.findAll(mDownloadResourceEntityList.get(i)._id);
					if (list != null) {
						int count = list.size();
						for (int j = 0; j < count; j++) {
							// 正在下载
							if (list.get(j).chapterStatus == LibraryConstant.DOWNLOAD_STATUS_GOING) {
								// 保存当前正在下载的章节序号
								mDownloadResourceEntityList.get(i).curDownloadChapterIndex = j;
								break;
							}
						}
					}
				}
			}
			dcDao.closeDb();
		}
	}

	// 在子线程中更新数据
	private void startUpdateThread() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				getDownloadResourceEntity(DownloadList.this);

				// 在主线程中刷新UI
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						updateMenuList();
					}
				});
			}
		}).start();
	}

	private void registerMyReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(LibraryConstant.ACTION_DOWNLOAD_STATUS);
		mDownloadReceiver = new DownloadReceiver();
		registerReceiver(mDownloadReceiver, filter);
	}

	private void unregisterMyReceiver() {
		if (null != mDownloadReceiver) {
			unregisterReceiver(mDownloadReceiver);
			mDownloadReceiver = null;
		}
	}

	// 下载服务发送的广播
	public class DownloadReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			MenuGlobal.debug("[DownloadReceiver] action = " + action);

			if (action.equals(LibraryConstant.ACTION_DOWNLOAD_STATUS)) {
				startUpdateThread();
			}
		}
	}
}
