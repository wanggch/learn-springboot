package jenny.learn.springboot.solr.model;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.SolrDocument;

@Data
@SolrDocument(collection = "sdck_core")
public class Doc {

  @Field("id")
  private String id;
  @Field("filename")
  private String name;
  @Field("content_type")
  private String fileType;
  @Field("attr_content")
  private String content;
}
