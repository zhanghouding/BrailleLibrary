package com.sunteam.library.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.menu.MenuListAdapter;
import com.sunteam.common.menu.menulistadapter.OnEnterListener;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.CommonUtils;
import com.sunteam.common.utils.Tools;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.GetAudioChapterAsyncTask;
import com.sunteam.library.asynctask.GetEbookChapterAsyncTask;
import com.sunteam.library.asynctask.GetSearchResultAsyncTask;
import com.sunteam.library.asynctask.GetVideoChapterAsyncTask;
import com.sunteam.library.entity.EbookNodeEntity;
import com.sunteam.library.listener.LibraryResultListener;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.LogUtils;
import com.sunteam.library.utils.PublicUtils;

/**
 * @Destryption 资源检索浏览界面；焦点一直在编辑框，按
 * @Author Jerry
 * @Date 2017-2-25 下午3:21:20
 * @Note
 */
@SuppressLint({ "NewApi", "InflateParams", "DefaultLocale" })
public class LibrarySearchView extends View implements TextWatcher, OnEnterListener, LibraryResultListener {
	private Context mContext = null;
	private View mView = null;
	private String mTitle;
	private TextView mTvTitle = null;
	private View mLine = null;
	private EditText mEditText;
	private ListView listView = null;
//	private TextView mTvEmpty = null; // 列表为空时的提示信息
	private MenuListAdapter mAdapter = null;
	private ArrayList<String> mMenuList = new ArrayList<String>();
	private ArrayList<EbookNodeEntity> mEbookNodeEntityList;
	private int focusPos = 0; // 焦点位置:0默认在编辑框,1列表框;当按【OK】键时根据焦点位置做不同处理;删除或输入时置0，按方向键时置1;其它键不变

	private boolean isScanning = true;
	
	// true 按下键时读列表第一条记录; 因为在重新检索后会朗读第一行，其实可以不使用该属性；之前是想通过该属性区分当前焦点在编辑框还是列表栏;
	// private boolean isResumeList = true;

	private long firstTime = 0;
	private long lastTime = 0; // 按键时间，处理长按键：按住不放时，每间隔1秒处理一次按键
	private boolean keyUpFlag = false;
	private int longKeyCode = 0; // 长按键值，0 表示没有按键; 只处理上键和下键
//	private int itemHeight;
//	private int dividerHeight;
//	private int visibleItemCount;

	public View getView() {
		return mView;
	}

	public LibrarySearchView(final Context context, String title) throws Exception {
		super(context);

		mContext = context;
		mTitle = title;
		initView();
	}

	// 在通过功能菜单设置了词典后，需要重新设置词典名称
	public void onResume() {
		mEditText.requestFocus();
		String inputStr = mEditText.getText().toString();
		if(inputStr.isEmpty()){
			inputStr = mEditText.getHint().toString();
		}
		TtsUtils.getInstance().speak(mTitle + "," + inputStr);
	}

	private void initView() {
		mView = LayoutInflater.from(mContext).inflate(R.layout.library_search_view, null);

		mTvTitle = (TextView) mView.findViewById(R.id.library_search_view_title); // 标题栏
		mLine = (View) mView.findViewById(R.id.library_search_view_line); // 分割线
		mEditText = (EditText) mView.findViewById(R.id.library_search_view_editText); // 编辑框
		listView = (ListView) mView.findViewById(R.id.library_search_view_menu_list); // listview
//		mTvEmpty = (TextView) mView.findViewById(R.id.library_search_view_list_empty); // 列表为空时的提示信息

		Tools mTools = new Tools(mContext);
		int fontSize = mTools.getFontSize();
		mView.setBackgroundColor(mTools.getBackgroundColor()); // 设置View的背景色
		mTvTitle.setText(mTitle);
		mTvTitle.setTextColor(mTools.getFontColor()); // 设置title的前景色
		mLine.setBackgroundColor(mTools.getFontColor()); // 设置分割线的背景色
		mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel()); // 设置title字号
		mTvTitle.setHeight(mTools.convertSpToPixel(fontSize));

