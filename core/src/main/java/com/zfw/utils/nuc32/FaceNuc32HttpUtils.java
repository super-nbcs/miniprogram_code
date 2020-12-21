package com.zfw.utils.nuc32;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class FaceNuc32HttpUtils {


    public static Logger logger = LoggerFactory.getLogger(FaceNuc32HttpUtils.class);

    private static char[] MULTIPART_CHARS = ("-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
    public final static String GET = "GET";
    public final static String POST = "POST";
    public final static String DELETE = "DELETE";
    public final static String PUT = "PUT";


    private static int connectTimeout = 2000;
    private static int readTimeout = 3000;

    private static String contentTypeKey = "Content-Type";
    private static String contentTypeValue = "application/json;charset=utf-8";

    /**
     * Authorization存放map
     * key：ip+账号
     * value：Authorization=？
     */
    private static Map<String,String> authorizationMap;

    private static String NEWLINE = "\r\n";
    private static String PREFIX = "--";
    private static String BOUNDARY = null;

    static {
        FaceNuc32HttpUtils.authorizationMap=new HashMap<>();
    }

    /**
     * 设置连接超时时间  不能小于0
     *
     * @param connectTimeout
     */
    public static void setConnectTimeout(int connectTimeout) {
        if (connectTimeout < 0) {
            throw new IllegalArgumentException("连接超时时间不能为负数");
        }
        FaceNuc32HttpUtils.connectTimeout = connectTimeout;
    }

    public static void setReadTimeout(int readTimeout) {
        if (readTimeout < 0) {
            throw new IllegalArgumentException("等待超时时间不能为负数");
        }
        FaceNuc32HttpUtils.readTimeout = readTimeout;
    }

    public static void setContentTypeValue(String contentTypeValue) {
        FaceNuc32HttpUtils.contentTypeValue = contentTypeValue;
    }


    /**
     * @param url
     * @param listener
     */
    public static void get(String url,String username,String password, FaceHttpCallbackListener listener) {
        request(url, GET, null,username,password, listener);
    }

    public static JSONObject get(String url, String username, String password) {
        return request(url, GET, null,username,password, null);
    }

    public static void post(String url, Map<String, Object> params,String username,String password, FaceHttpCallbackListener listener) {
        request(url, POST, params,username,password, listener);
    }

    public static JSONObject post(String url, Map<String, Object> params, String username, String password) {
        return request(url, POST, params,username,password, null);
    }

    public static void put(String url, Map<String, Object> params,String username,String password, FaceHttpCallbackListener listener) {
        request(url, PUT, params,username,password, listener);
    }

    public static JSONObject put(String url, Map<String, Object> params, String username, String password) {
        return request(url, PUT, params,username,password, null);
    }

    public static void delete(String url, Map<String, Object> params,String username,String password, FaceHttpCallbackListener listener) {
        request(url, DELETE, params,username,password, listener);
    }

    public static JSONObject delete(String url, Map<String, Object> params, String username, String password) {
        return request(url, DELETE, params,username,password, null);
    }

    public static JSONObject deleteByJson(String url, String json, String username, String password) {
        return requestJson(url, DELETE, json,username,password, null);
    }

    public static void post(String url, Map<String, Object> params, Map<String, File> fileMap,String username,String password, FaceHttpCallbackListener listener) {
        request(url, params, fileMap,username,password, listener);
    }


    public static JSONObject post(String url, Map<String, Object> params, Map<String, File> fileMap, String username, String password) {
        return request(url, params, fileMap,username,password, null);
    }

    public static JSONObject request(String url, Map<String, Object> params, Map<String, File> fileMap, String username, String password, FaceHttpCallbackListener listener) {
        BOUNDARY = generateMultipartBoundary();
        HttpURLConnection connection = null;
        try {
            URL httpUrl=new URL(url);
            connection = (HttpURLConnection) httpUrl.openConnection();
            connection.setRequestMethod(POST);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty(contentTypeKey, "multipart/form-data; boundary=" + BOUNDARY);

            String host = httpUrl.getHost();
            if (FaceNuc32HttpUtils.authorizationMap != null && !FaceNuc32HttpUtils.authorizationMap.isEmpty()&& FaceNuc32HttpUtils.authorizationMap.containsKey(host+"_"+username)) {
                String value = FaceNuc32HttpUtils.authorizationMap.get(host+"_"+username);
                if (StringUtils.isBlank(value)) {
                    if (listener != null) {
                        nuc31AuthToken(url,username,password);
                        request(url, params,fileMap,username,password, listener);
                    } else {
                        nuc31AuthToken(url,username,password);
                        return request(url, params,fileMap,username,password, null);
                    }
                }
                String[] s = value.split("=");
                connection.setRequestProperty(s[0], s[1]);
            }
            connection.connect();

            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            if (params != null && !fileMap.isEmpty()) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (ObjectUtils.isEmpty(entry.getValue())) {
                        continue;
                    }
                    dos.writeBytes(PREFIX + BOUNDARY + NEWLINE);
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + NEWLINE);
                    dos.writeBytes(NEWLINE);
                    String string=(String)entry.getValue();
                    dos.write(string.getBytes());
                    dos.writeBytes(NEWLINE);
                }
            }

            if (fileMap != null && !fileMap.isEmpty()) {
                for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                    dos.writeBytes(PREFIX + BOUNDARY + NEWLINE);
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"; filename=\"" + entry.getValue().getName() + "\"" + NEWLINE);
                    dos.writeBytes("Content-Type: image/jpeg");
                    dos.writeBytes(NEWLINE);
                    dos.writeBytes(NEWLINE);
                    dos.write(File2byte(entry.getValue()));
                    dos.writeBytes(NEWLINE);
                }
            }
            dos.writeBytes(PREFIX + BOUNDARY + PREFIX + NEWLINE);
            dos.flush();

            InputStream in = null;
            int responseCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode)
                in = connection.getInputStream();
            else
                in = connection.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = JSON.parseObject(response.toString());
                if (listener != null) {
                    if (jsonObject.getInteger("code")!=null&&Math.abs(jsonObject.getInteger("code")) == 1014) {
                        nuc31AuthToken(url,username,password);
                        request(url, params,fileMap,username,password, listener);
                    }
                    listener.onFinish(jsonObject);
                } else {
                    if (jsonObject.getInteger("code")!=null&&Math.abs(jsonObject.getInteger("code")) == 1014) {
                        nuc31AuthToken(url,username,password);
                        return request(url, params,fileMap,username,password, null);
                    }
                    return jsonObject;
                }
            } catch (JSONException exception) {
                if (listener != null) {
                    nuc31AuthToken(url,username,password);
                    request(url, params,fileMap,username,password, listener);
                } else {
                    nuc31AuthToken(url,username,password);
                    return request(url, params,fileMap,username,password, null);
                }
            }

        } catch (IOException e) {
            if (listener != null) {
                listener.onError(e);
            } else {
                e.printStackTrace();
            }
        }
        return null;
    }



    public static JSONObject request(String url, String method, Map<String, Object> params, String username, String password, FaceHttpCallbackListener listener) {
        HttpURLConnection connection = null;
        try {
            URL httpUrl=new URL(url);
            connection = (HttpURLConnection) httpUrl.openConnection();
            connection.setRequestMethod(method.toUpperCase());
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);
            connection.setDoInput(true);
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            connection.setDoOutput(true);

            if (method.toUpperCase().equals(DELETE)) {
                connection.setRequestProperty(contentTypeKey, "application/x-www-form-urlencoded");
            }

            if(!method.toUpperCase().equals(GET)) {
                connection.setRequestProperty(contentTypeKey, contentTypeValue);
            }
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Koala Admin");
            String host = httpUrl.getHost();
            if (FaceNuc32HttpUtils.authorizationMap != null && !FaceNuc32HttpUtils.authorizationMap.isEmpty()&& FaceNuc32HttpUtils.authorizationMap.containsKey(host+"_"+username)) {
                String value = FaceNuc32HttpUtils.authorizationMap.get(host+"_"+username);
                    if (StringUtils.isBlank(value)) {
                        if (listener != null) {
                            nuc31AuthToken(url,username,password);
                            request(url, method,params,username,password, listener);
                        } else {
                            nuc31AuthToken(url,username,password);
                            return request(url,method, params,username,password,null);
                        }
                    }
                    String[] s = value.split("=");
                    connection.setRequestProperty(s[0], s[1]);
            }
            connection.connect();

            if (method.toUpperCase().equals(POST)||method.toUpperCase().equals(PUT)||method.toUpperCase().equals(DELETE)) {
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                String s = JSON.toJSONString(params);
                dos.write(s.getBytes());
                dos.flush();
                dos.close();
            }
            InputStream in = null;
            int responseCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode)
                in = connection.getInputStream();
            else
                in = connection.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            if (response.toString().contains("404 Not Found")) {
                throw new RuntimeException("页面未找到");
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = JSON.parseObject(response.toString());
                if (listener != null) {
                    if (Math.abs(jsonObject.getInteger("code")) == 1014) {
                        nuc31AuthToken(url,username,password);
                        request(url, method, params,username,password, listener);
                    }
                    listener.onFinish(jsonObject);
                } else {
                    if (Math.abs(jsonObject.getInteger("code")) == 1014) {
                        nuc31AuthToken(url,username,password);
                        return request(url, method, params,username,password, null);
                    }
                    return jsonObject;
                }
            } catch (JSONException exception) {
                if (listener != null) {
                    nuc31AuthToken(url,username,password);
                    request(url, method, params,username,password, listener);
                } else {
                    nuc31AuthToken(url,username,password);
                    return request(url, method, params,username,password, null);
                }
            }

        } catch (IOException e) {
            if (listener != null) {
                listener.onError(e);
            } else {
                e.printStackTrace();
                return null;
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }


    public static JSONObject requestJson(String url, String method, String json, String username, String password, FaceHttpCallbackListener listener) {
        HttpURLConnection connection = null;
        try {
            URL httpUrl=new URL(url);
            connection = (HttpURLConnection) httpUrl.openConnection();
            connection.setRequestMethod(method.toUpperCase());
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);
            connection.setDoInput(true);
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            connection.setDoOutput(true);

            if (method.toUpperCase().equals(DELETE)) {
                connection.setRequestProperty(contentTypeKey, "application/x-www-form-urlencoded");
            }

            if(!method.toUpperCase().equals(GET)) {
                connection.setRequestProperty(contentTypeKey, contentTypeValue);
            }
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Koala Admin");
            String host = httpUrl.getHost();
            if (FaceNuc32HttpUtils.authorizationMap != null && !FaceNuc32HttpUtils.authorizationMap.isEmpty()&& FaceNuc32HttpUtils.authorizationMap.containsKey(host+"_"+username)) {
                String value = FaceNuc32HttpUtils.authorizationMap.get(host+"_"+username);
                if (StringUtils.isBlank(value)) {
                    if (listener != null) {
                        nuc31AuthToken(url,username,password);
                        requestJson(url, method,json,username,password, listener);
                    } else {
                        nuc31AuthToken(url,username,password);
                        return requestJson(url,method, json,username,password,null);
                    }
                }
                String[] s = value.split("=");
                connection.setRequestProperty(s[0], s[1]);
            }
            connection.connect();

            if (method.toUpperCase().equals(POST)||method.toUpperCase().equals(PUT)||method.toUpperCase().equals(DELETE)) {
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                dos.write(json.getBytes());
                dos.flush();
                dos.close();
            }
            InputStream in = null;
            int responseCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode)
                in = connection.getInputStream();
            else
                in = connection.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = JSON.parseObject(response.toString());
                if (listener != null) {
                    if (Math.abs(jsonObject.getInteger("code")) == 1014) {
                        nuc31AuthToken(url,username,password);
                        requestJson(url, method, json,username,password, listener);
                    }
                    listener.onFinish(jsonObject);
                } else {
                    if (Math.abs(jsonObject.getInteger("code")) == 1014) {
                        nuc31AuthToken(url,username,password);
                        return requestJson(url, method, json,username,password, null);
                    }
                    return jsonObject;
                }
            } catch (JSONException exception) {
                if (listener != null) {
                    nuc31AuthToken(url,username,password);
                    requestJson(url, method, json,username,password, listener);
                } else {
                    nuc31AuthToken(url,username,password);
                    return requestJson(url, method, json,username,password, null);
                }
            }

        } catch (IOException e) {
            if (listener != null) {
                listener.onError(e);
            } else {
                e.printStackTrace();
                return null;
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }



    private static String generateMultipartBoundary() {
        Random rand = new Random();
        char[] chars = new char[rand.nextInt(9) + 12]; // 随机长度(12 - 20个字符)
        for (int i = 0; i < chars.length; i++) {
            chars[i] = MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)];
        }
        return "----WebKitFormBoundary" + new String(chars);
    }


    public static byte[] File2byte(File tradeFile) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    public static void nuc31AuthToken(String requestAddr,String username,String password) throws MalformedURLException {
        URL url = new URL(requestAddr);
        String host = url.getHost();
        HashMap<String, Object> params = new HashMap<>();
//        logger.info(host + "主机无权限，执行登录");
        params.put("username", username);
        params.put("password", password);
        params.put("auth_token", "true");
        String loginUrl = "http://" + host + NucInterface.KOALA_1_1.PATH;
        JSONObject post = post(loginUrl, params,username,password);
        if (post.getInteger("code") != 0) {
            logger.error(host+"-"+username+"-"+password +"-"+ post.getString("desc"));
        }
        if(authorizationMap.containsKey(host+"_"+username)){
            authorizationMap.remove(host+"_"+username);
        }
        authorizationMap.put(host+"_"+username, "Authorization=" + post.getJSONObject("data").getString("auth_token"));
    }

}
