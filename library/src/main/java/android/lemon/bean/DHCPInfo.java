package android.lemon.bean;

import android.annotation.SuppressLint;
import android.net.DhcpInfo;

/**
 * Created by Kevin on 2016/2/27.
 */
@SuppressLint("ParcelCreator")
public class DHCPInfo extends DhcpInfo  {

    public String ipAddress;

    public String gateway;

    public String netmask;

    public String dns1;

    public String dns2;

    public String serverAddress;

    public int leaseDuration;



}
