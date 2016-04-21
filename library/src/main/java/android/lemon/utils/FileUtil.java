package android.lemon.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.lemon.assist.Check;
import android.lemon.config.BaseConfig;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Comparator;


/**
 * @author Kevin
 * @date 2014-08-10
 * <p>
 * Perssion
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.WRITE_SETTINGS" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.READ_OWNER_DATA" />
 * <uses-permission android:name="android.permission.RECORD_AUDIO" />
 */
public class FileUtil {


    /**
     * 默认APP根目录.
     */
    private static String downloadRootDir = null;

    /**
     * 默认下载图片文件目录.
     */
    private static String imageDownloadDir = null;

    /**
     * 默认下载文件目录.
     */
    private static String fileDownloadDir = null;

    /**
     * 默认缓存目录.
     */
    private static String cacheDownloadDir = null;

    /**
     * 默认下载数据库文件的目录.
     */
    private static String dbDownloadDir = null;

    /**
     * 剩余空间大于200M才使用SD缓存.
     */
    private static int freeSdSpaceNeededToCache = 200 * 1024 * 1024;


    /**
     * 描述：通过文件的本地地址从SD卡读取图片.
     *
     * @param file          the file
     * @param type          图片的处理类型（剪切或者缩放到指定大小，参考AbConstant类） 如果设置为原图，则后边参数无效，得到原图
     * @param desiredWidth  新图片的宽
     * @param desiredHeight 新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap getBitmapFromSD(File file, int type, int desiredWidth, int desiredHeight) {
        Bitmap bitmap = null;
        try {
            // SD卡是否存在
            if (!isCanUseSD()) {
                return null;
            }

            // 文件是否存在
            if (!file.exists()) {
                return null;
            }

            // 文件存在
            if (type == ImageUtil.CUTIMG) {
                bitmap = ImageUtil.getCutBitmap(file, desiredWidth, desiredHeight);
            } else if (type == ImageUtil.SCALEIMG) {
                bitmap = ImageUtil.getScaleBitmap(file, desiredWidth, desiredHeight);
            } else {
                bitmap = ImageUtil.getBitmap(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 描述：通过文件的本地地址从SD卡读取图片.
     *
     * @param file the file
     * @return Bitmap 图片
     */
    public static Bitmap getBitmapFromSD(File file) {
        Bitmap bitmap = null;
        try {
            // SD卡是否存在
            if (!isCanUseSD()) {
                return null;
            }
            // 文件是否存在
            if (!file.exists()) {
                return null;
            }
            // 文件存在
            bitmap = ImageUtil.getBitmap(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * Copy file.
     *
     * @param in  Input file (source).
     * @param out Output file (destination).
     * @throws IOException
     */
    public static void copyFile(InputStream in, OutputStream out)
            throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    /**
     * 描述：将图片的byte[]写入本地文件.
     *
     * @param imgByte       图片的byte[]形势
     * @param fileName      文件名称，需要包含后缀，如.jpg
     * @param type          图片的处理类型（剪切或者缩放到指定大小，参考AbConstant类）
     * @param desiredWidth  新图片的宽
     * @param desiredHeight 新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap getBitmapFromByte(byte[] imgByte, String fileName, int type, int desiredWidth,
                                           int desiredHeight) {
        FileOutputStream fos = null;
        DataInputStream dis = null;
        ByteArrayInputStream bis = null;
        Bitmap bitmap = null;
        File file = null;
        try {
            if (imgByte != null) {

                file = new File(imageDownloadDir + fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                int readLength = 0;
                bis = new ByteArrayInputStream(imgByte);
                dis = new DataInputStream(bis);
                byte[] buffer = new byte[1024];

                while ((readLength = dis.read(buffer)) != -1) {
                    fos.write(buffer, 0, readLength);
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                fos.flush();

                bitmap = getBitmapFromSD(file, type, desiredWidth, desiredHeight);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (Exception e) {
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (Exception e) {
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
        }
        return bitmap;
    }

    /**
     * 描述：根据URL从互连网获取图片.
     *
     * @param url           要下载文件的网络地址
     * @param type          图片的处理类型（剪切或者缩放到指定大小，参考AbConstant类）
     * @param desiredWidth  新图片的宽
     * @param desiredHeight 新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap getBitmapFromURL(String url, int type, int desiredWidth, int desiredHeight) {
        Bitmap bit = null;
        try {
            bit = ImageUtil.getBitmap(url, type, desiredWidth, desiredHeight);
        } catch (Exception e) {
            Log.d(FileUtil.class, "下载图片异常：" + e.getMessage());
        }
        return bit;
    }

    /**
     * 描述：获取src中的图片资源.
     *
     * @param src 图片的src路径，如（“image/arrow.png”）
     * @return Bitmap 图片
     */
    public static Bitmap getBitmapFromSrc(String src) {
        Bitmap bit = null;
        try {
            bit = BitmapFactory.decodeStream(FileUtil.class.getResourceAsStream(src));
        } catch (Exception e) {
            Log.d(FileUtil.class, "获取图片异常：" + e.getMessage());
        }
        return bit;
    }

    /**
     * 描述：获取Asset中的图片资源.
     *
     * @param context  the context
     * @param fileName the file name
     * @return Bitmap 图片
     */
    public static Bitmap getBitmapFromAsset(Context context, String fileName) {
        Bitmap bit = null;
        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(fileName);
            bit = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            Log.d(FileUtil.class, "获取图片异常：" + e.getMessage());
        }
        return bit;
    }

    /**
     * 描述：获取Asset中的图片资源.
     *
     * @param context  the context
     * @param fileName the file name
     * @return Drawable 图片
     */
    public static Drawable getDrawableFromAsset(Context context, String fileName) {
        Drawable drawable = null;
        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(fileName);
            drawable = Drawable.createFromStream(is, null);
        } catch (Exception e) {
            Log.d(FileUtil.class, "获取图片异常：" + e.getMessage());
        }
        return drawable;
    }


    /**
     * 获取文件名（不含后缀）.
     *
     * @param url 文件地址
     * @return 文件名
     */
    public static String getCacheFileNameFromUrl(String url) {
        if (Check.isEmpty(url)) {
            return null;
        }
        String name = null;
        try {
            name = MD5Util.md5(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }


    /**
     * 描述：从sd卡中的文件读取到byte[].
     *
     * @param path sd卡中文件路径
     * @return byte[]
     */
    public static byte[] getByteArrayFromSD(String path) {
        byte[] bytes = null;
        ByteArrayOutputStream out = null;
        try {
            File file = new File(path);
            // SD卡是否存在
            if (!isCanUseSD()) {
                return null;
            }
            // 文件是否存在
            if (!file.exists()) {
                return null;
            }

            long fileSize = file.length();
            if (fileSize > Integer.MAX_VALUE) {
                return null;
            }

            FileInputStream in = new FileInputStream(path);
            out = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int size = 0;
            while ((size = in.read(buffer)) != -1) {
                out.write(buffer, 0, size);
            }
            in.close();
            bytes = out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
        return bytes;
    }

    /**
     * 描述：将byte数组写入文件.
     *
     * @param path    the path
     * @param content the content
     * @param create  the create
     */
    public static void writeByteArrayToSD(String path, byte[] content, boolean create) {

        FileOutputStream fos = null;
        try {
            File file = new File(path);
            // SD卡是否存在
            if (!isCanUseSD()) {
                return;
            }
            // 文件是否存在
            if (!file.exists()) {
                if (create) {
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                        file.createNewFile();
                    }
                } else {
                    return;
                }
            }
            fos = new FileOutputStream(path);
            fos.write(content);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 描述：SD卡是否能用.
     *
     * @return true 可用,false不可用
     */
    public static boolean isCanUseSD() {
        try {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 描述：初始化存储目录.
     *
     * @param context the context
     */
    public static void initFileDir(Context context) {
        if (BaseConfig.DOWNLOAD_DIR == null) {
            PackageInfo info = AppUtil.getPackageInfo(context);
            BaseConfig.DOWNLOAD_DIR = info.packageName;
        }
        // 默认下载文件根目录.
        String downloadRootPath = File.separator + BaseConfig.DOWNLOAD_ROOT_DIR + File.separator + BaseConfig.DOWNLOAD_DIR
                + File.separator;

        // 默认下载图片文件目录.
        String imageDownloadPath = downloadRootPath + BaseConfig.DOWNLOAD_IMAGE_DIR + File.separator;

        // 默认下载文件目录.
        String fileDownloadPath = downloadRootPath + BaseConfig.DOWNLOAD_FILE_DIR + File.separator;

        // 默认缓存目录.
        String cacheDownloadPath = downloadRootPath + BaseConfig.CACHE_DIR + File.separator;

        // 默认DB目录.
        String dbDownloadPath = downloadRootPath + BaseConfig.DB_NAME + File.separator;

        try {
            if (!isCanUseSD()) {
                return;
            } else {

                File root = Environment.getExternalStorageDirectory();
                File downloadDir = new File(root.getAbsolutePath() + downloadRootPath);
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs();
                }
                downloadRootDir = downloadDir.getPath();

                File cacheDownloadDirFile = new File(root.getAbsolutePath() + cacheDownloadPath);
                if (!cacheDownloadDirFile.exists()) {
                    cacheDownloadDirFile.mkdirs();
                }
                cacheDownloadDir = cacheDownloadDirFile.getPath();

                File imageDownloadDirFile = new File(root.getAbsolutePath() + imageDownloadPath);
                if (!imageDownloadDirFile.exists()) {
                    imageDownloadDirFile.mkdirs();
                }
                imageDownloadDir = imageDownloadDirFile.getPath();

                File fileDownloadDirFile = new File(root.getAbsolutePath() + fileDownloadPath);
                if (!fileDownloadDirFile.exists()) {
                    fileDownloadDirFile.mkdirs();
                }
                fileDownloadDir = fileDownloadDirFile.getPath();

                File dbDownloadDirFile = new File(root.getAbsolutePath() + dbDownloadPath);
                if (!dbDownloadDirFile.exists()) {
                    dbDownloadDirFile.mkdirs();
                }
                dbDownloadDir = dbDownloadDirFile.getPath();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算sdcard上的剩余空间.
     *
     * @return the int
     */
    @SuppressWarnings("deprecation")
    public static int freeSpaceOnSD() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / 1024 * 1024;
        return (int) sdFreeMB;
    }

    /**
     * 根据文件的最后修改时间进行排序.
     */
    public static class FileLastModifSort implements Comparator<File> {

        /*
         * (non-Javadoc)
         *
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    /**
     * 删除所有缓存文件.
     *
     * @return true, if successful
     */
    public static boolean clearDownloadFile() {
        try {
            File fileDirectory = new File(downloadRootDir);
            deleteFile(fileDirectory);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除文件.
     *
     * @return true, if successful
     */
    public static boolean deleteFile(File file) {

        try {
            if (!isCanUseSD()) {
                return false;
            }
            if (file == null) {
                return true;
            }
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            } else {
                file.delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 保存方法
     */
    public static String saveBitmap(Context context, Bitmap bm) {
        File f = new File(getImageDownloadDir(context), System.currentTimeMillis() + ".jpg");
        if (f.exists()) {
            f.delete();
        }
        // SD卡是否存在
        if (!isCanUseSD()) {
            return null;
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return f.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 描述：读取Assets目录的文件内容.
     *
     * @param context  the context
     * @param name     the name
     * @param encoding the encoding
     * @return the string
     */
    public static String readAssetsByName(Context context, String name, String encoding) {
        String text = null;
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            inputReader = new InputStreamReader(context.getAssets().open(name));
            bufReader = new BufferedReader(inputReader);
            String line = null;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufReader.readLine()) != null) {
                buffer.append(line);
            }
            text = new String(buffer.toString().getBytes(), encoding);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufReader != null) {
                    bufReader.close();
                }
                if (inputReader != null) {
                    inputReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return text;
    }

    /**
     * 描述：读取Raw目录的文件内容.
     *
     * @param context  the context
     * @param id       the id
     * @param encoding the encoding
     * @return the string
     */
    public static String readRawByName(Context context, int id, String encoding) {
        String text = null;
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            inputReader = new InputStreamReader(context.getResources().openRawResource(id));
            bufReader = new BufferedReader(inputReader);
            String line = null;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufReader.readLine()) != null) {
                buffer.append(line);
            }
            text = new String(buffer.toString().getBytes(), encoding);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufReader != null) {
                    bufReader.close();
                }
                if (inputReader != null) {
                    inputReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return text;
    }

    /**
     * Gets the download root dir.
     *
     * @param context the context
     * @return the download root dir
     */
    public static String getDownloadRootDir(Context context) {
        if (downloadRootDir == null) {
            initFileDir(context);
        }
        return downloadRootDir;
    }

    /**
     * Gets the image download dir.
     *
     * @param context the context
     * @return the image download dir
     */
    public static String getImageDownloadDir(Context context) {
        if (downloadRootDir == null) {
            initFileDir(context);
        }
        return imageDownloadDir;
    }

    /**
     * Gets the file download dir.
     *
     * @param context the context
     * @return the file download dir
     */
    public static String getFileDownloadDir(Context context) {
        if (downloadRootDir == null) {
            initFileDir(context);
        }
        return fileDownloadDir;
    }

    /**
     * Gets the cache download dir.
     *
     * @param context the context
     * @return the cache download dir
     */
    public static String getCacheDownloadDir(Context context) {
        if (downloadRootDir == null) {
            initFileDir(context);
        }
        return cacheDownloadDir;
    }

    /**
     * Gets the db download dir.
     *
     * @param context the context
     * @return the db download dir
     */
    public static String getDbDownloadDir(Context context) {
        if (downloadRootDir == null) {
            initFileDir(context);
        }
        return dbDownloadDir;
    }

    /**
     * Gets the free sd space needed to cache.
     *
     * @return the free sd space needed to cache
     */
    public static int getFreeSdSpaceNeededToCache() {
        return freeSdSpaceNeededToCache;
    }


    public static void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            FileChannel in = fi.getChannel();//得到对应的文件通道
            FileChannel out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fo != null) fo.close();
                if (fi != null) fi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static String formatFileSizeToString(long fileLen) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = null;
        if (fileLen < 1024) {
            fileSizeString = df.format((double) fileLen) + "B";
        } else if (fileLen < 1048576) {
            fileSizeString = df.format((double) fileLen / 1024) + "K";
        } else if (fileLen < 1073741824) {
            fileSizeString = df.format((double) fileLen / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileLen / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获得SD卡总大小
     *
     * @return
     */
    public static long getSDTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks;
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static long getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /**
     * 获得机身内存总大小
     *
     * @return
     */
    public static long getRomTotalSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks;
    }

    /**
     * 获得机身可用内存
     *
     * @return
     */
    public static long getRomAvailableSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /***
     * 获取文件扩展名
     *
     * @param filename
     * @return 返回文件扩展名
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }


    /**
     * 读取指定文件的输出
     */
    public static String getFileOutputString(String path) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path), 8192);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append("\n").append(line);
            }
            bufferedReader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
