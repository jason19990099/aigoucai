package com.example.agc.aigoucai.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.bean.APPdata;
import com.example.agc.aigoucai.bean.DataSynevent;
import com.example.agc.aigoucai.bean.GetUrlDatas;
import com.example.agc.aigoucai.bean.base;
import com.example.agc.aigoucai.util.Apputil;
import com.example.agc.aigoucai.util.ByteUtil;
import com.example.agc.aigoucai.util.CustomDialog;
import com.example.agc.aigoucai.util.IntentUtil;
import com.example.agc.aigoucai.util.LogUtil;
import com.example.agc.aigoucai.util.SB;
import com.example.agc.aigoucai.util.SharePreferencesUtil;
import com.example.agc.aigoucai.util.SocketUtil;
import com.google.gson.Gson;
import com.xuhao.android.libsocket.sdk.bean.ISendable;
import com.xuhao.android.libsocket.sdk.connection.IConnectionManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SelectLinesActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    public IConnectionManager mManager;
    @BindView(R.id.listvie_id)
    ListView listvieId;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.tv_vertion)
    TextView tvVertion;
    @BindView(R.id.be_selectservice)
    Button beSelectservice;
    private Adapter_url adapter_url = new Adapter_url();
    private CustomDialog.Builder ibuilder;
    private String[] url_array;
    private String[] time_array;
    private String responsecode;
    private String badurl;
    private long long1, long2;
    private String long3;
    // 退出时间
    private static long currentBackPressedTime = 0;
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (null != adapter_url)
                        adapter_url.notifyDataSetChanged(); //发送消息通知ListView更新
                    break;
            }
        }
    };

    //
//    private MediaProjectionManager mediaProjectionManager;
//    private MediaProjection mMediaProjection;
//    private VirtualDisplay mVirtualDisplay;
//    private static Intent mResultData = null;
//    private ImageReader mImageReader;
//    private WindowManager mWindowManager;
//    private int mScreenWidth;
//    private int mScreenHeight;
//    private int mScreenDensity;
//    public static final int REQUEST_MEDIA_PROJECTION = 18;

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectlines);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        tvVertion.setText("版本号:" + Apputil.getVersion(SelectLinesActivity.this));
//        swipeLayout.setRefreshing(true);
        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        if (!Apputil.isNetConnection(SelectLinesActivity.this)) {
            ibuilder = new CustomDialog.Builder(SelectLinesActivity.this);
            ibuilder.setTitle("");
            ibuilder.setMessage("请检查你的网络是否连接");
            ibuilder.setPositiveButton("查看", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                    startActivity(intent);
                }
            });
            ibuilder.create().show();
        }
        long1 = System.currentTimeMillis();
        if (null == mManager) {
            mManager = SocketUtil.getmManager();
        }
        if (null != mManager) {
            if (!mManager.isConnect()) {
                mManager.connect();
            }
            mManager.send(new GetUrlDatas());
        }




//        /**
//         * 截图初始化变量
//         */
//        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
//        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
//
//        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics metrics = new DisplayMetrics();
//        mWindowManager.getDefaultDisplay().getMetrics(metrics);
//        mScreenDensity = metrics.densityDpi;
//        mScreenWidth = metrics.widthPixels;
//        mScreenHeight = metrics.heightPixels;
//        mImageReader = ImageReader.newInstance(mScreenWidth, mScreenHeight, PixelFormat.RGBA_8888, 1);



//        startScreenShot();  截图的使用方法

    }

