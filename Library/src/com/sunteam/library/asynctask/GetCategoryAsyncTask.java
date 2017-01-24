package com.sunteam.library.asynctask;

import java.util.ArrayList;

import com.sunteam.library.entity.CategoryInfoNodeEntity;
import com.sunteam.library.net.HttpDao;

import android.os.AsyncTask;

/**
 * 得到分类异步加载类
 * 
 * @author wzp
 * @Created 2017/01/24
 */
public class GetCategoryAsyncTask extends AsyncTask<Integer, Void, ArrayList<CategoryInfoNodeEntity>>
{
	private ArrayList<CategoryInfoNodeEntity> mCategoryInfoNodeEntityList;
	
	/**
	 * 根据父节点得到此节点下所有子节点信息
	 * @param fatherSeq：父节点在他那一级别中的序号
	 * @return 此父节点的所有子节点列表
	*/
	private ArrayList<CategoryInfoNodeEntity> getChildNodeList( int fatherSeq  )
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
				if( 1 == node.level )
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
		// TODO Auto-generated method stub
		mCategoryInfoNodeEntityList = HttpDao.getCategoryInfoList(params[0]);
		
		return	mCategoryInfoNodeEntityList;
	}
	
	@Override
	protected void onPreExecute() 
	{	
		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(ArrayList<CategoryInfoNodeEntity> result) 
	{	
		super.onPostExecute(result);
	}
}
