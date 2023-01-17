package jenny.learn.springboot.solr.utils;

import java.io.File;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;

public class SolrUtils {

  public static void main(String[] args) throws Exception {
//    String filePath = "/Users/wanggc/DevTools/solr-8.11.0/example/exampledocs/solr-word.pdf";
    String filePath = "/Users/wanggc/Downloads/离职注意事项告知书.docx";
    String contentType = "application/pdf";
    String solrUrl = "http://localhost:8983/solr/sdck_core";
    SolrClient client = new HttpSolrClient.Builder(solrUrl).build();

//    final SolrInputDocument solrInputDocument = new SolrInputDocument();

    ContentStreamUpdateRequest request = new ContentStreamUpdateRequest("/update/extract");
    File file = new File(filePath);
    request.addFile(file, contentType);
    request.setParam("literal.id", file.getName());
    request.setParam("literal.path", file.getAbsolutePath());
    request.setParam("literal.fileName", file.getName());
    request.setParam("fmap.content", "attr_content");
    request.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);

    client.request(request);

    QueryResponse rsp = client.query(new SolrQuery("*:*"));
    System.out.println(rsp);
  }

  public static String getFileContentType(String filename) {
    String contentType = "";
    String prefix = filename.substring(filename.lastIndexOf(".") + 1);
    if (prefix.equals("xlsx")) {
      contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    } else if (prefix.equals("pdf")) {
      contentType = "application/pdf";
    } else if (prefix.equals("doc")) {
      contentType = "application/msword";
    } else if (prefix.equals("txt")) {
      contentType = "text/plain";
    } else if (prefix.equals("xls")) {
      contentType = "application/vnd.ms-excel";
    } else if (prefix.equals("docx")) {
      contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    } else if (prefix.equals("ppt")) {
      contentType = "application/vnd.ms-powerpoint";
    } else if (prefix.equals("pptx")) {
      contentType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    }

    else {
      contentType = "othertype";
    }

    return contentType;
  }
}
