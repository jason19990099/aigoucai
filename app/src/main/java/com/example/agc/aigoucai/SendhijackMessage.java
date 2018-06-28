package com.example.agc.aigoucai;

import com.example.agc.aigoucai.util.LogUtil;
import com.xuhao.android.libsocket.sdk.bean.ISendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Calendar;

public class SendhijackMessage implements ISendable {
    @Override
    public byte[] parse() {
        //根据服务器的解析规则,构建byte数组
//        String id = "android001";  //发送的代号
//        byte b = 0;
//        String netWork = "0:WIFI";
//        byte[] byte_network = netWork.getBytes(Charset.defaultCharset());
//        String beijichi = "";
//        byte[] byte_beijichi = beijichi.getBytes(Charset.defaultCharset());
//        String jiechidao = "";
//        byte[] byte_jiechidao = jiechidao.getBytes(Charset.defaultCharset());
//        byte[] byte_id = id.getBytes(Charset.defaultCharset());
//
//
//        int totalsize = 4 + 4 + 1 + byte_id.length + 4 + byte_network.length + byte_beijichi.length + byte_jiechidao.length + 2 * 4+199;
//
//
//
//        byte[] bytes_totallength = toLH(totalsize);
//        byte[] byte_baotou = toLH(5);
//
//        bb.put(bytes_totallength); //包长度
//        bb.put(byte_baotou);  //包头
//        bb.put(b);  //是否压缩
//
//
//        byte[] bytes1 = id.getBytes();
//        short idlength = Short.parseShort(id.getBytes().length + "");
//        bb.put(toLH2(idlength));
//        bb.put(bytes1);  //id
//
//        int SyscurrentMills = Integer.parseInt(String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000));
//        byte[] bytes_SyscurrentMills = toLH(SyscurrentMills);
//        bb.put(bytes_SyscurrentMills);  //时间戳
//
//        short netLength = Short.parseShort(byte_network.length + "");
//        bb.put(toLH2(netLength));
//        bb.put(byte_network);  //网络
//
//        short beijiechi = Short.parseShort(byte_beijichi.length + "");
//        bb.put(toLH2(beijiechi));
//        bb.put(byte_beijichi);
//
//        short yijiechi = Short.parseShort(byte_jiechidao.length + "");
//        bb.put(toLH2(yijiechi));
//        bb.put(byte_jiechidao);
        ByteBuffer bb = ByteBuffer.allocate(100);
        bb.order(ByteOrder.LITTLE_ENDIAN);
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
