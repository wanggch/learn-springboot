package jenny.learn.springboot.redis.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class IdService {

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    /**
     * redis key模板
     */
    private static final String KEY_TEMPLATE = "{type}{date}";

    /**
     * ID模板
     */
    private static final String ID_TEMPLATE = "{type}{datetime}{index}";

    /**
     * 默认序列
     */
    private static final Integer DEFAULT_INDEX = 1;

    /**
     * 生成下一下ID
     * @param type ID类型
     * @return 新ID
     */
    public String nextId(String type) {
        if (StringUtils.isBlank(type)) {
            throw new RuntimeException("type is empty.");
        }
        Map<String, String> paramMap = paramMap(type);
        Integer index = redisTemplate.opsForValue().get(StrUtil.format(KEY_TEMPLATE, paramMap));
        index = Objects.isNull(index) ? DEFAULT_INDEX : index + 1;
        redisTemplate.opsForValue().set(StrUtil.format(KEY_TEMPLATE, paramMap), index, timeout(), TimeUnit.SECONDS);
        log.debug("index in cache: {}", index);
        paramMap.put("index", nextIndex(index));
        log.debug("params: {}", new Gson().toJson(paramMap));
        return StrUtil.format(ID_TEMPLATE, paramMap);
    }

    /**
     * 渲染模板用的参数
     * @param type ID类型
     * @return 渲染模板用的参数
     */
    private Map<String, String> paramMap(String type) {
        Map<String, String> resultMap = Maps.newHashMap();
        resultMap.put("type", type);
        resultMap.put("datetime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        resultMap.put("date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
        return resultMap;
    }

    /**
     * 5位自增序列号
     * 例：1 -> 00001
     * @param index 序列号
     * @return 5位自增序列号
     */
    private String nextIndex(Integer index) {
        return String.format("%05d", index);
    }

    /**
     * redis过期时间，当日有效
     * @return redis过期时间
     */
    private long timeout() {
        // 明日开始时间
        Date beginOfTomorrow = DateUtil.beginOfDay(DateUtil.tomorrow());
        // 当前时间
        Date now = DateUtil.date();
        // 当前时间与明日开始时间之间的秒数
        long timeout = DateUtil.between(now, beginOfTomorrow, DateUnit.SECOND);
        log.debug("timeout: {}", timeout);
        return timeout;
    }
}
