package com.sunteam.library.utils;

import android.util.Log;

/**
 * @TODO 该Log的封装带有ThreadName Classname  LineNumber MethodName等信息
 * @author wzp
 * @Created 2017/01/24
 */
public class LogUtils {

	private final static boolean isDebug = true;

	private static String tag = "Library";
	private static final int logLevel = Log.VERBOSE;
	
    /**
     * @TODO Get The Current Function Name
     * @return 
     * @author wzp
     * @Created 2017/01/24
     */
	private static String getFunctionName() 
	{
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts == null)
		{
			return null;
		}
		for (StackTraceElement st : sts)
		{
			if (st.isNativeMethod()) 
			{
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) 
			{
				continue;
			}
			if (st.getClassName().equals(LogUtils.class.getName())) 
			{
				continue;
			}
			return "[Thread---" + Thread.currentThread().getName() + ";  ClassName---" + st.getFileName() + ";  LineNumber---" + st.getLineNumber() + ";  MethodName---" + st.getMethodName() + "()]";
		}
		return null;
	}

	/**
	 * @TODO The Log Level:i
	 * @param str 
	 * @author wzp
	 * @Created 2017/01/24
	 */
	public static void i(String tag, Object str) 
	{
		if (isDebug) 
		{
			if (logLevel <= Log.INFO)
			{
				String name = getFunctionName();
				if (name != null)
				{
					Log.i(tag, name + " - " + str);
				} 
				else 
				{
					Log.i(tag, str.toString());
				}
			}
		}

	}

	/**
	 * @TODO  The Log Level:d
	 * @param str 
	 * @author wzp
	 * @Created 2017/01/24
	 */
	public static void d(String tag, Object str) 
	{
		if (isDebug) 
		{
			if (logLevel <= Log.DEBUG) 
			{
				String name = getFunctionName();
				if (name != null)
				{
					Log.d(tag, name + " - " + str);
				} 
				else 
				{
					Log.d(tag, str.toString());
				}
			}
		}
	}

	/**
	 * @TODO The Log Level:V
	 * @param str 
	 * @author wzp
	 * @Created 2017/01/24
	 */
	public static void v(String tag, Object str) 
	{
		if (isDebug) 
		{
			if (logLevel <= Log.VERBOSE)
			{
				String name = getFunctionName();
				if (name != null) 
				{
					Log.v(tag, name + " - " + str);
				} 
				else 
				{
					Log.v(tag, str.toString());
				}
			}
		}
	}

	/**
	 * @TODO The Log Level:w
	 * @param str 
	 * @author wzp
	 * @Created 2017/01/24
	 */
	public static void w(String tag, Object str) 
	{
		if (isDebug) 
		{
			if (logLevel <= Log.WARN) 
			{
				String name = getFunctionName();
				if (name != null)
				{
					Log.w(tag, name + " - " + str);
				} 
				else 
				{
					Log.w(tag, str.toString());
				}
			}
		}
	}

	/**
	 * @TODO The Log Level:e
	 * @param str 
	 * @author wzp
	 * @Created 2017/01/24
	 */
	public static void e(String tag, Object str) 
	{
		if ( isDebug && logLevel <= Log.ERROR) 
		{
			String name = getFunctionName();
			if (name != null)
			{
				Log.e(tag, name + " - " + str);
			} 
			else 
			{
				Log.e(tag, str.toString());
			}
		}
	}


	/**
	 * @TODO The Log Level:i
	 * @param str 
	 * @author wzp
	 * @Created 2017/01/24
	 */
	public static void i(Object str) 
	{
		if (isDebug) 
		{
			if (logLevel <= Log.INFO) 
			{
				String name = getFunctionName();
				if (name != null) 
				{
					Log.i(tag, name + " - " + str);
				} 
				else 
				{
					Log.i(tag, str.toString());
				}
			}
		}
	}

	/**
	 * @TODO  The Log Level:d
	 * @param str 
	 * @author wzp
	 * @Created 2017/01/24
	 */
	public static void d(Object str) 
	{
		if (isDebug) 
		{
			if (logLevel <= Log.DEBUG) 
			{
				String name = getFunctionName();
				if (name != null) 
				{
					Log.d(tag, name + " - " + str);
				} 
				else 
				{
					Log.d(tag, str.toString());
				}
			}
		}
	}

	/**
	 * @TODO The Log Level:V
	 * @param str 
	 * @author wzp
	 * @Created 2017/01/24
	 */
	public static void v(Object str) 
	{
		if (isDebug) 
		{
			if (logLevel <= Log.VERBOSE)
			{
				String name = getFunctionName();
				if (name != null)
				{
					Log.v(tag, name + " - " + str);
				} 
				else 
				{
					Log.v(tag, str.toString());
				}
			}
		}
	}

	/**
	 * @TODO The Log Level:w
	 * @param str 
	 * @author wzp
	 * @Created 2017/01/24
	 */
	public static void w(Object str) 
	{
		if (isDebug) 
		{
			if (logLevel <= Log.WARN) 
			{
				String name = getFunctionName();
				if (name != null) 
				{
					Log.w(tag, name + " - " + str);
				} 
				else 
				{
					Log.w(tag, str.toString());
				}
			}
		}
	}

	/**
	 * @TODO The Log Level:e
	 * @param str 
	 * @author wzp
	 * @Created 2017/01/24
	 */
	public static void e(Object str) 
	{
		if (isDebug && logLevel <= Log.ERROR) 
		{
			String name = getFunctionName();
			if (name != null) 
			{
				Log.e(tag, name + " - " + str);
			} 
			else 
			{
				Log.e(tag, str.toString());
			}
		}
	}
	
	/**
	 * @TODO The Log Level:e
	 * @param ex 
	 * @author wzp
	 * @Created 2017/01/24
	 */
	public static void e(Exception ex) 
	{
		if (isDebug && logLevel <= Log.ERROR) 
		{
			Log.e(tag, "error", ex);
		}
	}

	/**
	 * @TODO The Log Level:e
	 * @param log
	 * @param tr 
	 * @author wzp
	 * @Created 2017/01/24
	 */
	public static void e(String log, Throwable tr) 
	{
		String line = getFunctionName();
		Log.e(tag, "{Thread---" + Thread.currentThread().getName() + "}" + "[" + line + "---] " + log + "\n", tr);
	}
}