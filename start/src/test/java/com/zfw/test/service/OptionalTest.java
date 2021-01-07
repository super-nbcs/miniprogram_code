package com.zfw.test.service;

import com.zfw.core.sys.entity.User;
import org.junit.Test;

import java.util.Optional;

/**
 * @Author:zfw
 * @Date:2021-01-07
 * @Content: 测试Optional写法，干掉恶心的if-else
 */
public class OptionalTest {

    @Test
    public void test1(){
        User user=null;
//        user = Optional.ofNullable(user).orElse(new User());
//        System.out.println(user);
        Optional.ofNullable(user).map((u)->u.getUserName()).orElseThrow(()->new RuntimeException("用户名不能为空"));
        System.out.println(user);
    }

}
