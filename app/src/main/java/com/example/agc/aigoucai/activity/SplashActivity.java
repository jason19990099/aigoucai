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
 * 启动页  查看开关是否开启
 */
public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.iv_welcome)
    ImageView ivWelcome;
    @BindView(R.id.iv_weihu)
    ImageView ivWeihu;
    private DataInfo dataInfo;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        SystemUtil.setfullScreen(SplashActivity.this);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient client = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(5, TimeUnit.SECONDS)
                            .writeTimeout(5, TimeUnit.SECONDS)
                            .sslSocketFactory(createSSLSocketFactory())
                            .hostnameVerifier(new HostnameVerifier() {
                                @Override
                                public boolean verify(String hostname, SSLSession session) {
                                    return true;
                                }
                            })
                            .build();
                    Request request = new Request.Builder()
                            .url("https://hk1.android.jrapp.me/switch/35")//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response = null;
                    response = client.newCall(request).execute();//得到Response 对象
                    if (response.isSuccessful()) {
                        String s = response.body().string();
                        LogUtil.e("================="+s);
                        //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                        dataInfo = gson.fromJson(s, DataInfo.class);
                        if (dataInfo.getData().getApp_status().equals("1")) {
                            ivWelcome.setVisibility(View.VISIBLE);
                            ivWeihu.setVisibility(View.GONE);
                            Looper.prepare();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(SplashActivity.this, SelectLinesActivity.class));
                                    finish();
                                }
                            }, 1000);

                            Looper.loop();

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 提示維護中
                                    ivWelcome.setVisibility(View.GONE);
                                    ivWeihu.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 提示維護中
                                ivWelcome.setVisibility(View.GONE);
                                ivWeihu.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e("=============" + e);
                }
            }
        }).start();


    }


    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ssfFactory;
    }
}
