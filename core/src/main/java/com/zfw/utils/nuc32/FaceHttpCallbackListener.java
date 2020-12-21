package com.zfw.utils.nuc32;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zfw.
 *
 *  用于HttpURLConnectionUtil获取数据后回调更新主线程UI
 */

public interface FaceHttpCallbackListener {
    void onFinish(JSONObject response);
    default void onError(Exception e){
        e.printStackTrace();
    }
}
