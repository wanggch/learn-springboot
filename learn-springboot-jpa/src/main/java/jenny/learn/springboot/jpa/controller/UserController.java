package jenny.learn.springboot.jpa.controller;

import jenny.learn.springboot.jpa.entity.User;
import jenny.learn.springboot.jpa.param.UserParam;
import jenny.learn.springboot.jpa.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/query")
    public List<User> query(UserParam userParam) {
        return userService.query(userParam);
    }
}
