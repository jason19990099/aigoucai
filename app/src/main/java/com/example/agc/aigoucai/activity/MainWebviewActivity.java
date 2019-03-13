package com.example.agc.aigoucai.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.bean.Basedata;
import com.example.agc.aigoucai.util.LogUtil;
import com.example.agc.aigoucai.util.ShareUtils;
import com.example.agc.aigoucai.util.SimpleProgressDialog;
import com.example.agc.aigoucai.util.UrlUtil;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class MainWebviewActivity extends AppCompatActivity {
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.ll_refresh)
    LinearLayout llRefresh;
    @BindView(R.id.ll_xianlu)
    LinearLayout llXianlu;
    @BindView(R.id.ll_fenxiang)
    LinearLayout llFenxiang;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.web_layout)
    LinearLayout webLayout;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_title)
    RelativeLayout llTitle;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.line_bottom)
    View lineBottom;
    private String mUrl;
    private LinearLayout mLayout;
    private WebView mWebView;
//    private Dialog dialog;
    private View[] mviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        //解決挼鍵盤把輸入框遮擋的問題
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_web);

        ButterKnife.bind(this);

        mviews = new View[]{llHome, llRefresh, llXianlu, llFenxiang};
        changeSelectState(0);

//        dialog = new SimpleProgressDialog(MainWebviewActivity.this, "请稍等...");
        Bundle bundle = this.getIntent().getExtras();
//        if (null != bundle)
//            mUrl = bundle.getString("url");
        mUrl="http://15930.vip/";

        mLayout = findViewById(R.id.web_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView = new WebView(this);
        mWebView.setLayoutParams(params);
        mLayout.addView(mWebView);


        initWebSetting(mUrl);
    }


    /**
     * 切換底部按鈕顏色
     */
    private void changeSelectState(int index) {
        for (int i = 0; i < mviews.length; i++) {
            mviews[i].setSelected(index == i);
        }
    }



    /**
     * 初始化webview
     */
    private void initWebSetting(String url) {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSavePassword(true);
        settings.setSaveFormData(true);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setBlockNetworkImage(false);// 解决图片不显示
        settings.setAllowContentAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mWebView.requestFocus();
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setWebChromeClient(new AppCacheWebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                /***************************************判断是否被劫持******************************************************************/
                /********************************调起支付宝支付或者QQ第三方支付*************************************************************************/
                try {
                    if (url.startsWith("mqqapi://")
                            || url.contains("alipays://platformapi")//支付宝支付
                            || url.startsWith("https://messenger")  //聊天页
                            || url.startsWith("https://www.agcapp.me/app.apk")
                            || url.startsWith("upwrp://")   //银联云闪付
                            || url.startsWith("weixin://wap/pay")   //微信支付
                            ) {

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        //是聊天页就关闭  不然白屏
                        if (url.startsWith("https://messenger")) {
                            finish();
                        }
                    } else {
                        if (url.startsWith("intent://platformapi")) {
                            Intent intent;
                            try {
                                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                                intent.addCategory("android.intent.category.BROWSABLE");
                                intent.setComponent(null);
                                intent.setSelector(null);
                                startActivity(intent);
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                            return true;
                        }else{
                            view.loadUrl(url);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

//                view.loadUrl(url);
//                return true;
                return super.shouldOverrideUrlLoading(view, url);//设置不重新加载 依旧加载原来链接 （在个别手机上重新 view.loadUrl(url) 返回按钮失效）
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                dialog.show();
            }

            @Override
            public void onLoadResource(WebView view, String url) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                if (dialog != null && dialog.isShowing())
//                    dialog.dismiss();
                mWebView.setVisibility(View.VISIBLE);

            }


            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });


        mWebView.loadUrl(url);
    }



    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调</span>
    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息

    @OnClick({R.id.ll_home, R.id.ll_refresh, R.id.ll_xianlu, R.id.ll_fenxiang, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                changeSelectState(0);
                initWebSetting(mUrl);
                break;
            case R.id.ll_refresh:
                changeSelectState(1);
                mWebView.reload();  //刷新
                break;
            case R.id.ll_xianlu:
                changeSelectState(2);
                finish();
                break;
            case R.id.ll_fenxiang:
                changeSelectState(3);
                ShareUtils.shareText(MainWebviewActivity.this, "", "彩票分享", Basedata.share_url);
                break;
            case R.id.iv_back:

                break;

        }
    }


    private class AppCacheWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            mUploadCallbackAboveL = filePathCallback;
            take();
            return true;
        }

    }

    private Uri imageUri;

    private void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);

        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {

                if (result != null) {
                    String path = getPath(getApplicationContext(), result);
                    Uri uri = Uri.fromFile(new File(path));
                    mUploadMessage.onReceiveValue(uri);
                } else {
                    mUploadMessage.onReceiveValue(imageUri);
                }
                mUploadMessage = null;

            }
        }
    }


    @SuppressWarnings("null")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private long mOldTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mOldTime < 1500) {
                mWebView.clearHistory();
                mWebView.loadUrl(mUrl);
            } else if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
            mOldTime = System.currentTimeMillis();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
//            ClearCookie();
//            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }

    public void ClearCookie() {
        CookieSyncManager.createInstance(this);  //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now

        mWebView.setWebChromeClient(null);
        mWebView.setWebViewClient(null);
        mWebView.getSettings().setJavaScriptEnabled(false);
        mWebView.clearCache(true);
    }




    public static void syncCookie(Context context,String url,String CookieStr) {
       CookieSyncManager.createInstance(context);
       CookieManager cookieManager=CookieManager.getInstance();
       cookieManager.setAcceptCookie(true);
       cookieManager.removeSessionCookie();//移除  
        String[]  strs=CookieStr.split(";");
        for(int i=0,len=strs.length;i<len;i++){
            //游客是，0結尾的，游客的状态不保存。
            if (!UrlUtil.getURLDecoderString(strs[i]).contains(",0")){
                cookieManager.setCookie(url,strs[i]);
                LogUtil.e("=====CookieStr====strs[i].toString()======"+strs[i]);
            }
        }
       CookieSyncManager.getInstance().sync();
    }

}
