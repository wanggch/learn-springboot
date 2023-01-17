package jenny.learn.springboot.security.service;

import jenny.learn.springboot.security.dao.UserDao;
import jenny.learn.springboot.security.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@Service
public class UserService {
    @Resource
    private UserDao userDao;
    /**
     * 根据编码获取用户信息
     * @param code 编码
     * @return 用户信息
     */
    public User findByCode(String code) {
        if (StringUtils.isBlank(code)) {
            throw new RuntimeException("用户编码不能为空！");
        }
        User user = userDao.findByCode(code);
        if (Objects.isNull(user)) {
            log.error("用户[code={}]不存在！", code);
            throw new RuntimeException("用户不存在！");
        }
        return user;
    }
}
