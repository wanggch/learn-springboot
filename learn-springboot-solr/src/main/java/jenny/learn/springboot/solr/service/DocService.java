package jenny.learn.springboot.solr.service;

import java.util.List;
import javax.annotation.Resource;
import jenny.learn.springboot.solr.dao.DocDao;
import jenny.learn.springboot.solr.model.Doc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DocService {

  @Resource
  private DocDao docDao;
  public List<Doc> query(String content) {
    return docDao.findByContentLike(content);
  }
}
