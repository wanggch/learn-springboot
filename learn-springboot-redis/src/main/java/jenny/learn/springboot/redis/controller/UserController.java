package jenny.learn.springboot.redis.controller;

import jenny.learn.springboot.redis.entity.User;
import jenny.learn.springboot.redis.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/getUserById")
    public User getUserById(Integer id) {
        return userService.getUserById(id);
    }
}
