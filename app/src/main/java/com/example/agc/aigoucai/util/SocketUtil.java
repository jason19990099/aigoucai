package com.example.agc.aigoucai.util;


import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.example.agc.aigoucai.bean.TestSendData;
import com.xuhao.android.libsocket.sdk.ConnectionInfo;
import com.xuhao.android.libsocket.sdk.OkSocketOptions;
import com.xuhao.android.libsocket.sdk.SocketActionAdapter;
import com.xuhao.android.libsocket.sdk.bean.OriginalData;
import com.xuhao.android.libsocket.sdk.connection.IConnectionManager;
import com.xuhao.android.libsocket.sdk.protocol.IHeaderProtocol;

import org.apache.http.client.RedirectException;

import java.nio.ByteOrder;

import static com.xuhao.android.libsocket.sdk.OkSocket.open;

/**
 *  socket工具類
 */
public class SocketUtil {

    private static ConnectionInfo mInfo;
    private static OkSocketOptions mOkOptions;
    private  static IConnectionManager mManager;
    private ListView listvie_id;
    private CustomDialog2.Builder ibuilder;
    static String ip_array[] = {"39.106.217.117", "222.186.42.23", "103.17.116.117"};
    public static String ip_bei = ip_array[0];
    int index = 0;
    private static boolean tag = true;
    String[] url_array = null;
    String[] time_array = null;



    public static void  getSocket(){
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

            }

            @Override
            public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
                Log.e("=======fail=========", "连接失败=" + info.clone().getIp());
                if (ip_bei.equals(info.clone().getIp())) {
//                    if (index > 2) {
//                        return;
//                    }
//                    index++;
//                    ip_bei = ip_array[index];

                    LogUtil.e("=======正在重新连接其他网址========" + ip_bei);
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



    }









}
