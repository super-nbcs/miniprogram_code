package com.zfw.core.aspect;

import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author:zfw
 * @Date:2021-01-11
 * @Content: 打印请求日志
 */
@Aspect
@Component
public class RequestLogAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.zfw.*.controller..*.*(..))")
    public void requestLog() {}

    /**
     * 在切点之前织入
     * @param joinPoint
     * @throws Throwable
     */
    @Before("requestLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        logger.info("========================================== Start ==========================================");
        logger.info("请求url     : {}", request.getRequestURL().toString());
        logger.info("请求方法    : {}", request.getMethod());
        logger.info("请求方法名  : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        logger.info("请求ip      : {}", request.getRemoteAddr());
        Object[] args = joinPoint.getArgs();
        logger.info("请求参数    : {}", JSONObject.toJSONString(args));
    }


    @After("requestLog()")
    public void doAfter() throws Throwable {
        logger.info("=========================================== End ===========================================");
        logger.info("");
    }

    /**
     * 环绕
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("requestLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        logger.info("返回参数  : {}", result);
        logger.info("请求时长  : {} ms", System.currentTimeMillis() - startTime);
        return result;
    }
}
