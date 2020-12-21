package com.zfw.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author:zfw
 * @Date:2020/7/16
 * @Content: 后端防止表单重复提交
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubmitCommit {
    /**
     *  默认多少秒不能重复提交
     */
    long timeout()  default 3000 ;

}
