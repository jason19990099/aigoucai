package com.example.agc.aigoucai.activity;

import android.app.Activity;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.agc.aigoucai.R;
import com.example.agc.aigoucai.bean.SendhijackMessage;
import com.example.agc.aigoucai.bean.TestSendData;
import com.example.agc.aigoucai.http.Http;
import com.example.agc.aigoucai.util.CustomDialog2;
import com.example.agc.aigoucai.util.FormatTransfer;
import com.example.agc.aigoucai.util.IntentUtil;
import com.example.agc.aigoucai.util.LogUtil;
import com.example.agc.aigoucai.util.NoneReconnect;
import com.example.agc.aigoucai.util.SB;
import com.example.agc.aigoucai.util.SharePreferencesUtil;
import com.xuhao.android.libsocket.sdk.ConnectionInfo;
import com.xuhao.android.libsocket.sdk.OkSocketOptions;
import com.xuhao.android.libsocket.sdk.SocketActionAdapter;
import com.xuhao.android.libsocket.sdk.bean.OriginalData;
import com.xuhao.android.libsocket.sdk.connection.IConnectionManager;
import com.xuhao.android.libsocket.sdk.protocol.IHeaderProtocol;

import org.apache.http.client.RedirectException;

import static com.xuhao.android.libsocket.sdk.OkSocket.open;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class SelectLinesActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    IConnectionManager manager;
    private ConnectionInfo mInfo;
    private OkSocketOptions mOkOptions;
    private IConnectionManager mManager;
    private ListView listvie_id;
    Adapter_url adapter_url;
    private CustomDialog2.Builder ibuilder;
    String ip_array[] = {"103.17.116.117","39.106.217.117", "222.186.42.23" };
    public String ip_bei = ip_array[0];
    int index = 0;
    private boolean tag = true;
    String[] url_array = null;
    String[] time_array = null;
    // 退出时间
    public static long currentBackPressedTime = 0;
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (null!=adapter_url)
                    adapter_url.notifyDataSetChanged(); //发送消息通知ListView更新
                    break;
                default:
                    break;
            }
        }
    };
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wel_activity_main);
        listvie_id = (ListView) findViewById(R.id.listvie_id);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//        swipeLayout.setRefreshing(true);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_orange_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        if (!Http.isNetConnection(SelectLinesActivity.this)) {
            ibuilder = new CustomDialog2.Builder(SelectLinesActivity.this);
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
                    mManager.send(new TestSendData());
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
                LogUtil.e("===sock返回数据data.length============" + data.getBodyBytes().length);
                if (data.getBodyBytes().length < 15) {
                    return;
                }
                try {
                    byte[] bodyBytes = data.getBodyBytes();
                    String bytesToHex_16 = FormatTransfer.bytesToHex(bodyBytes, 0, data.getBodyBytes().length);
                    String substring = bytesToHex_16.substring(4 * 2 + 2, bytesToHex_16.length());
                    String nums_str = substring.substring(0, 4 * 2); //获取网址数量
                    byte[] bytes_nums = FormatTransfer.hexStringToByte(nums_str);
                    int nums_wangzhi = FormatTransfer.lBytesToInt(bytes_nums);  //网址数量
                    url_array = new String[nums_wangzhi];
                    String _www_string = bytesToHex_16.substring(2 * (4 + 1 + 4), bytesToHex_16.length());
                    byte[] bytes_www = FormatTransfer.hexStringToByte(_www_string); //所有网址字节

                    int index_len = 0;
                    int index_cout = 2;
                    int nums_wangleng = 0;

                    for (int i = 0; i < nums_wangzhi; i++) {
                        byte[] bytes = subBytes(bytes_www, index_len, 2);
                        nums_wangleng = FormatTransfer.lBytesToShort(bytes);  //网址长度(1)
                        byte[] www_ = subBytes(bytes_www, index_cout, nums_wangleng);
                        String _www = new String(www_);
                        index_len += (nums_wangleng + 2);
                        index_cout += (nums_wangleng + 2);
                        url_array[i] = _www;
                        Log.e("=网址=", _www);
                    }
                    adapter_url = new Adapter_url();
                    listvie_id.setAdapter(adapter_url);
                    time_array = new String[url_array.length];
                    for (int i = 0; i < url_array.length; i++) {
                        sendHttpRequest(url_array[i], i);
                    }
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            if (!mManager.isConnect()) {
                                mManager.connect();
                                mManager.send(new SendhijackMessage());
                            } else {
                                mManager.send(new SendhijackMessage());
                            }
                        }
                    }, 3000);

                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e("==============" + e);
                } finally {

                }


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

                    LogUtil.e("=======正在重新连接其他网址========" +ip_bei);
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
//        sendMessage();
    }


//    Timer timer = new Timer();
//
//    public void sendMessage() {
//        timer.schedule(new TimerTask() {
//            public void run() {
//                if (!mManager.isConnect()) {
//                    mManager.connect();
//                    mManager.send(new TestSendData());
//                } else {
//                    mManager.send(new TestSendData());
//                }
//            }
//        }, 2000);
//    }


    /**
     * 从一个byte[]数组中截取一部分
     *
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i = begin; i < begin + count; i++) bs[i - begin] = src[i];
        return bs;
    }


    /**
     * 刷新網址鏈接
     */
    private void refresh() {
        if (!mManager.isConnect()) {
            Log.e("===", "socket未连接，正在连接中.....");
            mManager.connect();
            mManager.send(new TestSendData());
        } else {
            mManager.send(new TestSendData());
        }

        if (null != adapter_url)
            adapter_url.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        refresh();
        swipeLayout.setRefreshing(false);
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
                    SharePreferencesUtil.addString(SelectLinesActivity.this,"main_url",url_array[i]);
                    IntentUtil.gotoActivity(SelectLinesActivity.this, MainWebviewActivity.class, bundleTab, false);

                }
            });

//          text_id.setText(url_array[i]);
            text_id.setText("线路" + (i + 1));//使用綫路123代表網址避免被劫持
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


            return view1;
        }
    }


    public SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//设置日期格式
    String time_string = "";

    //请求网址响应
    public String sendHttpRequest(final String address, final int i) {

        new Thread(new Runnable() {
            long between = 0;
            String date1 = "";
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
                        java.util.Date begin = dfs.parse(date1);
                        java.util.Date end = dfs.parse(date2);
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
                        time_string = "超时";
                        time_array[i] = time_string;
                        hander.sendEmptyMessage(0); // 下载完成后发送处理消息
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tag = false;
        if (manager != null) {
            manager.disConnect();
        }
//        if (timer != null) {
//            timer.cancel();
//        }
    }
}
