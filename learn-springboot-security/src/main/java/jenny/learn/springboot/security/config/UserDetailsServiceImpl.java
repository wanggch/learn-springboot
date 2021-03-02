package jenny.learn.springboot.security.config;

import jenny.learn.springboot.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;

/**
 * 获取用户信息
 * @author: wanggc
 */
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String userCode) throws UsernameNotFoundException {
        jenny.learn.springboot.security.entity.User user = userService.findByCode(userCode);
        return new User(user.getCode(), user.getPassword(), Lists.newArrayList());
    }
}
