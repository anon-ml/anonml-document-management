package ml.anon.docmgmt.controller;

import ml.anon.model.docmgmt.Document;
import ml.anon.docmgmt.service.IDocumentImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RepositoryRestController
public class DocumentController {

    @Autowired
    private IDocumentImportService service;

    @Autowired
    private EntityLinks links;

    // curl -i -X POST -H "Content-Type: multipart/form-data" -F "file=@simple.docx"
    // http://localhost:8080/upload


    @RequestMapping(value = "/document/import", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<?> upload(@RequestParam("doc") MultipartFile file) throws IOException {
        Document imported = service.doImport(file);
        Link link = links.linkToSingleResource(Document.class, imported.getId());
        return ResponseEntity.created(URI.create(link.getHref())).build();
    }

    @PostMapping("/document/{id}/export")
    public ResponseEntity<?> export(@PathVariable Long id) {
        return ResponseEntity.ok().body("Export " + id);
    }


}
