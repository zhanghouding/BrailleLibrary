package com.sunteam.library.service;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnRetryableFileDownloadStatusListener;

import com.sunteam.library.db.DownloadChapterDBDao;
import com.sunteam.library.db.DownloadResourceDBDao;
import com.sunteam.library.entity.DownloadChapterEntity;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.LogUtils;
import com.sunteam.library.utils.PublicUtils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

public class DownloadManagerService extends Service implements OnRetryableFileDownloadStatusListener 
{
	private static final String TAG = "DownloadManagerService";
    @Override
    public IBinder onBind(Intent intent) 
    {
    	LogUtils.e(TAG, "onBind");
        return null;
    }
    
    @Override
    public void onCreate() 
    {
        super.onCreate();
        LogUtils.e(TAG, "onCreate");
        // 将当前service注册为FileDownloader下载状态监听器
        FileDownloader.registerDownloadStatusListener(this);
        // 如果希望service启动就开始下载所有未完成的任务，则开启以下实现
        FileDownloader.continueAll(true);
    }
    
    @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
    	LogUtils.e(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}    
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        LogUtils.e(TAG, "onDestroy");
        // 将当前service取消注册为FileDownloader下载状态监听器
        FileDownloader.unregisterDownloadStatusListener(this);
       // 如果希望service停止就停止所有下载任务，则开启以下实现
        FileDownloader.pauseAll();// 暂停所有下载任务
    }

    @Override
    public void onFileDownloadStatusRetrying(DownloadFileInfo downloadFileInfo, int retryTimes) 
    {
    	// 正在重试下载（如果你配置了重试次数，当一旦下载失败时会尝试重试下载），retryTimes是当前第几次重试
    	LogUtils.e( TAG, "aaa onFileDownloadStatusRetrying");
    }
    
    @Override
    public void onFileDownloadStatusWaiting(DownloadFileInfo downloadFileInfo) 
    {
    	// 等待下载（等待其它任务执行完成，或者FileDownloader在忙别的操作）
    	LogUtils.e( TAG, "bbb onFileDownloadStatusWaiting");
    }
    
    @Override
    public void onFileDownloadStatusPreparing(DownloadFileInfo downloadFileInfo) 
    {
    	// 准备中（即，正在连接资源）
    	LogUtils.e( TAG, "ccc onFileDownloadStatusPreparing");
    }
    
    @Override
    public void onFileDownloadStatusPrepared(DownloadFileInfo downloadFileInfo) 
    {
    	// 已准备好（即，已经连接到了资源）
    	LogUtils.e( TAG, "ddd onFileDownloadStatusPrepared");
    }
    
    @Override
    public void onFileDownloadStatusDownloading(DownloadFileInfo downloadFileInfo, float downloadSpeed, long remainingTime) 
    {
    	// 正在下载，downloadSpeed为当前下载速度，单位KB/s，remainingTime为预估的剩余时间，单位秒
    	LogUtils.e( TAG, "eee onFileDownloadStatusDownloading");
    	
    	String url = downloadFileInfo.getUrl();
    	DownloadChapterDBDao dcDao = new DownloadChapterDBDao( this.getBaseContext() );
    	DownloadChapterEntity entity = dcDao.find(url);	//根据url查找章节下载记录
    	if( ( entity != null ) && ( entity.chapterStatus != LibraryConstant.DOWNLOAD_STATUS_GOING ) )
    	{
    		entity.chapterStatus = LibraryConstant.DOWNLOAD_STATUS_GOING;	//将状态设置为正在下载。
    		dcDao.update(url, entity.recorcdId, entity.chapterStatus);		//更新数据库中保存的状态。
    		dcDao.closeDb();
    		
    		DownloadResourceDBDao drDao = new DownloadResourceDBDao( this.getBaseContext() );
    		drDao.update(entity.chapterStatus, entity.recorcdId);			//更新数据库中保存的状态。	
    		drDao.closeDb();
    	}
    	else
    	{
    		dcDao.closeDb();
    	}
    	
    	Intent intent = new Intent(LibraryConstant.ACTION_DOWNLOAD_STATUS);
		sendBroadcast(intent);			//广播方式发送给客户端
    }
    
    @Override
    public void onFileDownloadStatusPaused(DownloadFileInfo downloadFileInfo) 
    {
    	// 下载已被暂停
    	LogUtils.e( TAG, "fff onFileDownloadStatusPaused");
    }
    
    @Override
    public void onFileDownloadStatusCompleted(DownloadFileInfo downloadFileInfo)
    {
    	// 下载完成（整个文件已经全部下载完成）
    	LogUtils.e( TAG, "ggg onFileDownloadStatusCompleted");
    	
    	String url = downloadFileInfo.getUrl();
    	DownloadChapterDBDao dcDao = new DownloadChapterDBDao( this.getBaseContext() );
    	DownloadChapterEntity entity = dcDao.find(url);	//根据url查找章节下载记录
    	if( ( entity != null ) && ( entity.chapterStatus != LibraryConstant.DOWNLOAD_STATUS_FINISH ) )
    	{
    		entity.chapterStatus = LibraryConstant.DOWNLOAD_STATUS_FINISH;	//将状态设置为下载完成。
    		dcDao.update(url, entity.recorcdId, entity.chapterStatus);			//更新数据库中保存的状态。
    		
    		boolean isFinish = true;	//是否所有的章节都已经下载完成？
    		ArrayList<DownloadChapterEntity> list = dcDao.findAll(entity.recorcdId);	//查找这本书所有章节状态。
    		if( list != null )
    		{
    			for( int i = 0; i < list.size(); i++ )
    			{
    				if( list.get(i).chapterStatus != LibraryConstant.DOWNLOAD_STATUS_FINISH )
    				{
    					isFinish = false;
    					break;
    				}
    			}
    		}
    		else
    		{
    			isFinish = false;
    		}
    		dcDao.closeDb();
    		
    		if( isFinish )
    		{
	    		DownloadResourceDBDao drDao = new DownloadResourceDBDao( this.getBaseContext() );
	    		drDao.update(entity.chapterStatus, entity.recorcdId);			//更新数据库中保存的状态。	
	    		drDao.closeDb();
    		}
    	}
    	else
    	{
    		dcDao.closeDb();
    	}
    	
    	String filename = downloadFileInfo.getFileName();
    	if( filename.contains(LibraryConstant.CACHE_FILE_SUFFIX) )	//电子书
    	{
    		String filedir = downloadFileInfo.getFileDir()+"/";
    		String content = PublicUtils.readContent( filedir, filename );
    		if( !TextUtils.isEmpty(content) )
    		{
    			content = parseEbookContent( content );
    			if( !TextUtils.isEmpty(content) )
    			{
    				File file = new File( downloadFileInfo.getFilePath() );
    				if( file.exists() )
    				{
    					file.delete();
    					PublicUtils.saveContent(filedir, filename, content);
    				}
    			}
    		}
    	}	//因为电子书章节是作为文件下载的，下载下来的数据是json格式，需要从中取出有用的数据。
    	
    	Intent intent = new Intent(LibraryConstant.ACTION_DOWNLOAD_STATUS);
		sendBroadcast(intent);			//广播方式发送给客户端
    	
    	/*
    	LogUtils.e( TAG, "wzp debug 000 getUrl = "+ downloadFileInfo.getUrl() );
    	LogUtils.e( TAG, "wzp debug 111 getId = "+ downloadFileInfo.getId() );
    	LogUtils.e( TAG, "wzp debug 222 getTempFilePath = "+ downloadFileInfo.getTempFilePath() );
    	LogUtils.e( TAG, "wzp debug 333 getTempFileName = "+ downloadFileInfo.getTempFileName() );
    	LogUtils.e( TAG, "wzp debug 444 getStatus = "+ downloadFileInfo.getStatus() );
    	LogUtils.e( TAG, "wzp debug 555 getFilePath = "+ downloadFileInfo.getFilePath() );
    	LogUtils.e( TAG, "wzp debug 666 getFileName = "+ downloadFileInfo.getFileName() );
    	LogUtils.e( TAG, "wzp debug 777 getFileDir = "+ downloadFileInfo.getFileDir() );
    	*/
    }
    
    @Override
    public void onFileDownloadStatusFailed(String url, DownloadFileInfo downloadFileInfo, FileDownloadStatusFailReason failReason) 
    {
    	// 下载失败了，详细查看失败原因failReason，有些失败原因你可能必须关心
    	LogUtils.e( TAG, "hhh onFileDownloadStatusFailed");
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
    
	/**
	 * 方法(解析ChapterList)
	 * 
	 * @param jsonArray
	 * @param list
	 * @return
	 * @author wzp
	 * @Created 2017/01/24
	 */
	private String parseChapterList( JSONArray jsonArray )
	{
		String content = "";
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			
			content += obj.optString("Content");
		}
		
		return	content;
	}
	
	/**
	 * 方法(解析items)
	 * 
	 * @param jsonArray
	 * @param list
	 * @return
	 * @author wzp
	 * @Created 2017/01/24
	 */
	private String parseItems( JSONArray jsonArray )
	{
		String content = "";
		for( int i = 0; i < jsonArray.length(); i++ )
		{
			JSONObject obj = jsonArray.optJSONObject(i);
			JSONArray array = obj.optJSONArray("ChapterList");
			
			content += parseChapterList( array );
		}
		
		return	content;
	}
   
    //解析电子图书内容
    private String parseEbookContent( String responseStr )
    {
	    try
		{
			JSONObject jsonObject = new JSONObject(responseStr);
			Boolean result = jsonObject.optBoolean("IsException") ;
			if( result )
			{
				return	null;
			}
			
			JSONArray jsonArray = jsonObject.optJSONArray("Items");
			if( (  null == jsonArray ) || ( 0 == jsonArray.length() ) )
			{
				return	null;
			}
			
			return	PublicUtils.parseHtml(parseItems( jsonArray ));
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	    
	    return null;
    }
}
