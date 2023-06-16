package com.landsem.common.tools;

import android.graphics.Color;
import android.util.Log;


public class LogManager {
	
	public static boolean IS_DEBUG_MODE = true;
	private static LogListener listener;
	private static final String TAG = "LogManager";
	public static String LOG_PRDFIX = "LQPDC==)    ";
	
	public static void closeDebugMode(){
		IS_DEBUG_MODE = false;
	}
	

	public static void v(String TAG, String msg) {
		if (IS_DEBUG_MODE) {
			Log.v(TAG, LOG_PRDFIX + msg);
			if (listener != null)
				listener.onAddLog(Level.VERBOSE, TAG, msg);
		}
	}

	public static void i(String TAG, String msg) {
		if (IS_DEBUG_MODE) {
			Log.i(TAG, LOG_PRDFIX + msg);
			if (listener != null)
				listener.onAddLog(Level.INFO, TAG, msg);
		}
	}

	public static void d(String TAG, String msg) {
		if (IS_DEBUG_MODE) {
			Log.d(TAG, LOG_PRDFIX + msg);
			if (listener != null)
				listener.onAddLog(Level.DEBUG, TAG, msg);
		}
	}

	public static void w(String TAG, String msg) {
		if (IS_DEBUG_MODE) {
			Log.w(TAG, LOG_PRDFIX + msg);
			if (listener != null)
				listener.onAddLog(Level.WARNING, TAG, msg);
		}
	}

	public static void e(String TAG, String msg) {
		if (IS_DEBUG_MODE) {
			Log.e(TAG, LOG_PRDFIX + msg);
			if (listener != null)
				listener.onAddLog(Level.ERROR, TAG, msg);
		}
	}
	
	
	public static void e(String TAG, String msg,Throwable e) {
		if (IS_DEBUG_MODE) {
			Log.e(TAG, msg,e);
			if (listener != null)
				listener.onAddLog(Level.ERROR, TAG, msg);
		}
	}

	public static enum Level {
		VERBOSE(Color.GRAY),
		INFO(Color.rgb(0, 192, 0)),
		DEBUG(Color.rgb(0, 0, 127)),
		WARNING(Color.rgb(255, 149, 144)),
		ERROR(Color.RED);
		private Level(int color) {
			this.color = color;
		}

		public int color;
	}

	public static void split() {
		String message = "---------------------------------------------------------------";
		d(createTag(), message);
	}

	public static void e(String message) {
		e(createTag(), message);
	}

	public static void d(String message) {
		d(createTag(), message);
	}

	public static void i(String message) {
		i(createTag(), message);
	}

	public static void w(String message) {
		w(createTag(), message);
	}

	public static void v(String message) {
		v(createTag(), message);
	}

	private static String createTag() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if (elements.length < 3) {
			return TAG;
		}
		else {
			String fullClassName = elements[4].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
			String methodName = elements[4].getMethodName();
			int lineNumber = elements[4].getLineNumber();
			String TAG = className + "." + methodName + "():" + lineNumber;
			return TAG;
		}
	}

	public static void printStackTrace() {
		
		StringBuilder builder = new StringBuilder();
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		if (stack != null) {
			for (int i = 0; i < stack.length; i++) {
				builder.append("\tat ");
				builder.append(stack[i].toString());
				builder.append("\n");
			}
		}
		i(builder.toString());
	}

	public static void setListener(LogListener listener) {
		LogManager.listener = listener;
	}

	public static interface LogListener {
		void onAddLog(Level level, String TAG, String msg);
	}
}
