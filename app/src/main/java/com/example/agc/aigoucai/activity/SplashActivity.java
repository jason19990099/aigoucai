package com.example.agc.aigoucai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.util.SocketUtil;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        List<String> ip_array = new ArrayList<>();
        ip_array.clear();
        ip_array.add("39.106.217.117");
        ip_array.add("222.186.42.23");
        ip_array.add("103.17.116.117");
        //ip和端口号传进去
        SocketUtil socketUtil=new SocketUtil(ip_array,1985);
        //调取方法开始连接
        socketUtil.getSocketConection();


    new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,SelecLinesActivity.class));
                finish();
            }
        }, 1000);


    }
}
