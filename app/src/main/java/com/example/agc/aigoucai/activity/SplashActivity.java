package com.example.agc.aigoucai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.util.SocketUtil;
import com.example.agc.aigoucai.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.agc.aigoucai.util.SocketUtil.ip_bei;


/**
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SystemUtil.setfullScreen(SplashActivity.this);


        List<String> ip_array = new ArrayList<>();
        ip_array.add("39.106.217.117");
        ip_array.add("222.186.42.23");
        ip_array.add("103.17.116.117");
        SocketUtil socketUtil=new SocketUtil(ip_array);

        socketUtil.getSocketConiction(ip_bei,1985);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, SelectLinesActivity.class));
                finish();
            }
        }, 1500);

    }

}
