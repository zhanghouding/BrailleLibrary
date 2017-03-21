package com.sunteam.library.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDeleteDownloadFilesListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.sunteam.common.utils.PromptDialog;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.dict.utils.DBUtil;
import com.sunteam.jni.SunteamJni;
import com.sunteam.library.R;
import com.sunteam.library.activity.WordSearchResultActivity;
import com.sunteam.library.db.DownloadChapterDBDao;
import com.sunteam.library.db.DownloadResourceDBDao;
import com.sunteam.library.entity.DownloadChapterEntity;
import com.sunteam.library.entity.DownloadResourceEntity;

/**
 * 可重用的方法工具类。
 * 
 * @author wzp
 */
public class PublicUtils 
{
	private static ProgressDialog progress;
	private static int mColorSchemeIndex = 0;	//配色方案索引
	
	//从系统配置文件中得到配色方案索引
	public static int getSysColorSchemeIndex()
	{
		return	(int)(System.currentTimeMillis()%7);
	}
	
	//设置配色方案
	public static void setColorSchemeIndex( int index )
	{
		mColorSchemeIndex = index;
	}
	
	//得到配色方案
	public static int getColorSchemeIndex()
	{
		return	mColorSchemeIndex;
	}
	
	//dip转px
	public static int dip2px( Context context, float dipValue )
	{ 
		final float scale = context.getResources().getDisplayMetrics().density;
		
        return (int)(dipValue * scale + 0.5f); 
	} 

	//px转dip
	public static int px2dip( Context context, float pxValue )
	{ 
		final float scale = context.getResources().getDisplayMetrics().density; 
        
		return (int)(pxValue / scale + 0.5f); 	
	}
	
	//byte转char
	public static char byte2char( byte[] buffer, int offset )
	{
		if( buffer[offset] >= 0 )
		{
			return	(char)buffer[offset];
		}
		 
		int hi = (int)(256+buffer[offset]);
		int li = (int)(256+buffer[offset+1]);
		 
		return	(char)((hi<<8)+li);
	}

	//byte转int
	public static int byte2int( byte[] buffer, int offset )
	{
		int[] temp = new int[4];
		
		for( int i = offset, j = 0; i < offset+4; i++, j++ )
		{
			if( buffer[i] < 0 )
			{
				temp[j] = 256+buffer[i];
			}
			else
			{
				temp[j] = buffer[i];
			}
		}
		
		int result = 0;
		
		for( int i = 0; i < 4; i++ )
		{
			result += (temp[i]<<(8*(i)));
		}
		
		return	result;
	}	
	
