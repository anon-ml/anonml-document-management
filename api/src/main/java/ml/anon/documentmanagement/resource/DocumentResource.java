package ml.anon.documentmanagement.resource;

import java.util.List;

import javax.annotation.Resource;
import ml.anon.documentmanagement.model.Document;
import ml.anon.resource.Read;
import ml.anon.resource.Update;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

/**
 * API Endpoint class to use in different projects
 * Created by mirco on 11.06.17.
 */
@Log
@Component
public class DocumentResource implements Read<Document>, Update<Document> {

  @Value("${documentmanagement.service.url}")
  private String documentManagementUrl;

  private RestTemplate restTemplate = new RestTemplate();


  @Override
  public Document update(String id, Document instance) {
    String url = documentManagementUrl + "/document/{id}";
    HttpEntity<Document> entity = new HttpEntity<>(instance);
    return restTemplate
        .exchange(url, HttpMethod.PUT, entity, Document.class, id).getBody();
  }

  @Override
  public Document findById(String id) {
    String url = documentManagementUrl + "/document/{id}";
    return restTemplate.getForEntity(url, Document.class, id).getBody();
  }

  @Override
  public List<Document> findAll() {
    ResponseEntity<List<Document>> result = restTemplate
        .exchange(documentManagementUrl + "/document/", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Document>>() {
            });
    return result.getBody();
  }
}
