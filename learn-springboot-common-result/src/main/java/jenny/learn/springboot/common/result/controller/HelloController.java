package jenny.learn.springboot.common.result.controller;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public Object hello(String name) {
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("message", StringUtils.isBlank(name) ? "Hello World." : "Hello " + name + ".");
        return resultMap;
    }
}