//    /**
//     * 截图拥戴的代码
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case REQUEST_MEDIA_PROJECTION:
//
//                if (resultCode == RESULT_OK && data != null) {
//                    mResultData = data;
//                    //startService(new Intent(getApplicationContext(), FloatWindowsService.class));
//                }
//                break;
//        }
//    }


    /**
     * 刷新網址鏈接
     */
    private void refresh() {
        long1 = System.currentTimeMillis();
        if (null == mManager) {
            mManager = SocketUtil.getmManager();
        }
        if (null != mManager) {
            if (!mManager.isConnect()) {
                Log.e("=================", "socket未连接，正在连接中.....");
                mManager.connect();
            }
            mManager.send(new GetUrlDatas());

            Log.e("=================", "發送已经发送.......");
        }
    }

    @Override
    public void onRefresh() {
        refresh();
        swipeContainer.setRefreshing(false);
    }

    @OnClick(R.id.be_selectservice)
    public void onViewClicked(View view) {
        switch(view.getId()){
            case  R.id.be_selectservice:
              startActivity(new Intent(SelectLinesActivity.this,SelectServiceActivity.class));
                break;
        }
    }


    class Adapter_url extends BaseAdapter {
        @Override
        public int getCount() {
            return url_array.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = LayoutInflater.from(SelectLinesActivity.this);
            View view1 = inflater.inflate(R.layout.item, null);
            TextView text_id = view1.findViewById(R.id.text_id);
            TextView text_id_sp = view1.findViewById(R.id.text_id_sp);
            final LinearLayout ll_listview = view1.findViewById(R.id.ll_listview);

            ll_listview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ll_listview.setBackgroundColor(getResources().getColor(R.color.blue_00ffff));
                    ll_listview.getBackground().setAlpha(25);

                    Bundle bundleTab = new Bundle();
                    bundleTab.putString("url", url_array[i]);
                    SharePreferencesUtil.addString(SelectLinesActivity.this, "main_url", url_array[i]);
                    IntentUtil.gotoActivity(SelectLinesActivity.this, MainWebviewActivity.class, bundleTab, false);

                }
            });

