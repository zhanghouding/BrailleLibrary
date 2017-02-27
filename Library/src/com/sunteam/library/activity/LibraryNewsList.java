package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.library.entity.InformationEntity;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;
import com.sunteam.library.utils.TextFileReaderUtils;

/**
 * @Destryption 图书馆新闻列表，新闻公告、服务资讯、文化活动三类资讯共用一个界面
 * @Author Jerry
 * @Date 2017-2-4 下午3:40:02
 * @Note
 */
public class LibraryNewsList extends MenuActivity {
	private int dataType = 0; // 数据类别：电子书、有声书、口述影像
	private int bookCount = 0; // 当前类资源总数，在分页加载时，需要使用该值
	private String fatherPath;	//父目录路径
	private ArrayList<InformationEntity> mInformationEntityList;

	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
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
		// TODO 进入对应的新闻资讯播放界面
		
		try 
		{
			InformationEntity entity = mInformationEntityList.get(selectItem);
			String fullpath = fatherPath+PublicUtils.format(entity.title+entity.date)+LibraryConstant.CACHE_FILE_SUFFIX;
			TextFileReaderUtils.getInstance().init(fullpath);
			
			Intent intent = new Intent( this, ReadTxtActivity.class );
			intent.putExtra("isNews", true);
			intent.putExtra("chapterName", menuItem);
			this.startActivity(intent);
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra(MenuConstant.INTENT_KEY_TITLE);
		mInformationEntityList = (ArrayList<InformationEntity>) intent.getSerializableExtra(MenuConstant.INTENT_KEY_LIST);
		mMenuList = getListFromInformationEntity(mInformationEntityList);
		dataType = intent.getIntExtra(LibraryConstant.INTENT_KEY_TYPE, dataType);
		bookCount = mMenuList.size();
		bookCount = intent.getIntExtra(LibraryConstant.INTENT_KEY_BOOKCOUNT, bookCount);
		fatherPath = this.getIntent().getStringExtra(LibraryConstant.INTENT_KEY_FATHER_PATH);
	}

	private ArrayList<String> getListFromInformationEntity(ArrayList<InformationEntity> listSrc) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < listSrc.size(); i++) {
			list.add(listSrc.get(i).title);
		}

		return list;
	}

}
