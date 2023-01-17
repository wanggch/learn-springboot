package jenny.learn.springboot.nacos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class LearnSpringbootNacosApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnSpringbootNacosApplication.class, args);
        log.info("## learn-springboot-nacos-application run successfully. ##");
    }
}
