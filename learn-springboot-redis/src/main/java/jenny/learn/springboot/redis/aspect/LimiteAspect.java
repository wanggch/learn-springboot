package jenny.learn.springboot.redis.aspect;

import cn.hutool.core.util.StrUtil;
import jenny.learn.springboot.redis.annotation.RateLimiter;
import jenny.learn.springboot.redis.util.IpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LimiteAspect {

    private final static String SEPARATOR = ":";
    private final static String REDIS_LIMIT_KEY_PREFIX = "limit:";
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisScript<Long> limitRedisScript;

    @Pointcut("@annotation(jenny.learn.springboot.redis.annotation.RateLimiter)")
    public void rateLimit() {
    }

    @Around("rateLimit()")
    public Object pointcut(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        // 获取RateLimiter注解
        RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, RateLimiter.class);
        if (rateLimiter != null) {
            String key = rateLimiter.key();
            // 如果key值为空，key值以类名+方法名为前缀
            if (StrUtil.isBlank(key)) {
                key = method.getDeclaringClass().getName() + StrUtil.DOT + method.getName();
            }
            key = key + SEPARATOR + IpUtil.getIpAddr();

            // 最大请求次数
            long max = rateLimiter.max();
            // 超时时间
            long timeout = rateLimiter.timeout();
            // 超时时间单位
            TimeUnit timeUnit = rateLimiter.timeUnit();
            boolean limited = shouldLimited(key, max, timeout, timeUnit);
            if (limited) {
                throw new RuntimeException("访问过于频繁！");
            }
        }

        return point.proceed();
    }

    /**
     * 是否限制
     * @param key Redis Key
     * @param max 最大请求次数
     * @param timeout 超时时间
     * @param timeUnit 超时时间单位
     * @return 是否限制，限制返回true
     */
    private boolean shouldLimited(String key, long max, long timeout, TimeUnit timeUnit) {
        // 最终的key格式：limit:定制key:ip 或者 limit:类名.方法名:ip
        key = REDIS_LIMIT_KEY_PREFIX + key;
        // 超时时间（毫秒）
        long ttl = timeUnit.toMillis(timeout);
        // 系统当前时间
        long now = Instant.now().toEpochMilli();
        // 过期
        long expired = now - ttl;
        Long executeTimes = stringRedisTemplate.execute(limitRedisScript, Collections.singletonList(key),
                String.valueOf(now), String.valueOf(ttl), String.valueOf(expired), String.valueOf(max));
        if (executeTimes != null) {
            if (executeTimes == 0) {
                log.error("[{}] The access limit has been reached within {} milliseconds per unit time, the current interface limit is {}", key, ttl, max);
                return true;
            } else {
                log.info("[{}] visit {} times in unit time {} milliseconds", key, ttl, executeTimes);
                return false;
            }
        }
        return false;
    }
}
