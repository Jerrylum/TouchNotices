package io.Jerry.TouchNotices.Util;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {
	private static ResourceBundle res;
	
	public static void run(){
		//res = ResourceBundle.getBundle("messages", Locale.getDefault());
		res = ResourceBundle.getBundle("messages", new Locale("en", "US"));
	}
	
	public static String t(String str){
		return res.getString(str);
	}
}
