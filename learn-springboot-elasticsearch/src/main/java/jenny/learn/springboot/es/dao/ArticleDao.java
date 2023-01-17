package jenny.learn.springboot.es.dao;

import java.util.List;
import jenny.learn.springboot.es.data.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleDao extends ElasticsearchRepository<Article, String> {

  List<Article> findByName(String name);
}
