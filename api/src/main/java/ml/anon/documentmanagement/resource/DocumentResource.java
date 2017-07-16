package ml.anon.documentmanagement.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ml.anon.documentmanagement.model.Document;
import ml.anon.documentmanagement.model.FileType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import ml.anon.model.anonymization.Anonymization;

/**
 * Created by mirco on 11.06.17.
 */
@AllArgsConstructor
@Log
public class DocumentResource {

    private final String IP = "http://127.0.0.1:9001";

    private RestTemplate restTemplate;

    public ResponseEntity<Document> getDocument(String id) {
        String url = IP + "/document/" + id;
        log.info(url);
        return restTemplate.getForEntity(url, Document.class, new Object[]{});
    }

    public Document updateDocument(String id, List<Anonymization> anon) {
        anon = this.removeDuplicates(anon);
        
        Map<String, Object> params = new HashMap<>();
        params.put("anonymizations", anon);
        restTemplate.put(IP + "/document/" + id, Document.builder().anonymizations(anon).id(id).originalFileType(FileType.PDF).build(), params);

        return getDocument(id).getBody();
    }
    
    public ArrayList<Anonymization> removeDuplicates(List<Anonymization> anonymizations) {
      
      ArrayList<Anonymization> noDuplicate = new ArrayList<Anonymization>();
      
      ObjectMapper mapper = new ObjectMapper();
      anonymizations = mapper.convertValue(anonymizations, new TypeReference<List<Anonymization>>(){});
      
      boolean contained = false;
      for (Anonymization anon1 : anonymizations) {
        for (Anonymization anon2 : noDuplicate) {
          if(anon1.getOriginal().equals(anon2.getOriginal())){
            contained = true;
            break;
          }
        }
        if(!contained){
          noDuplicate.add(anon1);
        }
        contained = false;
      }
     return noDuplicate;
    }
}
