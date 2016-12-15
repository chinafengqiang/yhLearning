package com.yh.utils;

import java.util.regex.Pattern;

public class StringUtils {
	public static boolean isBlank(String s){
		if(s == null || s.length() <= 0){
			return true;
		}
		return false;
	}
	
	public static boolean isNotBlank(String s){
		return !isBlank(s);
	}

	public static boolean isValidPass(String str){
		if (Pattern.matches("^[0-9a-zA-Z]{6,16}", str)) {
			return true;
		}
		return false;
	}
}
