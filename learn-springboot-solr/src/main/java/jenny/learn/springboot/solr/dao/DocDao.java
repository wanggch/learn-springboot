package jenny.learn.springboot.solr.dao;

import java.util.List;
import jenny.learn.springboot.solr.model.Doc;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocDao extends SolrCrudRepository<Doc, String> {

  List<Doc> findByContentLike(String content);
}
