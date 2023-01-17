package jenny.learn.springboot.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/sayHello")
    public Object sayHello(String name) {
        return "hello, " + name;
    }
}
