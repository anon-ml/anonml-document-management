package ml.anon.docmgmt.controller;

import ml.anon.model.docmgmt.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "document", path = "document")
public interface DocumentRepository extends MongoRepository<Document, String> {


}