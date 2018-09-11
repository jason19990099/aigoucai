package com.example.agc.aigoucai.util;

import android.content.Context;
import android.util.Log;
import com.example.agc.aigoucai.bean.DataSynevent;
import com.example.agc.aigoucai.bean.GetUrlDatas;
import com.xuhao.android.libsocket.sdk.ConnectionInfo;
import com.xuhao.android.libsocket.sdk.OkSocketOptions;
import com.xuhao.android.libsocket.sdk.SocketActionAdapter;
import com.xuhao.android.libsocket.sdk.bean.OriginalData;
import com.xuhao.android.libsocket.sdk.connection.IConnectionManager;
import com.xuhao.android.libsocket.sdk.protocol.IHeaderProtocol;
import org.apache.http.client.RedirectException;
import org.greenrobot.eventbus.EventBus;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import static com.xuhao.android.libsocket.sdk.OkSocket.open;

/**
 * socket工具類
 */
public class SocketUtil {
    private static ConnectionInfo mInfo;
    private static OkSocketOptions mOkOptions;
    private static IConnectionManager mManager;
    private static List<String> ip_array = new ArrayList<>();
    public static String ip_bei = "";
    private static int index = 0;
    private static int net_port=1985;
    private Context context;


    /**
     * 传入进去ip地址和端口号
     */
    public SocketUtil(List list, int port,Context context) {
        ip_array = list;
        ip_bei = ip_array.get(0);
        this.context=context;
        SharePreferencesUtil.addString(context,"s_ip",ip_bei);
        SharePreferencesUtil.addString(context,"port",port+"");
        net_port=port;
    }


    /**
     *  socket链接方法
     */
    public static void getSocketConection() {
        //socket连接
        mInfo = new ConnectionInfo(SocketUtil.ip_bei, net_port);
        mOkOptions = new OkSocketOptions.Builder(OkSocketOptions.getDefault()).setReconnectionManager(new NoneReconnect()).build();
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
                LogUtil.e("==onSocketConnectionSuccess=链接成功======");
            }

            @Override
            public void onSocketDisconnection(Context context, ConnectionInfo info, String action, Exception e) {
                super.onSocketDisconnection(context, info, action, e);
                if (e != null) {
                    if (e instanceof RedirectException) {
                        LogUtil.e("==onSocketDisconnection=异常断开===正在重定向连接===");
                    } else {
                        LogUtil.e("==onSocketDisconnection=socket已經断开======" + e.getMessage());
                    }
                    mManager.switchConnectionInfo(mInfo);
                    mManager.connect();
                } else {
                    LogUtil.e("==onSocketDisconnection=正常断开======");
                }

            }

            @Override
            public void onSocketReadResponse(Context context, ConnectionInfo info, String action, OriginalData data) {
                super.onSocketReadResponse(context, info, action, data);
                LogUtil.e("===sockutil返回数据data.length============" + data.getBodyBytes().length);
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
                    String[] url_array = new String[nums_wangzhi];
                    String _www_string = bytesToHex_16.substring(2 * (4 + 1 + 4), bytesToHex_16.length());
                    byte[] bytes_www = FormatTransfer.hexStringToByte(_www_string); //所有网址字节

                    int index_len = 0;
                    int index_cout = 2;
                    int nums_wangleng = 0;
                    List list = new ArrayList();
                    for (int i = 0; i < nums_wangzhi; i++) {
                        byte[] bytes = ByteUtil.subBytes(bytes_www, index_len, 2);
                        nums_wangleng = FormatTransfer.lBytesToShort(bytes);  //网址长度(1)
                        byte[] www_ = ByteUtil.subBytes(bytes_www, index_cout, nums_wangleng);
                        String _www = new String(www_);
                        index_len += (nums_wangleng + 2);
                        index_cout += (nums_wangleng + 2);
                        url_array[i] = _www;
                        list.add(url_array[i]);
                        Log.e("=====网址====", _www);

                    }
                    DataSynevent dataSynevent = new DataSynevent();
                    dataSynevent.setList(list);
                    //发送粘性事件
                    EventBus.getDefault().postSticky(dataSynevent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
                Log.e("=======fail=========", "连接失败=" + info.clone().getIp());
                if (SocketUtil.ip_bei.equals(info.clone().getIp())) {
                    if (index > (ip_array.size() - 1)) {
                        return;
                    }
                    index++;
                    LogUtil.e("===========index========" + index);
                    SocketUtil.ip_bei = ip_array.get(index);
                    SharePreferencesUtil.addString(context,"s_ip",ip_array.get(index));
                    LogUtil.e("=======正在重新连接其他网址========" + SocketUtil.ip_bei);
                }
                mInfo = new ConnectionInfo(SocketUtil.ip_bei, net_port);
                mInfo.setBackupInfo(mInfo.getBackupInfo());
                mManager.disConnect(new RedirectException());
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mManager.isConnect())
                            mManager.send(new GetUrlDatas());
                    }
                }, 500);
            }
        });


        /**
         *   开始链接
         */
        if (!mManager.isConnect()) {
            mManager.connect();
        }


    }

    /**
     * 获取到socket链接的manager
     *
     * @return
     */
    public static IConnectionManager getmManager() {
        return mManager;
    }

}