//          text_id.setText(url_array[i]);
            text_id.setText("线路" + (i + 1));//使用綫路123代表網址避免被劫持
            try {
                if (!TextUtils.isEmpty(time_array[i])) {
                    if (!time_array[i].equals("超时")) {
                        String s = time_array[i].split("#")[0];
                        String ms = time_array[i].split("#")[1];
                        if (Integer.valueOf(s) > 0) {
                            text_id_sp.setTextColor(Color.parseColor("#FFFF4081"));
                            text_id_sp.setText(s + "s" + " " + ms + "ms");
                        }
                        if (Integer.valueOf(s) == 0) {
                            text_id_sp.setTextColor(Color.parseColor("#FF277E42"));
                            text_id_sp.setText(ms + "ms");
                        } else {
                            text_id_sp.setTextColor(Color.parseColor("#FFFF4081"));
                            text_id_sp.setText(s + "s" + "" + ms + "ms");
                        }
                    } else {
                        text_id_sp.setTextColor(Color.parseColor("#FFFF4081"));
                        text_id_sp.setText(time_array[i]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return view1;
        }
    }


    public SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//设置日期格式
    String time_string = "";

    //请求网址响应
    public String sendHttpRequest(final String address, final int i) {

        new Thread(new Runnable() {
            long between = 0;
            String date2 = "";
            long day;
            long hour;
            long min;
            long s;
            long ms;

            public void run() {

                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(9999);
                    connection.setReadTimeout(9999);
                    final String date1 = dfs.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        date2 = dfs.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
                        Date begin = dfs.parse(date1);
                        Date end = dfs.parse(date2);
                        between = Math.abs((end.getTime() - begin.getTime()));// 得到两者的毫秒数
                        day = between / (24 * 60 * 60 * 1000);
                        hour = (between / (60 * 60 * 1000) - day * 24);
                        min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
                        s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                        ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                                - min * 60 * 1000 - s * 1000);
                        Log.e("两个时间相差", min + "分" + s + "秒" + ms + "毫秒");
                        time_string = s + "#" + ms + "";
                        time_array[i] = time_string;
                        hander.sendEmptyMessage(0); // 下载完成后发送处理消息
                    } else {
                        //30開頭的過濾掉
                        if (!String.valueOf(responseCode).startsWith("30")) {
                            time_string = "超时";
                            time_array[i] = time_string;
                            hander.sendEmptyMessage(0); // 下载完成后发送处理消息
                            badurl = address;

                            APPdata apPdata=new  APPdata();
                            apPdata.setB(Build.BRAND);
                            apPdata.setM(Build.MODEL);
                            apPdata.setIp(Apputil.getIP(badurl));
                            apPdata.setBv("Chromium_Blink");//浏览器版本
                            apPdata.setAv(Apputil.getVersion(SelectLinesActivity.this));
                            apPdata.setSt(long3);
                            apPdata.setS(Apputil.getSystemVersion());
                            apPdata.setS_ip(SharePreferencesUtil.getString(SelectLinesActivity.this,"s_ip","0"));
                            apPdata.setPort(SharePreferencesUtil.getString(SelectLinesActivity.this,"port","0"));
                            responsecode = new Gson().toJson(apPdata);
                            SocketsendMessage();
                        }else{
                            date2 = dfs.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
                            Date begin = dfs.parse(date1);
                            Date end = dfs.parse(date2);
                            between = Math.abs((end.getTime() - begin.getTime()));// 得到两者的毫秒数
                            day = between / (24 * 60 * 60 * 1000);
                            hour = (between / (60 * 60 * 1000) - day * 24);
                            min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
                            s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                            ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                                    - min * 60 * 1000 - s * 1000);
                            Log.e("两个时间相差", min + "分" + s + "秒" + ms + "毫秒");
                            time_string = s + "#" + ms + "";
                            time_array[i] = time_string;
                            hander.sendEmptyMessage(0); // 下载完成后发送处理消息
                        }
                    }

                } catch (Exception e) {
                    hander.sendEmptyMessage(0); // 下载完成后发送处理消息
                    e.printStackTrace();

                    APPdata apPdata=new  APPdata();
                    apPdata.setB(Build.BRAND);
                    apPdata.setM(Build.MODEL);
                    badurl = address;
                    apPdata.setIp(Apputil.getIP(badurl));
                    apPdata.setBv("Chromium_Blink");//浏览器版本
                    apPdata.setAv(Apputil.getVersion(SelectLinesActivity.this));
                    apPdata.setSt(long3);
                    apPdata.setS(Apputil.getSystemVersion());
                    apPdata.setErr(e.toString());
                    apPdata.setS_ip(SharePreferencesUtil.getString(SelectLinesActivity.this,"s_ip","0"));
                    apPdata.setPort(SharePreferencesUtil.getString(SelectLinesActivity.this,"port","0"));
                    responsecode = new Gson().toJson(apPdata);
                    LogUtil.e("======199999======="+responsecode);
                    SocketsendMessage();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }

                }
            }
        }).start();
        return time_string;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                // 判断2次点击事件时间
                if ((System.currentTimeMillis() - currentBackPressedTime) > 2000) {
                    SB.showShortMessage(SelectLinesActivity.this, "再按一次退出应用");
                    currentBackPressedTime = System.currentTimeMillis();
                } else {
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        refresh();
    }

    /**
     * EventBus的接收方法
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventBusReceive(DataSynevent dataSynevent) {
        LogUtil.e("====SELECactivity==接收到eventbus传递过来的数据========");
        long2 = System.currentTimeMillis();
        long3=String.valueOf((long2 - long1)) + "ms";

        url_array = dataSynevent.getList().toArray(new String[0]);
        listvieId.setAdapter(adapter_url);
        time_array = new String[url_array.length];
        for (int i = 0; i < url_array.length; i++) {
            sendHttpRequest(url_array[i], i);
        }

        if (null != adapter_url)
            adapter_url.notifyDataSetChanged();

    }

    public class SendhijackMessage2 implements ISendable {
        @Override
        public byte[] parse() {
            //根据服务器的解析规则,构建byte数组
            String id = base.appid;  //发送的代号
            byte b = 0;
            String network = "";
            if (Apputil.isVpnUsed()) {
                network = network + 1 + ":" + Apputil.netState(SelectLinesActivity.this) + ":" + Apputil.getOperator(SelectLinesActivity.this);
            } else {
                network = network + 0 + ":" + Apputil.netState(SelectLinesActivity.this) + ":" + Apputil.getOperator(SelectLinesActivity.this);
            }
            byte[] byte_network = network.getBytes(Charset.defaultCharset());
            String beijichi = badurl;
            byte[] byte_beijichi = beijichi.getBytes(Charset.defaultCharset());
            String jiechidao = responsecode;
            byte[] byte_jiechidao = jiechidao.getBytes();
            LogUtil.e("====beijichi==========" + beijichi);
            LogUtil.e("====jiechidao==========" + jiechidao);
            byte[] byte_id = id.getBytes(Charset.defaultCharset());


            int totalsize = 4 + 4 + 1 + byte_id.length + 4 + byte_network.length + byte_beijichi.length + byte_jiechidao.length + 2 * 4;
            ByteBuffer bb = ByteBuffer.allocate(totalsize);


            byte[] bytes_totallength = ByteUtil.toLH(totalsize);
            byte[] byte_baotou = ByteUtil.toLH(5);

            bb.put(bytes_totallength); //包长度
            bb.put(byte_baotou);  //包头
            bb.put(b);  //是否压缩


            byte[] bytes1 = id.getBytes();
            short idlength = Short.parseShort(id.getBytes().length + "");
            bb.put(ByteUtil.toLH2(idlength));
            bb.put(bytes1);  //id

            int SyscurrentMills = Integer.parseInt(String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000));
            byte[] bytes_SyscurrentMills = ByteUtil.toLH(SyscurrentMills);
            bb.put(bytes_SyscurrentMills);  //时间戳

            short netLength = Short.parseShort(byte_network.length + "");
            bb.put(ByteUtil.toLH2(netLength));
            bb.put(byte_network);  //网络

            short beijiechi = Short.parseShort(byte_beijichi.length + "");
            bb.put(ByteUtil.toLH2(beijiechi));
            bb.put(byte_beijichi);

            short yijiechi = Short.parseShort(byte_jiechidao.length + "");
            bb.put(ByteUtil.toLH2(yijiechi));
            bb.put(byte_jiechidao);

            bb.order(ByteOrder.LITTLE_ENDIAN);
            return bb.array();
        }
    }


    /**
     * socket发送信息到服务器
     */
    private void SocketsendMessage() {
        mManager = SocketUtil.getmManager();
        if (!mManager.isConnect()) {
            mManager.connect();
        }
        mManager.send(new SendhijackMessage2());
    }


 //====================== 以下是截图用到的代码  ==============================
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        try {
//            stopVirtual();
//            tearDownMediaProjection();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

//    private void startScreenShot() {
//        Handler handler1 = new Handler();
//        handler1.postDelayed(new Runnable() {
//            public void run() {
//                // start virtual
//                startVirtual();
//            }
//        }, 5);
//
//        handler1.postDelayed(new Runnable() {
//            public void run() {
//                // capture the screen
//                startCapture();
//
//            }
//        }, 30);
//    }
//
//    public void startVirtual() {
//        if (mMediaProjection != null) {
//            virtualDisplay();
//        } else {
//            setUpMediaProjection();
//            virtualDisplay();
//        }
//    }
//    @SuppressLint("NewApi")
//    private void stopVirtual() {
//        if (mVirtualDisplay == null) {
//            return;
//        }
//        mVirtualDisplay.release();
//        mVirtualDisplay = null;
//    }
//    @SuppressLint("NewApi")
//    private void startCapture() {
//
//        Image image = mImageReader.acquireLatestImage();
//
//        if (image == null) {
//            startScreenShot();
//        } else {
//            SaveTask mSaveTask = new SaveTask();
//            // mSaveTask.execute(image);
//            if (Build.VERSION.SDK_INT >= 11) {
//                // From API 11 onwards, we need to manually select the
//                // THREAD_POOL_EXECUTOR
//                AsyncTaskCompatHoneycomb.executeParallel(mSaveTask, image);
//            } else {
//                // Before API 11, all tasks were run in parallel
//                mSaveTask.execute(image);
//            }
//            // AsyncTaskCompat.executeParallel(mSaveTask, image);
//        }
//    }
//
//
//    static class AsyncTaskCompatHoneycomb {
//
//        static <Params, Progress, Result> void executeParallel(AsyncTask<Params, Progress, Result> task, Params... params) {
//            // 这里显示调用了THREAD_POOL_EXECUTOR，所以就可以使用该线程池了
//            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
//        }
//
//    }
//
//    @SuppressLint("NewApi")
//    private void virtualDisplay() {
//        Surface sf = mImageReader.getSurface();
//        mVirtualDisplay = mMediaProjection.createVirtualDisplay(
//                "screen-mirror", mScreenWidth, mScreenHeight, mScreenDensity,
//                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
//                mImageReader.getSurface(), null, null);
//    }
//    @SuppressLint("NewApi")
//    public void setUpMediaProjection() {
//        if (mResultData == null) {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            startActivity(intent);
//        } else {
//            mMediaProjection = getMediaProjectionManager().getMediaProjection(
//                    Activity.RESULT_OK, mResultData);
//        }
//    }
//    @SuppressLint("NewApi")
//    private MediaProjectionManager getMediaProjectionManager() {
//        return (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
//    }
//    @SuppressLint("NewApi")
//    public class SaveTask extends AsyncTask<Image, Void, Bitmap> {
//
//        @Override
//        protected Bitmap doInBackground(Image... params) {
//            if (params == null || params.length < 1 || params[0] == null) {
//                return null;
//            }
//
//            Image image = params[0];
//
//            int width = image.getWidth();
//            int height = image.getHeight();
//            final Image.Plane[] planes = image.getPlanes();
//            final ByteBuffer buffer = planes[0].getBuffer();
//            // 每个像素的间距
//            int pixelStride = planes[0].getPixelStride();
//            // 总的间距
//            int rowStride = planes[0].getRowStride();
//            int rowPadding = rowStride - pixelStride * width;
//            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
//            bitmap.copyPixelsFromBuffer(buffer);
//            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
//            image.close();
//
////			File file = new File(SAVE_REAL_PATH);
////			if (bitmap != null) {
////				try {
////                    if (!file.exists()) {
////                        file.mkdirs();
////                    }
////                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
////                    String fileImage = file.getAbsolutePath() + "/" + sdf.format(new Date()) + ".jpg";
////					FileOutputStream out = new FileOutputStream(fileImage);
////					if (out != null) {
////						bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
////						out.flush();
////						out.close();
////						Intent media = new Intent(
////								Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
////						Uri contentUri = Uri.fromFile(file);
////						media.setData(contentUri);
////						sendBroadcast(media);
////						fileDestUri = fileImage;
////					}
////				} catch (FileNotFoundException e) {
////					e.printStackTrace();
////					file = null;
////				} catch (IOException e) {
////					e.printStackTrace();
////					file = null;
////				}
////			}
//
////			if (file != null) {
//            return bitmap;
////			}
////			return null;
//        }
//
//        @Override
//        @SuppressLint("NewApi")
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            // 预览图片
//            if (bitmap != null) {
//                //也可以处理保存图片逻辑
////                iv_soul.setImageBitmap(bitmap);
//                //TODO
//            }
//        }
//    }
//    @SuppressLint("NewApi")
//    private void tearDownMediaProjection() {
//        if (mMediaProjection != null) {
//            mMediaProjection.stop();
//            mMediaProjection = null;
//        }
//    }

}
