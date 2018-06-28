package com.example.agc.aigoucai.util;

/**
 * Created by Administrator on 2018/3/20 0020.
 */

public class FormatTransfer {
    /**
     * 将int转为低字节在前，高字节在后的byte数组
     *
     * @param n int
     * @return byte[]
     */
    public static byte[] toLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * 将int转为高字节在前，低字节在后的byte数组
     *
     * @param n int
     * @return byte[]
     */
    public static byte[] toHH(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * 将short转为低字节在前，高字节在后的byte数组
     *
     * @param n short
     * @return byte[]
     */
    public static byte[] toLH(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    /**
     * 将short转为高字节在前，低字节在后的byte数组
     *
     * @param n short
     * @return byte[]
     */
    public static byte[] toHH(short n) {
        byte[] b = new byte[2];
        b[1] = (byte) (n & 0xff);
        b[0] = (byte) (n >> 8 & 0xff);
        return b;
    }


/**
 * 将将int转为高字节在前，低字节在后的byte数组

 public static byte[] toHH(int number) {
 int temp = number;
 byte[] b = new byte[4];
 for (int i = b.length - 1; i > -1; i--) {
 b = new Integer(temp & 0xff).byteValue();
 temp = temp >> 8;
 }
 return b;
 }

 public static byte[] IntToByteArray(int i) {
 byte[] abyte0 = new byte[4];
 abyte0[3] = (byte) (0xff & i);
 abyte0[2] = (byte) ((0xff00 & i) >> 8);
 abyte0[1] = (byte) ((0xff0000 & i) >> 16);
 abyte0[0] = (byte) ((0xff000000 & i) >> 24);
 return abyte0;
 }


 */

    /**
     * 将float转为低字节在前，高字节在后的byte数组
     */
    public static byte[] toLH(float f) {
        return toLH(Float.floatToRawIntBits(f));
    }

    /**
     * 将float转为高字节在前，低字节在后的byte数组
     */
    public static byte[] toHH(float f) {
        return toHH(Float.floatToRawIntBits(f));
    }

    /**
     * 将String转为byte数组
     */
    public static byte[] stringToBytes(String s, int length) {
        while (s.getBytes().length < length) {
            s += " ";
        }
        return s.getBytes();
    }


    /**
     * 将字符串转换为byte数组
     *
     * @param s String
     * @return byte[]
     */
    public static byte[] stringToBytes(String s) {
        return s.getBytes();
    }

//    /**
//     * 将高字节数组转换为int
//     * @param b byte[]
//     * @return int
//     */
//    public static int hBytesToInt(byte[] b) {
//        int s = 0;
//        for (int i = 0; i < 3; i++) {
//            if (b >= 0) {
//                s = s + b;
//            } else {
//                s = s + 256 + b;
//            }
//            s = s * 256;
//        }
//        if (b[3] >= 0) {
//            s = s + b[3];
//        } else {
//            s = s + 256 + b[3];
//        }
//        return s;
//    }

    /**
     * 将低字节数组转换为int
     *
     * @param b byte[]
     * @return int
     */
    public static int lBytesToInt(byte[] b) {
        int s = 0;
        for (int i = 0; i < 3; i++) {
            if (b[3 - i] >= 0) {
                s = s + b[3 - i];
            } else {
                s = s + 256 + b[3 - i];
            }
            s = s * 256;
        }
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        return s;
    }


    /**
     * 高字节数组到short的转换
     *
     * @param b byte[]
     * @return short
     */
    public static short hBytesToShort(byte[] b) {
        int s = 0;
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        s = s * 256;
        if (b[1] >= 0) {
            s = s + b[1];
        } else {
            s = s + 256 + b[1];
        }
        short result = (short) s;
        return result;
    }

    /**
     * 低字节数组到short的转换
     *
     * @param b byte[]
     * @return short
     */
    public static short lBytesToShort(byte[] b) {
        int s = 0;
        if (b[1] >= 0) {
            s = s + b[1];
        } else {
            s = s + 256 + b[1];
        }
        s = s * 256;
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        short result = (short) s;
        return result;
    }

    /**
     * 高字节数组转换为float
     *
     * @param b byte[]
     * @return float
     */
    public static float hBytesToFloat(byte[] b) {
        int i = 0;
        Float F = new Float(0.0);
        i = ((((b[0] & 0xff) << 8 | (b[1] & 0xff)) << 8) | (b[2] & 0xff)) << 8 | (b[3] & 0xff);
        return F.intBitsToFloat(i);
    }

    /**
     * 低字节数组转换为float
     *
     * @param b byte[]
     * @return float
     */
    public static float lBytesToFloat(byte[] b) {
        int i = 0;
        Float F = new Float(0.0);
        i = ((((b[3] & 0xff) << 8 | (b[2] & 0xff)) << 8) | (b[1] & 0xff)) << 8 | (b[0] & 0xff);
        return F.intBitsToFloat(i);
    }

//    /**
//     * 将byte数组中的元素倒序排列
//     */
//    public static byte[] bytesReverseOrder(byte[] b) {
//        int length = b.length;
//        byte[] result = new byte[length];
//        for(int i=0; i<length; i++) {
//            result[length-i-1] = b;
//        }
//        return result;
//    }

    /**
     * 打印byte数组
     */
    public static void printBytes(byte[] bb) {
        int length = bb.length;
        for (int i = 0; i < length; i++) {
            System.out.print(bb + " ");
        }
        System.out.println("");
    }

//    public static void logBytes(byte[] bb) {
//        int length = bb.length;
//        String ut = "";
//        for (int i=0; i<length; i++) {
//            ut = out + bb + " ";
//        }
//
//    }

    /**
     * 将int类型的值转换为字节序颠倒过来对应的int值
     * @param i int
     * @return int
     */
//    public static int reverseInt(int i) {
//        int result = FormatTransfer.hBytesToInt(FormatTransfer.toLH(i));
//        return result;
//    }

    /**
     * 将short类型的值转换为字节序颠倒过来对应的short值
     *
     * @param s short
     * @return short
     */
    public static short reverseShort(short s) {
        short result = FormatTransfer.hBytesToShort(FormatTransfer.toLH(s));
        return result;
    }

    /**
     * 将float类型的值转换为字节序颠倒过来对应的float值
     *
     * @param f float
     * @return float
     */
    public static float reverseFloat(float f) {
        float result = FormatTransfer.hBytesToFloat(FormatTransfer.toLH(f));
        return result;
    }

    /**
     * 字节数组转换为十六进制字符串
     *
     * @param b
     *            byte[] 需要转换的字节数组
     * @return String 十六进制字符串
     */
    public static final String byte2hex(byte b[]) {
        if (b == null) {
            throw new IllegalArgumentException(
                    "Argument b ( byte array ) is null! ");
        }
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
    /**
     * 16进制转换成为string类型字符串
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     * @param hexStr
     * @return
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    }
    //     转化十六进制编码为字符串
    public static String toStringHex(String s)
    {
        byte[] baKeyword = new byte[s.length()/2];
        for(int i = 0; i < baKeyword.length; i++)
        {
            try
            {
                baKeyword[i] = (byte)(0xff & Integer.parseInt(s.substring(i*2, i*2+2),16));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            s = new String(baKeyword, "utf-8");//UTF-16le:Not
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * 字节数组转十六进制
     *
     * @param bytes
     * @param begin
     * @param end
     * @return
     */
    public static String bytesToHex(byte[] bytes, int begin, int end) {
        StringBuilder hexBuilder = new StringBuilder(2 * (end - begin));
        for (int i = begin; i < end; i++) {
            hexBuilder.append(Character.forDigit((bytes[i] & 0xF0) >> 4, 16)); // 转化高四位
            hexBuilder.append(Character.forDigit((bytes[i] & 0x0F), 16)); // 转化低四位
//            hexBuilder.append(' '); // 加一个空格将每个字节分隔开
        }
        return hexBuilder.toString().toUpperCase();
    }
    /**
     * 16进制字符串转换成字节数组
     *
     * @param hex
     * @return
     */
    public static byte[] hexStringToByte(String hex) {
        byte[] b = new byte[hex.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hex.charAt(j++);
            char c1 = hex.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }
    private static int parse(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }
}
