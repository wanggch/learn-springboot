package jenny.learn.springboot.redis.service;

import jenny.learn.springboot.redis.dao.UserRepository;
import jenny.learn.springboot.redis.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserService {

    @Resource
    private UserRepository userRepository;
    @Resource
    private RedisTemplate<String, User> redisTemplate;

    /**
     * 获取所有用户列表
     * @return 所有用户列表
     */
    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Integer id) {
        String redisKey = String.format("user:%s", id);
        log.info("redis key: {}", redisKey);
        log.info("## Find user form redis. ##");
        User user = redisTemplate.opsForValue().get(redisKey);
        if (Objects.isNull(user)) {
            log.info("## Find user form DB. ##");
            user = userRepository.getById(id);
        }
        if (Objects.isNull(user)) {
            throw new RuntimeException(String.format("Can not find user [ id=%s ].", id));
        } else {
            redisTemplate.opsForValue().set(redisKey, user, 5, TimeUnit.MINUTES);
        }
        return user;
    }
}
