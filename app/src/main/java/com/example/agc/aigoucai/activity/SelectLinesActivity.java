package com.example.agc.aigoucai.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.R2;
import com.example.agc.aigoucai.bean.DataSynevent;
import com.example.agc.aigoucai.bean.TestSendData;
import com.example.agc.aigoucai.bean.base;
import com.example.agc.aigoucai.util.Apputil;
import com.example.agc.aigoucai.util.ByteUtil;
import com.example.agc.aigoucai.util.CustomDialog;
import com.example.agc.aigoucai.util.IntentUtil;
import com.example.agc.aigoucai.util.LogUtil;
import com.example.agc.aigoucai.util.SB;
import com.example.agc.aigoucai.util.SharePreferencesUtil;
import com.example.agc.aigoucai.util.SocketUtil;
import com.example.agc.aigoucai.util.senddata;
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
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectLinesActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener,senddata {
    public IConnectionManager mManager;
    @BindView(R2.id.listvie_id)
    ListView listvieId;
    @BindView(R2.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R2.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R2.id.tv_vertion)
    TextView tvVertion;
    private Adapter_url adapter_url = new Adapter_url();
    private CustomDialog.Builder ibuilder;
    private String[] url_array ;
    private String[] time_array ;
    private String responsecode;
    private String badurl;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectlines);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        tvVertion.setText("版本号:"+Apputil.getVersion(SelectLinesActivity.this));
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


        if (null==mManager){
            mManager = SocketUtil.getmManager();
        }
        if (null != mManager) {
            if (!mManager.isConnect()) {
                mManager.connect();
            }
            mManager.send(new TestSendData());
        }


    }



    /**
     * 刷新網址鏈接
     */
    private void refresh() {
        if (null==mManager){
            mManager = SocketUtil.getmManager();
        }
        if (null!=mManager){
            if (!mManager.isConnect()) {
                Log.e("=================", "socket未连接，正在连接中.....");
                mManager.connect();
            }
            mManager.send(new TestSendData());

            Log.e("=================", "發送已经发送.......");
        }


    }

    @Override
    public void onRefresh() {
        refresh();
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void getDate() {
        LogUtil.e("==========取得数据============");
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
            try{
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
            }}
            catch (Exception e){
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
                    connection.setConnectTimeout(3000);
                    connection.setReadTimeout(3000);
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
                        if (!String.valueOf(responseCode).startsWith("30")){
                            time_string = "超时";
                            time_array[i] = time_string;
                            hander.sendEmptyMessage(0); // 下载完成后发送处理消息
                            badurl=address;
                            responsecode=String.valueOf(responseCode)+"###"+Apputil.getIP(badurl);
                            SocketsendMessage();
                        }

                    }
                } catch (Exception e) {
                    //有错误就设置成超时
                    time_string = "超时*";
                    time_array[i] = time_string;
                    hander.sendEmptyMessage(0); // 下载完成后发送处理消息


                    e.printStackTrace();
                    badurl=address;
                    responsecode=e.toString()+"###"+Apputil.getIP(badurl);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
