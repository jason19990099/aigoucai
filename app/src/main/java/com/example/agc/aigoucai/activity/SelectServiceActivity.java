package com.example.agc.aigoucai.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.adapter.ChatAdapter;
import com.example.agc.aigoucai.bean.Basedata;
import com.example.agc.aigoucai.bean.ChatBean;
import com.example.agc.aigoucai.util.Apputil;
import com.example.agc.aigoucai.util.LogUtil;
import com.example.agc.aigoucai.util.ShareUtil;
import com.example.agc.aigoucai.util.TrustAllCerts;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

public class SelectServiceActivity extends Activity {
    @BindView(R.id.listvie_id)
    ListView listvieId;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.tv_vertion)
    TextView tvVertion;
    private Gson gson = new Gson();
    private ChatAdapter chatAdapter;
    private ListView listvie_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectservice);
        ButterKnife.bind(this);
        tvVertion.setText("版本号:" + Apputil.getVersion(SelectServiceActivity.this));
        listvie_id = findViewById(R.id.listvie_id);
        List<ChatBean> userList2 = ShareUtil.getUser(SelectServiceActivity.this, "duixiang", "userList");
        if (null != userList2 && userList2.size() > 0) {
            chatAdapter = new ChatAdapter(SelectServiceActivity.this, userList2);
            listvie_id.setAdapter(chatAdapter);
            chatAdapter.notifyDataSetChanged();
        }
        if (Basedata.ifgetService) {
            getChatdata();
        }

    }

    private void getChatdata() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Date date = new Date();
                    //设置要获取到什么样的时间
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    //获取String类型的时间
                    String createdate = sdf.format(date);
                    OkHttpClient client = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(9, TimeUnit.SECONDS)
                            .writeTimeout(9, TimeUnit.SECONDS)
                            .sslSocketFactory(createSSLSocketFactory())
                            .hostnameVerifier(new HostnameVerifier() {
                                @Override
                                public boolean verify(String hostname, SSLSession session) {
                                    return true;
                                }
                            })
                            .build();
                    String url = "https://appv1.whsurpass.com/appinfo/contact/" + Basedata.appid + "?date=" + createdate;
                    Request request = new Request.Builder()
                            .url(url)//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response = null;
                    response = client.newCall(request).execute();//得到Response 对象
                    final String s = response.body().string();

                    if (response.isSuccessful()) {
                        LogUtil.e("=========getChatdata==========" + s);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<ChatBean> userList = gson.fromJson(s, new TypeToken<List<ChatBean>>() {}.getType());

                                try {
                                    ShareUtil.saveUser(SelectServiceActivity.this, "duixiang", "userList", (ArrayList<ChatBean>) userList);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (userList.size() > 0) {
                                    chatAdapter = new ChatAdapter(SelectServiceActivity.this, userList);
                                    listvie_id.setAdapter(chatAdapter);
                                    chatAdapter.notifyDataSetChanged();
                                }

                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }).start();

        //设为false，不让重复请求/
        Basedata.ifgetService = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
