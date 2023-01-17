package jenny.learn.springboot.excel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class LearnSpringbootExcelApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnSpringbootExcelApplication.class, args);
        log.info("## learn-springboot-excel-application run successfully. ##");
    }
}
