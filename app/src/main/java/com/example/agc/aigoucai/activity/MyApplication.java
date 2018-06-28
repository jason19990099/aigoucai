package com.example.agc.aigoucai.activity;

import android.app.Application;

import com.xuhao.android.libsocket.sdk.OkSocket;


import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        OkSocket.initialize(this);
        //如果需要开启Socket调试日志,请配置
        OkSocket.initialize(this, true);

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
