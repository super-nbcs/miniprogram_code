package com.zfw.core.aspect;

import com.zfw.core.annotation.LogAnnotation;
import com.zfw.core.handler.AuthorizedHandler;
import com.zfw.core.sys.entity.Log;
import com.zfw.core.sys.entity.User;
import com.zfw.core.sys.service.ILogService;
import com.zfw.utils.SubjectUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author snow
 * @create 2020/4/13 15:37
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private ILogService iLogService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SubjectUtils subjectUtils;

    @Pointcut("@annotation(com.zfw.core.annotation.LogAnnotation)")
    public void pointcutLog() {}

    @Pointcut("@annotation(com.zfw.core.annotation.SubmitCommit)")
    public void pointcutSubmit(){}


    //日志环绕通知
    @Around("pointcutLog()")
    public Object aroundLog(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        result = point.proceed();

//         执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
//         保存日志
        saveLog(point, time, "1", null);
        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, long time, String type, String exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log log = new Log();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        if (logAnnotation != null) {
//             注解上的描述
            log.setTitle(logAnnotation.value());
        }
//         请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.setMethod(className + "." + methodName + "()");
//         请求的方法参数值
        Object[] args = joinPoint.getArgs();
//         请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            String params = "";
            for (int i = 0; i < args.length; i++) {
                params += "  " + paramNames[i] + ": " + args[i];
            }
            log.setParams(params);
        }

//        设置IP地址
        log.setRemoteAddr(request.getRemoteAddr());
//        设置请求uri
        log.setRequestUri(request.getRequestURI());
//        用户代理信息
        if (ObjectUtils.isEmpty(request.getHeader(AuthorizedHandler.TICKET_HEADER_NAME))) {
            log.setUserAgent("");
        } else {
            User user = subjectUtils.getPrincipal();
            log.setUserAgent(user.getUserName());
        }
//        操作执行时长
        log.setOpsTime((int) time);
//        异常原因
        log.setException(exception);
//        日志类型
        log.setType(type);
        iLogService.save(log);
    }



//
//    /**
//     * 表单重复提交环绕通知
//     *
//     * @param
//     *
////     */
//    @Around("pointcutSubmit()")
//    public Object around(ProceedingJoinPoint point) throws Throwable {
//        HttpServletRequest request  = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        String ip = HttpUtil.getIpAddr(request);
//        //获取注解
//        MethodSignature signature = (MethodSignature) point.getSignature();
//        Method method = signature.getMethod();
//        //目标类、方法
//        String className = method.getDeclaringClass().getName();
//        String name = method.getName();
//        String ipKey = String.format("%s#%s",className,name);
//        int hashCode = Math.abs(ipKey.hashCode());
//
//        String key = Constants.REDIS_ROOT+String.format("%s_%d",ip,hashCode);
//
//        log.info("ipKey={},hashCode={},key={}",ipKey,hashCode,key);
//        long timeout = avoidRepeatableCommit.timeout();
//        if (timeout < 0){
//            timeout = 3*Constants.SECOND;
//        }
//        String value = (String) redisTemplate.opsForValue().get(key);
//        if (StringUtils.isNotBlank(value)){
//
//            return "请勿重复提交";
//        }
//        redisTemplate.opsForValue().set(key, UUID.randomUUID().toString(),timeout, TimeUnit.MILLISECONDS);
//        //执行方法
//        Object object = point.proceed();
//
//        return object;
//    }

}
