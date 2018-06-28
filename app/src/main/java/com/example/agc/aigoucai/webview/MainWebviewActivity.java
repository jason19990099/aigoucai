package com.example.agc.aigoucai.webview;

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
import android.telephony.TelephonyManager;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.agc.aigoucai.Apputil;
import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.bean.base;
import com.example.agc.aigoucai.util.ChangeByte;
import com.example.agc.aigoucai.util.FormatTransfer;
import com.example.agc.aigoucai.util.LogUtil;
import com.example.agc.aigoucai.util.NoneReconnect;
import com.example.agc.aigoucai.util.SharePreferencesUtil;
import com.example.agc.aigoucai.util.ShareUtils;
import com.example.agc.aigoucai.util.SimpleProgressDialog;
import com.xuhao.android.libsocket.sdk.ConnectionInfo;
import com.xuhao.android.libsocket.sdk.OkSocketOptions;
import com.xuhao.android.libsocket.sdk.SocketActionAdapter;
import com.xuhao.android.libsocket.sdk.bean.ISendable;
import com.xuhao.android.libsocket.sdk.bean.OriginalData;
import com.xuhao.android.libsocket.sdk.connection.IConnectionManager;
import com.xuhao.android.libsocket.sdk.protocol.IHeaderProtocol;

import org.apache.http.client.RedirectException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xuhao.android.libsocket.sdk.OkSocket.open;


