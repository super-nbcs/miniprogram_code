package com.zfw.wx.controller;

import com.alibaba.fastjson.JSONObject;
import com.zfw.core.annotation.UnAuthorized;
import com.zfw.core.sys.controller.BaseController;
import com.zfw.core.sys.entity.User;
import com.zfw.core.sys.service.IUserService;
import com.zfw.utils.HttpUtils;
import com.zfw.wx.entity.WxMiniUserInfo;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Optional;

/**
 * @Author:zfw
 * @Date:2020-12-17
 * @Content:
 */
@Api(tags = "微信controller")
@Controller
public class WxController extends BaseController {
    @Value("${wx.mini.code2Session_api}")
    private String code2SessionApi;
    @Autowired
    private IUserService iUserService;

    @UnAuthorized
    @GetMapping("wx/code/{code}")
    public JSONObject getOpenIdByCode(@PathVariable("code") String code) {
        String s = HttpUtils.get(String.format(code2SessionApi, code));
        JSONObject data = JSONObject.parseObject(s);
        String openid = data.getString("openid");
        User user = iUserService.findTop1ByMiniOpenId(openid);

        if (user == null) {
            user = new User();
        } else {
            return success(data);
        }
        user.setUserName(openid).setDeptId(3).setRoleId(2).setMiniOpenId(openid);
        iUserService.createUser(user);
        return success(data);
    }

    @UnAuthorized
    @PutMapping("wx/openId")
    public JSONObject updateWxUserByOpenId(WxMiniUserInfo wxMiniUserInfo) {
        System.out.println(wxMiniUserInfo);
        String openId = wxMiniUserInfo.getOpenId();
        if (StringUtils.isBlank(openId)) {
            return fail("openId 获取为null");
        }
        User user = iUserService.findTop1ByMiniOpenId(openId);
        user.setGender(wxMiniUserInfo.getGender()).setName(wxMiniUserInfo.getNickName());
        user.setPhoto(wxMiniUserInfo.getAvatarUrl());
        iUserService.save(user);
        return success();
    }

    @UnAuthorized
    @PostMapping("wx/login")
    public JSONObject wxMiniLogin(WxMiniUserInfo wxMiniUserInfo) {
        //如果openId为空,证明是第一次登录，就去拿code去换openId
        if (StringUtils.isBlank(wxMiniUserInfo.getOpenId())) {
            String s = HttpUtils.get(String.format(code2SessionApi, wxMiniUserInfo.getCode()));
            JSONObject data = JSONObject.parseObject(s);
            String openid = data.getString("openid");
            wxMiniUserInfo.setOpenId(openid);
        }

        String openId = wxMiniUserInfo.getOpenId();
        User user = iUserService.findTop1ByMiniOpenId(openId);
        user = Optional.ofNullable(user).orElse(new User().setGender(wxMiniUserInfo.getGender()).setName(wxMiniUserInfo.getNickName())
                .setPhoto(wxMiniUserInfo.getAvatarUrl()).setUserName(openId).setDeptId(3).setRoleId(2).setMiniOpenId(openId)
        );
        if (!iUserService.existsByMiniOpenId(openId)) {
            iUserService.createUser(user);
        } else {
            user.setGender(wxMiniUserInfo.getGender()).setName(wxMiniUserInfo.getNickName());
            user.setPhoto(wxMiniUserInfo.getAvatarUrl());
            iUserService.save(user);
        }
        return success(wxMiniUserInfo);
    }

}
