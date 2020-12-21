package com.zfw.core.aspect.cache;

import com.zfw.core.annotation.cache.CacheAble;
import com.zfw.core.constant.CacheName;
import com.zfw.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author:zfw
 * @Date:2020/8/5
 * @Content:
 */
@Aspect
@Component
public class CacheAspect {
    @Autowired
    private RedisUtils redisUtils;
    @Pointcut("@annotation(com.zfw.core.annotation.cache.CacheAble)")
    public void pointcutCacheAble() {}
   @Pointcut("@annotation(com.zfw.core.annotation.cache.CacheDisable)")
    public void pointcutCacheDisable() {}

    @Around("pointcutCacheAble()")
    public Object aroundCacheAble(ProceedingJoinPoint joinPoint) throws Throwable {
        String[] key = generationKey(joinPoint);
        if (redisUtils.hasKey(key[0])){
            Object o = redisUtils.get(key[0]);
            return o;
        }
        Object proceed = joinPoint.proceed();
        redisUtils.set(key[0], Hibernate.unproxy(proceed),Long.valueOf(key[1]));
        return proceed;
    }

    @Around("pointcutCacheDisable()")
    public Object aroundCacheDisable(ProceedingJoinPoint joinPoint) throws Throwable {
        String s = generationKeyLike(joinPoint);
        redisUtils.delete(s);
        return  joinPoint.proceed();
    }




    /**
     * 生成缓存的key
     * @param joinPoint
     * @return
     * @throws NoSuchMethodException
     */
    private String[] generationKey(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = getSignature(joinPoint);
        String methodName = signature.getName();
        Class<?>[] parameterTypes = ((MethodSignature) signature).getParameterTypes();
        Class<?> clazz = getClazz(joinPoint);
        String className = clazz.getName();
        Method objMethod = clazz.getMethod(methodName, parameterTypes);

        String methodAllName = objMethod.toString();
        String methodNameAndParams = StringUtils.substring(methodAllName, methodAllName.indexOf(methodName), -1);

        CacheAble cacheAble = objMethod.getAnnotation(CacheAble.class);

        // 获取所有值
        Object[] args = joinPoint.getArgs();
        StringBuilder values=new StringBuilder();
        for (Object arg : args) {
            if (arg==null)
                arg="null";
            values.append(arg.toString()+",");
        }
        if (values.length()>0){
            values.deleteCharAt(values.length()-1);
        }
        String key = String.format("%s:%s:%s:%s:%s", CacheName.SERVICE_INTERFACE,className,methodNameAndParams, values.toString(),values.toString());
        return new String[]{key,String.valueOf(cacheAble.ttl())};
    }
    private String generationKeyLike(ProceedingJoinPoint joinPoint){
        String className = getClazz(joinPoint).getName();
        return String.format("%s:%s",CacheName.SERVICE_INTERFACE,className);
    }

    private Signature getSignature(ProceedingJoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        return signature;
    }

    private Class<?> getClazz(ProceedingJoinPoint joinPoint){
        Class<?> aClass = joinPoint.getTarget().getClass();
        return aClass;
    }


}
