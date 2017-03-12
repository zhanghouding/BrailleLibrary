package com.sunteam.library.service;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnRetryableFileDownloadStatusListener;

import com.sunteam.library.download.DownloadEbook;
import com.sunteam.library.download.DownloadEbook.OnDownloadEbookStatusListener;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DownloadManagerService extends Service implements OnRetryableFileDownloadStatusListener, OnDownloadEbookStatusListener 
{
    @Override
    public IBinder onBind(Intent intent) 
    {
        return null;
    }
    
    @Override
    public void onCreate() 
    {
        super.onCreate();
        
        DownloadEbook.registerDownloadStatusListener(this);
        
        // 将当前service注册为FileDownloader下载状态监听器
        FileDownloader.registerDownloadStatusListener(this);
        // 如果希望service启动就开始下载所有未完成的任务，则开启以下实现
        FileDownloader.continueAll(true);
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        
        DownloadEbook.unregisterDownloadStatusListener();
        
        // 将当前service取消注册为FileDownloader下载状态监听器
        FileDownloader.unregisterDownloadStatusListener(this);
       // 如果希望service停止就停止所有下载任务，则开启以下实现
        FileDownloader.pauseAll();// 暂停所有下载任务
    }

    @Override
    public void onFileDownloadStatusRetrying(DownloadFileInfo downloadFileInfo, int retryTimes) 
    {
    	// 正在重试下载（如果你配置了重试次数，当一旦下载失败时会尝试重试下载），retryTimes是当前第几次重试
    }
    
    @Override
    public void onFileDownloadStatusWaiting(DownloadFileInfo downloadFileInfo) 
    {
    	// 等待下载（等待其它任务执行完成，或者FileDownloader在忙别的操作）
    }
    
    @Override
    public void onFileDownloadStatusPreparing(DownloadFileInfo downloadFileInfo) 
    {
    	// 准备中（即，正在连接资源）
    }
    
    @Override
    public void onFileDownloadStatusPrepared(DownloadFileInfo downloadFileInfo) 
    {
    	// 已准备好（即，已经连接到了资源）
    }
    
    @Override
    public void onFileDownloadStatusDownloading(DownloadFileInfo downloadFileInfo, float downloadSpeed, long remainingTime) 
    {
    	// 正在下载，downloadSpeed为当前下载速度，单位KB/s，remainingTime为预估的剩余时间，单位秒
    }
    
    @Override
    public void onFileDownloadStatusPaused(DownloadFileInfo downloadFileInfo) 
    {
    	// 下载已被暂停
    }
    
    @Override
    public void onFileDownloadStatusCompleted(DownloadFileInfo downloadFileInfo)
    {
    	// 下载完成（整个文件已经全部下载完成）
    }
    
    @Override
    public void onFileDownloadStatusFailed(String url, DownloadFileInfo downloadFileInfo, FileDownloadStatusFailReason failReason) 
    {
    	// 下载失败了，详细查看失败原因failReason，有些失败原因你可能必须关心

        String failType = failReason.getType();
        String failUrl = failReason.getUrl();// 或：failUrl = url，url和failReason.getType()会是一样的
        
        if( FileDownloadStatusFailReason.TYPE_URL_ILLEGAL.equals(failType) )
        {
            // 下载failUrl时出现url错误
        }
        else if( FileDownloadStatusFailReason.TYPE_STORAGE_SPACE_IS_FULL.equals(failType) )
        {
            // 下载failUrl时出现本地存储空间不足
        }
        else if( FileDownloadStatusFailReason.TYPE_NETWORK_DENIED.equals(failType) )
        {
            // 下载failUrl时出现无法访问网络
        }
        else if( FileDownloadStatusFailReason.TYPE_NETWORK_TIMEOUT.equals(failType) )
        {
            // 下载failUrl时出现连接超时
        }
        else
        {
            // 更多错误....
        }

        // 查看详细异常信息
        Throwable failCause = failReason.getCause();// 或：failReason.getOriginalCause()

        // 查看异常描述信息
        String failMsg = failReason.getMessage();// 或：failReason.getOriginalCause().getMessage()
    }

	@Override
	public void onDownloadEbookCompleted() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDownloadEbookError() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDownloadEbookProgress(int index, int status) 
	{
		// TODO Auto-generated method stub
		
	}
}
