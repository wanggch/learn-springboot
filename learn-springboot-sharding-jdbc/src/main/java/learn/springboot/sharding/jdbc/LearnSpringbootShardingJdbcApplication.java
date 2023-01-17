package learn.springboot.sharding.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@MapperScan("learn.springboot.sharding.jdbc.dao")
@SpringBootApplication
public class LearnSpringbootShardingJdbcApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnSpringbootShardingJdbcApplication.class, args);
    }
}
