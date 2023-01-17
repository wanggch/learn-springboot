package jenny.learn.springboot.es.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import jenny.learn.springboot.es.dao.ArticleDao;
import jenny.learn.springboot.es.data.Article;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DirectoryService {


  @Resource
  private ArticleDao articleDao;
  @Resource
  private ElasticsearchRestTemplate elasticsearchRestTemplate;

  public List<Article> findByName(String name) {
    return articleDao.findByName(name);
  }

  public boolean exist(String name) {
    return findByName(name).size() > 0;
  }

//  public Article save(Article article) {
//    return articleDao.save(article);
//  }

  public void save(File file) {
    if (!exist(file.getName())) {
      Article article = new Article();
      article.setName(file.getName());
      article.setContent(readToString(file));
      articleDao.save(article);
      log.info("save done. name: {}, content: {}", article.getName(), article.getContent());
    } else {
      articleDao.deleteAll();
      log.info("exist");
    }
  }

  public Object all() {
    return articleDao.findAll();
  }

  public void execute(String direcotryPath) {
    File directory = new File(direcotryPath);
    if (!directory.exists()) {
      log.error("not exist!");
      return;
    }
    if (!directory.isDirectory()) {
      log.error("unvalid direcotry path!");
      return;
    }
    File[] files = directory.listFiles();
    for (int i = 0; i < files.length; i++) {
      // 创建索引
      save(files[i]);
    }
  }

//  public void clean() {
//    Iterable<Article> iterable = articleDao.findAll();
//    for()
//    while (iterable.iterator().hasNext()) {
//      articleDao.deleteById(iterable.iterator().next().getId());
//    }
//  }

  public Object search(String content) {
    CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria()
        .and(new Criteria("content").contains(content)))
        .setPageable(PageRequest.of(0, 10));

    SearchHits<Article> searchHits = elasticsearchRestTemplate.search(criteriaQuery, Article.class);
    List<Article> result = searchHits.get().map(SearchHit::getContent).collect(Collectors.toList());
    return result;
//    FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("content", content);
//    Iterable<Article> resultList = articleDao.search(fuzzyQueryBuilder);
//    return resultList;
//    Article article = new Article();
//    article.setContent(content);
//    Page<Article> result = articleDao.searchSimilar(article, null, PageRequest.of(0, 10));
//    return result;
  }

//  @Resource
//  private JestClient jestClient;

//  public void execute(String direcotryPath) throws IOException {
//    File directory = new File(direcotryPath);
//    if (!directory.exists()) {
//      log.error("not exist!");
//      return;
//    }
//    if (!directory.isDirectory()) {
//      log.error("unvalid direcotry path!");
//      return;
//    }
//    File[] files = directory.listFiles();
//    for(int i = 0; i < files.length; i++) {
//      // 创建索引
//      createIndex(files[i]);
//    }
//  }
//
//  public void createIndex(File file) {
//    String fileName = file.getName();
//    String[] strArray = fileName.split("\\.");
//    int suffixIndex = strArray.length - 1;
//    String fileType = strArray[suffixIndex];
//
//    Dict isIndexResult = isIndex(file);
////    log.info("文件名：{}，文件类型：{}，MD5：{}，建立索引：{}", file.getPath(), fileType, isIndexResult.getString("fileFingerprint"), isIndexResult.getBoolean("isIndex"));
//
//    if ((Boolean) isIndexResult.get("isIndex")) {
//      log.info("exist!");
//    }
//    //1. 给ES中索引(保存)一个文档
//    Article article = new Article();
//    article.setTitle(file.getName());
//    article.setAuthor(file.getParent());
//    article.setPath(file.getPath());
//    article.setContent(readToString(file, fileType));
//    article.setFileFingerprint((String) isIndexResult.get("fileFingerprint"));
//    //2. 构建一个索引
//    Index index = new Index.Builder(article).index("diskfile").type("files").build();
//    try {
//      //3. 执行
//      if (!jestClient.execute(index).getId().isEmpty()) {
//        log.info("构建索引成功！");
//      }
//    } catch (IOException e) {
//      log.error("{}", e.getLocalizedMessage());
//    }
//  }
//
//  //判断是否已经索引
//  private Dict isIndex(File file) {
//    Dict result = Dict.create();
//    //用MD5生成文件指纹,搜索该指纹是否已经索引
//    String fileFingerprint = file.getName();
//    result.put("fileFingerprint", fileFingerprint);
//    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//    searchSourceBuilder.query(QueryBuilders.termQuery("fileFingerprint", fileFingerprint));
//    Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("diskfile").addType("files").build();
//    try {
//      //执行
//      SearchResult searchResult = jestClient.execute(search);
//      JsonObject jsonObject = searchResult.getJsonObject();
//      JsonElement jsonElement = jsonObject.get("hits").getAsJsonObject().get("total").getAsJsonObject().get("value");
//      Long total =  jsonElement.getAsLong();
//      if (Objects.nonNull(total) && total > 0L) {
//        result.put("isIndex", true);
//      } else {
//        result.put("isIndex", false);
//      }
//    } catch (IOException e) {
//      log.error("{}", e.getLocalizedMessage());
//    }
//    return result;
//  }
//
  //读取文件内容转换为字符串
  private String readToString(File file) {
    StringBuffer result = new StringBuffer();
    try (FileInputStream in = new FileInputStream(file)) {
      Long filelength = file.length();
      byte[] filecontent = new byte[filelength.intValue()];
      in.read(filecontent);
      result.append(new String(filecontent, "utf8"));
    } catch (FileNotFoundException e) {
      log.error("{}", e.getLocalizedMessage());
    } catch (IOException e) {
      log.error("{}", e.getLocalizedMessage());
    }
    return result.toString();
  }
}
