package com.sunteam.library.activity;

import java.io.File;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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

import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.RefreshScreenUtils;
import com.sunteam.common.utils.Tools;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.jni.SunteamJni;
import com.sunteam.library.R;
import com.sunteam.library.asynctask.AddHistoryAsyncTask;
import com.sunteam.library.entity.BookmarkEntity;
import com.sunteam.library.entity.HistoryEntity;
import com.sunteam.library.utils.EbookConstants;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.MediaPlayerUtils;
import com.sunteam.library.utils.MediaPlayerUtils.OnMediaPlayerListener;
import com.sunteam.library.utils.MediaPlayerUtils.PlayStatus;
import com.sunteam.library.utils.PublicUtils;
import com.sunteam.library.utils.TTSUtils;

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
	
	private String chapterName;		//章节名称
	private String fatherPath;		//父目录
	private String resourceUrl;		//资源url
	private int resType;			//资源类型
	private int totalTime;			//总时间
	private int curChapter;			//当前章节序号，从0开始
	private int totalChapter;		//总章节数目。
	private String sysId;			//sysid
	private String dbCode;
	private String resourceName;
	private String categoryName;
	private String categoryCode;
	private BookmarkEntity mBookmarkEntity;
	private String fullpath = null;
	private boolean isRunThead = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TtsUtils.getInstance().stop();	//先暂停TTS播放。
		TtsUtils.getInstance().setMuteFlag(false);
		RefreshScreenUtils.enableRefreshScreen();
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);	//禁止休眠
		setContentView(R.layout.library_activity_play_audio_vedio);
		
		mBookmarkEntity = (BookmarkEntity) this.getIntent().getSerializableExtra("book_mark");
		sysId = this.getIntent().getStringExtra("sysId");
		dbCode = this.getIntent().getStringExtra("dbCode");
		resourceName = this.getIntent().getStringExtra("resourceName");
		categoryName = this.getIntent().getStringExtra("categoryName");
		categoryCode = this.getIntent().getStringExtra("categoryCode");
		resType = this.getIntent().getIntExtra(LibraryConstant.INTENT_KEY_TYPE, LibraryConstant.LIBRARY_DATATYPE_AUDIO);
		chapterName = this.getIntent().getStringExtra("chapterName");
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
    	mTvTitle.setText(chapterName);
    	
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
					exit();
				}
			});
    	}
    	else
    	{
	    	MediaPlayerUtils.getInstance().OnMediaPlayerListener(this);
	    	
	    	fullpath = fatherPath + getFilename(resourceUrl);
	    	File file = new File(fullpath);
	    	if( file.exists() )
	    	{
	    		SunteamJni mSunteamJni = new SunteamJni();
	    		mSunteamJni.decryptFile(fullpath);	//解密文件
	    		
	    		MediaPlayerUtils.getInstance().play(fullpath, false);	//播放音频
	    		if( mBookmarkEntity != null )
		    	{
		    		MediaPlayerUtils.getInstance().seek(mBookmarkEntity.begin);
		    	}
	    		totalTime = MediaPlayerUtils.getInstance().getTotalTime()/1000;
	    		showTime(mTvEndTime, totalTime);	//显示总时间
	    		mHandler.sendEmptyMessageDelayed(0, 500);
	    	}
	    	else
	    	{
	    		/*
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
		    	*/	//不在后台自动缓存文件了。
	    		
	    		fullpath = null;
		    	MediaPlayerUtils.getInstance().play(resourceUrl, false);	//播放音视频
		    	if( mBookmarkEntity != null )
		    	{
		    		MediaPlayerUtils.getInstance().seek(mBookmarkEntity.begin);
		    	}
		    	totalTime = MediaPlayerUtils.getInstance().getTotalTime()/1000;
	    		showTime(mTvEndTime, totalTime);	//显示总时间
		    	mHandler.sendEmptyMessageDelayed(0, 500);
	    	}
    	}
    	
    	new Thread() {
			@Override
			public void run() {
				while( isRunThead )
				{
					if( MediaPlayerUtils.getInstance().getPlayStatus() == PlayStatus.PLAY )
					{
						PublicUtils.execShellCmd("input tap 0 0");		//不断发送模拟点击消息，不让系统进入休眠状态。
					}
					try 
					{
						Thread.sleep(5000);
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
				}
			}
		}.start();
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
					exit();
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
			exit();
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
				int pos = MediaPlayerUtils.getInstance().getCurTime();
				pos -= 3000;
				if( pos > 0 )
				{
					MediaPlayerUtils.getInstance().fastBackward();
					mHandler.removeMessages(0);
					mHandler.sendEmptyMessage(0);
				}
				else
				{
					MediaPlayerUtils.getInstance().pause();
					PublicUtils.showToast(this, this.getString(R.string.library_chapter_start_tips), new PromptListener() {

						@Override
						public void onComplete() {
							// TODO Auto-generated method stub
							MediaPlayerUtils.getInstance().fastBackward();
							MediaPlayerUtils.getInstance().resume();
							mIbStatus.setBackgroundResource(R.drawable.play);
							mHandler.removeMessages(0);
							mHandler.sendEmptyMessage(0);
						}
					});
				}
				
				return	true;
			case KeyEvent.KEYCODE_DPAD_RIGHT:	//右(快进)
				int pos1 = MediaPlayerUtils.getInstance().getCurTime();
				pos1 += 3000;
				int total = MediaPlayerUtils.getInstance().getTotalTime();
				if( pos1 < total )
				{
					MediaPlayerUtils.getInstance().fastForward();
					mHandler.removeMessages(0);
					mHandler.sendEmptyMessage(0);
				}
				else
				{
					MediaPlayerUtils.getInstance().pause();
					PublicUtils.showToast(this, this.getString(R.string.library_chapter_end_tips), new PromptListener() {

						@Override
						public void onComplete() {
							// TODO Auto-generated method stub
							toNextChapter();
						}
					});
				}
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
		isRunThead = false;
		MediaPlayerUtils.getInstance().OnMediaPlayerListener(null);
		if( fullpath != null )
		{
			SunteamJni mSunteamJni = new SunteamJni();
			mSunteamJni.encryptFile(fullpath);	//加密文件。
		}
	}

	//退出此界面
	private void back()
	{
		MediaPlayerUtils.getInstance().OnMediaPlayerListener(null);
		MediaPlayerUtils.getInstance().stop();
		mHandler.removeMessages(0);
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
	
	private String getCurTime( String time )
	{
		String[] split = time.split(":");
		if( null == split )
		{
			return	time;
		}
		
		if( 2 == split.length )
		{
			return	split[0]+this.getString(R.string.library_minute)+split[1]+this.getString(R.string.library_second);
		}
		else if( 3 == split.length )
		{
			return	split[0]+this.getString(R.string.library_hour)+split[1]+this.getString(R.string.library_minute)+split[2]+this.getString(R.string.library_second);
		}
		
		return	time;
	}

	public void startFunctionMenu()
	{
		Intent intent = getIntent();
		// TODO 传递功能菜单所需参数

		BookmarkEntity entity = new BookmarkEntity();
		entity.userName = PublicUtils.getUserName(this);
		entity.bookId = sysId;
		entity.begin = MediaPlayerUtils.getInstance().getCurTime();
		entity.chapterIndex = curChapter;
		entity.chapterTitle = chapterName;
		entity.markName = chapterName + " " + getCurTime((String) mTvStartTime.getText()) ;
		float percent = MediaPlayerUtils.getInstance().getPercent();
		DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
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
						MediaPlayerUtils.getInstance().resume();
						MediaPlayerUtils.getInstance().seek(0);
						mHandler.removeMessages(0);
						mHandler.sendEmptyMessage(0);
						break;
					case EbookConstants.TO_PART_PAGE:	//到一个部分的百分比
						MediaPlayerUtils.getInstance().resume();
						float percent = data.getFloatExtra("percent", 0.0f);
						MediaPlayerUtils.getInstance().seek(percent);
						mHandler.removeMessages(0);
						mHandler.sendEmptyMessage(0);
						break;
					case EbookConstants.TO_BOOK_MARK:	//到某个书签位置
						BookmarkEntity entity = (BookmarkEntity) data.getSerializableExtra("book_mark");
						if( entity != null )
						{
							if( entity.chapterIndex == curChapter )	//如果在同一章跳转
							{
								MediaPlayerUtils.getInstance().seek(entity.begin);
								MediaPlayerUtils.getInstance().resume();
								mIbStatus.setBackgroundResource(R.drawable.play);
							}
							else	//在不同章节跳转
							{
								RefreshScreenUtils.disableRefreshScreen();
								TTSUtils.getInstance().stop();
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
			else
			{
				MediaPlayerUtils.getInstance().resume();
				mIbStatus.setBackgroundResource(R.drawable.play);
			}
		}
		else
		{
			MediaPlayerUtils.getInstance().resume();
			mIbStatus.setBackgroundResource(R.drawable.play);
		}
	}
	
	private void exit()
	{
		HistoryEntity entity = new HistoryEntity();
		
		int time = MediaPlayerUtils.getInstance().getCurTime();
		int h = time / 3600;
		int m = (time % 3600) / 60;
		int s = (time % 3600) % 60;
		
		entity.userName = PublicUtils.getUserName(this);			//用户名
	    entity.title = resourceName;							//标题
	    entity.dbCode = dbCode;									//数据编码
	    entity.sysId = sysId;									//sysId
	    entity.resType = resType;				//资源类型 1:有声读物 2:电子图书  3:视频影像
	    entity.lastChapterIndex = curChapter;	//最后阅读的章节序号
	    entity.enterPoint = String.format("%02d:%02d:%02d", h, m, s);			//最后阅读的音视频时间点，格式"00:00:00"
	    entity.bookTitle = resourceName;		//标题
	    float percent = MediaPlayerUtils.getInstance().getPercent();
		DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		entity.percent = decimalFormat.format(percent)+"%";
	    entity.categoryFullName = PublicUtils.getCategoryName(this, entity.resType) + "-" + categoryName + "-" + resourceName;	//完整的分类名，格式"有声读物-刘兰芳-古今荣耻谈"
	    entity.categoryCode = categoryCode;		//分类编码
		
		new AddHistoryAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, entity);
	}	
}
