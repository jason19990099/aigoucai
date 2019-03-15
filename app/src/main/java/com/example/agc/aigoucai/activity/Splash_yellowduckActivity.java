package com.example.agc.aigoucai.activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;
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
public class Splash_yellowduckActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SystemUtil.setfullScreen(Splash_yellowduckActivity.this);

        Observable<List<String>> oble = Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
                List list=new ArrayList();
                list.add("com.aigoucai.lottery.makesure.AliasActivity");
                list.add("com.aigoucai.lottery.makesure.AliasActivity2");
                e.onNext(list);
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

                changIconUtils.addmore(Splash_yellowduckActivity.this,s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };

        oble.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(oser);




    }


    @Override
    protected void onResume() {
        super.onResume();


        ComponentName componentName = Splash_yellowduckActivity.this.getComponentName();
        PackageManager pm = getPackageManager();
        ActivityInfo activityInfo = null;
        try {
            activityInfo = pm.getActivityInfo (componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Log.d ("ActivityLabel=====", activityInfo.loadLabel (pm).toString ());
        Log.d("getIntent()======",getIntent().getComponent().getClassName());
        if (getIntent().getComponent().getClassName().contains("AliasActivity")) {
            // from AliasActivity
            startActivity(new Intent(this,SplashActivity.class));
            finish();
        } else {
            Intent intent=new Intent(Splash_yellowduckActivity.this,MainWebviewActivity.class);
            startActivity(intent);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

}
