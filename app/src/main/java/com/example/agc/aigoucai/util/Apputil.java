package com.example.agc.aigoucai.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.agc.aigoucai.util.LogUtil;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

import static android.media.audiofx.AcousticEchoCanceler.isAvailable;

public class Apputil {


    /**
     * 判断是否在使用VPN
     * @return
     */
    public static boolean isVpnUsed() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if(niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    LogUtil.e("isVpnUsed() NetworkInterface Name: " + intf.getName());
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())){
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }



     public static String   netState(Context context){

         //获取网络连接管理者
         ConnectivityManager connectionManager = (ConnectivityManager)
                 context.getSystemService(Context.CONNECTIVITY_SERVICE);
         //获取网络的状态信息，有下面三种方式
         NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
//         getDetailedState();//获取详细状态。
//         getExtraInfo();//获取附加信息。
//         getReason();//获取连接失败的原因。
//         getType();//获取网络类型(一般为移动或Wi-Fi)。
        return networkInfo.getTypeName();//获取网络类型名称(一般取值“WIFI”或“MOBILE”)。
//
//         isAvailable();//判断该网络是否可用。
//         isConnected();//判断是否已经连接。
//         isConnectedOrConnecting();  //判断是否已经连接或正在连接。
//         isFailover();//判断是否连接失败。
//         isRoaming();   //判断是否漫游

     }


    public static String getOperator(Context context) {
        String ProvidersName = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI = telephonyManager.getSubscriberId();
        Log.e("qweqwes", "运营商代码" + IMSI);
        if (IMSI != null) {
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                ProvidersName = "中国移动";
            } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信";
            }
            return ProvidersName;
        } else {
            return "没有获取到sim卡信息";
        }
    }
}
