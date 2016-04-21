package android.lemon.utils;

import android.content.Context;
import android.lemon.bean.DHCPInfo;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * Created by Kevin on 2016/2/27.
 */
public class NetworkUtils {


    public static final String WIFI = "WIFI";

    public static final String X4G = "4G";

    public static final String X3G = "3G";

    public static final String X2G = "2G";


    /**
     * 得到网络类型
     *
     * @param context
     * @return
     */
    public static String getNetworkType(Context context) {
        String strNetworkType = "";

        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                Log.e("Network getSubtypeName : " + _strSubTypeName);

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = X2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = X3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = X4G;
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = X3G;
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

                Log.e("Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }

        Log.e("Network Type : " + strNetworkType);

        return strNetworkType;
    }


    /**
     * int 转 String 类型
     *
     * @param ip
     * @return
     */
    public static String int2Ip(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 24) & 0xFF);
    }


    /**
     * 获取 MAC 地址
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     */
    public static String getMacAddress(Context context) {
        //wifi mac地址
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        if (Log.isPrint) {
            Log.i(" MAC：" + mac);
        }
        return mac;
    }


    /**
     *
     */
    public static DhcpInfo getWIFIDHCPInfo(Context context) {
        WifiManager wifiManager = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE));
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        return dhcpInfo;
    }


    /**
     * 描述：判断网络是否有效.
     *
     * @param context the context
     * @return true, if is network available
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    /** 输入int值ip地址 和 子网掩码 得到广播地址 int值
     * @param ipAddress
     * @param mask
     * @return
     */
    public static int getBroadcastAddress(int ipAddress, int mask) {
        mask = mask ^ 0xffffffff;
        return ipAddress | mask;
    }

}
