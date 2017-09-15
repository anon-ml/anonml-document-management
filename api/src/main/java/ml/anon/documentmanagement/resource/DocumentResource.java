package ml.anon.documentmanagement.resource;

import java.util.List;

import javax.annotation.Resource;

import ml.anon.documentmanagement.model.Document;
import ml.anon.exception.BadRequestException;
import ml.anon.resource.Delete;
import ml.anon.resource.Read;
import ml.anon.resource.ReadAll;
import ml.anon.resource.Update;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.index.Index;
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
public class DocumentResource implements Read<Document>, ReadAll<Document>, Update<Document>, Delete {

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

    public int getCount() {
        String url = documentManagementUrl + "/document/count";
        return restTemplate.getForEntity(url, Integer.class).getBody();
    }

    @Override
    public Document findById(String id) {
        String url = documentManagementUrl + "/document/{id}";
        return restTemplate.getForEntity(url, Document.class, id).getBody();
    }


    @Override
    public List<Document> findAll(int page) {
        ResponseEntity<List<Document>> exchange = restTemplate.exchange(documentManagementUrl + "/document?page={page}", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Document>>() {
                }, page);

        return exchange.getBody();
    }

    @Override
    public void delete(String id) throws BadRequestException {
        restTemplate.delete(documentManagementUrl + "/document/{id}", id);
    }
}
