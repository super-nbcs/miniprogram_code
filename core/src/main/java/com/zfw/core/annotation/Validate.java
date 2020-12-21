package com.zfw.core.annotation;

import java.lang.annotation.*;

/**
 * @Author:zfw
 * @Date:2019/7/25
 * @Content:    配合 {@link ParamValidate} 使用，只能controller注解到方法上，使 {@link ParamValidate} 注解生效
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validate {
}
