package jenny.learn.springboot.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class LearnSpringbootGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnSpringbootGatewayApplication.class, args);
        log.info("## learn-springboot-gateway-application run successfully. ##");
    }
}
