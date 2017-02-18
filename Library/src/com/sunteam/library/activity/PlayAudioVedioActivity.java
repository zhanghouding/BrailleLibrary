package com.sunteam.library.activity;

import java.io.File;
import java.text.DecimalFormat;

import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDetectBigUrlFileListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.RefreshScreenUtils;
import com.sunteam.common.utils.Tools;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.library.R;
import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.utils.EbookConstants;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.MediaPlayerUtils;
import com.sunteam.library.utils.MediaPlayerUtils.OnMediaPlayerListener;
import com.sunteam.library.utils.MediaPlayerUtils.PlayStatus;
import com.sunteam.library.utils.PublicUtils;

/**
 * 音视频播放界面
 * 
 * @author wzp
 */
public class PlayAudioVedioActivity extends Activity implements OnMediaPlayerListener
{
	private static final String TAG = "PlayAudioVedioActivity";
	private static final int MENU_CODE = 10;
	private TextView mTvTitle = null;
	private View mLine = null;
	private ImageButton mIbStatus = null;
	private TextView mTvNum = null;
	private TextView mTvStartTime = null;
	private TextView mTvEndTime = null;
	private SeekBar mSeekBar = null;
	private String filename;		//章节名称
	private String fatherPath;		//父目录
	private String resourceUrl;		//资源url
	private int totalTime;			//总时间
	private int curChapter;			//当前章节序号，从0开始
	private int totalChapter;		//总章节数目。
	private String identifier;		//书本id
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TtsUtils.getInstance().stop();	//先暂停TTS播放。
		TtsUtils.getInstance().setMuteFlag(false);
		RefreshScreenUtils.enableRefreshScreen();
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);	//禁止休眠
		setContentView(R.layout.library_activity_play_audio_vedio);
		
		identifier = this.getIntent().getStringExtra("identifier");
		filename = this.getIntent().getStringExtra("filename");
		fatherPath = this.getIntent().getStringExtra(LibraryConstant.INTENT_KEY_FATHER_PATH);
		resourceUrl = this.getIntent().getStringExtra(LibraryConstant.INTENT_KEY_URL);
		curChapter = this.getIntent().getIntExtra("curChapter", 0);
		totalChapter = this.getIntent().getIntExtra("totalChapter", 0);
		String num = (curChapter+1)+"/"+totalChapter;
		
		Tools tools = new Tools(this);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(tools.getBackgroundColor())); // 设置窗口背景色
    	mTvTitle = (TextView)this.findViewById(R.id.library_main_title);
    	mLine = (View)this.findViewById(R.id.library_line);
    	mIbStatus = (ImageButton)this.findViewById(R.id.library_ib_status);
    	mTvNum = (TextView)this.findViewById(R.id.library_num);
    	mTvStartTime = (TextView)this.findViewById(R.id.library_starttime);
    	mTvEndTime = (TextView)this.findViewById(R.id.library_totaltime);
    	mSeekBar = (SeekBar)this.findViewById(R.id.library_seek_bar);
    	
    	mTvTitle.setTextColor(tools.getFontColor());
    	mTvNum.setTextColor(tools.getFontColor());
    	mTvStartTime.setTextColor(tools.getFontColor());
    	mTvEndTime.setTextColor(tools.getFontColor());
    	mLine.setBackgroundColor(tools.getFontColor()); // 设置分割线的背景色
    	
    	final float scale = this.getResources().getDisplayMetrics().density/0.75f;	//计算相对于ldpi的倍数;
		float fontSize = tools.getFontSize() * scale;
    	mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize-2*EbookConstants.LINE_SPACE*scale);
    	mTvTitle.setHeight((int)fontSize); // 设置控件高度
    	mTvTitle.setText(filename);
    	
    	mTvNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize-2*EbookConstants.LINE_SPACE*scale);
    	mTvNum.setHeight((int)fontSize); // 设置控件高度
    	mTvNum.setText(num);
    	
    	mTvStartTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize-2*EbookConstants.LINE_SPACE*scale);
    	mTvStartTime.setHeight((int)fontSize); // 设置控件高度
    	//mTvStartTime.setText(num);
    	
    	mTvEndTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize-2*EbookConstants.LINE_SPACE*scale);
    	mTvEndTime.setHeight((int)fontSize); // 设置控件高度
    	//mTvEndTime.setText(num);
    	
    	if( ( null == resourceUrl ) || TextUtils.isEmpty(resourceUrl) || "null".equalsIgnoreCase(resourceUrl) )
    	{
    		String tips = this.getString(R.string.library_resource_does_not_exist);
			PublicUtils.showToast(this, tips, new PromptListener() {

				@Override
				public void onComplete() {
					// TODO Auto-generated method stub
					finish();
				}
			});
    	}
    	else
    	{
	    	MediaPlayerUtils.getInstance().OnMediaPlayerListener(this);
	    	
	    	final String fullpath = fatherPath + getFilename(resourceUrl);
	    	File file = new File(fullpath);
	    	if( file.exists() )
	    	{
	    		MediaPlayerUtils.getInstance().play(fullpath, false);	//播放音频
	    		totalTime = MediaPlayerUtils.getInstance().getTotalTime()/1000;
	    		showTime(mTvEndTime, totalTime);	//显示总时间
	    		mHandler.sendEmptyMessageDelayed(0, 500);
	    	}
	    	else
	    	{
		    	FileDownloader.detect(resourceUrl, new OnDetectBigUrlFileListener() {
		    		@Override
		    		public void onDetectNewDownloadFile(String url, String fileName, String saveDir, long fileSize) 
		    		{
		    			// 如果有必要，可以改变文件名称fileName和下载保存的目录saveDir
		    			FileDownloader.createAndStart(url, fatherPath, fileName);
		    		}
		    		
		    		@Override
		    		public void onDetectUrlFileExist(String url) 
		    		{
		    			FileDownloader.start(url);	
		    			//如果文件没被下载过，将创建并开启下载，否则继续下载，自动会断点续传（如果服务器无法支持断点续传将从头开始下载）
		    		}
		    		
		    		@Override
		    		public void onDetectUrlFileFailed(String url, DetectBigUrlFileFailReason failReason) 
		    		{
		    			// 探测一个网络文件失败了，具体查看failReason
		    		}
		    	});
		    	MediaPlayerUtils.getInstance().play(resourceUrl, false);	//播放音视频
		    	totalTime = MediaPlayerUtils.getInstance().getTotalTime()/1000;
	    		showTime(mTvEndTime, totalTime);	//显示总时间
		    	mHandler.sendEmptyMessageDelayed(0, 500);
	    	}
    	}
	}
	
	//得到文件名
	private String getFilename( String url )
	{
		int seq = url.lastIndexOf("/");
		if( -1 == seq )
		{
			return	url;
		}
		
		return	url.substring(seq+1);
	}
	
	//显示时间
	private void showTime( TextView tv, int time )
	{
		if( time >= 0 )
		{
			int h = time / 3600;
			int m = (time % 3600) / 60;
			int s = (time % 3600) % 60;
			
			if( 0 == h )
			{
				tv.setText(String.format("%02d:%02d", m, s));
			}
			else
			{
				tv.setText(String.format("%02d:%02d:%02d", h, m, s));
			}
		}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	//到上一个章节
	private void toPreChapter()
	{
		if( MediaPlayerUtils.getInstance().getPlayStatus() == PlayStatus.PLAY )
		{
			MediaPlayerUtils.getInstance().pause();
			mIbStatus.setBackgroundResource(R.drawable.pause);
		}
		if( 0 == curChapter )
		{
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
			RefreshScreenUtils.disableRefreshScreen();
			MediaPlayerUtils.getInstance().stop();
			Intent intent = new Intent();
			intent.putExtra("action", EbookConstants.TO_PRE_PART);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
	
	//到下一个章节
	private void toNextChapter()
	{
		if( MediaPlayerUtils.getInstance().getPlayStatus() == PlayStatus.PLAY )
		{
			MediaPlayerUtils.getInstance().pause();
			mIbStatus.setBackgroundResource(R.drawable.pause);
		}
		if( totalChapter-1 == curChapter )
		{
			String tips = this.getString(R.string.library_last_chapter);
			PublicUtils.showToast(this, tips, new PromptListener() {

				@Override
				public void onComplete() {
					// TODO Auto-generated method stub
					MediaPlayerUtils.getInstance().stop();
					finish();
				}	//如果到最后一个章节，退出到章节列表界面。
			});
		}
		else
		{
			RefreshScreenUtils.disableRefreshScreen();
			MediaPlayerUtils.getInstance().stop();
			TtsUtils.getInstance().setMuteFlag(true);
			Intent intent = new Intent();
			intent.putExtra("action", EbookConstants.TO_NEXT_PART);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		switch( keyCode )
		{
			case KeyEvent.KEYCODE_DPAD_UP:		//上(到上一个章节)
				toPreChapter();
				return	true;
			case KeyEvent.KEYCODE_DPAD_DOWN:	//下(到下一个章节)
				toNextChapter();
				return	true;
			case KeyEvent.KEYCODE_DPAD_LEFT:	//左(快退)
				MediaPlayerUtils.getInstance().fastBackward();
				mHandler.removeMessages(0);
				mHandler.sendEmptyMessage(0);
				return	true;
			case KeyEvent.KEYCODE_DPAD_RIGHT:	//右(快进)
				MediaPlayerUtils.getInstance().fastForward();
				mHandler.removeMessages(0);
				mHandler.sendEmptyMessage(0);
				return	true;
			case KeyEvent.KEYCODE_DPAD_CENTER:	//确定(暂停、恢复)
			case KeyEvent.KEYCODE_ENTER:
				if( MediaPlayerUtils.getInstance().getPlayStatus() == PlayStatus.PLAY )
				{
					MediaPlayerUtils.getInstance().pause();
					mIbStatus.setBackgroundResource(R.drawable.pause);
				}
				else if( MediaPlayerUtils.getInstance().getPlayStatus() == PlayStatus.PAUSE )
				{
					MediaPlayerUtils.getInstance().resume();
					mIbStatus.setBackgroundResource(R.drawable.play);
				}
				return	true;
			case KeyEvent.KEYCODE_MENU:
				MediaPlayerUtils.getInstance().pause();
				mIbStatus.setBackgroundResource(R.drawable.pause);
				startFunctionMenu();
				return	true;
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		MediaPlayerUtils.getInstance().OnMediaPlayerListener(null);
	}

	//退出此界面
	private void back()
	{
		MediaPlayerUtils.getInstance().OnMediaPlayerListener(null);
		MediaPlayerUtils.getInstance().stop();
		mHandler.removeMessages(0);
		finish();
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
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg)
		{
			switch( msg.what )
			{
				case 0:	//显示播放时间，设置进度。
					int curTime = MediaPlayerUtils.getInstance().getCurTime()/1000;
		    		showTime(mTvStartTime, curTime);	//显示当前时间
		    		mSeekBar.setProgress(curTime*10000/totalTime);
		    		mHandler.sendEmptyMessageDelayed(0, 500);
					break;
				default:
					break;
			}
		};
	};

	@Override
	public void onPlayCompleted() 
	{
		// TODO 自动生成的方法存根
		toNextChapter();
	}

	@Override
	public void onPlayError() {
		// TODO 自动生成的方法存根
		mHandler.removeMessages(0);
		String tips = this.getString(R.string.library_speak_error);
		PublicUtils.showToast(this, tips, new PromptListener() {

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void onPlayProgress(int percent) {
		// TODO 自动生成的方法存根
		
	}

	public void startFunctionMenu()
	{
		Intent intent = getIntent();
		// TODO 传递功能菜单所需参数

		BookmarkEntity entity = new BookmarkEntity();
		entity.userName = PublicUtils.getUserName();
		entity.bookId = identifier;
		entity.begin = MediaPlayerUtils.getInstance().getCurTime();
		entity.chapterIndex = curChapter;
		entity.chapterTitle = filename;
		entity.markName = filename + " " + mTvStartTime.getText();
		float percent = MediaPlayerUtils.getInstance().getPercent();
		DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		entity.percent = decimalFormat.format(percent)+"%";
		intent.putExtra("book_mark", entity);
		intent.putExtra("percent", percent);

		intent.setClass(this, AudioFunctionMenu.class);
		startActivityForResult(intent, MENU_CODE);
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
						MediaPlayerUtils.getInstance().seek(0);
						MediaPlayerUtils.getInstance().resume();
						mIbStatus.setBackgroundResource(R.drawable.play);
						break;
					case EbookConstants.TO_PART_PAGE:	//到一个部分的百分比
						float percent = data.getFloatExtra("percent", 0.0f);
						MediaPlayerUtils.getInstance().seek(percent);
						MediaPlayerUtils.getInstance().resume();
						mIbStatus.setBackgroundResource(R.drawable.play);
						break;
					default:
						break;
				}
			}
		}
	}	
}
