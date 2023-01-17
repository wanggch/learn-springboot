package jenny.learn.springboot.redis.handler;

import cn.hutool.core.lang.Dict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description 全局异常处理器
 * @author: wanggc
 * @date:  2022/7/22 8:39
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public Dict handler(RuntimeException ex) {
        return Dict.create().set("msg", ex.getMessage()).set("code", 500);
    }
}
