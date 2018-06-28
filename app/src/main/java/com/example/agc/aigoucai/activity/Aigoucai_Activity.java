package com.example.agc.aigoucai.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.agc.aigoucai.R;
import com.just.library.AgentWeb;

/**
 * Created by Administrator on 2017/9/8 0008.
 */

public class Aigoucai_Activity extends Activity implements View.OnClickListener {
    private AgentWeb mAgentWeb;
    private LinearLayout tab;
    private TextView home_tv;
    private TextView resh_tv;
    private TextView back_tv, back_chat_tv;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;
    //获取信息
    WebView mWebView;
    String url = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = this.getIntent().getExtras();
        url = bundle.getString("url");
        LinearLayout x = (LinearLayout) findViewById(R.id.xianshi);
        back_chat_tv = (TextView) findViewById(R.id.back_chat_tv);
        home_tv = (TextView) findViewById(R.id.home_tv);
        resh_tv = (TextView) findViewById(R.id.resh_tv);
        back_tv = (TextView) findViewById(R.id.back_tv);
        tab = (LinearLayout) findViewById(R.id.tab);
        mWebView = (WebView) findViewById(R.id.home_ad_webview);
        back_chat_tv.setVisibility(View.VISIBLE);
        home_tv.setOnClickListener(this);
        back_chat_tv.setOnClickListener(this);
        resh_tv.setOnClickListener(this);
        back_tv.setOnClickListener(this);
        mAgentWeb = AgentWeb.with(Aigoucai_Activity.this)//传入Activity
                .setAgentWebParent(tab, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
                .createAgentWeb()//
                .ready()
                .go(url);
        mWebView = mAgentWeb.getWebCreator().get();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                Aigoucai_Activity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg);
            }
            // For Lollipop 5.0+ Devices
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }
                uploadMessage = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    return false;
                }
                return true;
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (errorCode==(-10)) {
                    view.removeAllViews();
                    view.clearHistory(); // 清除
                    view.goBack();
                    mWebView.loadUrl(url); // 你要回到的那个首页的URL
                }
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                loadBuilder = new LoadingDailog.Builder(Aigoucai_Activity.this)
//                        .setMessage("加载中...")
//                        .setCancelable(true)
//                        .setCancelOutside(true);
//                dialog = loadBuilder.create();
//                dialog.show();
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                dialog.cancel();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.startsWith("http") || url.startsWith("https")) { //http和https协议开头的执行正常的流程
                    return super.shouldInterceptRequest(view, url);
                } else {
                    try{
                        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(in);
                    }catch (Exception e){
                    }
                    return null;
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != SelectLinesActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                mWebView.goBack();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_tv:
                mWebView.clearHistory(); // 清除 // 你要回到的那个首页的URL
                mWebView.loadUrl(url); // 你要回到的那个首页的URL
                break;
            case R.id.back_chat_tv:
                finish();
                break;
            case R.id.resh_tv:
                mWebView.reload();  //刷新
                break;
            case R.id.back_tv:
                mWebView.goBack();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.stopLoading();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;
    }
}
