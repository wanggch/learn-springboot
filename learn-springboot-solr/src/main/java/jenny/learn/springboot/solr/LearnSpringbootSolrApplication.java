package jenny.learn.springboot.solr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class LearnSpringbootSolrApplication {

  public static void main(String[] args) {
    SpringApplication.run(LearnSpringbootSolrApplication.class, args);
    log.info("## LearnSpringbootSolrApplication run successfully. ##");
  }
}
