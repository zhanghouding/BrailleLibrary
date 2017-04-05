package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.menu.menulistadapter.ShowView;
import com.sunteam.common.utils.Tools;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.GetInformationAsyncTask;
import com.sunteam.library.utils.LibraryConstant;

/**
 * @Destryption 图书馆新闻分类列表，是固定菜单
 * @Author Jerry
 * @Date 2017-2-4 下午3:38:19
 * @Note
 */
public class LibraryNewsCategoryList extends MenuActivity implements ShowView {
	private String fatherPath;	//父目录路径
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
		new GetInformationAsyncTask(this, fatherPath, menuItem).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1, LibraryConstant.LIBRARY_INFO_PAGESIZE, selectItem);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}

	private void initView() {
		fatherPath = this.getIntent().getStringExtra(LibraryConstant.INTENT_KEY_FATHER_PATH);

		mTools = new Tools(this);
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
		vh.tvIcon.setBackgroundResource(R.drawable.folder);

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
