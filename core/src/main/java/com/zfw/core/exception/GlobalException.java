package com.zfw.core.exception;

import com.zfw.core.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author:zfw
 * @Date:2019/7/11
 * @Content: 全局异常
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class GlobalException extends RuntimeException {
    private Constant constant;
    private Integer code;
    private String zhCode;
    private String enCode;

    public GlobalException(Constant constant) {
        this.constant = constant;
        this.code=constant.CODE;
        this.zhCode=constant.ZH_CODE;
        this.enCode=constant.EN_CODE;
    }

    public GlobalException(Integer code, String zhCode, String enCode) {
        this.code = code;
        this.zhCode = zhCode;
        this.enCode = enCode;
    }
    public GlobalException(String zhCode){
        this.zhCode=zhCode;
        this.code=Constant.FAIL_MSG.CODE;
        this.enCode=Constant.FAIL_MSG.EN_CODE;
    }

}
