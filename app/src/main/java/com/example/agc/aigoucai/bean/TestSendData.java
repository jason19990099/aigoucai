package com.example.agc.aigoucai.bean;

import com.example.agc.aigoucai.bean.base;
import com.xuhao.android.libsocket.sdk.bean.ISendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Calendar;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class TestSendData implements ISendable {
    String str = base.appid;  //发送的代号
    byte b = 0;



    @Override
    public byte[] parse() {
        //根据服务器的解析规则,构建byte数组
        byte[] body = str.getBytes(Charset.defaultCharset());  //

        ByteBuffer bb = ByteBuffer.allocate(body.length + 4 + 1 +2+4);
        byte[] bytes = toLH(body.length + 4 + 1 + 2);
        byte[] byte_1 = toLH(1);
        bb.put(bytes);
        bb.put(byte_1);
        bb.put(b);
        byte[] bytes1 = toLH2((short) body.length);
        bb.put(bytes1);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(body);
        return bb.array();
    }





    public static byte[] toLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }
    public static byte[] toLH2(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }
}
