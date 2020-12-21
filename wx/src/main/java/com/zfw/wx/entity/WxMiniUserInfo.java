package com.zfw.wx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author:zfw
 * @Date:2020-12-18
 * @Content: 接收微信小程序用户信息
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class WxMiniUserInfo {
    private String nickName;
    private Integer gender;
    private String language;
    private String city;
    private String province;
    private String country;
    private String avatarUrl;
    private String openId;
    private String code;
}
