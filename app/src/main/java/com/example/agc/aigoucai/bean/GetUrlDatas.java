package com.example.agc.aigoucai.bean;

import com.example.agc.aigoucai.util.ByteUtil;
import com.xuhao.android.libsocket.sdk.bean.ISendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 *   获取url返回的连接
 *
 *   发送的数据
 */
public class GetUrlDatas implements ISendable {
    String str = Basedata.appid;  //发送的代号
    byte b = 0;

    @Override
    public byte[] parse() {
        //根据服务器的解析规则,构建byte数组
        byte[] body = str.getBytes(Charset.defaultCharset());  //

        ByteBuffer bb = ByteBuffer.allocate(body.length + 4 + 1 + 2 + 4);
        byte[] bytes = ByteUtil.toLH(body.length + 4 + 1 + 2);
        byte[] byte_1 = ByteUtil.toLH(1);
        bb.put(bytes);
        bb.put(byte_1);
        bb.put(b);
        byte[] bytes1 = ByteUtil.toLH2((short) body.length);
        bb.put(bytes1);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(body);
        return bb.array();
    }
}
