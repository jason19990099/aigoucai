package com.example.agc.aigoucai.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.bean.base;
import com.example.agc.aigoucai.util.Apputil;
import com.example.agc.aigoucai.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
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


        getChatdata();
    }

    private void getChatdata() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //我要获取当前的日期
                Date date = new Date();
                //设置要获取到什么样的时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                //获取String类型的时间
                String createdate = sdf.format(date);
                String url="https://appv1.whsurpass.com/appinfo/"+ base.appid+".contact?date="+createdate;
                LogUtil.e("=====url123========"+url);
                OkHttpClient mOkHttpClient=new OkHttpClient();
                Request.Builder requestBuilder = new Request.Builder().url(url);
                //可以省略，默认是GET请求
                requestBuilder.method("GET",null);
                Request request = requestBuilder.build();
                Call mcall= mOkHttpClient.newCall(request);
                mcall.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                           LogUtil.e("=====fail=============="+e.toString());
                    }

                    @Override
                    public void onResponse(Call call,final Response response) throws IOException {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (null != response.body().toString()) {
                                    String str = response.body().toString();
                                    Log.e("InfoMSG", "cache---" + str);



                                }
                            }
                        });
                    }
                });
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
}
