package jenny.learn.springboot.redis.controller;

import jenny.learn.springboot.redis.service.IdService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/id")
public class IdController {

    @Resource
    private IdService idService;

    @RequestMapping("/nextId")
    public String nextId(String type) {
        return idService.nextId(type);
    }
}
