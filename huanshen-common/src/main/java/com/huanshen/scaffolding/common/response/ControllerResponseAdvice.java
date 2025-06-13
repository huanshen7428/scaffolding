package com.huanshen.scaffolding.common.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * ControllerResponseAdvice
 *
 * @description
 * @date 2023-11-21 15:25
 */
@RestControllerAdvice(basePackages = { "com.huanshen.scaffolding"})
public class ControllerResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> clazz) {
        //BaseResponse类型的返回不封装
        if (methodParameter.hasMethodAnnotation(NoControllerAdvice.class) || methodParameter.getContainingClass().getAnnotation(NoControllerAdvice.class) != null) {
            return false;
        }
        return !methodParameter.getParameterType().isAssignableFrom(BaseResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object data,
                                  MethodParameter returnType,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> clazz,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // String类型不能直接包装
        if (returnType.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装后转换为json串
                return objectMapper.writeValueAsString(BaseResponse.success(data));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        BaseResponse res = BaseResponse.success(data);
        //为分页数据增加total属性
        if (data != null && data.getClass() == Page.class) {
            res.setTotal(((Page<?>) data).getTotal());
        }
        return res;
    }
}
