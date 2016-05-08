package android.lemon.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.io.File;

public class IntentUtils {

	public final static int REQUEST_CODE_CAPTURE_CAMERA = 3001;
	public final static int REQUEST_CODE_PICK_IMAGE = 3002;

	/**
	 * 网络设置
	 * 
	 * @param context
	 */
	public static void startNetworkSetting(Context context) {
		Intent intent = new Intent();
		int sdkVersion = VERSION.SDK_INT;
		if (sdkVersion >= 10) {
			intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
		} else {
			intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
		}
		context.startActivity(intent);
	}

	public static void startNFCSettingActivity(Context context) {
		if (Build.VERSION.SDK_INT >= 16) {
			context.startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
		} else {
			context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
		}
	}

	public static void goToWeb(Context context, String url) {
		Uri uri = Uri.parse(url);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
		context.startActivity(it);
	}

	public static void goToMap(Context context, String latitude, String longitude) {
		Uri uri = Uri.parse("geo:" + latitude + "," + longitude);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		context.startActivity(it);

	}

	public static void goToCall(Context context, String phone) {
		Uri uri = Uri.parse("tel:" + phone);
		Intent it = new Intent(Intent.ACTION_DIAL, uri);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		context.startActivity(it);
	}

	/**
	 * @param context
	 * @param email
	 */
	public static void goToEmail(Context context, String email) {
		Uri uri = Uri.parse("mailto:" + email);

		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		context.startActivity(it);

	}

	public static void getImageFromCamera(Activity context) {
		getImageFromCamera(context,null);
	}

	public static void getImageFromCamera(Activity context,String path) {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
			if(path==null||path.equals("")) {
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

	/**
	 * 调用系统分享
	 */
	public static void shareToOtherApp(Context context, String title, String content, String dialogTitle) {
		Intent intentItem = new Intent(Intent.ACTION_SEND);
		intentItem.setType("text/plain");
		intentItem.putExtra(Intent.EXTRA_SUBJECT, title);
		intentItem.putExtra(Intent.EXTRA_TEXT, content);
		intentItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intentItem, dialogTitle));
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
	
	/** 短信窗口中 发送短信
	 * @param context
	 * @param phone
	 * @param msg
	 */
	public static void sendIntentSMS(Context context, String phone, String msg) {
		Uri uri = Uri.parse("smsto://" + phone);
		Intent in = new Intent(Intent.ACTION_SENDTO, uri);
		in.putExtra("sms_body", msg);
		context.startActivity(in);
	}

	/** 静默发送短信
	 * @param phone
	 * @param msg
	 */
	public static void sendSMS(String phone, String msg) {
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phone, null, msg, null, null);
	}
	
	public static void callPhone(Context context, String phone) {
		Intent intent = new Intent(Intent.ACTION_CALL,
				Uri.parse("tel:" + phone));
		context.startActivity(intent);

	}

}
