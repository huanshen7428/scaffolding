package com.huanshen.scaffolding.common.utils;


import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;

/**
 * <Description> ByteUtil<br>
 * AAA - byte工具类
 *
 */
public abstract class ByteUtil {

    /**
     * 格式化输出十六进制字符
     */
    private static char[] digit = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * 生成十六进制字符串
     *
     * @param ib 字节
     * @return 十六进制字符串
     */
    public static String toHexStr(byte ib) {
        char[] ob = new char[2];
        ob[0] = digit[(ib >>> 4) & 0x0f];
        ob[1] = digit[ib & 0x0f];
        return new String(ob);
    }

    /**
     * 生成十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    public static String toHexStr(byte[] bytes) {
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            r.append(toHexStr(bytes[i]));
        }
        return r.toString();
    }

    /**
     * 生成十六进制字符串
     *
     * @param bytes 字节数组
     * @param len   换行
     * @return 十六进制字符串
     */
    public static String toHexStr(byte[] bytes, int len) {
        StringBuilder r = new StringBuilder();
        if (bytes != null) {
            for (int i = 0; i < bytes.length; i++) {
                r.append(toHexStr(bytes[i]));
                if ((i + 1) != bytes.length) {
                    if ((i + 1) % len == 0) {
                        r.append("\n");
                    } else {
                        r.append(" ");
                    }
                }
            }
        }
        return r.toString();
    }


    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 字节转整型
     *
     * @param buff 字节数组
     * @param pos  位置
     * @return 整型
     */
    public static int getInt(byte[] buff, int pos) {
        return buff[pos] & 0x0ff;
    }

    /**
     * 字节转整型 转换为有符号数
     *
     * @param bytes 字节数组
     * @return 整型
     */
    public static int byteToInt(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
    }

    /**
     * 字节转整型 转换为无符号数
     *
     * @param bytes 字节数组
     * @return 整型
     */
    public static long byteTouUnsignedInt(byte[] bytes) {
        return ((long) byteToInt(bytes)) & 0xffffffffL;
    }

    /**
     * 将int数值转换为占四个字节的byte数组
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytes(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }



    /**
     * 截取字节数组
     *
     * @param buff 字节数组
     * @param pos  起始位置
     * @param len  长度
     * @return 字节数组
     */
    public static byte[] getBytes(byte[] buff, int pos, int len) {
        byte[] by = new byte[len];
        System.arraycopy(buff, pos, by, 0, len);
        return by;
    }

    /**
     * Description: getRandomBytes<br>
     * 获取X位随机字节数组
     *
     */
    public static byte[] getRandomBytes(int length) {
        Random random = new Random();
        byte[] byteArr = new byte[length];
        random.nextBytes(byteArr);

        return byteArr;
    }

    /**
     * Description: mergeByteArray<br>
     * 合并多个byte数组，返回byte数组
     *
     */
    public static byte[] mergeByteArray(List<byte[]> list) {
        int length = 0;
        for (byte[] srcArr : list) {
            length += srcArr.length;

        }

        byte[] destArray = new byte[length];
        int destLength = 0;

        for (byte[] srcArray : list) {
            System.arraycopy(srcArray, 0, destArray, destLength, srcArray.length);
            destLength += srcArray.length;
        }

        return destArray;
    }

    /**
     * Description: getNewByteArray<br>
     * 将源数组拷贝到长度为X的新字节数组中
     * 源数组长度不足X，以字节[0]补
     * 源数组长度超过X，截断
     *
     * @param srcArr byte[]
     * @param length int
     * @return byte[]
     */
    public static byte[] getNewByteArray(byte[] srcArr, int length) {
        if (0 < length) {
            byte[] newArr = new byte[length];
            if (srcArr.length <= length) {
                System.arraycopy(srcArr, 0, newArr, 0, srcArr.length);
            } else {
                System.arraycopy(srcArr, 0, newArr, 0, length);
            }

            return newArr;
        }

        return new byte[0];
    }

    /**
     * 十六进制字符串转换成字节数组
     *
     * @param hex 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hex2bytes(String hex) {
        if (null == hex || 0 == hex.trim().length()) {
            return null;
        }

        byte[] dest = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length() / 2; i++) {
            String h = hex.substring(2 * i, 2 * i + 2);
            dest[i] = hex2byte(h);
        }

        return dest;
    }

    /**
     * 十六进制字符串转换成字节
     *
     * @param hex 十六进制字符串
     * @return 字节
     */
    public static byte hex2byte(String hex) {
        return (byte) (Integer.parseInt(hex, 16) & 0xff);
    }

}
