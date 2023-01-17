package jenny.learn.springboot.jpa.dao;

import com.google.common.collect.Lists;
import jenny.learn.springboot.jpa.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.List;

@SpringBootTest
public class UserRepositoryTest {

    @Resource
    private UserRepository userRepository;

    @Test
    public void testFindAll() {
        List<User> userList = userRepository.findAll(((root, criteriaQuery, criteriaBuilder) -> {
            // 定义集合，用于存放动态查询条件
            List<Predicate> predicateList = Lists.newArrayList();
            predicateList.add(criteriaBuilder.like(root.get("userCode").as(String.class), "%0%"));
            predicateList.add(criteriaBuilder.like(root.get("userName").as(String.class), "%汪%"));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }));
        userList.forEach(System.out::println);
    }

    @Test
    public void testFindByUserName() {
        List<User> userList = userRepository.findByUserName("汪小成");
        userList.forEach(System.out::println);
    }
}
