package com.example.agc.aigoucai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.bean.Basedata;
import com.example.agc.aigoucai.util.Apputil;
import com.example.agc.aigoucai.util.SharePreferencesUtil;
import com.example.agc.aigoucai.util.SocketUtil;
import com.example.agc.aigoucai.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.iv_welcome)
    ImageView ivWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        SystemUtil.setfullScreen(SplashActivity.this);
        String getintent = SharePreferencesUtil.getString(SplashActivity.this, "getIntent", "");

        if (getintent.contains("com.500CPActivity")) {
            ivWelcome.setImageDrawable(getResources().getDrawable(R.mipmap.welcome_500));
            Basedata.share_url = "https://www.500app.me/app.html";
        }
        if (getintent.contains("com.AigoucaiActivity")) {
            ivWelcome.setImageDrawable(getResources().getDrawable(R.mipmap.welcome_agc));
            Basedata.share_url = "https://www.agcapp.me/app.html";
        }
        if (getintent.contains("com.k7Activity")) {
            ivWelcome.setImageDrawable(getResources().getDrawable(R.mipmap.welcome_k7));
            Basedata.share_url = "https://www.k7app.me/app.html";
        }
        if (getintent.contains("com.ttActivity")) {
            ivWelcome.setImageDrawable(getResources().getDrawable(R.mipmap.welcome_tt));
            Basedata.share_url = "https://www.ttapp.me/app.html";
        }
        if (getintent.contains("com.678yuleActivity")) {
            ivWelcome.setImageDrawable(getResources().getDrawable(R.mipmap.welcome_678));
            Basedata.share_url = "https://www.appkings.me/678.html";
        }
        if (getintent.contains("com.xpjActivity")) {
            ivWelcome.setImageDrawable(getResources().getDrawable(R.mipmap.welcome_xpj));
            Basedata.share_url = "https://www.appkings.me/xpj.html";
        }
        if (getintent.contains("com.zzcActivity")) {
            ivWelcome.setImageDrawable(getResources().getDrawable(R.mipmap.welcome_zzc));
            Basedata.share_url = "https://www.appkings.me/zzc.html";
        }
        if (getintent.contains("com.egoActivity")) {
            ivWelcome.setImageDrawable(getResources().getDrawable(R.mipmap.welcome_egou));
            Basedata.share_url = "https://www.appkings.me/eg.html";
        }
        if (getintent.contains("com.zxcActivity")) {
            ivWelcome.setImageDrawable(getResources().getDrawable(R.mipmap.welcome_zxc));
            Basedata.share_url = "https://www.appkings.me/zxc.html";
        }
        if (getintent.contains("com.8HaoActivity")) {
            ivWelcome.setImageDrawable(getResources().getDrawable(R.mipmap.welcome_8hao));
            Basedata.share_url = "https://www.appkings.me/8hao.html";
        }
        if (getintent.contains("com.pandaActivity")) {
            ivWelcome.setImageDrawable(getResources().getDrawable(R.mipmap.welcome_panda));
            Basedata.share_url = "https://www.agcapp.me/xm.html";
        }
        Observable<List<String>> oble = Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {

                List<String> ip_array = new ArrayList<>();
                ip_array.clear();
                String[] strings = Apputil.parseHostGetIPAddress("bobo.shyqyl.com");
                if (null == strings) {
                    return;
                }
                int size = strings.length;
                for (int i = 0; i < size; i++) {
                    ip_array.add(strings[i]);
                }
                e.onNext(ip_array);
                SystemClock.sleep(1500);
                e.onComplete();

            }
        });

        Observer<List<String>> oser = new Observer<List<String>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull List<String> s) {
                SocketUtil socketUtil = new SocketUtil(s, 1985, SplashActivity.this);
                //调取方法开始连接
                socketUtil.getSocketConection();
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }

            @Override
            public void onComplete() {
                startActivity(new Intent(SplashActivity.this, SelectLinesActivity.class));
                finish();
            }
        };

        oble.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(oser);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
