package ml.anon.documentmanagement.resource;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import ml.anon.documentmanagement.model.Document;
import ml.anon.exception.BadRequestException;
import ml.anon.resource.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    public File findOriginalById(String id) {
        String url = documentManagementUrl + "/document/{id}/original";
        return restTemplate.getForEntity(url, File.class, id).getBody();
    }

    public Document importDocument(String fileName, byte[] doc) throws BadRequestException {

        String base64 = Base64Utils.encodeToString(doc);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "multipart/form-data");
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("doc", base64);
        map.add("title", fileName);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Document> exchange = restTemplate
                .exchange(documentManagementUrl + "/document/import", HttpMethod.POST, entity,
                        Document.class);
        return exchange.getBody();
    }
}