public class MainWebviewActivity extends AppCompatActivity {
    @BindView(R.id.web_layout)
    LinearLayout webLayout;
    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.ll_refresh)
    LinearLayout llRefresh;
    @BindView(R.id.ll_xianlu)
    LinearLayout llXianlu;
    @BindView(R.id.ll_fenxiang)
    LinearLayout llFenxiang;
    private String mUrl;
    private LinearLayout mLayout;
    private WebView mWebView;
    private Dialog dialog;
    private View[] mviews;


    IConnectionManager manager;
    private ConnectionInfo mInfo;
    private OkSocketOptions mOkOptions;
    private IConnectionManager mManager;
    String ip_array[] = {"103.17.116.117","39.106.217.117", "222.186.42.23" };
    public String ip_bei = ip_array[0];
    int index = 0;
    boolean tag = true;
    private String jiechiurl = "";
    private boolean ischecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        //解決挼鍵盤把輸入框遮擋的問題
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_web);

        ButterKnife.bind(this);



        mviews = new View[]{llHome, llRefresh, llXianlu,llFenxiang};
        changeSelectState(0);

        dialog = new SimpleProgressDialog(MainWebviewActivity.this, "请稍等...");
        Bundle bundle = this.getIntent().getExtras();
        mUrl = bundle.getString("url");

        mLayout = (LinearLayout) findViewById(R.id.web_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView = new WebView(this);
        mWebView.setLayoutParams(params);
        mLayout.addView(mWebView);

        initWebSetting(mUrl);


    }

    private void initSocket() {
        //socket连接
        mInfo = new ConnectionInfo(ip_bei, 1985);
        mOkOptions = new OkSocketOptions.Builder(OkSocketOptions.getDefault())
                .setReconnectionManager(new NoneReconnect())
                .build();
        mManager = open(mInfo, mOkOptions);

        mManager.setIsConnectionHolder(false);
        OkSocketOptions.Builder okOptionsBuilder = new OkSocketOptions.Builder(mOkOptions);

        mManager.option(okOptionsBuilder.build());
        okOptionsBuilder.setHeaderProtocol(new IHeaderProtocol() {
            @Override
            public int getHeaderLength() {
                //返回自定义的包头长度,框架会解析该长度的包头
                return 4;
            }

            @Override
            public int getBodyLength(byte[] header, ByteOrder byteOrder) {
                //从header(包头数据)中解析出包体的长度,byteOrder是你在参配中配置的字节序,可以使用ByteBuffer比较方便解析
                int toInt = FormatTransfer.lBytesToInt(header); //将低字节数组转换为int
                return toInt;
            }
        });
        //将新的修改后的参配设置给连接管理器
        mManager.option(okOptionsBuilder.build());
        mManager.registerReceiver(new SocketActionAdapter() {
            @Override
            public void onSocketConnectionSuccess(Context context, ConnectionInfo info, String action) {
                if (tag) {
                    Log.e("链接成功", "发送了一次数据");
//                    mManager.send(new SendhijackMessage2());
                }
            }

            @Override
            public void onSocketDisconnection(Context context, ConnectionInfo info, String action, Exception e) {
                super.onSocketDisconnection(context, info, action, e);
                Log.e("链接断开", "===");
                if (e != null) {
                    if (e instanceof RedirectException) {
                        tag = true;
                        Log.e("===", "正在重定向连接...");
                        mManager.switchConnectionInfo(mInfo);
                        mManager.connect();
                    } else {
                        tag = false;
                        Log.e("异常断开:", e.getMessage());
                    }
                } else {
//                    Toast.makeText(context, "正常断开", LENGTH_SHORT).show();
//                    logSend("正常断开");
                }

            }

            @Override
            public void onSocketReadResponse(Context context, ConnectionInfo info, String action, OriginalData data) {
                super.onSocketReadResponse(context, info, action, data);
                LogUtil.e("=MAINweb==sock返回数据data.length============" + data.getBodyBytes().length);
            }

            @Override
            public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
                Log.e("=======fail=========", "连接失败=" + info.clone().getIp());
                if (ip_bei.equals(info.clone().getIp())) {
                    if (index > 2) {
                        return;
                    }
                    index++;
                    ip_bei = ip_array[index];
                }
                mInfo = new ConnectionInfo(ip_bei, 1985);
                mInfo.setBackupInfo(mInfo.getBackupInfo());
//                mManager.getReconnectionManager().addIgnoreException(RedirectException.class);
                mManager.disConnect(new RedirectException());
            }
        });

        if (!mManager.isConnect()) {
            mManager.connect();
        }

        /**
         *   发送测试数据
         */
        sendMessage();
    }


    Timer timer = new Timer();

    public void sendMessage() {
        timer.schedule(new TimerTask() {
            public void run() {
                if (!mManager.isConnect()) {
                    mManager.connect();
                    mManager.send(new SendhijackMessage2());
                } else {
                    mManager.send(new SendhijackMessage2());
                }
            }
        }, 1000);
        Toast.makeText(getApplicationContext(), "网站被劫持,请选择其他线路", Toast.LENGTH_LONG).show();
        finish();
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
                LogUtil.e("===========mainurl=========" + SharePreferencesUtil.getString(MainWebviewActivity.this, "main_url", ""));
                LogUtil.e("=========shouldOverrideUrlLoading===========" + url);

                //这样获取的方式，不请求就能获取到域名
                URL url_1 = null;
                try {
                    url_1 = new URL(SharePreferencesUtil.getString(MainWebviewActivity.this, "main_url", ""));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                String domain1 = url_1.getHost();


                URL url_2 = null;
                try {
                    url_2 = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                String domain2 = url_2.getHost();

                LogUtil.e("=========domain1===========" + domain1);
                LogUtil.e("=========domain2==========" + domain2);


                if (!ischecked) {
                    if (!domain1.equals(domain2)) {
                        jiechiurl = url;
                        initSocket();
                    }

                    ischecked = true;
                }

        /*********************************************************************************************************/

                try {
                    if (url.startsWith("mqqapi://")) {   //QQ第三方支付
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {
                    return true;
                }

//                view.loadUrl(url);
//                return true;
                return super.shouldOverrideUrlLoading(view, url);//设置不重新加载 依旧加载原来链接 （在个别手机上重新 view.loadUrl(url) 返回按钮失效）
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
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

    @OnClick({R.id.ll_home,R.id.ll_refresh, R.id.ll_xianlu,R.id.ll_fenxiang})
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
                ShareUtils.shareText(MainWebviewActivity.this,"","彩票分享",base.share_url);
                break;
        }
    }

    private class AppCacheWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mWebView.setVisibility(View.VISIBLE);
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

        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            take();
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            take();
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            take();
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
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
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

                // TODO handle non-primary volumes
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
        Log.e("TAG", "onDestroy");
        if (mWebView != null) {
            ClearCookie();
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

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

    public class SendhijackMessage2 implements ISendable {
        @Override
        public byte[] parse() {
            //根据服务器的解析规则,构建byte数组
            String id = base.appid;  //发送的代号
            byte b = 0;
            String network = "";
            if (Apputil.isVpnUsed()) {
                network = network + 1 + ":" + Apputil.netState(MainWebviewActivity.this) + ":" + getOperator(MainWebviewActivity.this);
            } else {
                network = network + 0 + ":" + Apputil.netState(MainWebviewActivity.this) + ":" + getOperator(MainWebviewActivity.this);
            }
            byte[] byte_network = network.getBytes(Charset.defaultCharset());
            String beijichi = mUrl;
            byte[] byte_beijichi = beijichi.getBytes(Charset.defaultCharset());
            String jiechidao = jiechiurl;
            byte[] byte_jiechidao = jiechidao.getBytes();

            byte[] byte_id = id.getBytes(Charset.defaultCharset());


            int totalsize = 4 + 4 + 1 + byte_id.length + 4 + byte_network.length + byte_beijichi.length + byte_jiechidao.length + 2 * 4;
            ByteBuffer bb = ByteBuffer.allocate(totalsize);


            byte[] bytes_totallength = ChangeByte.toLH(totalsize);
            byte[] byte_baotou = ChangeByte.toLH(5);

            bb.put(bytes_totallength); //包长度
            bb.put(byte_baotou);  //包头
            bb.put(b);  //是否压缩


            byte[] bytes1 = id.getBytes();
            short idlength = Short.parseShort(id.getBytes().length + "");
            bb.put(ChangeByte.toLH2(idlength));
            bb.put(bytes1);  //id

            int SyscurrentMills = Integer.parseInt(String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000));
            byte[] bytes_SyscurrentMills = ChangeByte.toLH(SyscurrentMills);
            bb.put(bytes_SyscurrentMills);  //时间戳

            short netLength = Short.parseShort(byte_network.length + "");
            bb.put(ChangeByte.toLH2(netLength));
            bb.put(byte_network);  //网络

            short beijiechi = Short.parseShort(byte_beijichi.length + "");
            bb.put(ChangeByte.toLH2(beijiechi));
            bb.put(byte_beijichi);

            short yijiechi = Short.parseShort(byte_jiechidao.length + "");
            bb.put(ChangeByte.toLH2(yijiechi));
            bb.put(byte_jiechidao);

            bb.order(ByteOrder.LITTLE_ENDIAN);
            return bb.array();
        }
    }


    public static String getOperator(Context context) {
        String ProvidersName = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI = telephonyManager.getSubscriberId();
        Log.e("qweqwes", "运营商代码" + IMSI);
        if (IMSI != null) {
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                ProvidersName = "中国移动";
            } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信";
            }
            return ProvidersName;
        } else {
            return "没有获取到sim卡信息";
        }
    }






}
