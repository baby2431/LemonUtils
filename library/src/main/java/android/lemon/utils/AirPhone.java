package android.lemon.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

/** 飞行模式 AirMode
 * @author Kevin
 *
 */
public class AirPhone {

	/**
	 * 设置手机飞行模式
	 * 
	 * @param context
	 * @param enabling
	 *            true:设置为飞行模式 false:取消飞行模式
	 */
	public static void setAirplaneModeOn(Context context, boolean enabling) {
		if (enabling) {
			if (Build.VERSION.SDK_INT >= 17) {
				ShellUtils
						.execCommand(
								"settings put global airplane_mode_on 1 \n am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true \n",
								true);
			} else {
				Settings.System.putString(context.getContentResolver(), "airplane_mode_on", "1");

				context.sendBroadcast(new Intent("android.intent.action.AIRPLANE_MODE"));
			}
		} else {
			if (Build.VERSION.SDK_INT >= 17) {
				ShellUtils
						.execCommand(
								"settings put global airplane_mode_on 0 \n  am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false \n",
								true);
			} else {

				Settings.System.putString(context.getContentResolver(), "airplane_mode_on", "0");
				context.sendBroadcast(new Intent("android.intent.action.AIRPLANE_MODE"));
			}
		}

	}

	/**
	 * 判断手机是否是飞行模式
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getAirplaneMode(Context context) {
		int isAirplaneMode = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
		return (isAirplaneMode == 1) ? true : false;
	}

}
