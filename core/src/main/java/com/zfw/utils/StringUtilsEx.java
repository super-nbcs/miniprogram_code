package com.zfw.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 扩展String工具类
 */
public class StringUtilsEx extends StringUtils {

    /**
     * 获取UUID
     *
     * @return d3acf124-5587-49a0-ada0-6f3726ee717a
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }


    /**
     * 获取过滤横杠后UUID
     *
     * @return d3acf124558749a0ada06f3726ee717a
     */
    public static String getUUID2() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成指定位数字符串0
     *
     * @param key 5
     * @return 00000
     */
    public static String someZero(int key) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < key; i++) {
            sb.append("0");
        }
        return sb.toString();
    }

    /**
     * 十进制转换16进制
     *
     * @param length
     * @return
     */
    public static String changebytelength(int length) {
        String tentosix = Integer.toHexString(length);
        if (tentosix.length() > 7)
            return tentosix;
        else
            return someZero(8 - tentosix.length()) + tentosix;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、 换行符组成的字符串
     * 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return
     */
    public static boolean hasEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static String getMD5(String str) {
        return DigestUtils.md5Hex(str);
    }

    public static String getMD5(String str, Integer size) {
        if (size == null || size == 0) {
            return getMD5(str);
        }
        for (int i = 0; i < size; i++) {
            str = getMD5(str);
        }
        return str;
    }

    public static Integer toInt(Object obj, Integer defaultVal) {
        if (obj == null)
            return defaultVal;
        if (isEmpty(obj.toString()))
            return defaultVal;
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    /**
     * 将String n位一截取，返回数组
     *
     * @param str  "243234"
     * @param size 3
     * @return ["243","234"]
     */
    public static String[] substrings(String str, int size) {
        if (isBlank(str)) {
            throw new RuntimeException("str 位数不能为null或空");
        }
        if (size <= 0) {
            throw new RuntimeException("size 位数不能小于0");
        }
        int length = str.length() % size == 0 ? str.length() / size : str.length() / size + 1;
        String[] array = new String[length];
        for (int i = 0; i < array.length; i++) {
            array[i] = substring(str, i * size, i * size + size);
        }
        return array;
    }


    /**
     * 从指定字符串中获取ip 如果没有返回""
     * @param str rtsp://192.168.1.15/user=admin&password=&channel=1&stream=0.sdp
     * @return 192.168.1.15
     */
    public static String getIp(String str) {
        if (str==null){
            return "";
        }
        String ip = "";
        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
        Matcher matcher = p.matcher(str);
        if (matcher.find())
            ip = matcher.group();
        return ip;
    }

    /**
     * 从字符中获取包含的数字，没有返回0
     * @param str
     * @return
     */
    public static int getInt(String str){
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String trim = m.replaceAll("").trim();
        if (StringUtils.isNotBlank(trim)){
            return Integer.valueOf(trim);
        }else {
            return 0;
        }
    }

}
