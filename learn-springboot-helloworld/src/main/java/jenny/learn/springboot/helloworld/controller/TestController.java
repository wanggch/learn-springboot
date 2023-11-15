package jenny.learn.springboot.helloworld.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public List<Map<String, Object>> all() {
        List<Map<String, Object>> list = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("key", i);
            map.put("value", String.format("all数据%s", i));
            list.add(map);
        }
        return list;
    }

    @PostMapping
    public List<Map<String, Object>> list() {
        List<Map<String, Object>> list = Lists.newArrayList();
        for (int i = 0; i < 2; i++) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("key", i);
            map.put("value", String.format("list数据%s", i));
            list.add(map);
        }
        return list;
    }
}
