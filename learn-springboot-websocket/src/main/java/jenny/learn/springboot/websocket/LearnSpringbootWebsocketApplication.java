package jenny.learn.springboot.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class LearnSpringbootWebsocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnSpringbootWebsocketApplication.class, args);
        log.info("## learn-springboot-websocket-application run successfully. ##");
    }
}
