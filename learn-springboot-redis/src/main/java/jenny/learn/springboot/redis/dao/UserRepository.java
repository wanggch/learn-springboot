package jenny.learn.springboot.redis.dao;

import jenny.learn.springboot.redis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User getById(Integer id);
}
