package jenny.learn.springboot.jpa.dao;

import jenny.learn.springboot.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User getById(Integer id);
    List<User> findByUserName(String userName);

    @Query("from User where userCode = ?1")
    List<User> findUserByUserCodeTypeA(String userCode);

    @Query("from User where userCode = :usercode")
    List<User> findUserByUserCodeTypeB(@Param("usercode") String userCode);

    @Query(value = "select * from t_user where user_code = :usercode", nativeQuery = true)
    List<User> findUserByUserCodeTypeC(@Param("usercode") String userCode);


}
