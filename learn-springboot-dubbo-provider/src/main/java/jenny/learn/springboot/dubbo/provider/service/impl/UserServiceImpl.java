package jenny.learn.springboot.dubbo.provider.service.impl;

import cn.hutool.core.lang.ObjectId;
import com.google.common.collect.Lists;
import jenny.learn.springboot.dubbo.api.domain.User;
import jenny.learn.springboot.dubbo.api.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Date;
import java.util.List;

@DubboService
public class UserServiceImpl implements UserService {
    @Override
    public List<User> listUsers() {
        User user = new User();
        user.setId(ObjectId.next());
        user.setCode("001");
        user.setName("懒懒");
        user.setCreateTime(new Date());
        return Lists.newArrayList(user);
    }
}
