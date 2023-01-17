package jenny.learn.springboot.solr.controller;

import cn.hutool.core.util.StrUtil;
import javax.annotation.Resource;
import jenny.learn.springboot.solr.service.DocService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doc")
public class DocController {

  @Resource
  private DocService docService;
  @Value("${spring.data.solr.host}")
  private String solrHost;

  @RequestMapping("/search")
  public Object search(String content) {
    SolrQuery solrQuery = new SolrQuery();
    solrQuery.setQuery(StrUtil.format("attr_content:{}", content));
    // 设置高亮
    solrQuery.setHighlight(true);
    try {
      SolrClient solrClient = new HttpSolrClient.Builder(StrUtil.format("{}/sdck_core", solrHost)).build();
      QueryResponse queryResponse = solrClient.query(solrQuery);
      SolrDocumentList solrDocumentList = queryResponse.getResults();
      return solrDocumentList;
    } catch (Exception e) {
      //
      e.printStackTrace();
    }
    return null;
  }
  @RequestMapping("/query")
  public Object query(String content) {
    return docService.query(content);
  }
}
