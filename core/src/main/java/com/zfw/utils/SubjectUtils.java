package com.zfw.utils;

import com.zfw.core.constant.CacheName;
import com.zfw.core.handler.AuthorizedHandler;
import com.zfw.core.sys.entity.User;
import com.zfw.core.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;


/**
 * @Author:zfw
 * @Date:2019/7/15
 * @Content:
 */
@Component
public class SubjectUtils {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private IUserService iUserService;

    public User getPrincipal() {
        String token = request.getHeader(AuthorizedHandler.TICKET_HEADER_NAME);
        token = token.split(AuthorizedHandler.TICKET_HEADER_VALUE_SPLIT)[1];
        return (User) redisUtils.get(CacheName.AUTHORIZED_NAME + token);
    }

    public User resetPrincipal(Integer userId){
        Set<Object> cacheTokens = redisUtils.sGet(CacheName.USER_ID + userId);
        User userRole = iUserService.getUserRole(userId);
        cacheTokens.forEach(cacheToken->{
            redisUtils.set((String) cacheToken,userRole);
        });
        return userRole;
    }

/*    public User getPrincipal() {
        String token = request.getHeader(AuthorizedHandler.TICKET_HEADER_NAME);
        if (token==null){
            throw new GlobalException(Constant.UNAUTHORIZED);
        }
        String[] s = token.split(TICKET_HEADER_VALUE_SPLIT);
        if (s.length!=2||!s[0].equals(TICKET_HEADER_VALUE_PREFIX)){
            throw new GlobalException(UNAUTHORIZED);
        }
        token = token.split(TICKET_HEADER_VALUE_SPLIT)[1];
        if (StringUtilsEx.isBlank(token)){
            throw new GlobalException(UNAUTHORIZED);
        }
        Object o = redisUtils.get(Cache.AUTHORIZED_NAME + token);
        if (!(o instanceof User)){
            throw new GlobalException(CODE_10005.CODE,CODE_10005.ZH_CODE+"：用户类型转换失败",CODE_10005.EN_CODE);
        }
        return (User) o;
    }*/

}
