package jenny.learn.springboot.undertow.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("msg", "hello world");
        return resultMap;
    }
}
