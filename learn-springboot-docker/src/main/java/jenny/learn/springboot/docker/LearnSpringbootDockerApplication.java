package jenny.learn.springboot.docker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class LearnSpringbootDockerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnSpringbootDockerApplication.class, args);
        log.info("## learn-springboot-docker-application run successfully. ##");
    }
}
