package com.zfw.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMsgVo {

    private String name;

    private String base64;

    private String deviceName;

    private String time;

    private String remark;

}
