package jenny.learn.springboot.security.dao;

import jenny.learn.springboot.security.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    /**
     * 根据用户编码获取用户
     * @param code 用户编码
     * @return /
     */
    User findByCode(String code);
}
