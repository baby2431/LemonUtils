package android.lemon.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.WindowManager;

public class ScreenUtils {

    /**
     * 点亮屏幕 权限
     * <p/>
     * <uses-permission android:name="android.permission.WAKE_LOCK"/> 你可能还需要
     * <uses-permission android:name="android.permission.DEVICE_POWER"/>
     * 另外WakeLock的设置是 Activiy 级别的，不是针对整个Application应用的。
     *
     * @param context
     */
    public static void wakeUp(Context context) {

        // 获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        // 点亮屏幕
        wl.acquire();
        // 释放
        wl.release();
    }

    /**
     * 解锁屏幕 需要的权限
     * <uses-permission android:name="android.permission.DISABLE_KEYGUARD"/>
     * @param context
     */
    public static void unLock(Context context){
        //屏幕解锁
        KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
        keyguardLock.disableKeyguard();
    }

    /**
     * 锁定屏幕
     * 需要的权限
     * <uses-permission android:name="android.permission.DISABLE_KEYGUARD"/>
     * @param context
     */
    public static void lock(Context context){
        //屏幕解锁
        KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
        keyguardLock.reenableKeyguard();
    }


    /** 设置永不休眠
     * @param context
     */
    public static void setupSleepTime(Context context){

        Settings.System.putInt(context.getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, -1);
    }

    /** 设置休眠时间，在一些情况下有效，并不是特别的准，设置为-1时，永不休眠。
     * @param context
     */
    public static void setupSleepTime(Context context,int time){
        Settings.System.putInt(context.getContentResolver(),android.provider.Settings.System.SCREEN_OFF_TIMEOUT,time);
    }


    /**
     * 保持屏幕常亮
     *
     * @param context
     */
    public static void screenOn(Activity context) {
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 不保持保持屏幕常亮
     *
     * @param context
     */
    public static void screenOff(Activity context) {
        context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


}
