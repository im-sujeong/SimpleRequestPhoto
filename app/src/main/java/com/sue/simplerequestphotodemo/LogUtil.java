package com.sue.simplerequestphotodemo;

import android.util.Log;

public class LogUtil {
	private static boolean isDebugMode = true;
	private static String TAG = "SimpleRequestPhoto_";

	public static void e(String tag, String msg){
		if(isDebugMode)
			Log.e(TAG+tag, msg);
	}
	
	public static void e(String tag, Exception e){
		if(isDebugMode){
			Log.e(TAG+tag, getErrorlog(e));
		}
	}
	
	public static void i(String tag, String msg){
		if(isDebugMode)
			Log.i(TAG+tag, msg);
	}
	
	public static void v(String tag, String msg){
		if(isDebugMode)
			Log.v(TAG+tag, msg);
	}
	
	public static String getErrorlog(Exception e) {
		/*
		 * String msg = ""; ByteArrayOutputStream out = new
		 * ByteArrayOutputStream(); PrintStream ps = new PrintStream(out);
		 * 
		 * msg = ps.toString();
		 */

		StringBuffer sb = new StringBuffer();
		try {
			sb.append(e.toString());
			sb.append("\n");
			StackTraceElement element[] = e.getStackTrace();
			int length = element.length;

			for (int idx = 0; idx < length ; idx++) {
				sb.append("\tat ");
				sb.append(element[idx].toString());
				sb.append("\n");
			}
		} catch (Exception ex) {
			return e.toString();
		}
		return sb.toString();
	}
}
