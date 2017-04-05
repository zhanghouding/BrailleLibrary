package com.sunteam.library.asynctask;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sunteam.common.menu.MenuConstant;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.library.R;
import com.sunteam.library.activity.CategoryList;
import com.sunteam.library.db.CategoryDBDao;
import com.sunteam.library.entity.CategoryInfoNodeEntity;
import com.sunteam.library.net.HttpDao;
import com.sunteam.library.utils.LibraryConstant;
import com.sunteam.library.utils.PublicUtils;

/**
 * 得到分类异步加载类
 * 
 * @author wzp
 * @Created 2017/01/24
 */
public class GetCategoryAsyncTask extends AsyncTask<Integer, Void, ArrayList<CategoryInfoNodeEntity>>
{
	private Context mContext;
	private String mFatherPath;
	private String mTitle;
	private int type;
	private static ArrayList<CategoryInfoNodeEntity> mCategoryInfoNodeEntityList = new ArrayList<CategoryInfoNodeEntity>();
	
	public GetCategoryAsyncTask(Context context, String fatherPath, String title) 
	{
		PublicUtils.createCacheDir(fatherPath, title);	//创建缓存目录
		
		mContext = context;
		mFatherPath = fatherPath+title+"/";
		mTitle = title;
	}

	/**
	 * 根据父节点得到此节点下所有子节点信息
	 * @param fatherSeq：父节点在他那一级别中的序号
	 * @return 此父节点的所有子节点列表
	*/
	public static ArrayList<CategoryInfoNodeEntity> getChildNodeList( int fatherSeq  )
	{
		ArrayList<CategoryInfoNodeEntity> list = new ArrayList<CategoryInfoNodeEntity>();
		
		int size = mCategoryInfoNodeEntityList.size();
		if( 0 == size )
		{
			return	list;
		}
		
		if( -1 == fatherSeq )	//得到第一级别子节点
		{	
			for( int i = 0; i < size; i++ )
			{
				CategoryInfoNodeEntity node = mCategoryInfoNodeEntityList.get(i);
				if( 0 == node.level )
				{
					list.add(node);
				}
			}
		}
		else
		{
			CategoryInfoNodeEntity fatherNode = mCategoryInfoNodeEntityList.get(fatherSeq);	//先得到父节点信息
			for( int i = fatherSeq+1; i < size; i++ )
			{
				CategoryInfoNodeEntity node = mCategoryInfoNodeEntityList.get(i);
				if( fatherNode.seq == node.father )
				{
					list.add(node);
				}
			}
		}
		
		return	list;
	}

	@Override
	protected ArrayList<CategoryInfoNodeEntity> doInBackground(Integer... params) 
	{
		type = params[0];
		ArrayList<CategoryInfoNodeEntity> list = HttpDao.getCategoryInfoList(type);
		
		if( ( list != null ) && ( list.size() > 0 ) )
		{
			mCategoryInfoNodeEntityList.addAll(list);
			
			CategoryDBDao dao = new CategoryDBDao( mContext );
			//dao.deleteAll(type);	//先删除缓存的此类型所有数据
			ArrayList<CategoryInfoNodeEntity> listOld = dao.findAll(type);	//得到此类型所有的数据
			dao.insert(listOld, list, type);	//再缓存新的数据
			dao.closeDb();			//关闭数据库
		}
		else
		{
			CategoryDBDao dao = new CategoryDBDao( mContext );
			list = dao.findAll(type);	//得到此类型所有的数据
			dao.closeDb();				//关闭数据库
			
			if( ( list != null ) && ( list.size() > 0 ) )
			{
				mCategoryInfoNodeEntityList.addAll(list);
			}
		}
		
		return	mCategoryInfoNodeEntityList;
	}
	
	@Override
	protected void onPreExecute() 
	{	
		super.onPreExecute();
		PublicUtils.showProgress(mContext, this);
		String s = mContext.getResources().getString(R.string.library_wait_reading_data);
		TtsUtils.getInstance().speak(s);
		mCategoryInfoNodeEntityList.clear();
	}
	
	@Override
	protected void onPostExecute(ArrayList<CategoryInfoNodeEntity> result) 
	{	
		super.onPostExecute(result);
		PublicUtils.cancelProgress();
	
		if(null != result && result.size() > 0)
		{
			startNextActivity();
		}
		else
		{
			String s = mContext.getResources().getString(R.string.library_reading_data_error);
			PublicUtils.showToast(mContext, s);
		}
	}
	
	private void startNextActivity() {
		Intent intent = new Intent();
		intent.putExtra(MenuConstant.INTENT_KEY_TITLE, mTitle); // 菜单名称
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER, LibraryConstant.LIBRARY_CATEGORY_ROOT_ID); // 父节点ID
		intent.putExtra(LibraryConstant.INTENT_KEY_TYPE, type); // 数据类别：电子书、有声书、口述影像
		intent.putExtra(LibraryConstant.INTENT_KEY_FATHER_PATH, mFatherPath);	//父目录
		intent.setClass(mContext, CategoryList.class);

		// 如果希望启动另一个Activity，并且希望有返回值，则需要使用startActivityForResult这个方法，
		// 第一个参数是Intent对象，第二个参数是一个requestCode值，如果有多个按钮都要启动Activity，则requestCode标志着每个按钮所启动的Activity
		mContext.startActivity(intent);
	}
	
}
