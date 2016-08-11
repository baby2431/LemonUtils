package android.lemon.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * 注意，调节音量需要权限 <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
 * Created by Kevin on 2016/3/19.
 */
public class VolumeUtils {

    /**
     * 调节系统的音量到目标音量
     *
     * @param context
     * @param volume
     */
    public static void adjustVolume(Context context, int volume) {
        AudioManager am = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_SYSTEM, volume,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    /**
     * 调节通话音量到目标音量
     *
     * @param context
     * @param volume
     */
    public static void adjustVoiceVolume(Context context, int volume) {
        AudioManager am = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, volume,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    /**
     * 调节媒体音量到目标音量
     *
     * @param context
     * @param volume
     */
    public static void adjustMusicVolume(Context context, int volume) {
        AudioManager am = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, volume,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }


    /**
     * 得到系统最大音量
     *
     * @param context
     * @return
     */
    public static int getMaxVolume(Context context) {
        AudioManager am = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        int audioMaxVolum = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        return audioMaxVolum;
    }

    /**
     * 得到通话最大音量
     *
     * @param context
     * @return
     */
    public static int getMaxVoiceVolume(Context context) {
        AudioManager am = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        int audioMaxVolum = am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        return audioMaxVolum;
    }

    /**
     * 得到音乐最大音量
     *
     * @param context
     * @return
     */
    public static int getMaxMusicVolume(Context context) {
        AudioManager am = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        int audioMaxVolum = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return audioMaxVolum;
    }

    /**
     * 设置系统最大音量
     *
     * @param context
     */
    public static void setMaxVolume(Context context) {
        AudioManager am = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);// 实例化
        int audioMaxVolum = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        am.setStreamVolume(AudioManager.STREAM_SYSTEM, audioMaxVolum,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

    }

    /**
     * 设置音乐最大音量
     *
     * @param context
     */
    public static void setMaxVoiceVolume(Context context) {
        AudioManager am = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);// 实例化
        int audioMaxVolum = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioMaxVolum,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

    }

    /**
     * 设置音乐最大音量
     *
     * @param context
     */
    public static void setMaxMusicVolume(Context context) {
        AudioManager am = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);// 实例化
        int audioMaxVolum = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        am.setStreamVolume(AudioManager.STREAM_SYSTEM, audioMaxVolum,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

    }


}
