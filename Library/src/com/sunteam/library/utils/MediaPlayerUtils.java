package com.sunteam.library.utils;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Handler;
import android.os.Message;

/**
 * MediaPlayer工具类。
 * 
 * @author wzp
 */
public class MediaPlayerUtils
{
	private static final int MSG_PLAY_COMPLETION = 100;		//播放完成
	private static final int MSG_PLAY_PROGRESS = 200;			//播放进度
	private static MediaPlayerUtils instance = null;
	private MediaPlayer mMediaPlayer = null;
	private OnMediaPlayerListener mOnMediaPlayerListener = null;
	private PlayStatus mPlayStatus = PlayStatus.STOP;
	private AdvancedCountdownTimer mCountDownTimer = null;
	
	public interface OnMediaPlayerListener 
	{
		public void onPlayCompleted();				//播放完成
		public void onPlayError();					//播放错误
		public void onPlayProgress(int percent);	//播放进度
	}
	
	public enum PlayStatus
	{
		STOP, 	//停止
		PAUSE,	//暂停
		PLAY,	//播放
	}	//播放状态
	
	//设置监听器
	public void OnMediaPlayerListener( OnMediaPlayerListener listener )
	{
		mOnMediaPlayerListener = listener;
	}
	
	//得到当前播放状态
	public PlayStatus getPlayStatus()
	{
		return	mPlayStatus;
	}
	
	public static MediaPlayerUtils getInstance()
	{
		if( null == instance )
		{
			instance = new MediaPlayerUtils();
		}
		
		return instance;
	}
	
	//初始化
	public void init()
	{
		if( null == mMediaPlayer )
		{
			mMediaPlayer = new MediaPlayer();
		}
	}
	
