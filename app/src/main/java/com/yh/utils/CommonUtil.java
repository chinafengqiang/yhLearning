package com.yh.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by FQ on 14-3-14.
 */
public class CommonUtil {
	protected static final String TAG = "CommonUtil";

	public static Dialog m_dialog = null;


	public static boolean isBlank(String s){
		if(s == null || s.length() <= 0){
			return true;
		}
		return false;
	}
	
	public static boolean isNotBlank(String s){
		return !isBlank(s);
	}

	public static String getSignTimestamp() {
		long time = new Date().getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMHHmmssdd");
		return format.format(new Date(time));
	}

	public static boolean isValidMobiNumber(String paramString) {
		String regex = "^1[2,3,4,5,6,7,8,9]\\d{9}$";
		if (paramString.matches(regex)) {
			return true;
		}
		return false;
	}

	public synchronized static String getRandomCode(int length) {
		String sRand = "";
		for (int i = 0; i < length; i++) {
			Random random = new Random();
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
		}
		return sRand;
	}

	public static Date stringTime2DateNotss(String date) {
		long longDate = dateTimeString2LongNotss(date);
		return new Date(longDate);
	}

	public static Long dateTimeString2LongNotss(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date thisDate = null;
		try {
			thisDate = format.parse(date);
		} catch (ParseException e) {
			return null;
		}
		return new Long(thisDate.getTime());
	}

	public static String dateTime2String(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String thisDate = "";
		try {
			thisDate = format.format(date);
		} catch (Exception e) {
			return "";
		}
		return thisDate;
	}

	public static String dateTime2StringNotS(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String thisDate = "";
		try {
			thisDate = format.format(date);
		} catch (Exception e) {
			return "";
		}
		return thisDate;
	}

	public static Date getEndValidDate4Day(Date date, int step) {
		Calendar end = Calendar.getInstance();
		end.setTime(date);
		end.add(Calendar.DATE, step);
		return end.getTime();
	}

	public static String getPhoneNumber(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String mobile = phoneManager.getLine1Number();
		if (mobile != null && !"".equals(mobile)) {
			if (mobile.startsWith("+86")) {
				mobile = mobile.substring(mobile.length() - 11);
			}
		}
		return mobile;
	}

	/*
	 * 璁剧疆Toast銆佸彲鎵╁睍鎴愯嚜瀹氫箟Toast
	 */
	public static void showToast(Context context, int resId, int duration) {
		Toast toast = Toast.makeText(context, resId, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void showToast(Context context, String info, int duration) {
		Toast toast = Toast.makeText(context, info, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static String[] getNode(String str, String splid) {

		if (str == null) {
			return null;
		}
		String[] result;
		Vector<String> v = new Vector<String>();
		int begin = 0, end = 0;
		int splidlength = splid.length();
		while (str.indexOf(splid, begin) > 0) {
			end = str.indexOf(splid, begin);
			v.addElement(str.substring(begin, end));
			begin = end + splidlength;
		}
		if (begin < str.length()) {
			v.addElement(str.substring(begin));
		}
		result = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			result[i] = (String) v.elementAt(i);
		}
		return result;
	}

	public static String getDynamicPassword(String str) {
		Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]+");
		Matcher m = continuousNumberPattern.matcher(str);
		String dynamicPassword = "";
		while (m.find()) {
			if (m.group().length() == 6) {
				System.out.print(m.group());
				dynamicPassword = m.group();
			}
		}
		return dynamicPassword;
	}
	
	/**
	 * 第三方调用sdk时，想替换sdk里的一些图片，通过程序包名、图片名、资源类型获取三方是否有更换，如果过没有，则默认使用sdk本身
	 * @param mContext 上下文
	 * @param ImgName 替换的图片名字，已经在string.xml里定义了其名字的value，可在那里替换名字，name为：imm_video_bg_third_party
	 * @param resType 资源类型：drawable
	 * @param defaultResId 默认的图片资源
	 * @return 如果是0，代表没有，不想替换，使用默认
	 */
	public static int getResourceID(Context mContext, String ImgName, String resType, int defaultResId){
		try {
			int resID = mContext.getResources().getIdentifier(ImgName, resType, mContext.getPackageName());
			if(resID == 0)
				return defaultResId;
			else
				return resID;
		} catch (Exception e) {
			Log.e("replace_img", "sdk_replace_img_error");
			return defaultResId;
		}
		
	}
	
	public static String getAppVersionName(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String getApkPackageName(Context context){
			PackageManager pm = context.getPackageManager();
			try {
	        PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
	        if (info != null) {  
	            ApplicationInfo appInfo = info.applicationInfo;  
	            return appInfo.packageName; 
	        } else {  
	            return null;  
	        }  
		} catch (Exception e) {
			return null;
		}
        
    }

}