		mEditText.addTextChangedListener(this);
		mEditText.setHintTextColor(mTools.getFontColor()); // 设置edit hint的前景色
		mEditText.setTextColor(mTools.getFontColor()); // 设置edit的前景色
		mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTools.getFontPixel());
		mEditText.setHeight(mTools.convertSpToPixel(fontSize));
		mEditText.setOnKeyListener(mOnKeyListener);
		mEditText.setText("三国"); // 测试时避免输入

		mAdapter = new MenuListAdapter(mContext, listView, mMenuList, LibrarySearchView.this);
		mAdapter.setOnEnterListener(this);
		listView.setAdapter(mAdapter);
		listView.setFocusable(false); // 不获取焦点
		listView.setDividerHeight(0);

		if(mMenuList.isEmpty()){
//			mTvEmpty.setTextColor(mTools.getFontColor()); // 设置title的前景色
//			listView.setVisibility(View.INVISIBLE);
//			mTvEmpty.setVisibility(View.INVISIBLE);
		}
	}

	private boolean isScanning() {
		return isScanning;
	}

	private void setScanning(boolean isScanning) {
		this.isScanning = isScanning;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean ret = false;
		long time = event.getEventTime();
		LogUtils.d("[onKeyDown]1 keyCode=" + keyCode + ", keyUpFlag = " + keyUpFlag + ", time = " + time + ", isScanning = " + isScanning());

		if (keyUpFlag) {
			keyUpFlag = false;
			longKeyCode = 0;
			setScanning(false);
		}

		if (0 == event.getRepeatCount()) {
			// LogUtils.d("[onKeyDown]2 keyCode=" + keyCode + ", keyUpFlag = "
			// + keyUpFlag + ", isScanning = " +
			// isScanning()+", firstTime = "+time);
			firstTime = time;
			lastTime = time;
			setScanning(false);
			ret = processKeyEnevt(keyCode, event);
		} else if (time - lastTime >= MenuConstant.SHORT_PRESS_TIME) {
			LogUtils.d("[onKeyDown]3 keyCode=" + keyCode + ", keyUpFlag = " + keyUpFlag + ", isScanning = " + isScanning());
			lastTime = time;
			ret = processKeyEnevt(keyCode, event);
		}

		if ((KeyEvent.KEYCODE_DPAD_UP == keyCode || KeyEvent.KEYCODE_DPAD_DOWN == keyCode) && time - firstTime >= MenuConstant.LONG_PRESS_TIME) {
			if (!isScanning()) {
				LogUtils.d("[onKeyDown]4 keyCode=" + keyCode + ", keyUpFlag = " + keyUpFlag + ", isScanning = " + isScanning());
				longKeyCode = keyCode;
				setScanning(true);
			}
		}

		return ret;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		keyUpFlag = true;
		return processKeyEnevt(keyCode, event);
	}

	private boolean processKeyEnevt(int keyCode, KeyEvent event) {
		boolean ret = true;
		int action = event.getAction();
		LogUtils.d("[processKeyEnevt]action = " + action + ", keyCode=" + keyCode + ", isScanning = " + isScanning());

		// 虽然按下时间到达了自动浏览的条件，但没有抬起的情况下，还是按重复按键处理，直到抬起后才按自动浏览处理。
		if (isScanning() && keyUpFlag) {
			return ret;
		}

		if (KeyEvent.ACTION_DOWN == action) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_ENTER:
				PublicUtils.hideMsgIputKeyboard((Activity) mContext);
				if (0 == focusPos) {
					startSearch();
				} else {
					mAdapter.enter();
				}
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				PublicUtils.hideMsgIputKeyboard((Activity) mContext);
				mAdapter.up();
//				scrollView();
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				PublicUtils.hideMsgIputKeyboard((Activity) mContext);
				mAdapter.down();
//				scrollView();
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				PublicUtils.hideMsgIputKeyboard((Activity) mContext);
				mAdapter.left(); //scrollPgup();
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				PublicUtils.hideMsgIputKeyboard((Activity) mContext);
				mAdapter.right(); //scrollPgdn();
				break;
			case KeyEvent.KEYCODE_BACK:
				PublicUtils.hideMsgIputKeyboard((Activity) mContext);
				focusPos = 0;
				ret = processKeyBack();
				break;
			default:
				ret = false;
				break;
			}
		} else if (KeyEvent.ACTION_UP == action) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_ENTER:
				// 已经在按下事件中处理了
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_DPAD_LEFT: 
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				// 已经在按下事件中处理了
				focusPos = 1;
				break;
			case KeyEvent.KEYCODE_MENU: // 菜单键只能在抬起事件中处理，以便与长按菜单键区分开来
				PublicUtils.hideMsgIputKeyboard((Activity) mContext);
