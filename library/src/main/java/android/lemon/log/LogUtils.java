package android.lemon.log;


import android.lemon.utils.AndroidUtils;

/**
 * Created by kevin on 2016/4/22.
 * 日志工具
 */
public  class LogUtils  {

    private static Logger logger;
    static {
        logger = new Logger();
    }

    // 允许输出日志
    public static boolean configAllowLog = true;

    // 配置日志Tag前缀
    public static String configTagPrefix = "";

    /**
     * verbose输出
     * @param msg
     * @param args
     */
    public static void v(String msg, Object... args){
        logger.v(AndroidUtils.getStackTrace(), msg, args);
    }
    public static void v(Object object){
        logger.v(AndroidUtils.getStackTrace(), object);
    }


    /**
     * debug输出
     * @param msg
     * @param args
     */
    public static void d(String msg, Object... args) {
        logger.d(AndroidUtils.getStackTrace(), msg, args);
    }

    public static void d(Object object) {
        logger.d(AndroidUtils.getStackTrace(), object);
    }

    /**
     * info输出
     * @param msg
     * @param args
     */
    public static void i(String msg, Object... args){
        logger.i(AndroidUtils.getStackTrace(), msg, args);
    }
    public static void i(Object object){
        logger.i(AndroidUtils.getStackTrace(), object);
    }

    /**
     * warn输出
     * @param msg
     * @param args
     */
    public static void w(String msg, Object... args){
        logger.w(AndroidUtils.getStackTrace(), msg, args);
    }
    public static void w(Object object){
        logger.w(AndroidUtils.getStackTrace(), object);
    }

    /**
     * error输出
     * @param msg
     * @param args
     */
    public static void e(String msg, Object... args){
        logger.e(AndroidUtils.getStackTrace(), msg, args);
    }
    public static void e(Object object){
        logger.e(AndroidUtils.getStackTrace(), object);
    }

    /**
     * assert输出
     * @param msg
     * @param args
     */
    public static void wtf(String msg, Object... args){
        logger.wtf(AndroidUtils.getStackTrace(), msg, args);
    }
    public static void wtf(Object object){
        logger.wtf(AndroidUtils.getStackTrace(), object);
    }

    /**
     * 打印json
     * @param json
     */
    public static void json(String json){
        logger.json(AndroidUtils.getStackTrace(), json);
    }

}
