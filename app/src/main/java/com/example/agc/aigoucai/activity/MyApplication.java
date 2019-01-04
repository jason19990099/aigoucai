package com.example.agc.aigoucai.activity;

import android.app.Application;
import com.example.agc.aigoucai.util.Apputil;
import com.xuhao.android.libsocket.sdk.OkSocket;

//import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //去掉证书验证的问题/
        Apputil.handleSSLHandshake();

//        OkSocket.initialize(this);
//        如果需要开启Socket调试日志,请配置
        OkSocket.initialize(this, true);

//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);
    }


}
