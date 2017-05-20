package ml.anon.docmgmt.controller;

import ml.anon.docmgmt.model.DocumentRepository;
import ml.anon.docmgmt.service.IDocumentImportService;
import ml.anon.model.docmgmt.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;

@RepositoryRestController
public class DocumentController {

    @Autowired
    private IDocumentImportService service;

    @Autowired
    private DocumentRepository repo;

    @Autowired
    private EntityLinks links;

    @RequestMapping(value = "/document/import", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<?> bulkUpload(@RequestParam("doc") MultipartFile... file) throws IOException {
        for (MultipartFile multipartFile : file) {
            Document imported = service.doImport(multipartFile);
        }
        return ResponseEntity.created(links.linkFor(Document.class).toUri()).build();
    }

    @PostMapping("/document/{id}/export")
    public ResponseEntity<?> export(@PathVariable String id) {
        if ("all".equals(id)) {
            return ResponseEntity.ok(repo.findAll().stream().map(Document::getText).collect(Collectors.toList()));

        } else {
            return ResponseEntity.ok().body(repo.findOne(id));
        }

    }


}
