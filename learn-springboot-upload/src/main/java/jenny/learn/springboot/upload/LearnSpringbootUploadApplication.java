package jenny.learn.springboot.upload;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class LearnSpringbootUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnSpringbootUploadApplication.class, args);
        log.info("## learn-springboot-upload-application run successfully. ##");
    }
}
