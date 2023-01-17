package jenny.learn.springboot.es.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "article")
public class Article {
  @Id
  private String id;
  private String name;
  private String path;
  private String content;
}
