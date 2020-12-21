package com.zfw.core.constant;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author:zfw
 * @Date:2019/7/11
 * @Content:
 */
@Data
@AllArgsConstructor
public class ReturnCode {

    private Integer code;
    private String zhCode;
    private String enCode;

}
