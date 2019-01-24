package com.example.agc.aigoucai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.util.Apputil;
import com.example.agc.aigoucai.util.LogUtil;
import com.example.agc.aigoucai.util.SocketUtil;
import com.example.agc.aigoucai.util.SystemUtil;
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


         new Thread(new Runnable() {
             @Override
             public void run() {
                 String[] strings= Apputil.parseHostGetIPAddress("bobo.shyqyl.com");
                 if (null==strings){
                     return;
                 }
                 for (int i=0;i<strings.length;i++){
                     LogUtil.e("========strings[i]===="+strings[i]);
                     ip_array.add(strings[i]);
                 }
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         //ip和端口号传进去
                      SocketUtil socketUtil=new SocketUtil(ip_array,1985,SplashActivity.this);
                     //调取方法开始连接
                      socketUtil.getSocketConection();
                     }
                 });


             }
         }).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, SelectLinesActivity.class));
                finish();
            }
        }, 1500);
    }
}
