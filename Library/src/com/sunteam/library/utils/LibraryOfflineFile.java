package com.sunteam.library.utils;

import java.io.File;
import java.util.ArrayList;

import com.sunteam.common.menu.MenuGlobal;
import com.sunteam.library.R;

import android.content.Context;
import android.os.Environment;

/**
 * @Destryption 数字图书馆中本地内存中创建离线类别文件夹和资源文件
 * @Author Jerry
 * @Date 2017-1-26 上午9:59:58
 * @Note
 */
public class LibraryOfflineFile {

	public boolean createCategoryDirs(Context context, int type, ArrayList<String> list) {
		boolean ret = false;
		if (null != list) {
			for (int i = 0; i < list.size(); i++) {
				ret = createCategoryDir(context, type, list.get(i));
				if (!ret) {
					break;
				}
			}
		}
		return ret;
	}

	/**
	 * @Descryption 在指定类别的数据目录下创建子目录。
	 * 
	 * @param context
	 * @param type
	 *            数据类型
	 * @param path
	 *            文件夹字符串
	 * @return
	 */
	public boolean createCategoryDir(Context context, int type, String path) {
		String pathStr;
		switch (type) {
		case LibraryConstant.LIBRARY_DATATYPE_EBOOK: // 电子书
			pathStr = context.getResources().getString(R.string.library_category_ebook);
			break;
		case LibraryConstant.LIBRARY_DATATYPE_AUDIO: // 有声书
			pathStr = context.getResources().getString(R.string.library_category_audio);
			break;
		case LibraryConstant.LIBRARY_DATATYPE_VIDEO: // 口述影像
			pathStr = context.getResources().getString(R.string.library_category_video);
			break;
		default:
			return false;
		}

		pathStr = LibraryConstant.LIBRARY_ROOT_PATH + pathStr + "/" + path;
		return createDir(pathStr);
	}

	public boolean createDir(String path) {
		boolean ret = true;

		MenuGlobal.debug("[Library-LibraryOfflineFile][createDir] path = " + path);
		File file = new File(path);
		if (!file.exists()) {
			ret = file.mkdirs();
		}
		return ret;
	}

}
