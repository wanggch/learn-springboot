package jenny.learn.springboot.jpa.service;

import jenny.learn.springboot.jpa.dao.UserRepository;
import jenny.learn.springboot.jpa.entity.User;
import jenny.learn.springboot.jpa.param.UserParam;
import jenny.learn.springboot.jpa.util.QueryHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Resource
    private UserRepository userRepository;

    /**
     * 获取所有用户列表
     * @return 所有用户列表
     */
    public List<User> query(UserParam userParam) {
        log.info("UserService.query.param: {}", userParam);
        return userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelper.getPredicate(root, userParam, criteriaBuilder));
    }
}
