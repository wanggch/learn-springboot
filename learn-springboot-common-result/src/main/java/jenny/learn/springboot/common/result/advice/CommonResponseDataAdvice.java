package jenny.learn.springboot.common.result.advice;

import jenny.learn.springboot.common.result.base.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
* @description 返回结果统一封装
* @author wanggc
* @date 2020/11/6 3:07 下午
*/
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // TODO 目前处理所有请求
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter methodParameter,
                                  MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (Objects.isNull(data)) {
            return Result.success();
        }
        if (data instanceof Result) {
            return data;
        }
        return Result.success(data);
    }
}
