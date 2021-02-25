package jenny.learn.springboot.security.dao;

import jenny.learn.springboot.security.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    User findByCode(String code);
}
