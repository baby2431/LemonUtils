package android.lemon.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

/**
 * @author Kevin
 */
public class SoundPoolUtil {

    private static SoundPool soundPool;
    private static int soundid;
    private static float volumnRatio = 0;
    private static int playCount = 0;

    public static boolean playSound = true;

    /**
     * 播放一个铃声
     *
     * @param context
     * @param resId   音乐资源ID
     */
    public static void soundPlay(Context context, int resId) {
        if (playSound) {
            playCount = 0;
            replease();
            AudioManager am = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);// 实例化
            int audioMaxVolum = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);// 音效最大值
            am.setStreamVolume(AudioManager.STREAM_SYSTEM, audioMaxVolum,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            float audioCurrentVolum = am
                    .getStreamVolume(AudioManager.STREAM_SYSTEM);
            volumnRatio = audioCurrentVolum / audioMaxVolum;
            soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
            soundid = soundPool.load(context, resId, 1);
            soundPool.setOnLoadCompleteListener(new LoadCompleteListener());

        }
    }

    /**
     * 播放一个铃声
     *
     * @param context
     * @param resId   音乐资源ID
     * @param count   循环次数
     */
    public static void soundPlay(Context context, int resId, int count) {
        if (playSound) {
            playCount = count;
            replease();
            AudioManager am = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);// 实例化
            int audioMaxVolum = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);// 音效最大值
            am.setStreamVolume(AudioManager.STREAM_SYSTEM, audioMaxVolum,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            float audioCurrentVolum = am
                    .getStreamVolume(AudioManager.STREAM_SYSTEM);
            volumnRatio = audioCurrentVolum / audioMaxVolum;

            soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
            soundid = soundPool.load(context, resId, 1);
            soundPool.setOnLoadCompleteListener(new LoadCompleteListener());
        }
    }

    /**
     * 停止播放 回收内存
     */
    public static void replease() {

        if (soundPool != null) {
            soundPool.stop(soundid);
            soundPool.release();
            soundPool = null;
            System.gc();
        }
    }

    /**
     * 当加载完成后 调用的类
     *
     * @author Kevin
     * @date 2014年10月9日 下午10:51:45
     */
    @SuppressLint("NewApi")
    private static class LoadCompleteListener implements OnLoadCompleteListener {

        @Override
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            soundPool.play(soundid, volumnRatio, volumnRatio, 1, playCount, 1);
            // 左右声道 为 1 优先级为 1 循环三次 ，速度正常 (小于1放慢 大于1放快 最大为2)
        }

    }
}
