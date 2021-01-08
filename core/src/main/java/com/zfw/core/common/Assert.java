package com.zfw.core.common;

import com.zfw.core.exception.GlobalException;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @Author:zfw
 * @Date:2021-01-08
 * @Content: 断言接口，用于service,dao,controller实现，并进行断言
 */
public interface Assert {
    /**
     * 断言对象是否为null
     * @param o
     * @param msg
     * @return
     */
    default Assert assertBlank(Object o, String msg) {
        Optional.ofNullable(o).orElseThrow(() -> new GlobalException(msg));
        if (o instanceof String && StringUtils.isBlank((CharSequence) o)) throw new GlobalException(msg);
        return this;
    }
}
