package jenny.learn.springboot.redis.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiter {

    long DEFAULT_REQUEST = 10;

    /**
     * 最大请求次数
     */
    @AliasFor("max")
    long value() default DEFAULT_REQUEST;

    /**
     * 最大请求次数
     */
    @AliasFor("value")
    long max() default DEFAULT_REQUEST;

    /**
     * Redis key值
     */
    String key() default "";

    /**
     * 超时时间，默认1分钟
     */
    long timeout() default 1;

    /**
     * 超时单位，默认分钟
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;
}
