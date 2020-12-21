package com.zfw.core.annotation;

import java.lang.annotation.*;

/**
 * @Author:zfw
 * @Date:2019/8/6
 * @Content:  权限注解 作用到controller方法上
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authorized {
    /**
     * 数组,对应角色表中的code
     * @return
     */
    String[] code();
}
