package com.sunteam.library.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.ArrayUtils;
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.dialog.ConfirmListener;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.AddCollectCategoryAsyncTask;
import com.sunteam.library.asynctask.AddCollectResourceAsyncTask;
import com.sunteam.library.entity.CollectCategoryEntity;
import com.sunteam.library.entity.CollectResourceEntity;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

public class ResourceFunctionMenu extends MenuActivity {
	private String dbCode;			//数据库编码
	private String sysId;			//系统id
	private String categoryName;	//分类名称
	private String categoryCode;	//分类编码
	private String resourceName;	//资源名称
	private int dataType = 0; // 数据类别：电子书、有声书、口述影像
	private String fatherPath;	//父目录路径

	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		TtsUtils.getInstance().restoreSettingParameters();
		super.onResume();
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
		case 0: // 收藏分类
			{
				CollectCategoryEntity entity = new CollectCategoryEntity();
				entity.categoryCode = categoryCode;
				entity.categoryName = categoryName;
				entity.userName = PublicUtils.getUserName(this);
				entity.resType = dataType;
				entity.categoryFullName = PublicUtils.getCategoryName(this, dataType) + "-" + categoryName;
				
				new AddCollectCategoryAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, entity);
			}
			break;
		case 1: // 收藏当前资源
			{
				CollectResourceEntity entity = new CollectResourceEntity();

				entity.title = resourceName;
				entity.dbCode = dbCode;
				entity.sysId = sysId;
				entity.userName = PublicUtils.getUserName(this);
				entity.resType = dataType;
				entity.categoryFullName = PublicUtils.getCategoryName(this, dataType) + "-" + categoryName + "-" + resourceName;
				
				new AddCollectResourceAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, entity);
			}
			break;
		case 2: // 删除当前资源
			deleteConfirm(1);
			break;
		case 3: // 清空分类资源
			deleteConfirm(0);
			break;
		default:
			break;
		}
	}

	private void initView() {
		Intent intent = getIntent();
		categoryName = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		resourceName = intent.getStringExtra(LibraryConstant.INTENT_KEY_RESOURCE);
		dataType = intent.getIntExtra(LibraryConstant.INTENT_KEY_TYPE, 0);
		fatherPath = intent.getStringExtra(LibraryConstant.INTENT_KEY_FATHER_PATH);
		categoryCode = intent.getStringExtra(LibraryConstant.INTENT_KEY_CATEGORY_CODE);
		dbCode = intent.getStringExtra(LibraryConstant.INTENT_KEY_DBCODE);
		sysId = intent.getStringExtra(LibraryConstant.INTENT_KEY_SYSID);
		mTitle = getResources().getString(R.string.common_functionmenu);
		mMenuList = ArrayUtils.strArray2List(getResources().getStringArray(R.array.library_resource_function_menu_list));
	}

	// type:0清空分类资源;1删除当前资源
	private void deleteConfirm(final int type) {
		String title = this.getString(R.string.library_dialog_clear);
		if(0 != type){
			title = this.getString(R.string.library_dialog_delete);
		}
		ConfirmDialog mConfirmDialog = new ConfirmDialog(this, title);
		mConfirmDialog.setConfirmListener(new ConfirmListener() {

			@Override
			public void doConfirm() {
				String hint;
				if(0 == type){
					clearResources();
					hint = getString(R.string.library_dialog_clear_su);
				} else{
					deleteResource();
					hint = getString(R.string.library_dialog_delete_su);
				}
				PublicUtils.showToast(ResourceFunctionMenu.this, hint, true);
			}

			@Override
			public void doCancel() {

			}
		});
		mConfirmDialog.show();
	}

	// 清空本地分类资源
	private void clearResources() {
		File file = new File( fatherPath );
		PublicUtils.deleteFiles(file);
		PublicUtils.createCacheDir(fatherPath, "");	//创建缓存目录(因为用deleteFiles会连fatherPath也给删除了，所以必须重建)
	}

	// 删除当前资源
	private void deleteResource() {
		String path = fatherPath + resourceName + "/";
		File file = new File( path );
		PublicUtils.deleteFiles(file);
	}

}
