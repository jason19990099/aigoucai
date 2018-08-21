package com.example.agc.aigoucai.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.adapter.ChatAdapter;
import com.example.agc.aigoucai.bean.ChatBean;
import com.example.agc.aigoucai.bean.base;
import com.example.agc.aigoucai.util.Apputil;
import com.example.agc.aigoucai.util.LogUtil;
import com.example.agc.aigoucai.util.TrustAllCerts;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
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

public class SelectServiceActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.listvie_id)
    ListView listvieId;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.tv_vertion)
    TextView tvVertion;
    private Gson gson = new Gson();
    private ChatBean chatBean;
    private ChatAdapter chatAdapter;
    private  ListView listvie_id ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectservice);
        ButterKnife.bind(this);
        tvVertion.setText("版本号:" + Apputil.getVersion(SelectServiceActivity.this));
        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        listvie_id=findViewById(R.id.listvie_id);

        getChatdata();
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
                 base.appid="test";
                String url="https://appv1.whsurpass.com/appinfo/contact/"+ base.appid+"?date="+createdate;
                    Request request = new Request.Builder()
                            .url(url)//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response = null;
                    response = client.newCall(request).execute();//得到Response 对象
                    final String s = response.body().string();

                    if (response.isSuccessful()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<ChatBean> userList = gson.fromJson(s, new TypeToken<List<ChatBean>>(){}.getType());
                                if (userList.size()>0)
                                    chatAdapter=new ChatAdapter(SelectServiceActivity.this,userList);
                                listvie_id.setAdapter(chatAdapter);
                                chatAdapter.notifyDataSetChanged();
                                //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                            }
                        });


                    } else {
                        Toast.makeText(getApplicationContext(),"请求失败，请下来刷新。",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }).start();
    }


    @Override
    public void onRefresh() {
        getChatdata();
        swipeContainer.setRefreshing(false);
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
}
