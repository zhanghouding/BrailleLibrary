package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;

import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.RefreshScreenUtils;
import com.sunteam.common.utils.Tools;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.AddHistoryAsyncTask;
import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.entity.HistoryEntity;
import com.sunteam.library.entity.ReadMode;
import com.sunteam.library.entity.ReverseInfo;
import com.sunteam.library.utils.CustomToast;
import com.sunteam.library.utils.EbookConstants;
import com.sunteam.library.utils.FileOperateUtils;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.MediaPlayerUtils;
import com.sunteam.library.utils.PublicUtils;
import com.sunteam.library.utils.TTSUtils;
import com.sunteam.library.utils.TextFileReaderUtils;
import com.sunteam.library.view.TextReaderView;
import com.sunteam.library.view.TextReaderView.OnPageFlingListener;

/**
 * TXT文件显示
 * 
 * @author wzp
 */
public class ReadTxtActivity extends Activity implements OnPageFlingListener
{
	private static final String TAG = "ReadTxtActivity";
	private static final int MENU_CODE = 10;
	private TextView mTvTitle = null;
	private TextView mTvPageCount = null;
	private TextView mTvCurPage = null;
	private View mLine = null;
	private TextReaderView mTextReaderView = null;
	
	private boolean isNews = false;		//是否是盲人资讯
	private boolean isAuto = false;
	private boolean isReadPage = false;	//是否朗读页码
	private boolean isFinish;//是否读完
	private String chapterName;		//章节名称
	private String identifier;		//书本id
	private String dbCode;			//dbCode
	private String categoryName;	//categoryName
	private String resourceName;	//resourceName
	private String categoryCode;	//categoryCode
	private int curChapter;			//当前章节序号，从0开始
	private int totalChapter;		//总章节数目。
	private BookmarkEntity mBookmarkEntity;
	private boolean isEntryMenu = false;	//是否进入了功能菜单。
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TtsUtils.getInstance().stop();	//先暂停TTS播放。
		TtsUtils.getInstance().setMuteFlag(false);
		RefreshScreenUtils.enableRefreshScreen();
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);	//禁止休眠
		setContentView(R.layout.library_activity_read_txt);
		
		isNews = this.getIntent().getBooleanExtra("isNews", false);
		isAuto = this.getIntent().getBooleanExtra("isAuto", false);
		chapterName = this.getIntent().getStringExtra("chapterName");
		curChapter = this.getIntent().getIntExtra("curChapter", 0);
		totalChapter = this.getIntent().getIntExtra("totalChapter", 0);
		identifier = this.getIntent().getStringExtra(LibraryConstant.INTENT_KEY_IDENTIFIER);
		dbCode = this.getIntent().getStringExtra("dbCode");
		categoryName = this.getIntent().getStringExtra("categoryName");
		resourceName = this.getIntent().getStringExtra("resourceName");
		categoryCode = this.getIntent().getStringExtra("categoryCode");
		mBookmarkEntity = (BookmarkEntity) this.getIntent().getSerializableExtra("book_mark");
		
		Tools tools = new Tools(this);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(tools.getBackgroundColor())); // 设置窗口背景色
    	mTvTitle = (TextView)this.findViewById(R.id.library_main_title);
    	mTvPageCount = (TextView)this.findViewById(R.id.library_pageCount);
    	mTvCurPage = (TextView)this.findViewById(R.id.library_curPage);
    	mLine = (View)this.findViewById(R.id.library_line);
    	
    	mTvTitle.setTextColor(tools.getFontColor());
    	mTvPageCount.setTextColor(tools.getFontColor());
    	mTvCurPage.setTextColor(tools.getFontColor());
    	mLine.setBackgroundColor(tools.getFontColor()); // 设置分割线的背景色
    	
    	final float scale = this.getResources().getDisplayMetrics().density/0.75f;	//计算相对于ldpi的倍数;
		float fontSize = tools.getFontSize() * scale;
    	mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize-2*EbookConstants.LINE_SPACE*scale);
    	mTvPageCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, (fontSize-3*EbookConstants.LINE_SPACE)/2*scale);
    	mTvCurPage.setTextSize(TypedValue.COMPLEX_UNIT_PX, (fontSize-3*EbookConstants.LINE_SPACE)/2*scale);
    	mTvTitle.setHeight((int)fontSize); // 设置控件高度
    	mTvTitle.setText(chapterName);
    	mTvPageCount.setHeight((int)(fontSize/2));
    	mTvCurPage.setHeight((int)(fontSize/2));
    	
    	mTextReaderView = (TextReaderView) findViewById(R.id.library_read_txt_view);
    	mTextReaderView.setOnPageFlingListener(this);
    	mTextReaderView.setTextColor(tools.getFontColor());
    	mTextReaderView.setReverseColor(tools.getHighlightColor());
    	mTextReaderView.setBackgroundColor(tools.getBackgroundColor());
    	//mTextReaderView.setTextSize(tools.getFontSize());
    	   	
    	playMusic();
    	TTSUtils.getInstance().init(this);	//初始化TTS
    	if( mTextReaderView.openBook(TextFileReaderUtils.getInstance().getParagraphBuffer(0), TextFileReaderUtils.getInstance().getCharsetName(), 0, 0, 0, 0, isAuto, chapterName) == false )
    	{
    		
    		TTSUtils.getInstance().stop();
			TTSUtils.getInstance().OnTTSListener(null);
			PublicUtils.showToast( this, this.getString(R.string.library_checksum_error), new PromptListener() {
				@Override
				public void onComplete() 
				{
					// TODO Auto-generated method stub
					{
						exit();
					}
				}
			});
    	}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) 
	{
		// TODO 自动生成的方法存根
		super.onWindowFocusChanged(hasFocus);
		
		if( mBookmarkEntity != null )
		{
			int lineNumber = mTextReaderView.getlineNumber(mBookmarkEntity.begin);	//根据书签位置得到行号
			if( mTextReaderView.openBook(TextFileReaderUtils.getInstance().getParagraphBuffer(0), TextFileReaderUtils.getInstance().getCharsetName(), lineNumber, mBookmarkEntity.begin, 0, 0, true, chapterName) == false )
	    	{
	    		
	    		TTSUtils.getInstance().stop();
				TTSUtils.getInstance().OnTTSListener(null);
				PublicUtils.showToast( this, this.getString(R.string.library_checksum_error), new PromptListener() {
					@Override
					public void onComplete() 
					{
						// TODO Auto-generated method stub
						{
							exit();
						}
					}
				});
	    	}
		}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		TTSUtils.getInstance().init(this);	//初始化TTS
		if( isReadPage )
		{
			mTextReaderView.readPage();		//朗读页码
		}
		if( isEntryMenu )
		{
			isEntryMenu = false;		//是否进入了功能菜单。
			if( isReadPage )
			{
				mTextReaderView.enter();	//如果是从功能菜单退出的，则自动全文播放。
			}
		}
		
		isReadPage = true;
		playMusic();
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		isFinish = false;
		mTextReaderView.setIsPlayParagraph(!isFinish, false);
		switch( keyCode )
		{
			case KeyEvent.KEYCODE_DPAD_UP:		//上
				mTextReaderView.up();
				return	true;
			case KeyEvent.KEYCODE_DPAD_DOWN:	//下
				mTextReaderView.down();
				return	true;
			case KeyEvent.KEYCODE_DPAD_LEFT:	//左
				mTextReaderView.left();
				return	true;
			case KeyEvent.KEYCODE_DPAD_RIGHT:	//右
				mTextReaderView.right();
				return	true;
			case KeyEvent.KEYCODE_DPAD_CENTER:	//确定
			case KeyEvent.KEYCODE_ENTER:
				mTextReaderView.enter();
				return	true;
			case KeyEvent.KEYCODE_5:
			case KeyEvent.KEYCODE_NUMPAD_5:		//精读
				mTextReaderView.intensiveReading();
				return	true;
			case KeyEvent.KEYCODE_7:
			case KeyEvent.KEYCODE_NUMPAD_7:		//朗读上一个字
				mTextReaderView.preCharacter();
				return	true;
			case KeyEvent.KEYCODE_9:
			case KeyEvent.KEYCODE_NUMPAD_9:		//朗读下一个字
				mTextReaderView.nextCharacter(false);
				return	true;
			case KeyEvent.KEYCODE_4:
			case KeyEvent.KEYCODE_NUMPAD_4:		//朗读上一个词
				mTextReaderView.preWord();
				return	true;
			case KeyEvent.KEYCODE_6:
			case KeyEvent.KEYCODE_NUMPAD_6:		//朗读下一个词
				mTextReaderView.nextWord(false);
				return	true;
			case KeyEvent.KEYCODE_2:
			case KeyEvent.KEYCODE_NUMPAD_2:		//朗读上一个段落
				mTextReaderView.preParagraph();
				return	true;
			case KeyEvent.KEYCODE_8:
			case KeyEvent.KEYCODE_NUMPAD_8:		//朗读下一个段落
				mTextReaderView.nextParagraph(false);
				return	true;
			case KeyEvent.KEYCODE_1:
			case KeyEvent.KEYCODE_NUMPAD_1:		//开始选词
				if( mTextReaderView.startSelect() )
				{
					PublicUtils.showToast(this, this.getString(R.string.library_select_start));
				}
				return	true;
			case KeyEvent.KEYCODE_3:
			case KeyEvent.KEYCODE_NUMPAD_3:		//结束选词
				if( mTextReaderView.endSelect() )
				{
					PublicUtils.showToast(this, this.getString(R.string.library_select_end));
				}
				return	true;
			case KeyEvent.KEYCODE_0:
			case KeyEvent.KEYCODE_NUMPAD_0:		//百科查询
				return	true;
			case KeyEvent.KEYCODE_MENU:
				if( !isNews )
				{
					isEntryMenu = true;	//是否进入了功能菜单。
					MediaPlayerUtils.getInstance().stop();
					startFunctionMenu();
				}
				break;
			case KeyEvent.KEYCODE_STAR:			//反查
				String content = mTextReaderView.getReverseText();	//得到当前反显内容
				PublicUtils.jumpFanCha(this, content);
				break;
			case KeyEvent.KEYCODE_POUND:		//#号键
				mTextReaderView.readPage();		//朗读页码
				break;
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) 
	{
		switch( keyCode )
		{
			case KeyEvent.KEYCODE_2:
			case KeyEvent.KEYCODE_NUMPAD_2:		//朗读上一个段落
				mTextReaderView.setIsPlayParagraph(!isFinish, true);
				return	true;
			case KeyEvent.KEYCODE_8:
			case KeyEvent.KEYCODE_NUMPAD_8:		//朗读下一个段落
				mTextReaderView.setIsPlayParagraph(!isFinish, true);
				return	true;
			default:
				break;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	//到上一个章节
	private void toPreChapter()
	{
		if( 0 == curChapter )
		{
			isReadPage = false;
			String tips = this.getString(R.string.library_first_chapter);
			PublicUtils.showToast(this, tips, new PromptListener() {

				@Override
				public void onComplete() {
					// TODO Auto-generated method stub
					
				}
			});
		}
		else
		{
			TTSUtils.getInstance().stop();
			TTSUtils.getInstance().OnTTSListener(null);
			RefreshScreenUtils.disableRefreshScreen();
			TtsUtils.getInstance().setMuteFlag(true);
			Intent intent = new Intent();
			intent.putExtra("action", EbookConstants.TO_PRE_PART);
			setResult(RESULT_OK, intent);
			exit();
		}
	}
	
	//到下一个章节
	private void toNextChapter()
	{
		if( curChapter+1 < totalChapter )	//还有下一章节需要朗读
		{
			TTSUtils.getInstance().stop();
			TTSUtils.getInstance().OnTTSListener(null);
			RefreshScreenUtils.disableRefreshScreen();
			TtsUtils.getInstance().setMuteFlag(true);
			Intent intent = new Intent();
			intent.putExtra("action", EbookConstants.TO_NEXT_PART);
			setResult(RESULT_OK, intent);
			exit();
		}
		else
		{
			isReadPage = false;
			String tips = this.getString(R.string.library_last_chapter);
			PublicUtils.showToast(this, tips, new PromptListener() {

				@Override
				public void onComplete() {
					// TODO Auto-generated method stub
					exit();
				}	//如果到最后一个章节，退出到章节列表界面。
			});
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(RESULT_OK == resultCode)
		{
			if(null != data)
			{
				int action = data.getIntExtra("action", EbookConstants.TO_NEXT_PART);
				switch (action) 
				{
					case EbookConstants.TO_NEXT_PART:
						toNextChapter();	//到下一个章节
						break;
					case EbookConstants.TO_PRE_PART:
						toPreChapter();		//到上一个章节
						break;
					case EbookConstants.TO_BOOK_START:	//到一本书的开头
						break;
					case EbookConstants.TO_PART_START:	//到一个部分的开头
						isReadPage = false;
						mTextReaderView.setCurPage(1);
						break;
					case EbookConstants.TO_PART_PAGE:	//到一个部分的某页
						isReadPage = false;
						int curPage = data.getIntExtra("page", 1);
						mTextReaderView.setCurPage(curPage);
						break;
					case EbookConstants.TO_BOOK_MARK:	//到某个书签位置
						BookmarkEntity entity = (BookmarkEntity) data.getSerializableExtra("book_mark");
						if( entity != null )
						{
							if( entity.chapterIndex == curChapter )	//如果在同一章跳转
							{
								int lineNumber = mTextReaderView.getlineNumber(entity.begin);	//根据书签位置得到行号
								if( mTextReaderView.openBook(TextFileReaderUtils.getInstance().getParagraphBuffer(0), TextFileReaderUtils.getInstance().getCharsetName(), lineNumber, entity.begin, 0, 0, true, chapterName) == false )
						    	{
						    		
						    		TTSUtils.getInstance().stop();
									TTSUtils.getInstance().OnTTSListener(null);
									PublicUtils.showToast( this, this.getString(R.string.library_checksum_error), new PromptListener() {
										@Override
										public void onComplete() 
										{
											// TODO Auto-generated method stub
											{
												exit();
											}
										}
									});
						    	}
							}
							else	//在不同章节跳转
							{
								TTSUtils.getInstance().stop();
								TTSUtils.getInstance().OnTTSListener(null);
								RefreshScreenUtils.disableRefreshScreen();
								TtsUtils.getInstance().setMuteFlag(true);
								setResult(RESULT_OK, data);
								exit();
							}
						}
						break;
					default:
						break;
				}
			}
		}
	}
	
	//播放背景音乐
	@SuppressWarnings("deprecation")
	private void playMusic()
	{
		int mode = Context.MODE_WORLD_READABLE + Context.MODE_MULTI_PROCESS;
		SharedPreferences shared = getSharedPreferences(EbookConstants.SETTINGS_TABLE, mode);
		int isMusic = shared.getInt(EbookConstants.MUSICE_STATE, 0);
		if(0 == isMusic)
		{
			String path = shared.getString(EbookConstants.MUSICE_PATH, null);
			if(null == path)
			{
				path = FileOperateUtils.getFirstMusicInDir();
			}
			else
			{
				File file = new File(path);
				if(!file.exists())
				{
					path = FileOperateUtils.getFirstMusicInDir();
				}
			}
			if(null != path)
			{
				MediaPlayerUtils.getInstance().play(path, true);
			}
		}
		else
		{
			MediaPlayerUtils.getInstance().stop();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		TtsUtils.getInstance().restoreSettingParameters(); // 退出文本朗读界面后使用系统设置进行朗读
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		MediaPlayerUtils.getInstance().stop();
		TextFileReaderUtils.getInstance().destroy();
	}
	
	@Override
	public void onPageFlingToTop() 
	{
		// TODO Auto-generated method stub
		/*
		String tips = this.getString(R.string.library_to_top);
		PublicUtils.showToast(this, tips);
		*/
		isFinish = true;
		CustomToast.showToast(this, this.getString(R.string.library_to_top), Toast.LENGTH_SHORT);
	}

	@Override
	public void onPageFlingToBottom( boolean isContinue ) 
	{
		isFinish = true;
		// TODO Auto-generated method stub
		if( !isContinue || ( mTextReaderView.getReadMode() != ReadMode.READ_MODE_ALL ) )
		{
			PublicUtils.showToast(this, this.getString(R.string.library_to_bottom));
			return;
		}
		
		toNextChapter();	//到下一个章节
	}

	@Override
	public void onPageFlingCompleted(int curPage) 
	{
		// TODO Auto-generated method stub
		mTvCurPage.setText(curPage+"");
	}

	@Override
	public void onLoadCompleted(int pageCount, int curPage) 
	{
		// TODO Auto-generated method stub
		mTvPageCount.setText(pageCount+"");
		mTvCurPage.setText(curPage+"");
	}
	
	//退出此界面
	private void back()
	{
		TTSUtils.getInstance().stop();
		TTSUtils.getInstance().OnTTSListener(null);
		exit();
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) 
	{  
		if( event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN )
		{
			back();
			return true;   
		}     
	     
		return super.dispatchKeyEvent(event);
	}

	public void startFunctionMenu()
	{
		int pageCount = mTextReaderView.getPageCount();	//总页码
		int curPage = mTextReaderView.getCurPage();		//当前页码
		String reverseText = mTextReaderView.getReverseText();	//反显内容
		float percent = mTextReaderView.getCurPercent();	//当前阅读百分比
		ReverseInfo ri = mTextReaderView.getReverseInfo();
		
		Intent intent = getIntent();
		intent.putExtra("page_count", pageCount);
		intent.putExtra("page_cur", curPage);
		intent.putExtra("page_text", reverseText);
		
		BookmarkEntity entity = new BookmarkEntity();
		entity.userName = PublicUtils.getUserName(this);
		entity.bookId = identifier;
		entity.begin = ri.startPos;
		entity.chapterIndex = curChapter;
		entity.chapterTitle = chapterName;
		entity.markName = chapterName + " " + String.format(this.getString(R.string.library_page_read_tips2), curPage) + " " + reverseText;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		entity.percent = decimalFormat.format(percent)+"%";
		intent.putExtra("book_mark", entity);

		intent.setClass(this, EbookFunctionMenu.class);
		startActivityForResult(intent, MENU_CODE);
	}
	
	private void exit()
	{
		if( isNews )
		{
			finish();
			
			return;
		}
	
		HistoryEntity entity = new HistoryEntity();
		
		entity.userName = PublicUtils.getUserName(this);			//用户名
	    entity.title = resourceName;							//标题
	    entity.dbCode = dbCode;									//数据编码
	    entity.sysId = identifier;								//bookId
	    entity.resType = LibraryConstant.LIBRARY_DATATYPE_EBOOK;				//资源类型 1:有声读物 2:电子图书  3:视频影像
	    entity.lastChapterIndex = curChapter;	//最后阅读的章节序号
	    entity.enterPoint = "00:00:00";			//最后阅读的音视频时间点，格式"00:00:00"
	    entity.bookTitle = resourceName;		//标题
	    float percent = mTextReaderView.getCurPercent();	//当前阅读百分比
	    DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		entity.percent = decimalFormat.format(percent)+"%";	//电子书阅读进度，格式"0.00%"
	    entity.categoryFullName = PublicUtils.getCategoryName(this, entity.resType) + "-" + categoryName + "-" + resourceName;	//完整的分类名，格式"有声读物-刘兰芳-古今荣耻谈"
	    entity.categoryCode = categoryCode;		//分类编码
		
		new AddHistoryAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, entity);
	}
}
