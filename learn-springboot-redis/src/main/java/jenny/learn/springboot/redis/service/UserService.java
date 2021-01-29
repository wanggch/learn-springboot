package jenny.learn.springboot.thymeleaf.service;

import jenny.learn.springboot.thymeleaf.dao.UserRepository;
import jenny.learn.springboot.thymeleaf.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserRepository userRepository;

    /**
     * 获取所有用户列表
     * @return 所有用户列表
     */
    public List<User> listAllUsers() {
        return userRepository.findAll();
    }
}
