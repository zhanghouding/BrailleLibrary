package com.sunteam.library.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;

import com.sunteam.library.R;

/**
 * 可重用的方法工具类。
 * 
 * @author wzp
 */
public class PublicUtils 
{
	private static ProgressDialog progress;

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
}	
