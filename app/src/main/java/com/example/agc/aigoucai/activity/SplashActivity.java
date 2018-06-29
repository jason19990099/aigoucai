package com.example.agc.aigoucai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;


import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.bean.DataInfo;
import com.example.agc.aigoucai.util.LogUtil;
import com.example.agc.aigoucai.util.SocketUtil;
import com.example.agc.aigoucai.util.SystemUtil;
import com.example.agc.aigoucai.util.TrustAllCerts;
import com.google.gson.Gson;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SystemUtil.setfullScreen(SplashActivity.this);


        SocketUtil.getSocketConiction();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, SelectLinesActivity.class));
                finish();
            }
        }, 1500);


    }


}
