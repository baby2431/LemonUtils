package android.lemon.utils;

import java.util.Locale;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;

/**主要是App的相关配置改变
 * @author Kevin
 * @date 2014年10月4日 下午6:04:56
 * 
 */
public class LocalUtil {
	/**
	 * @param activity
	 * @param isEnglish
	 * @param clazz
	 */
	public static  <T> void changeLanguage(Activity activity ,boolean isEnglish,Class<T> clazz){
		Resources resource = activity.getResources();  
		Configuration config = resource.getConfiguration();    
		config.locale = isEnglish?Locale.ENGLISH:Locale.CHINESE;  
		activity.getBaseContext().getResources().updateConfiguration(config, null); 
		Intent intent = new Intent();
		intent.setClass(activity, clazz);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(intent);
	}

	/**
	 * @param myApplication
	 * @param englishLanguage
	 */
	public static void changeLanguage(Application myApplication,
			boolean englishLanguage) {
		Resources resource = myApplication.getResources();  
		Configuration config = resource.getConfiguration();    
		config.locale = englishLanguage?Locale.ENGLISH:Locale.CHINESE;  
		myApplication.getBaseContext().getResources().updateConfiguration(config, null); 
	}
	
	/**
	 * @param context
	 * @return
	 */
	public static boolean getIsChinese(Context context) {
		Resources resource = context.getResources();  
		Configuration config = resource.getConfiguration();    
		if(config.locale ==Locale.CHINESE){
			return true;
		}
		return false;
	}
}
