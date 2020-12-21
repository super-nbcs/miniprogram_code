package com.zfw.core.aspect;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zfw.utils.GZipStrUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Base64;
import java.util.List;


/**
 * @Author:zfw
 * @Date:2020-11-10
 * @Content:
 */
@ControllerAdvice
public class ResponseControllerAdvice  implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        String path = request.getURI().getPath();

        CharSequence[] charSequences = {"swagger", "api", "/error", "export", "client/count", "client/like", "client/download", "/download"};
        if (StringUtils.containsAny(StringUtils.lowerCase(path), charSequences)){
            return body;
        }
        HttpHeaders headers = request.getHeaders();
        List<String> list = headers.get("zfw");
        if (list!=null&&!list.isEmpty()){
            return body;
        }
        String s1 = JSONObject.toJSONStringWithDateFormat(body, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
        String encode= Base64.getEncoder().encodeToString(s1.getBytes());
        return GZipStrUtils.compress(encode);
    }
}
