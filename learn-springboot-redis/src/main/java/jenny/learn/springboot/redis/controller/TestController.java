package jenny.learn.springboot.redis.controller;

import cn.hutool.core.lang.Dict;
import jenny.learn.springboot.redis.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
public class TestController {

    @RateLimiter(value = 2)
    @GetMapping("/test1")
    public Dict test1() {
        return Dict.create().set("msg", "hello world.").set("description", "1分钟内最多访问两次，超过两次返回“访问过于频繁！”");
    }
}
