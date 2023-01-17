package jenny.learn.springboot.dubbo.api.service;

import jenny.learn.springboot.dubbo.api.domain.User;

import java.util.List;

public interface UserService {
    List<User> listUsers();
}
