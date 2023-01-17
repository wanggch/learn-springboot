package jenny.learn.springboot.es.config;

import java.time.Duration;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class ElasticsearchConfiguration extends AbstractElasticsearchConfiguration {

  @Value("${spring.elasticsearch.rest.uris}")
  private String uris;

  @Override
  public RestHighLevelClient elasticsearchClient() {
    final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
        .connectedTo(uris.split(","))
        .withConnectTimeout(Duration.ofSeconds(5))
        .withSocketTimeout(Duration.ofSeconds(30))
        .build();
    return RestClients.create(clientConfiguration).rest();
  }

  @Bean
  public ElasticsearchRestTemplate elasticsearchRestTemplate() {
    return new ElasticsearchRestTemplate(elasticsearchClient());
  }
}
