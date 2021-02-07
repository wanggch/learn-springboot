package jenny.learn.springboot.dubbo.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class LearnSpringbootDubboProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnSpringbootDubboProviderApplication.class, args);
        log.info("## learn-springboot-dubbo-provider-application run successfully. ##");
    }
}
