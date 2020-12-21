package com.zfw.utils;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.*;

/**
 * @Author:zfw
 * @Date:2020-11-09
 * @Content:
 */
public class GZipStrUtils {
    public static final String GZIP_ENCODE_UTF_8 = "UTF-8";


    public static ByteArrayOutputStream compress(String str, String encoding) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    public static byte[] compress(String str) {
        ByteArrayOutputStream out = compress(str, GZIP_ENCODE_UTF_8);
        return out.toByteArray();
    }

    public static ByteArrayOutputStream uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    public static String unCompressToString(byte[] bytes, String encoding) {
        ByteArrayOutputStream out = uncompress(bytes);
        try {
            return out.toString(encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String unCompressToString(byte[] bytes) {
        return unCompressToString(bytes, GZIP_ENCODE_UTF_8);
    }

    public static void main(String[] args) throws IOException {
        String s = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        System.out.println("字符串长度："+s.length());
        System.out.println("压缩后：："+compress(s).length);
        System.out.println("解压后："+unCompressToString(compress(s)).length());
        System.out.println("解压字符串后：："+ unCompressToString(compress(s)).length());
    }
}
