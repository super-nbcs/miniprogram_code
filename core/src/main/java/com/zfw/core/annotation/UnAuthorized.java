package com.zfw.core.annotation;

import java.lang.annotation.*;

/**
 * 加此注解的方法，不需要验证授权。用到controller的方法上
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UnAuthorized {
}
