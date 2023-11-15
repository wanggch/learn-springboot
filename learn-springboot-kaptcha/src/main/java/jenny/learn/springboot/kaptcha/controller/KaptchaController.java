package jenny.learn.springboot.kaptcha.controller;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.common.collect.Maps;
import jenny.learn.springboot.kaptcha.param.LoginParam;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 验证码控制器
 */
@RestController
@AllArgsConstructor
@RequestMapping("/kaptcha")
public class KaptchaController {

    private DefaultKaptcha kaptchaProducer;
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 验证码缓存Key格式
     */
    private static final String KAPTCHA_KEY_FORMAT = "kaptcha:{}";

    /**
     * 生成验证码
     * @return /
     */
    @GetMapping("/generate")
    public Map<String, Object> generate() {
        // 验证码文本
        String text = kaptchaProducer.createText();
        // 验证码图片
        BufferedImage bufferedImage = kaptchaProducer.createImage(text);
        // 将验证码图片转成Base64字符串
        String base64 = ImgUtil.toBase64(bufferedImage, "jpg");
        // 验证码缓存Key
        String uuid = IdUtil.fastSimpleUUID();
        // 缓存验证码，5分钟内有效
        redisTemplate.opsForValue().set(String.format(KAPTCHA_KEY_FORMAT, uuid), text, 300, TimeUnit.SECONDS);

        // 组装返回结果
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("image", base64);
        resultMap.put("uuid", uuid);
        // 返回
        return resultMap;
    }

    @PostMapping("/check")
    public Boolean check(@RequestBody LoginParam param) {
        String key = String.format(KAPTCHA_KEY_FORMAT, param.getUuid());
        if (StrUtil.isNotEmpty(key)) {
            return Objects.equals(redisTemplate.opsForValue().get(key), param.getKaptcha());
        }
        return false;
    }
}
