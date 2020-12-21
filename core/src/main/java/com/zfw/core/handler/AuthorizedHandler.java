package com.zfw.core.handler;

import com.zfw.utils.StringUtilsEx;
import com.zfw.core.annotation.UnAuthorized;
import com.zfw.core.constant.CacheName;
import com.zfw.core.exception.GlobalException;
import com.zfw.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.zfw.core.constant.Constant.UNAUTHORIZED;


/**
 * @Author:zfw
 * @Date:2019/7/15
 * @Content:
 */
public class AuthorizedHandler implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public final static String TICKET_HEADER_NAME="Authorization";
    public final static String TICKET_HEADER_VALUE_PREFIX="Ticket";
    public final static String TICKET_HEADER_VALUE_SPLIT=" ";

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod method = null;
        if (handler instanceof HandlerMethod) {
            method = (HandlerMethod) handler;
        } else {
            return true;
        }

        if (method.getMethodAnnotation(UnAuthorized.class) != null) {
            return true;
        }else {
            return authorized(request,response,handler);
        }
    }

    public boolean authorized(HttpServletRequest request, HttpServletResponse response, Object handler){
        String auth= request.getHeader(TICKET_HEADER_NAME);
        if (StringUtilsEx.isBlank(auth)) {
            throw new GlobalException(UNAUTHORIZED);
        }else {
            String[] s = auth.split(TICKET_HEADER_VALUE_SPLIT);
            if (s.length!=2||!s[0].equals(TICKET_HEADER_VALUE_PREFIX)){
                throw new GlobalException(UNAUTHORIZED);
            }
            if(StringUtilsEx.isBlank(s[1])){
                throw new GlobalException(UNAUTHORIZED);
            }
            auth=s[1];
            Object token = redisUtils.get(CacheName.AUTHORIZED_NAME + auth);
            if (token==null){
                throw new GlobalException(UNAUTHORIZED);
            }
            //每次请求后刷新token过期时间
            redisUtils.expire(CacheName.AUTHORIZED_NAME + auth, CacheName.AUTHORIZED_NAME_TIMEOUT);
        }
        return true;
    }
}
