package jenny.learn.springboot.dubbo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class LearnSpringbootDubboConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnSpringbootDubboConsumerApplication.class, args);
        log.info("## learn-springboot-dubbo-consumer-application run successfully. ##");
    }
}
