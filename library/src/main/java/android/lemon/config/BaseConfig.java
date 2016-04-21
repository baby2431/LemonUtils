package android.lemon.config;


/**
 * Created by Kevin on 2015/9/28.
 */
public class BaseConfig {

    /**
     * 默认 SharePreferences文件名.
     */
    public static String SHARED_PATH;

    /**
     * 默认下载文件存储位置. 一般是公司名字
     * e.g SD卡/DOWNLOAD_ROOT_DIR/
     */
    public static String DOWNLOAD_ROOT_DIR = "lemon";

    /**
     * 当DOWNLOAD_DIR 为空时 取应用包名为第二层目录
     * e.g SD卡/DOWNLOAD_ROOT_DIR/DOWNLOAD_DIR/
     *
     */
    public static String DOWNLOAD_DIR ;

    /**
     * 默认下载图片文件地址.
     *  SD卡/DOWNLOAD_ROOT_DIR/DOWNLOAD_DIR/DOWNLOAD_IMAGE_DIR/
     */
    public static String DOWNLOAD_IMAGE_DIR = "images";

    /**
     * 默认下载文件地址.
     * /DOWNLOAD_ROOT_DIR/DOWNLOAD_DIR/DOWNLOAD_FILE_DIR/
     */
    public static String DOWNLOAD_FILE_DIR = "files";

    /**
     * APP缓存目录.
     * /DOWNLOAD_ROOT_DIR/DOWNLOAD_DIR/CACHE_DIR/
     */
    public static String CACHE_DIR = "cache";

    /**
     * 默认数据库的名字.
     *  /DOWNLOAD_ROOT_DIR/DOWNLOAD_DIR/DB_DIR/
     */
    public static String DB_NAME = "lemon";




}
