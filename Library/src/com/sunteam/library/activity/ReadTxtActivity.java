package com.sunteam.library.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sunteam.common.utils.RefreshScreenUtils;
import com.sunteam.common.utils.Tools;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.entity.ReadMode;
import com.sunteam.library.utils.CustomToast;
import com.sunteam.library.utils.EbookConstants;
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
	private TextView mTvTitle = null;
	private TextView mTvPageCount = null;
	private TextView mTvCurPage = null;
	private View mLine = null;
	private TextReaderView mTextReaderView = null;
	private boolean isAuto = false;
	private boolean isReadPage = false;	//是否朗读页码
	private boolean isFinish;//是否读完
	private String filename;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RefreshScreenUtils.enableRefreshScreen();
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);	//禁止休眠
		setContentView(R.layout.library_activity_read_txt);
		
		isAuto = this.getIntent().getBooleanExtra("isAuto", false);
		filename = this.getIntent().getStringExtra("filename");
		
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
    	mTvTitle.setText(filename);
    	mTvPageCount.setHeight((int)(fontSize/2));
    	mTvCurPage.setHeight((int)(fontSize/2));
    	
    	mTextReaderView = (TextReaderView) findViewById(R.id.library_read_txt_view);
    	mTextReaderView.setOnPageFlingListener(this);
    	mTextReaderView.setTextColor(tools.getFontColor());
    	mTextReaderView.setReverseColor(tools.getHighlightColor());
    	mTextReaderView.setBackgroundColor(tools.getBackgroundColor());
    	//mTextReaderView.setTextSize(tools.getFontSize());
    	   	
    	if( mTextReaderView.openBook(TextFileReaderUtils.getInstance().getParagraphBuffer(0), TextFileReaderUtils.getInstance().getCharsetName(), 0, 0, 0, 0, isAuto, filename) == false )
    	{
    		
    		TTSUtils.getInstance().stop();
			TTSUtils.getInstance().OnTTSListener(null);
			PublicUtils.showToast( this, this.getString(R.string.library_checksum_error), new PromptListener() {
				@Override
				public void onComplete() 
				{
					// TODO Auto-generated method stub
					{
						back(true);
					}
				}
			});
    	}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		if( isReadPage )
		{
			mTextReaderView.readPage();		//朗读页码
		}
		isReadPage = true;
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(RESULT_OK == resultCode){
			if(null != data){
				int result = data.getIntExtra("result", 0);
				switch(result){
				case 10:
					isReadPage = false;
					int curPage = data.getIntExtra("page", 1);
					mTextReaderView.setCurPage(curPage);
					break;
				}
			}
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
	
	@Override
	public void onPageFlingToTop() 
	{
		// TODO Auto-generated method stub
		/*
		String tips = this.getString(R.string.library_to_top);
		PublicUtils.showToast(this, tips);
		*/
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
		
		{
			String tips = this.getString(R.string.library_has_finished_reading_the_last_book);
			PublicUtils.showToast(this, tips);
		}
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
	private void back( boolean isSetResult )
	{
		TTSUtils.getInstance().stop();
		TTSUtils.getInstance().OnTTSListener(null);
		if( isSetResult )
		{
			setResult(RESULT_OK);
		}
		finish();
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) 
	{  
		if( event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN )
		{
			back(true);
			return true;   
		}     
	     
		return super.dispatchKeyEvent(event);
	}
}
