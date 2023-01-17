package jenny.learn.springboot.jimu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = {"org.jeecg.modules.jmreport"})
public class LearnSpringbootJimuApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnSpringbootJimuApplication.class, args);
        log.info("## learn-springboot-jimu application run successfully. ##");
    }
}
