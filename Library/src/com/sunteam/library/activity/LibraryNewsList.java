package com.sunteam.library.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.menu.menulistadapter.ShowView;
import com.sunteam.common.menu.menuview.OnMenuKeyListener;
import com.sunteam.common.utils.Tools;
import com.sunteam.library.R;
import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.entity.InformationEntity;
import com.sunteam.library.utils.EbookConstants;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;
import com.sunteam.library.utils.TextFileReaderUtils;

/**
 * @Destryption 图书馆新闻列表，新闻公告、服务资讯、文化活动三类资讯共用一个界面
 * @Author Jerry
 * @Date 2017-2-4 下午3:40:02
 * @Note
 */
public class LibraryNewsList extends MenuActivity implements OnMenuKeyListener, ShowView {
	private int dataType = 0; // 数据类别：电子书、有声书、口述影像
	private int bookCount = 0; // 当前类资源总数，在分页加载时，需要使用该值
	private String fatherPath;	//父目录路径
	private ArrayList<InformationEntity> mInformationEntityList;

	private Tools mTools;

	public void onCreate(Bundle savedInstanceState) {
		initView();
		super.onCreate(savedInstanceState);
		mMenuView.setShowView(this);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMenuView.setMenuKeyListener(this);
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

		int action = data.getIntExtra("action", EbookConstants.TO_NEXT_PART);
		switch (action) {
			case EbookConstants.TO_NEXT_PART:	//上一章
				mMenuView.down();
				mMenuView.enter();
				break;
			case EbookConstants.TO_PRE_PART:	//下一章
				mMenuView.up();
				mMenuView.enter();
				break;
			default:
				break;
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
			intent.putExtra("curChapter", selectItem); 	// 当前item序号
			intent.putExtra("totalChapter", mInformationEntityList.size()); // 总item数
			this.startActivityForResult(intent,selectItem);
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
		
		mTools = new Tools(this);
	}

	private ArrayList<String> getListFromInformationEntity(ArrayList<InformationEntity> listSrc) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < listSrc.size(); i++) {
			list.add(listSrc.get(i).title);
		}

		return list;
	}

	@Override
	public void onMenuKeyCompleted(int selectItem, String menuItem) {
		Intent intent = new Intent();
//		String title = getResources().getString(R.string.common_functionmenu); // 功能菜单
//		String[] list = getResources().getStringArray(R.array.library_resource_function_menu_list);
//		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, title); // 菜单名称
//		intent.putExtra(MenuConstant.INTENT_KEY_LIST, list); // 菜单列表
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, mTitle); // 分类名称
		intent.putExtra(LibraryConstant.INTENT_KEY_RESOURCE, menuItem);
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, dataType); // 数据类别：电子书、有声书、口述影像
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, fatherPath); // 父目录
		intent.setClass(this, ResourceFunctionMenu.class);

//		startActivityForResult(intent, selectItem);
	}

	@Override
	public View getView(Context context, final int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;

		if (null == convertView) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.library_menu_item, null);

			vh.tvIcon = (TextView) convertView.findViewById(R.id.library_menu_item_icon);

			vh.tvMenu = (TextView) convertView.findViewById(R.id.library_menu_item_childs);
//			vh.tvMenu.setOnClickListener(this);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.tvMenu.setTag(String.valueOf(position));

		int fontSize = mTools.getFontSize();
		vh.tvIcon.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel()); // 图标占用一个汉字宽度，随汉字字体大小而伸缩
		vh.tvIcon.setHeight(mTools.convertSpToPixel(fontSize));
		vh.tvIcon.setBackgroundResource(R.drawable.text);

		vh.tvMenu.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel());
		vh.tvMenu.setHeight(mTools.convertSpToPixel(fontSize));

		if (getSelectItem() == position) {
			convertView.setBackgroundColor(mTools.getHighlightColor());
			vh.tvMenu.setSelected(true);
		} else {
			convertView.setBackgroundColor(mTools.getBackgroundColor());
			vh.tvMenu.setSelected(false);
		}

		if (!TextUtils.isEmpty((CharSequence) mMenuList.get(position))) {
			vh.tvMenu.setText((CharSequence) mMenuList.get(position));
		} else {
			vh.tvMenu.setText("");
		}
		vh.tvMenu.setTextColor(mTools.getFontColor());

		return convertView;
	}

	private class ViewHolder {
		TextView tvIcon = null; // 图标
		TextView tvMenu = null; // 菜单项
	}

}
