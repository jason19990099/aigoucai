package com.example.agc.aigoucai.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ParseHostGetIPAddress {
    /**
     * 解析域名获取IP数组
     * @param host
     * @return
     */
    public static String[] parseHostGetIPAddress(String host) {
        String[] ipAddressArr = null;
        try {
            InetAddress[] inetAddressArr = InetAddress.getAllByName(host);
            if (inetAddressArr != null && inetAddressArr.length > 0) {
                ipAddressArr = new String[inetAddressArr.length];
                for (int i = 0; i < inetAddressArr.length; i++) {
                    ipAddressArr[i] = inetAddressArr[i].getHostAddress();
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
        return ipAddressArr;
    }
}
