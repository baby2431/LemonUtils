package android.lemon.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.lemon.R;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.net.URL;
import java.util.List;

public class IntentUtils {

    public final static int REQUEST_CODE_CAPTURE_CAMERA = 3001;
    public final static int REQUEST_CODE_PICK_IMAGE = 3002;


    /** 分享文件
     * @param file
     * @return
     */
    public static Intent shareFile(File file){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM,
                Uri.fromFile(file));
        intent.setType("*/*");//此处可发送多种文件
        return intent;
    }

    /**
     * 描述：卸载程序.
     *
     * @param context
     *            the context
     * @param packageName
     *            包名
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    /**
     * open network setting page
     *
     * @param context
     */
    public static void openNetworkSetting(Context context) {
        Intent intent = new Intent();
        int sdkVersion = VERSION.SDK_INT;
        if (sdkVersion >= 10) {
            intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        } else {
            intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
        }
        context.startActivity(intent);
    }

    /**
     * Open app page at Google Play. If Play Store application isn't available on the device
     * then web browser will be opened
     *
     * @param context Application context
     */
    public static Intent openPlayStore(Context context) {
        return openPlayStore(context, true);
    }

    /**
     * Open app page at Google Play
     *
     * @param context       Application context
     * @param openInBrowser Should we try to open application page in web browser
     *                      if Play Store app not found on device
     */
    public static Intent openPlayStore(Context context, boolean openInBrowser) {
        String appPackageName = context.getPackageName();
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
        if (isIntentAvailable(context, marketIntent)) {
            return marketIntent;
        }
        if (openInBrowser) {
            return openLink("https://play.google.com/store/apps/details?id=" + appPackageName);
        }
        return marketIntent;
    }

    /**
     * Send email message
     *
     * @param to      Receiver email
     * @param subject Message subject
     * @param text    Message body
     * @see #sendEmail(String[], String, String)
     */
    public static Intent sendEmail(String to, String subject, String text) {
        return sendEmail(new String[]{to}, subject, text);
    }

    /**
     * @see #sendEmail(String, String, String)
     */
    public static Intent sendEmail(String[] to, String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        return intent;
    }

    /**
     * Share text via thirdparty app like twitter, facebook, email, sms etc.
     *
     * @param subject Optional subject of the message
     * @param text    Text to share
     */
    public static Intent shareText(String subject, String text) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        if (!TextUtils.isEmpty(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        return intent;
    }

    /**
     * Send SMS message using built-in app
     *
     * @param context Application context
     * @param to      Receiver phone number
     * @param message Text to send
     */
    public static Intent sendSms(Context context, String to, String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + to));
            intent.putExtra("sms_body", message);
            if (defaultSmsPackageName != null) {
                intent.setPackage(defaultSmsPackageName);
            }
            return intent;
        } else {
            Uri smsUri = Uri.parse("tel:" + to);
            Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
            intent.putExtra("address", to);
            intent.putExtra("sms_body", message);
            intent.setType("vnd.android-dir/mms-sms");
            return intent;
        }
    }
    /**
     * 静默发送短信
     *
     * @param phone
     * @param msg
     */
    public static void sendSMS(String phone, String msg) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, msg, null, null);
    }

    /**
     * Opens the Street View application to the given location.
     * The URI scheme is based on the syntax used for Street View panorama information in Google Maps URLs.
     *
     * @param latitude  Latitude
     * @param longitude Longitude
     * @param yaw       Panorama center-of-view in degrees clockwise from North.
     *                  <p/>
     *                  Note: The two commas after the yaw parameter are required.
     *                  They are present for backwards-compatibility reasons.
     * @param pitch     Panorama center-of-view in degrees from -90 (look straight up) to 90 (look straight down.)
     * @param zoom      Panorama zoom. 1.0 = normal zoom, 2.0 = zoomed in 2x, 3.0 = zoomed in 4x, and so on.
     *                  A zoom of 1.0 is 90 degree horizontal FOV for a nominal landscape mode 4 x 3 aspect ratio display Android
     *                  phones in portrait mode will adjust the zoom so that the vertical FOV is approximately the same as the
     *                  landscape vertical FOV. This means that the horizontal FOV of an Android phone in portrait mode is much
     *                  narrower than in landscape mode. This is done to minimize the fisheye lens effect that would be present
     *                  if a 90 degree horizontal FOV was used in portrait mode.
     * @param mapZoom   The map zoom of the map location associated with this panorama.
     *                  This value is passed on to the Maps activity when the Street View "Go to Maps" menu item is chosen.
     *                  It corresponds to the zoomLevel parameter in {@link #showLocation(float, float, Integer)}
     */
    public static Intent showStreetView(float latitude,
                                        float longitude,
                                        Float yaw,
                                        Integer pitch,
                                        Float zoom,
                                        Integer mapZoom) {
        StringBuilder builder = new StringBuilder("google.streetview:cbll=").append(latitude).append(",").append(longitude);
        if (yaw != null || pitch != null || zoom != null) {
            String cbpParam = String.format("%s,,%s,%s", yaw == null ? "" : yaw, pitch == null ? "" : pitch, zoom == null ? "" : zoom);
            builder.append("&cbp=1,").append(cbpParam);
        }
        if (mapZoom != null) {
            builder.append("&mz=").append(mapZoom);
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(builder.toString()));
        return intent;
    }

    /**
     * Opens the Maps application to the given location.
     *
     * @param latitude  Latitude
     * @param longitude Longitude
     * @param zoomLevel A zoom level of 1 shows the whole Earth, centered at the given lat,lng.
     *                  A zoom level of 2 shows a quarter of the Earth, and so on. The highest zoom level is 23.
     *                  A larger zoom level will be clamped to 23.
     * @see #findLocation(String)
     */
    public static Intent showLocation(float latitude, float longitude, Integer zoomLevel) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        String data = String.format("geo:%s,%s", latitude, longitude);
        if (zoomLevel != null) {
            data = String.format("%s?z=%s", data, zoomLevel);
        }
        intent.setData(Uri.parse(data));
        return intent;
    }

    /**
     * Opens the Maps application to the given query.
     *
     * @param query Query string
     * @see #showLocation(float, float, Integer)
     */
    public static Intent findLocation(String query) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        String data = String.format("geo:0,0?q=%s", query);
        intent.setData(Uri.parse(data));
        return intent;
    }

    /**
     * Open system settings location services screen for turning on/off GPS
     */
    public static Intent showLocationServices() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        return intent;
    }

    /**
     * Open a browser window to the URL specified.
     *
     * @param url Target url
     */
    public static Intent openLink(String url) {
        // if protocol isn't defined use http by default
        if (!TextUtils.isEmpty(url) && !url.contains("://")) {
            url = "http://" + url;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        return intent;
    }

    /**
     * @see #openLink(String)
     */
    public static Intent openLink(URL url) {
        return openLink(url.toString());
    }







    /**
     * Pick file from sdcard with file manager. Chosen file can be obtained from Intent in onActivityResult.
     * See code below for example:
     * <p/>
     * <pre><code>
     *     @Override
     *     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     *         Uri file = data.getData();
     *     }
     * </code></pre>
     */
    public static Intent pickFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        return intent;
    }

    /**
     * Calls the entered phone number. Valid telephone numbers as defined in the IETF RFC 3966 are accepted.
     * Valid examples include the following:
     * tel:2125551212
     * tel: (212) 555 1212
     * <p/>
     * Note: This requires your application to request the following permission in your manifest:
     * <code>&lt;uses-permission android:name="android.permission.CALL_PHONE"/&gt;</code>
     *
     * @param phoneNumber Phone number
     */
    public static Intent callPhone(String phoneNumber) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        return intent;
    }

    /**
     * Pick contact from phone book
     */
    public static Intent pickContact() {
        return pickContact(null);
    }

    /**
     * Pick contact from phone book
     *
     * @param scope You can restrict selection by passing required content type. Examples:
     *              <p/>
     *              <code><pre>
     *                                                                                                                                                // Select only from users with emails
     *                                                                                                                                                IntentUtils.pickContact(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE);
     *              <p/>
     *                                                                                                                                                // Select only from users with phone numbers on pre Eclair devices
     *                                                                                                                                                IntentUtils.pickContact(Contacts.Phones.CONTENT_TYPE);
     *              <p/>
     *                                                                                                                                                // Select only from users with phone numbers on devices with Eclair and higher
     *                                                                                                                                                IntentUtils.pickContact(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
     *                                                                                                                                                </pre></code>
     */
    public static Intent pickContact(String scope) {
        Intent intent;
        if (isSupportsContactsV2()) {
            intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://com.android.contacts/contacts"));
        } else {
            intent = new Intent(Intent.ACTION_PICK, Contacts.People.CONTENT_URI);
        }

        if (!TextUtils.isEmpty(scope)) {
            intent.setType(scope);
        }
        return intent;
    }

    /**
     * Pick contact only from contacts with telephone numbers
     */
    public static Intent pickContactWithPhone() {
        Intent intent;
        if (isSupportsContactsV2()) {
            intent = pickContact(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        } else { // pre Eclair, use old contacts API
            intent = pickContact(Contacts.Phones.CONTENT_TYPE);
        }
        return intent;
    }

    /**
     * Pick image from gallery
     */
    public static Intent pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        return intent;
    }

    /**
     * Dials (but does not actually initiate the call) the number given.
     * Telephone number normalization described for {@link #callPhone(String)} applies to dial as well.
     *
     * @param phoneNumber Phone number
     */
    public static Intent dialPhone(String phoneNumber) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        return intent;
    }

    /**
     * Check that cropping application is available
     *
     * @param context Application context
     * @return true if cropping app is available
     * @see #cropImage(android.content.Context, java.io.File, int, int, int, int, boolean)
     */
    public static boolean isCropAvailable(Context context) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        return IntentUtils.isIntentAvailable(context, intent);
    }

    /**
     * Crop image. Before using, cropImage requires especial check that differs from
     * {@link #isIntentAvailable(android.content.Context, android.content.Intent)}
     * see {@link #isCropAvailable(android.content.Context)} instead
     *
     * @param context Application context
     * @param image   Image that will be used for cropping. This image is not changed during the cropImage
     * @param outputX Output image width
     * @param outputY Output image height
     * @param aspectX Crop frame aspect X
     * @param aspectY Crop frame aspect Y
     * @param scale   Scale or not cropped image if output image and cropImage frame sizes differs
     * @return Intent with <code>data</code>-extra in <code>onActivityResult</code> which contains result as a
     * {@link android.graphics.Bitmap}. See demo app for details
     */
    public static Intent cropImage(Context context, File image, int outputX, int outputY, int aspectX, int aspectY, boolean scale) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 0);
        ResolveInfo res = list.get(0);

        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("scale", scale);
        intent.putExtra("return-data", true);
        intent.setData(Uri.fromFile(image));

        intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
        return intent;
    }

    /**
     * Call standard camera application for capturing an image
     *
     * @param file Full path to captured file
     */
    public static Intent photoCapture(String file) {
        Uri uri = Uri.fromFile(new File(file));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    /**
     * Check that in the system exists application which can handle this intent
     *
     * @param context Application context
     * @param intent  Checked intent
     * @return true if intent consumer exists, false otherwise
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private static Intent openMedia(Uri uri, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mimeType);
        return intent;
    }

    private static boolean isSupportsContactsV2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
    }

    public static void startNFCSettingActivity(Context context) {
        if (Build.VERSION.SDK_INT >= 16) {
            context.startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        } else {
            context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        }
    }



    public static void getImageFromCamera(Activity context) {
        getImageFromCamera(context, null);
    }

    public static void getImageFromCamera(Activity context, String path) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            if (path != null && !path.equals("")) {
                getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(path)));
            }
            context.startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMERA);
        } else {
            Toast.makeText(context, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }


    public static void getImageFromAlbum(Activity context) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");// 相片类型
        context.startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }


    public static Intent buildImageGetIntent(Uri saveTo, int outputX, int outputY, boolean returnData) {
        return buildImageGetIntent(saveTo, 1, 1, outputX, outputY, returnData);
    }


    public static Intent buildImageGetIntent(Uri saveTo, int aspectX, int aspectY, int outputX, int outputY,
                                             boolean returnData) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        intent.putExtra("output", saveTo);
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", returnData);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        return intent;
    }

    public static Intent buildImageCropIntent(Uri uriFrom, Uri uriTo, int outputX, int outputY, boolean returnData) {
        return buildImageCropIntent(uriFrom, uriTo, 1, 1, outputX, outputY, returnData);
    }

    public static Intent buildImageCropIntent(Uri uriFrom, Uri uriTo, int aspectX, int aspectY, int outputX,
                                              int outputY, boolean returnData) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uriFrom, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("output", uriTo);
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", returnData);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        return intent;
    }

    public static Intent buildImageCaptureIntent(Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }



    /** call phone
     * @param context
     * @param phone
     */
    @SuppressWarnings("MissingPermission")
    public static void callPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + phone));
        context.startActivity(intent);

    }

    /** open html
     * @param file
     * @return
     */
    public static Intent getHtmlFileIntent(File file) {
        Uri uri = Uri.parse(file.toString()).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(file.toString()).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    /** open image
     * @param file
     * @return
     */
    //android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**open pdf
     * @param file
     * @return
     */
    public static Intent getPdfFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    /** open text
     * @param file
     * @return
     */
    public static Intent getTextFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "text/plain");
        return intent;
    }

    /** open audio
     * @param file
     * @return
     */
    //android获取一个用于打开音频文件的intent
    public static Intent getAudioFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    /** open video
     * @param file
     * @return
     */
    public static Intent getVideoFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "video/*");
        return intent;
    }


    /** open chm
     * @param file
     * @return
     */
    public static Intent getChmFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }


    /** open word
     * @param file
     * @return
     */
    public static Intent getWordFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    /** open excel
     * @param file
     * @return
     */
    //android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }
    /** open ppt
     * @param file
     * @return
     */
    public static Intent getPPTFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    /** open apk install
     * @param file
     * @return
     */
    public static Intent getApkFileIntent(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        return intent;
    }

    /**  Check the suffix type
     * @param checkItsEnd
     * @param fileEndings
     * @return
     */
    private static boolean checkEndsWithInStringArray(String checkItsEnd,
                                                      String[] fileEndings) {
        for (String aEnd : fileEndings) {
            if (checkItsEnd.endsWith(aEnd))
                return true;
        }
        return false;
    }

    /** search can be open app
     * @param context
     * @param currentPath
     * @return
     */
    public static boolean openFile(Context context, File currentPath) {
        if (currentPath != null && currentPath.isFile()) {
            String fileName = currentPath.toString();
            Intent intent;
            if (checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingImage))) {
                intent = getImageFileIntent(currentPath);
                context.startActivity(intent);
            } else if (checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingWebText))) {
                intent = getHtmlFileIntent(currentPath);
                context.startActivity(intent);
            } else if (checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingPackage))) {
                intent = getApkFileIntent(currentPath);
                context.startActivity(intent);

            } else if (checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingAudio))) {
                intent = getAudioFileIntent(currentPath);
                context.startActivity(intent);
            } else if (checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingVideo))) {
                intent = getVideoFileIntent(currentPath);
                context.startActivity(intent);
            } else if (checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingText))) {
                intent = getTextFileIntent(currentPath);
                context.startActivity(intent);
            } else if (checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingPdf))) {
                intent = getPdfFileIntent(currentPath);
                context.startActivity(intent);
            } else if (checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingWord))) {
                intent = getWordFileIntent(currentPath);
                context.startActivity(intent);
            } else if (checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingExcel))) {
                intent = getExcelFileIntent(currentPath);
                context.startActivity(intent);
            } else if (checkEndsWithInStringArray(fileName, context.getResources().
                    getStringArray(R.array.fileEndingPPT))) {
                intent = getPPTFileIntent(currentPath);
                context.startActivity(intent);
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

}
