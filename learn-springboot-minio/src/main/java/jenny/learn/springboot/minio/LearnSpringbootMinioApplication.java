package jenny.learn.springboot.minio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class LearnSpringbootMinioApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnSpringbootMinioApplication.class, args);
        log.info("## learn-springboot-minio-application run successfully. ##");
    }
}
