package com.zfw.core.annotation.cache;

import java.lang.annotation.*;

/**
 * @Author:zfw
 * @Date:2020/8/5
 * @Content:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheAble {
    /**
     * 是否缓存空值
     * @return
     */
    boolean cacheNull() default false;

    /**
     * 秒 -1永不失效
     * @return
     */
    int ttl() default 60*60*24;

    /**
     * 再次请求是否刷新缓存
     * @return
     */
    boolean state() default true;
}
