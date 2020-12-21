package com.zfw.utils;

import java.io.*;
import java.net.*;

/**
 * @Author:zfw
 * @Date:2020/5/27
 * @Content:
 */
public class NetUtils {
    public static boolean isHostConnectable(String host, int port) {
        if (!isHostReachable(host,1000)){
            return false;
        }

        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    public static boolean isHostReachable(String host, Integer timeOut) {
        try {
            return InetAddress.getByName(host).isReachable(timeOut);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从fastdfs中下载文件，此处为了网速快，写死地址：127.0.0.1
     * @param path
     * @return
     */
    public static byte[] downloadFromDfs(String path){
        try {
            String url="http://127.0.0.1:8888/"+path;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len = 1024;
                byte tmp [] = new byte[len];
                int i ;
                while((i=inputStream.read(tmp, 0, len))>0){
                    baos.write(tmp, 0, i);
                }
                byte imgs[] = baos.toByteArray();
                return imgs;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static File getFileFromBytes(byte[] b, String outputFile) {

        BufferedOutputStream stream = null;

        File file = null;

        try {

            file = new File(outputFile);

            FileOutputStream fstream = new FileOutputStream(file);

            stream = new BufferedOutputStream(fstream);

            stream.write(b);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (stream != null) {

                try {

                    stream.close();

                } catch (IOException e1) {

                    e1.printStackTrace();

                }

            }

        }

        return file;

    }
}
