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
public @interface CacheDisable {
}
