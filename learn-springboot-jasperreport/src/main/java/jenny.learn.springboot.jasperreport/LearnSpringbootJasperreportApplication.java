package jenny.learn.springboot.jasperreport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class LearnSpringbootJasperreportApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnSpringbootJasperreportApplication.class, args);
        log.info("## learn-springboot-jasperreport-application run successfully. ##");
    }
}
