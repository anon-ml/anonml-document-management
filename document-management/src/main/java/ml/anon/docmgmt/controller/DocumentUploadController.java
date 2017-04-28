package ml.anon.docmgmt.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ml.anon.docmgmt.model.Document;
import ml.anon.docmgmt.service.IDocumentImportService;

@RestController
public class DocumentUploadController {

  @Autowired
  private IDocumentImportService service;

  @Autowired
  private EntityLinks links;

  // curl -i -X POST -H "Content-Type: multipart/form-data" -F "file=@simple.docx"
  // http://localhost:8080/upload



  @PostMapping("/upload")
  public ResponseEntity<Link> upload(@RequestParam("file") MultipartFile file) throws IOException {
    Document imported = service.doImport(file);
    Link link = links.linkToSingleResource(Document.class, imported.getId());
    return new ResponseEntity<Link>(link, HttpStatus.CREATED);

  }

}