	/**
	 * 加载提示
	 * 
	 * @param context
	 */
	public static void showProgress(Context context, String info, final AsyncTask<?, ?, ?> asyncTask) {
		cancelProgress();

		progress = new ProgressDialog(context, R.style.progress_dialog);
		progress.setIndeterminate(false);
		progress.setCancelable(true);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		progress.setContentView(R.layout.progress_layout);
		TextView tvInfo = (TextView) progress.findViewById(R.id.tv_info);
		tvInfo.setText(info);
		
		TTSUtils.getInstance().speakMenu(info);
		
		progress.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) 
			{
				// TODO Auto-generated method stub
				asyncTask.cancel(true);
				//执行异步线程取消操作　　
			}
		});
	}
	
	/**
	 * 加载提示
	 * 
	 * @param context
	 */
	public static void showProgress(Context context, final AsyncTask<?, ?, ?> asyncTask) {
		cancelProgress();

		progress = new ProgressDialog(context, R.style.progress_dialog);
		progress.setIndeterminate(false);
		progress.setCancelable(true);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		progress.setContentView(R.layout.progress_layout);
		
		progress.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) 
			{
				// TODO Auto-generated method stub
				asyncTask.cancel(true);
				//执行异步线程取消操作　　
			}
		});
	}

	public static void cancelProgress() {
		if (null != progress) {
			if (progress.isShowing()) {
				progress.cancel();
			}
			progress = null;
		}
	}
	
	//显示提示信息并朗读(不需要接收TTS结束回调)
	public static void showToast( final Context context, String tips,final boolean isFinish )
	{		
		//用后鼎提供的系统提示对话框
		TTSUtils.getInstance().stop();
		PromptDialog pd = new PromptDialog(context, tips);
		pd.setPromptListener( new PromptListener() 
		{
			public void onComplete() 
			{
				((Activity)context).finish();
			}
		});
		pd.show();
	}
		
	//显示提示信息并朗读(不需要接收TTS结束回调)
	public static void showToast( Context context, String tips )
	{
		/*
		TTSUtils.getInstance().speakMenu(tips, listener);
		CustomToast.showToast(context, tips, Toast.LENGTH_SHORT);
		*/
		
		TTSUtils.getInstance().stop();
		//用后鼎提供的系统提示对话框
		PromptDialog pd = new PromptDialog(context, tips);
		pd.setPromptListener( new PromptListener() 
		{
			public void onComplete() 
			{
			}
		});
		pd.show();
	}
	
	//显示提示信息并朗读(需要接收TTS结束回调)
	public static void showToast( Context context, String tips, PromptListener listener )
	{
		/*
		TTSUtils.getInstance().speakMenu(tips, listener);
		CustomToast.showToast(context, tips, Toast.LENGTH_SHORT);
		*/
		
		TTSUtils.getInstance().stop();
		//用后鼎提供的系统提示对话框
		PromptDialog pd = new PromptDialog(context, tips);
		pd.setPromptListener( listener );
		pd.show();
	}

	//检查讯飞语音服务是否安装
	public static boolean checkSpeechServiceInstalled(Context context)
	{
		return true;	//SpeechUtility.getUtility().checkServiceInstalled();
	}
	
	//跳到反查
	public static void jumpFanCha(final Context context, final String content)
	{
		if( TextUtils.isEmpty(content) )
		{
			PublicUtils.showToast( context, context.getString(R.string.library_search_fail) );
		}
		else
		{
			DBUtil dbUtils = new DBUtil();
			final String result = dbUtils.search(content);
			if( TextUtils.isEmpty(result) )
			{
				PublicUtils.showToast( context, context.getString(R.string.library_search_fail) );
			}
			else
			{
				TTSUtils.getInstance().stop();
				TTSUtils.getInstance().OnTTSListener(null);
				PublicUtils.showToast( context, context.getString(R.string.library_dict_search_success), new PromptListener() {
					@Override
					public void onComplete() 
					{
						// TODO Auto-generated method stub
						Intent intent = new Intent( context, WordSearchResultActivity.class );
						intent.putExtra("word", content);
						intent.putExtra("explain", result);
						context.startActivity(intent);
					}
				});
			}
		}
	}
	
	//判断一个文件是否为纯文本文件
    public static boolean checkIsTextFile(byte[] buffer)
    {
        boolean isTextFile = true;
        
        try
        {
            int i = 0;
            int length = (int)buffer.length;
            byte data;
            while (i < length && isTextFile)
            {
                data = (byte)buffer[i];
                isTextFile = (data != 0);
                i++;
            }
            
            return isTextFile;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
            return	false;
        }
    }
    
	/**
	 * 方法(创建缓存目录)
	 * 
	 * @param parentPath
	 * 			父路径
	 * @param dirName
	 * 			目录名
	 * @return
	 * @author wzp
	 * @Created 2017/01/29
	 */
	public static String createCacheDir( String parentPath, String dirName )
	{
		String dirPath = parentPath+dirName+"/";
		try
		{
			File f = new File(dirPath);
			if( !f.exists() )
			{
				f.mkdirs();
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return	dirPath;
	}
    
	/**
	 * 方法(加载下载Content)
	 * 
	 * @param filepath
	 * 			文件路径
	 * @param filaname
	 * 			文件名
	 * @return
	 * @author wzp
	 * @Created 2017/01/31
	 */
	public static String readDownloadContent( String filepath, String filename )
	{
		try
		{
			File file = new File(filepath+filename);
			if( file.exists() )
			{
				int len = (int) file.length();
				byte[] buffer = new byte[len];
				FileInputStream inStream = new FileInputStream(file);
				inStream.read(buffer);
				inStream.close();
				
				return	new String(buffer);
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return	"";
	}
     
	/**
	 * 方法(加载Content)
	 * 
	 * @param filepath
	 * 			文件路径
	 * @param filaname
	 * 			文件名
	 * @return
	 * @author wzp
	 * @Created 2017/01/31
	 */
	public static String readContent( String filepath, String filename )
	{
		try
		{
			File file = new File(filepath+filename);
			if( file.exists() )
			{
				SunteamJni mSunteamJni = new SunteamJni();
				mSunteamJni.decryptFile(filepath+filename);	//解密文件
				
				int len = (int) file.length();
				byte[] buffer = new byte[len-LibraryConstant.ENCRYPT_FLAGS_LENGTH];
				FileInputStream inStream = new FileInputStream(file);
				inStream.read(buffer);
				inStream.close();
				
				return	new String(buffer);
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return	"";
	}
    
	/**
	 * 方法(保存Content)
	 * 
	 * @param filepath
	 * 			文件路径
	 * @param filaname
	 * 			文件名
	 * @param content
	 * 			文件内容
	 * @return
	 * @author wzp
	 * @Created 2017/01/29
	 */
	public static void saveContent( String filepath, String filename, String content )
	{
		if( !TextUtils.isEmpty(content) )
		{
			try
			{
				File f = new File(filepath);
				if( !f.exists() )
				{
					f.mkdirs();
				}
				File file = new File(filepath+filename);
				if( !file.exists() )
				{
					byte[] flags = new byte[LibraryConstant.ENCRYPT_FLAGS_LENGTH]; 
					FileOutputStream outStream = new FileOutputStream(file);
					outStream.write(content.getBytes());
					outStream.write(flags);	//加密标记
					outStream.close();
					
					SunteamJni mSunteamJni = new SunteamJni();
					mSunteamJni.encryptFile(filepath+filename);	//加密文件 
				}
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	}
	
	//加密文件
	public static void encryptFile( String fullpath )
	{
		try
		{
			File file = new File(fullpath);
			if( !file.exists() )
			{
				byte[] flags = new byte[LibraryConstant.ENCRYPT_FLAGS_LENGTH];
				
				// 打开一个随机访问文件流，按读写方式  
	            RandomAccessFile randomFile = new RandomAccessFile(fullpath, "rw");  
	            // 文件长度，字节数  
	            long fileLength = randomFile.length();  
	            // 将写文件指针移到文件尾。  
	            randomFile.seek(fileLength);  
	            randomFile.write(flags);		//加密标记
	            randomFile.close();  
	            
				SunteamJni mSunteamJni = new SunteamJni();
				mSunteamJni.encryptFile(fullpath);	//加密文件 
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
    //去掉一个字符串中的标点符号
    public static String format(String s)
    {
    	String str = s.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", ""); 
    	
    	return str.replaceAll("//", "");
    }
    
	/**
	 * 方法(解析Html)
	 * 
	 * @param html
	 * @return
	 * @author wzp
	 * @Created 2017/01/29
	 */
	public static String parseHtml( String html )
	{
		if( TextUtils.isEmpty(html) )
		{
			return	"";
		}
		
		String txtcontent = html.replaceAll("</?[^>]+>", ""); 			//剔出<html>的标签  
		txtcontent = txtcontent.replaceAll("&nbsp;", "");				//替换空格
        txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");	//去除字符串中的空格,回车,换行符,制表符  
        
        return txtcontent;
	}
	
	//删除目录
	public static void deleteFiles(File file) 
	{  
		if( file.exists() == false )
		{
			return;
		}
		if( file.isFile() && !file.getName().contains(".temp") ) 
		{  
			file.delete();  
			return;  
		}
		if( file.isDirectory() )
		{  
			File[] childFiles = file.listFiles();  
			if( childFiles == null || childFiles.length == 0 ) 
			{  
				//file.delete();  //不删除目录了
				return;  
			}  

			for( int i = 0; i < childFiles.length; i++ ) 
			{  
				deleteFiles(childFiles[i]);  
			}  
			//file.delete();	//不删除目录了  
		}  
	}
	
	//得到用户名
	public static String getUserName( Context context )
	{
		SharedPreferences spf = context.getSharedPreferences(LibraryConstant.USER_INFO, Activity.MODE_PRIVATE);
		return	spf.getString(LibraryConstant.USER_NAME, "");
	}
	
	//得到分类名称
	public static String getCategoryName( Context context, int type )
	{
		String[] list = context.getResources().getStringArray(R.array.library_category_list);
		
		return	list[type];
	}
	
	//得到网络是否连接
	public static boolean isNetworkConnect()
	{
		try 
		{
			Process p = Runtime.getRuntime().exec( "ping -c 1 -w 5 www.baidu.com");
			int status = p.waitFor();
			if (status == 0)
			{
				return	true;
			} 
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch( InterruptedException e )
		{
			e.printStackTrace();
		}
		
		return	false;
	}
	
	//保存用户信息
	public static void saveUserInfo( Context context, String username, String password )
	{
		SharedPreferences spf = context.getSharedPreferences(LibraryConstant.USER_INFO, Activity.MODE_PRIVATE);
		Editor editor = spf.edit();
		editor.putString( LibraryConstant.USER_NAME, username );
		editor.putString( LibraryConstant.USER_PASSWORD, password );
		editor.commit();
	}

	// 隐藏输入法
	public static void hideMsgIputKeyboard(Activity activity) {
		if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (activity.getCurrentFocus() != null) {
				InputMethodManager inputKeyBoard = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputKeyBoard.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
	
	//删除下载任务
	public static void deleteDownloadTask( final Context context, final DownloadResourceEntity entity )
	{
		DownloadChapterDBDao dcDao = new DownloadChapterDBDao( context );
		ArrayList<DownloadChapterEntity> list = dcDao.findAll(entity._id);	//得到所有的章节信息
		dcDao.closeDb();
		
		if( list != null )
		{
			ArrayList<String> urls = new ArrayList<String>();
			int size = list.size();
			for( int i = 0; i < size; i++ )
			{
				urls.add(list.get(i).chapterUrl);
			}
			
			if( urls.size() > 0 )
			{
				//删除所有的章节任务，不论下载状态
				FileDownloader.delete(urls, false, new OnDeleteDownloadFilesListener() {

					@Override
					public void onDeleteDownloadFilesCompleted( List<DownloadFileInfo> arg0, List<DownloadFileInfo> arg1) 
					{
						// TODO Auto-generated method stub
						DownloadChapterDBDao dcDao = new DownloadChapterDBDao( context );
						dcDao.deleteAll(entity._id);
						dcDao.closeDb();
						
						DownloadResourceDBDao drDao = new DownloadResourceDBDao( context );
						drDao.delete(entity);
						drDao.closeDb();
					}

					@Override
					public void onDeleteDownloadFilesPrepared( List<DownloadFileInfo> arg0) 
					{
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onDeletingDownloadFiles( List<DownloadFileInfo> arg0, List<DownloadFileInfo> arg1, List<DownloadFileInfo> arg2, DownloadFileInfo arg3) 
					{
						// TODO Auto-generated method stub
						
					}
				});
			}
			else
			{
				DownloadResourceDBDao drDao = new DownloadResourceDBDao( context );
				drDao.delete(entity);
				drDao.closeDb();
			}
		}
		else
		{
			DownloadResourceDBDao drDao = new DownloadResourceDBDao( context );
			drDao.delete(entity);
			drDao.closeDb();
		}
	}
	
	//清空下载任务 
	public static void clearDownloadTask( final Context context, final boolean isFinished )
	{
		final String userName = PublicUtils.getUserName(context);
		DownloadResourceDBDao drDao = new DownloadResourceDBDao( context );
		ArrayList<DownloadResourceEntity> allResourceList = null;
		
		if( isFinished )	//删除已完成任务
		{
			allResourceList = drDao.findAllCompleted(userName);
		}
		else				//删除未完成任务
		{
			allResourceList = drDao.findAllUnCompleted(userName);
		}	//得到所有的资源信息
		
		drDao.closeDb();
		
		if( ( null == allResourceList ) || ( 0 == allResourceList.size() ) )
		{
			return;
		}
		
		DownloadChapterDBDao dcDao = new DownloadChapterDBDao( context );		
		final ArrayList<DownloadChapterEntity> allChapterList = new ArrayList<DownloadChapterEntity>();	//所有章节信息
		
		int size = allResourceList.size();
		for( int i = 0; i < size; i++ )
		{
			ArrayList<DownloadChapterEntity> chapterList = dcDao.findAll(allResourceList.get(i)._id);	//得到所有的章节信息
			if( ( chapterList != null ) && ( chapterList.size() > 0 ) )
			{
				allChapterList.addAll(chapterList);
			}
		}
		
		dcDao.closeDb();
				
		ArrayList<String> urls = new ArrayList<String>();
		size = allChapterList.size();
		for( int i = 0; i < size; i++ )
		{
			urls.add(allChapterList.get(i).chapterUrl);
		}
			
		if( urls.size() > 0 )
		{
			//删除所有的章节任务，不论下载状态
			FileDownloader.delete(urls, false, new OnDeleteDownloadFilesListener() {

				@Override
				public void onDeleteDownloadFilesCompleted( List<DownloadFileInfo> arg0, List<DownloadFileInfo> arg1) 
				{
					// TODO Auto-generated method stub
					DownloadChapterDBDao dcDao = new DownloadChapterDBDao( context );
					int size = allChapterList.size();
					for( int i = 0; i < size; i++ )
					{
						dcDao.deleteAll(allChapterList.get(i).recorcdId);
					}
					dcDao.closeDb();
						
					DownloadResourceDBDao drDao = new DownloadResourceDBDao( context );
					if( isFinished )	//删除已完成任务
					{
						drDao.deleteAllCompleted(userName);
					}
					else				//删除未完成任务
					{
						drDao.deleteAllUnCompleted(userName);
					}	//得到所有的资源信息
					drDao.closeDb();
				}

				@Override
				public void onDeleteDownloadFilesPrepared( List<DownloadFileInfo> arg0) 
				{
					// TODO Auto-generated method stub
						
				}

				@Override
				public void onDeletingDownloadFiles( List<DownloadFileInfo> arg0, List<DownloadFileInfo> arg1, List<DownloadFileInfo> arg2, DownloadFileInfo arg3) 
				{
					// TODO Auto-generated method stub
						
				}
			});
		}
		else
		{
			drDao = new DownloadResourceDBDao( context );
			if( isFinished )	//删除已完成任务
			{
				drDao.deleteAllCompleted(userName);
			}
			else				//删除未完成任务
			{
				drDao.deleteAllUnCompleted(userName);
			}	//得到所有的资源信息
			drDao.closeDb();
		}
	}
	
	//检测下载文件是否加密，没有加密的需要先加密
	public static void checkEncryptFile( File file )
	{
		if( file.exists() == false )
		{
			return;
		}
		
		if( file.isFile() )
		{
			if( file.getName().contains(".temp") )	//如果是临时下载文件
			{
				return;
			}
			
			String fullpath = file.getPath();
			
			//需要判断此文件是否加密，如果没有加密，这需要加密
			SunteamJni mSunteamJni = new SunteamJni();
			int state = mSunteamJni.getFileEncryptedState(fullpath); //0 原始数据；1加密数据；2数据损坏，表示状态无法确定
			if( state != 1 )
			{
				mSunteamJni.encryptFile(fullpath);
			}
		}
		
		if( file.isDirectory() )
		{  
			File[] childFiles = file.listFiles();  
			if( childFiles == null || childFiles.length == 0 ) 
			{  
				return;  
			}  

			for( int i = 0; i < childFiles.length; i++ ) 
			{  
				checkEncryptFile(childFiles[i]);  
			}  
		}	
	}
}
