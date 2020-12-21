package com.zfw.core.handler;

import com.zfw.utils.StringUtilsEx;
import com.zfw.core.annotation.ParamValidate;
import com.zfw.core.annotation.Validate;
import com.zfw.core.exception.GlobalException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @Author:zfw
 * @Date:2019/7/10
 * @Content:
 */
public class ParamValidateHandlerInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod method=null;
        if (handler instanceof HandlerMethod){
            method= (HandlerMethod) handler;
        }else {
            return true;
        }
        if (method.getMethodAnnotation(Validate.class)==null){
            return true;
        }

        MethodParameter[] methodParameters = method.getMethodParameters();
        for (MethodParameter methodParameter : methodParameters) {
            Class<?> parameterType = methodParameter.getParameterType();
            Field[] fields = parameterType.getDeclaredFields();
            for (Field field : fields) {
                ParamValidate paramValidate = field.getAnnotation(ParamValidate.class);
                if (paramValidate != null) {
                    boolean b = paramValidate(request, response, field.getName(), paramValidate);
                    if(b)
                        continue;
                    else
                        return b;
                }
            }

        }
        return true;
    }


    private boolean paramValidate(HttpServletRequest request, HttpServletResponse response, String fieldName, ParamValidate paramValidate) throws IOException {
        String parameter = request.getParameter(fieldName);
        if (StringUtilsEx.isBlank(parameter)) {
            throw new GlobalException(HttpStatus.BAD_REQUEST.value(),"参数：" + fieldName + " 为必填项",HttpStatus.BAD_REQUEST.getReasonPhrase());
            //response.getWriter().append("参数：" + fieldName + " 为必填项");
            //return false;
        }
        int[] length = paramValidate.length();

        if (length.length == 0) {
            //logger.info("参数：" + fieldName + "没做length验证");
        } else if (length.length != 2) {
            throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"后端注解：@ParamValidate中的length变量，在参数：" + fieldName + "上使用格式有误",HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            //response.getWriter().append("后端注解：@ParamValidate中的length变量，在参数：" + fieldName + "上使用格式有误");
            //return false;
        } else if (length.length == 2) {
            if (parameter.length() < length[0] || parameter.length() > length[1]) {
                String s = "参数：" + fieldName + "长度：在" + length[0] + "-" + length[1] + "之间";
                String s1 = "参数：" + fieldName + paramValidate.msg()[0];
                throw new GlobalException(HttpStatus.BAD_REQUEST.value(),StringUtils.isBlank(paramValidate.msg()[0])? s : s1,HttpStatus.BAD_REQUEST.getReasonPhrase());
                //response.getWriter().append(StringUtils.isBlank(paramValidate.msg()[0])? s : s1);
                //return false;
            }
        }
        String regular = paramValidate.regular();
        if (StringUtils.isBlank(regular)) {
            //logger.info("参数：" + fieldName + "没做正则验证");
        } else if (StringUtils.isNotBlank(regular)) {
            boolean matches = parameter.matches(regular);
            if (!matches) {
                String s = "参数：" + fieldName + "正则不匹配";
                String s1 = "参数：" + fieldName + paramValidate.msg()[1];
                throw new GlobalException(HttpStatus.BAD_REQUEST.value(),StringUtils.isBlank(paramValidate.msg()[1])? s : s1,HttpStatus.BAD_REQUEST.getReasonPhrase());
               // response.getWriter().append( StringUtils.isBlank(paramValidate.msg()[1]) ? s : s1);
               // return false;
            }
        }
        return true;
    }
}
