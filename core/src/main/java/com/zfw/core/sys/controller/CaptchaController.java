package com.zfw.core.sys.controller;

import com.zfw.core.annotation.UnAuthorized;
import com.zfw.core.constant.CacheName;
import com.zfw.core.constant.Constant;
import com.zfw.core.exception.GlobalException;
import com.zfw.utils.CaptchaUtils;
import com.zfw.utils.RedisUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * @Author:zfw
 * @Date:2019/7/16
 * @Content:
 */
@Api(tags = "验证码接口,不需要鉴权")
@Controller
public class CaptchaController extends BaseController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private CaptchaUtils captchaUtils;

    @Autowired
    private RedisUtils redisUtils;


    @UnAuthorized
    @GetMapping("/captcha")
    public void generateCaptcha(String key) {
        if (key==null){
            throw new GlobalException(Constant.FAIL);
        }
        Map<String, BufferedImage> map = captchaUtils.getImageCaptcha();
        response.setContentType("image/jpeg");
        map.forEach((k,v)->{
            try {
                ImageIO.write(v,"jpg",response.getOutputStream());
                redisUtils.set(String.format("%s%s", CacheName.CAPTCHA_PREFIX,key),k,300);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

}
