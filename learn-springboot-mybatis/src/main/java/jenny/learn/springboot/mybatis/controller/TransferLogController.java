package jenny.learn.springboot.mybatis.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transferLog")
public class TransferLogController {

    @RequestMapping("/query")
    public Object query() {
        return "hello world";
    }
}
