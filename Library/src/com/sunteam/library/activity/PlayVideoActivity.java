package com.sunteam.library.activity;

import java.io.File;

import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDetectBigUrlFileListener;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.sunteam.common.utils.RefreshScreenUtils;
import com.sunteam.common.utils.Tools;
import com.sunteam.library.R;
import com.sunteam.library.utils.EbookConstants;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.MediaPlayerUtils;
import com.sunteam.library.utils.MediaPlayerUtils.PlayStatus;

/**
 * 视频播放界面
 * 
 * @author wzp
 */
public class PlayVideoActivity extends Activity
{
	private static final String TAG = "PlayVideoActivity";
	private TextView mTvTitle = null;
	private View mLine = null;
	private String filename;
	private String fatherPath;
	private String videoUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RefreshScreenUtils.enableRefreshScreen();
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);	//禁止休眠
		setContentView(R.layout.library_activity_play_video);
		
		filename = this.getIntent().getStringExtra("filename");
		fatherPath = this.getIntent().getStringExtra(LibraryConstant.INTENT_KEY_FATHER_PATH);
		videoUrl = this.getIntent().getStringExtra(LibraryConstant.INTENT_KEY_URL);
		
		Tools tools = new Tools(this);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(tools.getBackgroundColor())); // 设置窗口背景色
    	mTvTitle = (TextView)this.findViewById(R.id.library_main_title);
    	mLine = (View)this.findViewById(R.id.library_line);
    	
    	mTvTitle.setTextColor(tools.getFontColor());
    	mLine.setBackgroundColor(tools.getFontColor()); // 设置分割线的背景色
    	
    	final float scale = this.getResources().getDisplayMetrics().density/0.75f;	//计算相对于ldpi的倍数;
		float fontSize = tools.getFontSize() * scale;
    	mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize-2*EbookConstants.LINE_SPACE*scale);
    	mTvTitle.setHeight((int)fontSize); // 设置控件高度
    	mTvTitle.setText(filename);
    	
    	final String fullpath = fatherPath + getFilename(videoUrl);
    	File file = new File(fullpath);
    	if( file.exists() )
    	{
    		MediaPlayerUtils.getInstance().play(fullpath);	//播放音频
    	}
    	else
    	{
	    	FileDownloader.detect(videoUrl, new OnDetectBigUrlFileListener() {
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
	    	MediaPlayerUtils.getInstance().play(videoUrl);	//播放视频
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
	
	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		switch( keyCode )
		{
			case KeyEvent.KEYCODE_DPAD_UP:		//上
				return	true;
			case KeyEvent.KEYCODE_DPAD_DOWN:	//下
				return	true;
			case KeyEvent.KEYCODE_DPAD_LEFT:	//左
				return	true;
			case KeyEvent.KEYCODE_DPAD_RIGHT:	//右
				return	true;
			case KeyEvent.KEYCODE_DPAD_CENTER:	//确定
			case KeyEvent.KEYCODE_ENTER:
				if( MediaPlayerUtils.getInstance().getPlayStatus() == PlayStatus.PLAY )
				{
					MediaPlayerUtils.getInstance().pause();
				}
				else if( MediaPlayerUtils.getInstance().getPlayStatus() == PlayStatus.PAUSE )
				{
					MediaPlayerUtils.getInstance().resume();
				}
				return	true;
			case KeyEvent.KEYCODE_5:
			case KeyEvent.KEYCODE_NUMPAD_5:		//精读
				return	true;
			case KeyEvent.KEYCODE_7:
			case KeyEvent.KEYCODE_NUMPAD_7:		//朗读上一个字
				return	true;
			case KeyEvent.KEYCODE_9:
			case KeyEvent.KEYCODE_NUMPAD_9:		//朗读下一个字
				return	true;
			case KeyEvent.KEYCODE_4:
			case KeyEvent.KEYCODE_NUMPAD_4:		//朗读上一个词
				return	true;
			case KeyEvent.KEYCODE_6:
			case KeyEvent.KEYCODE_NUMPAD_6:		//朗读下一个词
				return	true;
			case KeyEvent.KEYCODE_2:
			case KeyEvent.KEYCODE_NUMPAD_2:		//朗读上一个段落
				return	true;
			case KeyEvent.KEYCODE_8:
			case KeyEvent.KEYCODE_NUMPAD_8:		//朗读下一个段落
				return	true;
			case KeyEvent.KEYCODE_1:
			case KeyEvent.KEYCODE_NUMPAD_1:		//开始选词
				return	true;
			case KeyEvent.KEYCODE_3:
			case KeyEvent.KEYCODE_NUMPAD_3:		//结束选词
				return	true;
			case KeyEvent.KEYCODE_0:
			case KeyEvent.KEYCODE_NUMPAD_0:		//百科查询
				return	true;
			case KeyEvent.KEYCODE_MENU:
				break;
			case KeyEvent.KEYCODE_STAR:			//反查
				break;
			case KeyEvent.KEYCODE_POUND:		//#号键
				break;
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	//退出此界面
	private void back( boolean isSetResult )
	{
		MediaPlayerUtils.getInstance().stop();
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
