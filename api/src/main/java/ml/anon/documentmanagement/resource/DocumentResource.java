package ml.anon.documentmanagement.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ml.anon.documentmanagement.model.Document;
import ml.anon.documentmanagement.model.FileType;
import ml.anon.resource.Read;
import ml.anon.resource.Update;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import ml.anon.anonymization.model.Anonymization;

/**
 * Created by mirco on 11.06.17.
 */
@AllArgsConstructor
@Log
public class DocumentResource implements Read<Document>, Update<Document> {

  private final String IP = "http://127.0.0.1:9001";

  private RestTemplate restTemplate;

  public ArrayList<Anonymization> removeDuplicates(List<Anonymization> anonymizations) {

    ArrayList<Anonymization> noDuplicate = new ArrayList<Anonymization>();

    ObjectMapper mapper = new ObjectMapper();
    anonymizations = mapper.convertValue(anonymizations, new TypeReference<List<Anonymization>>() {
    });

    boolean contained = false;
    for (Anonymization anon1 : anonymizations) {
      for (Anonymization anon2 : noDuplicate) {
        if (anon1.getOriginal().equals(anon2.getOriginal())) {
          contained = true;
          break;
        }
      }
      if (!contained) {
        noDuplicate.add(anon1);
      }
      contained = false;
    }
    return noDuplicate;
  }

  @Override
  public Document update(String id, Document instance) {
    String url = IP + "/document/{id}";
    HttpEntity<Document> entity = new HttpEntity<>(instance);
    return restTemplate
        .exchange(url, HttpMethod.PUT, entity, Document.class, id).getBody();
  }

  @Override
  public Document findById(String id) {
    String url = IP + "/document/{id}";
    return restTemplate.getForEntity(url, Document.class, id).getBody();
  }

  @Override
  public List<Document> findAll() {
    ResponseEntity<List<Document>> result = restTemplate
        .exchange(IP + "/document/", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Document>>() {
            });
    return result.getBody();
  }
}
