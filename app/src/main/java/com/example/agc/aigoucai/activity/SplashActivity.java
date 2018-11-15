package com.example.agc.aigoucai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.util.LogUtil;
import com.example.agc.aigoucai.util.SocketUtil;
import com.example.agc.aigoucai.util.SystemUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;



/**
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SystemUtil.setfullScreen(SplashActivity.this);

        final List<String> ip_array = new ArrayList<>();
        ip_array.clear();
        ip_array.add("112.74.175.185");
        ip_array.add("39.106.148.87");
        //ip和端口号传进去
        SocketUtil socketUtil=new SocketUtil(ip_array,1985,SplashActivity.this);
        //调取方法开始连接
        socketUtil.getSocketConection();


//         new Thread(new Runnable() {
//             @Override
//             public void run() {
//                 String[] strings=parseHostGetIPAddress("117.shyqyl.com");
//                 LogUtil.e("====strings====="+strings[0] );
//                 ip_array.add(strings[0]);
//                 runOnUiThread(new Runnable() {
//                     @Override
//                     public void run() {
//                         //ip和端口号传进去
//        SocketUtil socketUtil=new SocketUtil(ip_array,1985,SplashActivity.this);
//        //调取方法开始连接
//        socketUtil.getSocketConection();
//                     }
//                 });
//             }
//         }).start();



        LogUtil.e("======applicationid======="+getApplication().getPackageName());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, SelectLinesActivity.class));
                finish();
            }
        }, 1500);

    }





    /**
     * 解析域名获取IP数组
     * @param host
     * @return
     */
    public String[] parseHostGetIPAddress(String host) {
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
            LogUtil.e("========123======="+e.toString());
            e.printStackTrace();
            return null;
        }
        return ipAddressArr;
    }

}
