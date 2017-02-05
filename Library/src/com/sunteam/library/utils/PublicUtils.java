package com.sunteam.library.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.sunteam.common.utils.PromptDialog;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.dict.utils.DBUtil;
import com.sunteam.library.R;
import com.sunteam.library.activity.WordSearchResultActivity;

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
	public static void showProgress(Context context, String info) {
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
	}
	
	/**
	 * 加载提示
	 * 
	 * @param context
	 */
	public static void showProgress(Context context) {
		cancelProgress();

		progress = new ProgressDialog(context, R.style.progress_dialog);
		progress.setIndeterminate(false);
		progress.setCancelable(true);
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		progress.setContentView(R.layout.progress_layout);
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
					FileOutputStream outStream = new FileOutputStream(file);
					outStream.write(content.getBytes());
					outStream.close();
				}
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	}
	
    //去掉一个字符串中的标点符号
    public static String format(String s)
    {
    	String str = s.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", ""); 
    	
    	return str;
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
		if( file.isFile() ) 
		{  
			file.delete();  
			return;  
		}
		if( file.isDirectory() )
		{  
			File[] childFiles = file.listFiles();  
			if( childFiles == null || childFiles.length == 0 ) 
			{  
				file.delete();  
				return;  
			}  

			for( int i = 0; i < childFiles.length; i++ ) 
			{  
				deleteFiles(childFiles[i]);  
			}  
			file.delete();  
		}  
	}
	
	//得到用户名
	public static String getUserName()
	{
		return	"test1";
	}
	
	//得到分类名称
	public static String getCategoryName( Context context, int type )
	{
		String[] list = context.getResources().getStringArray(R.array.library_category_list);
		
		return	list[type];
	}
}	
