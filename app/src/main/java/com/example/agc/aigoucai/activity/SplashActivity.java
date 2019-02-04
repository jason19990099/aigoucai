package com.example.agc.aigoucai.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.util.Apputil;
import com.example.agc.aigoucai.util.SocketUtil;
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
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SystemUtil.setfullScreen(SplashActivity.this);

        Observable<List<String>> oble = Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {

                List<String> ip_array = new ArrayList<>();
                ip_array.clear();
                String[] strings= Apputil.parseHostGetIPAddress("bobo.shyqyl.com");
                if (null==strings){
                    return;
                }
                int size=strings.length;
                for (int i=0;i<size;i++){
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
                SocketUtil socketUtil=new SocketUtil( s,1985,SplashActivity.this);
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
}
