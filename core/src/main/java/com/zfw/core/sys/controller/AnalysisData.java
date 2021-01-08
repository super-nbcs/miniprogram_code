package com.zfw.core.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.zfw.core.constant.Constant;
import com.zfw.core.exception.GlobalException;
import com.zfw.core.sys.common.Assert;
import com.zfw.utils.ReturnUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @Author:zfw
 * @Date:2020-11-06
 * @Content: 解析返回数据
 */
public interface AnalysisData extends Assert {
    default JSONObject success() {
        return success(null);
    }

    default JSONObject success(Object data) {
        return success(Constant.SUCCESS, data);
    }

    default JSONObject success(Constant constant, Object data) {
        return ReturnUtils.success(constant, data);
    }


    default JSONObject fail() {
        return fail(null);
    }

    default JSONObject fail(Object data) {
        return ReturnUtils.fail(data);
    }

}
