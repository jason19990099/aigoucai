package com.example.agc.aigoucai.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2018/3/28 0028.
 */

public class Http {
    public SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//设置日期格式
    String time_string = "";
    //请求网址响应
    /**
     * 判断是否有网络连接
     * @return
     * @version 1.0
     * @updateInfo
     */
    public static boolean isNetConnection(Context context) {
        // 获取网络服务
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取所有可用连接
            @SuppressLint("MissingPermission") NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo info : infos) {
                    // 如果有连接
                    if (info.getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
