package jenny.learn.springboot.es.controller;

import cn.hutool.core.lang.Dict;
import javax.annotation.Resource;
import jenny.learn.springboot.es.service.DirectoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/doc")
public class DocController {

//  @Resource
//  private JestClient jestClient;

  @Resource
  private DirectoryService directoryService;

  @RequestMapping(value = "/init", method = RequestMethod.GET)
  public Dict init() throws Exception {
    directoryService.execute("/Users/wanggc/02-my/00-default/es-test-directory");
    return Dict.create().set("result", true);
  }

  @RequestMapping(value = "/all", method = RequestMethod.GET)
  public Object all() {
    return directoryService.all();
  }

//  @RequestMapping(value = "/clean", method = RequestMethod.GET)
//  public Object clean() {
//    directoryService.clean();
//    return Dict.create().set("result", true);
//  }

  @RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
  public Object search(@PathVariable String keyword) {
    return directoryService.search(keyword);
//    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//    searchSourceBuilder.query(QueryBuilders.queryStringQuery(keyword));
//
//    HighlightBuilder highlightBuilder = new HighlightBuilder();
//    //path属性高亮度
//    HighlightBuilder.Field highlightPath = new HighlightBuilder.Field("path");
//    highlightPath.highlighterType("unified");
//    highlightBuilder.field(highlightPath);
//    //title字段高亮度
//    HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("title");
//    highlightTitle.highlighterType("unified");
//    highlightBuilder.field(highlightTitle);
//    //content字段高亮度
//    HighlightBuilder.Field highlightContent = new HighlightBuilder.Field("content");
//    highlightContent.highlighterType("unified");
//    highlightBuilder.field(highlightContent);
//
//    //高亮度配置生效
//    searchSourceBuilder.highlighter(highlightBuilder);
//
//    log.info("搜索条件{}", searchSourceBuilder.toString());
//
//    //构建搜索功能
//    Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("gf")
//        .addType("news").build();
//    try {
//      //执行
//      SearchResult result = jestClient.execute(search);
//      return result.getHits(Article.class);
//    } catch (IOException e) {
//      log.error("{}", e.getLocalizedMessage());
//    }
//    return null;
  }
}
