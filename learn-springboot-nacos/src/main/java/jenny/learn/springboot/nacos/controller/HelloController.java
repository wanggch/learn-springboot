package jenny.learn.springboot.nacos.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class HelloController {
    @Value("${name}")
    private String name;
    @GetMapping
    public String index() {
        return name;
    }
}
