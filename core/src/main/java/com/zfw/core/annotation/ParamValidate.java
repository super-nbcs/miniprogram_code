package com.zfw.core.annotation;

import java.lang.annotation.*;

/**
 * @Author:zfw
 * @Date:2019/7/10
 * @Content: 自定义注解，只能加到属性上，验证 Controller 入参，加此注解入参默认不能为null
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamValidate {

    /**
     * 数组：[0] min   [1] max
     * @return
     */
    int[] length() default {};

    /**
     * 正则
     * @return
     */
    String regular() default "";

    /**
     * 数组： 错误提示信息 [0] length    [1] regular
     * @return
     */
    String[] msg() default {"",""};

}
