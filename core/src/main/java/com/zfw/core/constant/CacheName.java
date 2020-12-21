package com.zfw.core.constant;

/**
 * @Author:zfw
 * @Date:2019/7/15
 * @Content: 缓存名
 */
public class CacheName {
    public static final String SUBJECT_NAME = "subject:";
    //缓存名，token
    public static final String AUTHORIZED_NAME = "authorized:";

    //缓存用户名
    public static final String USER_ID ="userId:";

    //token缓存时间
    public static final long AUTHORIZED_NAME_TIMEOUT = 60 * 60 * 24 * 7;

    //验证码缓存前缀
    public static final String CAPTCHA_PREFIX="Captcha:";

    //服务接口缓存名
    public static final String SERVICE_INTERFACE="service_interface";
}
