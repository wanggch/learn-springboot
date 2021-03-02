package jenny.learn.springboot.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动类
 * @author: wanggc
 */
@Slf4j
@SpringBootApplication
public class LearnSpringbootSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnSpringbootSecurityApplication.class, args);
        log.info("## learn-springboot-security-application run successfully. ##");
    }
}