	//销毁
	public void destroy()
	{
		stop();
		if( mMediaPlayer != null )
		{
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	//得到进度百分比
	public float getPercent()
	{
		if( mMediaPlayer != null )
		{
			try
			{
				return	(float)mMediaPlayer.getCurrentPosition() * 100.0f / (float)mMediaPlayer.getDuration();
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
		
		return	0.0f;
	}
	
	//设置进度百分比
	public boolean seek( float progress )
	{
		if( mMediaPlayer != null )
		{
			try
			{
				int total = mMediaPlayer.getDuration();
				mMediaPlayer.seekTo((int)(total*progress/100.0f));
				
				return	true;
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
		
		return	false;
	}
	
	//得到总时间
	public int getTotalTime()
	{
		if( mMediaPlayer != null )
		{
			return	mMediaPlayer.getDuration();
		}
		
		return	0;
	}
	
	//得到当前播放的时间
	public int getCurTime()
	{
		if( mMediaPlayer != null )
		{
			return	mMediaPlayer.getCurrentPosition();
		}
		
		return	0;
	}
	
	//快进
	public boolean fastForward()
	{
		if( mMediaPlayer != null )
		{
			try
			{
				int pos = mMediaPlayer.getCurrentPosition();
				pos += 3000;
				mMediaPlayer.seekTo(pos);
				
				return	true;
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
		
		return	false;
	}

	//快退
	public boolean fastBackward()
	{
		if( mMediaPlayer != null )
		{
			try
			{
				int pos = mMediaPlayer.getCurrentPosition();
				pos -= 3000;
				mMediaPlayer.seekTo(pos);
				
				return	true;
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
		
		return	false;
	}
	
	//暂停
	public void pause()
	{
		if( mMediaPlayer != null )
		{
			if( PlayStatus.PLAY == mPlayStatus )
			{
				if( mCountDownTimer != null )
				{
					mCountDownTimer.pause();
				}
				mMediaPlayer.pause();
				mPlayStatus = PlayStatus.PAUSE;
			}	//如果正在播放，先暂停
		}
	}
	
	//恢复
	public void resume()
	{
		if( mMediaPlayer != null )
		{
			if( PlayStatus.PAUSE == mPlayStatus )
			{
				if( mCountDownTimer != null )
				{
					mCountDownTimer.resume();
				}
				mMediaPlayer.start();
				mPlayStatus = PlayStatus.PLAY;
			}	//如果正在暂停，先恢复
		}
	}
	
	//停止
	public void stop()
	{
		if( mCountDownTimer != null )
		{
			mCountDownTimer.cancel();
			mCountDownTimer = null;
		}
		if( mMediaPlayer != null )
		{
			if( PlayStatus.STOP != mPlayStatus )
			{
				mMediaPlayer.stop();
				mPlayStatus = PlayStatus.STOP;
			}	//如果没有停止，先停止
		}
	}

	/**
     * 开始
     *
     * @param text
     */
	public void play( final String audioPath,  final long startTime,  final long endTime ) 
	{
		if( ( null == audioPath ) || ( ( endTime - startTime ) <= 0 ) )
		{
			//mHandler.sendEmptyMessage(MSG_PLAY_COMPLETION);
			return;
		}
		
		stop();	//先停止当前播放
		if( mMediaPlayer != null )
		{
			try 
			{
				mMediaPlayer.reset();
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mMediaPlayer.setDataSource(audioPath);
				mMediaPlayer.prepareAsync();
				
				mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer m) {
						// TODO Auto-generated method stub
						mMediaPlayer.seekTo((int)startTime+1);	//多seek 1毫秒，否则有些文件不能播放。
					}
				});
				
				mMediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener(){
					@Override
					public void onSeekComplete(MediaPlayer m) 
					{
						// TODO Auto-generated method stub
						mMediaPlayer.start();
						mPlayStatus = PlayStatus.PLAY;
						
						final long time = endTime-startTime;
						mCountDownTimer = new AdvancedCountdownTimer( time, time/10 )
						{
							@Override
							public void onFinish() 
							{
								// TODO Auto-generated method stub
								if( mMediaPlayer != null && mMediaPlayer.isPlaying() )
								{
									mHandler.sendEmptyMessage(MSG_PLAY_COMPLETION);
								}
							}

							@Override
							public void onTick(long millisUntilFinished, int percent) 
							{
								// TODO Auto-generated method stub
								if( mMediaPlayer != null && mMediaPlayer.isPlaying() )
								{
									Message msg = mHandler.obtainMessage();
									msg.what = MSG_PLAY_PROGRESS;
									msg.arg1 = percent;
									
									mHandler.sendMessage(msg);
								}
							}
							
						};
						mCountDownTimer.start();  
					}
				});
				
				mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) 
					{										
						// TODO Auto-generated method stub
						mPlayStatus = PlayStatus.STOP;
						if( ( mOnMediaPlayerListener != null ) && ( mCountDownTimer != null ) )
						{
							mOnMediaPlayerListener.onPlayCompleted();
						}
					}
				});	//播放完成
				
				mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
						// TODO Auto-generated method stub
						mPlayStatus = PlayStatus.STOP;
						if( mOnMediaPlayerListener != null )
						{
							mOnMediaPlayerListener.onPlayError();
						}

						return false;
					}
				});	//播放错误
			} 
			catch (IllegalArgumentException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (SecurityException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalStateException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

	/**
     * 开始
     *
     * @param text
     */
	public void play( final String audioPath, boolean isLoop ) 
	{
		stop();	//先停止当前播放
		if( mMediaPlayer != null )
		{
			try 
			{
				mMediaPlayer.reset();
				mMediaPlayer.setDataSource(audioPath);
				mMediaPlayer.prepare();
				mMediaPlayer.setLooping(isLoop);
				mMediaPlayer.start();
				mPlayStatus = PlayStatus.PLAY;
				
				mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) 
					{										
						// TODO Auto-generated method stub
						mPlayStatus = PlayStatus.STOP;
						if( mOnMediaPlayerListener != null )
						{
							mOnMediaPlayerListener.onPlayCompleted();
						}
					}
				});	//播放完成
				
				mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
						// TODO Auto-generated method stub
						mPlayStatus = PlayStatus.STOP;
						if( mOnMediaPlayerListener != null )
						{
							mOnMediaPlayerListener.onPlayError();
						}
						return false;
					}
				});	//播放错误
			} 
			catch (IllegalArgumentException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (SecurityException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalStateException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) 
        {
            switch (msg.what) 
            {
            	case MSG_PLAY_COMPLETION:	//播放完毕
            		mCountDownTimer = null;
            		stop();	//先停止当前播放
					if( mOnMediaPlayerListener != null )
					{
						mOnMediaPlayerListener.onPlayCompleted();
					}
            		break;
            	case MSG_PLAY_PROGRESS:		//播放进度
            		if ( mOnMediaPlayerListener != null ) 
        			{
            			mOnMediaPlayerListener.onPlayProgress(msg.arg1);
        			}
            		break;
                default:
                    break;
            }
            
            return false;
        }
    });
}