//				startSearch();
				break;
			default:
				ret = false;
				break;
			}
		}
		return ret;
	}

	// 处理【退出】键:1.焦点不在编辑框时，直接返回；2.焦点在编辑框且编辑框内容为空时直接返回；3.焦点在编辑框且编辑框内容不为空则删除编辑框尾部字符
	private boolean processKeyBack() {
		if (!mEditText.isFocused() || mEditText.getText().toString().isEmpty()) {
			return false;
		} else {
			CommonUtils.sendKeyEvent(KeyEvent.KEYCODE_DEL);
			return true;
		}
	}

	/*private void scrollView() {
		if (mMenuList == null || 0 == mMenuList.size()) {
			return;
		}
		calcItemCount();
		int selectPos = mAdapter.getSelectItem();
		int firstPos = listView.getFirstVisiblePosition();
		int lastPos = listView.getLastVisiblePosition();
		int top = ((View) listView.getChildAt(0)).getTop();
//		MenuGlobal.debug("[scrollView]selectPos=" + selectPos + ", firstPos=" + firstPos + ", lastPos=" + lastPos + ", top=" + top + ", visibleItemCount=" + visibleItemCount);
		if (selectPos < firstPos) {
			listView.setSelection(selectPos);
		} else if (selectPos == firstPos && top < 0) {
			listView.setSelection(selectPos);
		} else if (selectPos >= lastPos) {
			int y = (itemHeight + dividerHeight) * (visibleItemCount - 1);
			listView.setSelectionFromTop(selectPos, y);
		}
	}

	private void scrollPgup() {
		if (mMenuList == null || 0 == mMenuList.size()) {
			return;
		}
		calcItemCount();
		int selectPos = mAdapter.getSelectItem();
		int firstPos = listView.getFirstVisiblePosition();
//		int lastPos = listView.getLastVisiblePosition();
//		int top = ((View) listView.getChildAt(0)).getTop();
//		MenuGlobal.debug("[scrollPgup]selectPos=" + selectPos + ", firstPos=" + firstPos + ", lastPos=" + lastPos + ", top=" + top);
		int y;

		if (firstPos >= visibleItemCount) { // 焦点行位置不变，但数据要刷新
			y = itemHeight * (selectPos - firstPos);
			selectPos -= visibleItemCount;
			mAdapter.setSelectItem(selectPos);
			listView.setSelectionFromTop(selectPos, y);
		} else if (0 == selectPos) { // 已经在第一行，跳到最后一页的最后一行
			// MenuGlobal.showToast(mContext, R.string.turn2end, null, -1);
			// MenuGlobal.getTts().speak(mContext.getResources().getString(MenuResource.getIdByName(mContext,
			// "string",
			// "turn2end")));
			selectPos = mAdapter.getCount() - 1;
			mAdapter.setSelectItem(selectPos, TtsUtils.TTS_QUEUE_ADD);
			y = (selectPos >= visibleItemCount) ? (visibleItemCount - 1) : (selectPos - 1);
			listView.setSelectionFromTop(selectPos, itemHeight * y);
		} else if (selectPos >= visibleItemCount) { // 焦点行以上还有一页
			selectPos -= visibleItemCount;
			mAdapter.setSelectItem(selectPos);
			listView.setSelectionFromTop(selectPos, itemHeight * selectPos);
		} else {
			mAdapter.setSelectItem(0);
			listView.setSelectionFromTop(0, 0);
		}
	}

	private void scrollPgdn() {
		if (mMenuList == null || 0 == mMenuList.size()) {
			return;
		}
		calcItemCount();
		int selectPos = mAdapter.getSelectItem();
		int firstPos = listView.getFirstVisiblePosition();
		int lastPos = listView.getLastVisiblePosition();
//		int top = ((View) listView.getChildAt(0)).getTop();
//		MenuGlobal.debug("[scrollPgup]selectPos=" + selectPos + ", firstPos=" + firstPos + ", lastPos=" + lastPos + ", top=" + top);
		int y;

		if (lastPos + visibleItemCount < mAdapter.getCount()) {
			y = itemHeight * (selectPos - firstPos);
			selectPos += visibleItemCount;
			mAdapter.setSelectItem(selectPos);
			listView.setSelectionFromTop(selectPos, y);
		} else if ((mAdapter.getCount() - 1) == selectPos) {
			// MenuGlobal.showToast(mContext, R.string.turn2start, null, -1);
			// MenuGlobal.getTts().speak(mContext.getResources().getString(MenuResource.getIdByName(mContext,
			// "string",
			// "turn2start"));
			mAdapter.setSelectItem(0, TtsUtils.TTS_QUEUE_ADD);
			listView.setSelectionFromTop(0, 0);
		} else if (selectPos + visibleItemCount < mAdapter.getCount()) {
			selectPos += visibleItemCount;
			mAdapter.setSelectItem(selectPos);
			y = itemHeight * (selectPos - (mAdapter.getCount() - visibleItemCount));
			listView.setSelectionFromTop(selectPos, y);
		} else {
			selectPos = mAdapter.getCount() - 1;
			mAdapter.setSelectItem(selectPos);
			y = (mAdapter.getCount() >= visibleItemCount) ? (visibleItemCount - 1) : (selectPos - 1);
			listView.setSelectionFromTop(selectPos, y);
		}
	}

	private void calcItemCount() {
		int listHeight = listView.getHeight(); // listView.getMeasuredHeight();
		itemHeight = ((View) listView.getChildAt(0)).getHeight();
		int divideHeight = listView.getDividerHeight();
		visibleItemCount = listHeight / (itemHeight + divideHeight);

		LogUtils.d("[calcItemCount]itemHeight=" + itemHeight + ", listHeight=" + listHeight + ", divideHeight=" + divideHeight + ", visibleItemCount="
				+ visibleItemCount);
	}*/

	public void processLongKey() {
		if (!keyUpFlag) { // 如果没有抬起，则不作为长按键处理
			releaseWakeLock();
			return;
		}
		if (KeyEvent.KEYCODE_DPAD_UP == longKeyCode) {
			acquireWakeLock(mContext);
			mAdapter.up();
//			scrollView();
		} else if (KeyEvent.KEYCODE_DPAD_DOWN == longKeyCode) {
			acquireWakeLock(mContext);
			mAdapter.down();
//			scrollView();
		} else {
			releaseWakeLock();
			setScanning(false);
			longKeyCode = 0;
		}

	}

	// 在编辑控件中截获按键
	View.OnKeyListener mOnKeyListener = new View.OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
