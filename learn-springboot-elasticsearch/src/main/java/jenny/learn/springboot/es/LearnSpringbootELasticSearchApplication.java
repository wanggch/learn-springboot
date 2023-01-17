package jenny.learn.springboot.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LearnSpringbootELasticSearchApplication {

  public static void main(String[] args) {
    SpringApplication.run(LearnSpringbootELasticSearchApplication.class, args);
  }

//  @Bean
//  public JestClient jestClient() {
//    JestClientFactory factory = new JestClientFactory();
//    factory.setHttpClientConfig(
//        new HttpClientConfig.Builder("http://localhost:9200")
//            .multiThreaded(true)
//            .defaultMaxTotalConnectionPerRoute(2)
//            .maxTotalConnection(10)
//            .build());
//    return factory.getObject();
//  }
}
