package jenny.learn.springboot.xxljob;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class LearnSpringbootXxljobApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnSpringbootXxljobApplication.class, args);
        log.info("## learn-springboot-xxljob-application run successfully. ##");
    }
}
