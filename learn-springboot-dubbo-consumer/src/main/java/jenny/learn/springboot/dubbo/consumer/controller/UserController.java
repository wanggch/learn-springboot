package jenny.learn.springboot.dubbo.consumer.controller;

import jenny.learn.springboot.dubbo.api.domain.User;
import jenny.learn.springboot.dubbo.api.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @DubboReference
    private UserService userService;

    @RequestMapping("/listUsers")
    public List<User> listUsers() {
        return userService.listUsers();
    }
}