//			LogUtils.d("[onKey] keyCode=" + keyCode + ", action = " + event.getAction());
			if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode || KeyEvent.KEYCODE_DPAD_RIGHT == keyCode || KeyEvent.KEYCODE_DPAD_CENTER == keyCode
					|| KeyEvent.KEYCODE_ENTER == keyCode) {
				// 截获左右键、OK键, 编辑框不处理这三个键，由词典列表界面处理
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					onKeyDown(keyCode, event);
				} else {
					onKeyUp(keyCode, event);
				}
				return true;
			} else if (KeyEvent.KEYCODE_STAR == keyCode) { // 在设置了输入法后，键盘上的数字键、星号键和井号键已经被输入法所消费!
			// if (event.getAction() == KeyEvent.ACTION_UP) {
			// keyUpFlag = true;
			// CommonUtils.sendKeyEvent(KeyEvent.KEYCODE_DEL);
			// }
			// return true;
			}

			// 把其它键都传给下一个控件
			return false;
		}
	};

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		if (0 == after && count > 0) {
			String s1 = s.toString().substring(start, start + count);
			s1 = mContext.getResources().getString(R.string.common_delete) + " " + s1 + ",";
			TtsUtils.getInstance().speak(s1);
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// 朗读新增数据；暂不朗读，因为afterTextChanged()中朗读完整字符串
		if (count <= 0) {
			return;
		}
		String s1 = s.toString().substring(start, start + count);
		TtsUtils.getInstance().speak(s1);
	}

	@Override
	public void afterTextChanged(Editable s) {
		// 朗读完整字符串
		String s1 = s.toString();
		if(s1.isEmpty()){
			s1 = mEditText.getHint().toString();
		}
		TtsUtils.getInstance().speak(s1, TtsUtils.TTS_QUEUE_ADD);
		focusPos = 0;
	}

	// 进入章节列表界面
	public void onEnterCompleted(int selectItem, String itemStr) {
		EbookNodeEntity entity = mEbookNodeEntityList.get(selectItem);
		String dbCode = entity.dbCode;
		String sysId = entity.sysId;
		String identifier = entity.identifier;
		String categoryCode = entity.categoryCode;
		String categoryName = entity.categoryName;
		String fatherPath = LibraryConstant.LIBRARY_ROOT_PATH;
		String title = entity.title;
		
		switch( entity.resType )
		{
			case LibraryConstant.LIBRARY_DATATYPE_EBOOK:
				fatherPath += (PublicUtils.getCategoryName(mContext, entity.resType)+"/"+categoryName+"/");
				new GetEbookChapterAsyncTask(mContext, fatherPath, title).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbCode, sysId, categoryName, identifier, categoryCode);
				break;
			case LibraryConstant.LIBRARY_DATATYPE_AUDIO:
				fatherPath += (PublicUtils.getCategoryName(mContext, entity.resType)+"/"+categoryName+"/");
				new GetAudioChapterAsyncTask(mContext, fatherPath, title).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbCode, sysId, categoryName, identifier, categoryCode);
				break;
			case LibraryConstant.LIBRARY_DATATYPE_VIDEO:
				fatherPath += (PublicUtils.getCategoryName(mContext, entity.resType)+"/"+categoryName+"/");
				new GetVideoChapterAsyncTask(mContext, fatherPath, title).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbCode, sysId, categoryName, identifier, categoryCode);
				break;
			default:
				break;
		}
	}

	// 启动检索
	private void startSearch() {
		String inputStr = mEditText.getText().toString();
		if (!inputStr.isEmpty()) {
			String pageIndex = "1";
			String pageSize = LibraryConstant.LIBRARY_RESOURCE_PAGESIZE+"";
			new GetSearchResultAsyncTask(mContext, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pageIndex, pageSize, inputStr);
		} else {
			inputStr = mEditText.getHint().toString();
			TtsUtils.getInstance().speak(inputStr);
		}
	}

	private WakeLock mWakeLock = null;
	@SuppressWarnings("deprecation")
	private void acquireWakeLock(Context context) {
		if (null == mWakeLock) {
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, context.getClass().getName());
			mWakeLock.acquire();
		}
	}

	private void releaseWakeLock() {
		if (null != mWakeLock && mWakeLock.isHeld()) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onResult(ArrayList<Object> list) {
		focusPos = 1;
		mEbookNodeEntityList = (ArrayList)list;
		mMenuList.clear();
		for (int i = 0; i < mEbookNodeEntityList.size(); i++) {
			mMenuList.add(mEbookNodeEntityList.get(i).categoryFullName);
		}
		mAdapter.setListData(mMenuList);
		mAdapter.setSelectItem(0);
	}

	@Override
	public void onFail(String error) {
		String s = mContext.getResources().getString(R.string.library_search_resource_empty);
		PublicUtils.showToast(mContext, s);
		focusPos = 0;
	}
}