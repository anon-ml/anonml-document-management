package ml.anon.docmgmt.controller;

import lombok.extern.java.Log;
import ml.anon.docmgmt.export.Export;
import ml.anon.docmgmt.service.IDocumentImportService;
import ml.anon.model.docmgmt.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RepositoryRestController
@Log
public class DocumentController {

    @Autowired
    private IDocumentImportService service;

    @Autowired
    private DocumentRepository repo;

    @Autowired
    private EntityLinks links;


    @RequestMapping(value = "/document/import", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<Document> bulkUpload(@RequestParam("doc") String file, @RequestParam("title") String title) throws IOException {
        log.info("Importing " + title);
        byte[] bytes = Base64Utils.decodeFromString(file);
        Document body = service.doImport(bytes, title);
        log.info("Result: " + body);
        return ResponseEntity.ok(body);
    }


    @GetMapping(value = "/document/{id}/export")
    @ResponseBody
    public ResponseEntity<InputStreamResource> export(@PathVariable String id) throws FileNotFoundException {
        log.info("Exporting document " + id);
        Document one = repo.findOne(id);
        File export = Export.export(one);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XHTML_XML);
        headers.setContentLength(export.length());
        headers.setContentDispositionFormData("attachment", one.getFileName());

        InputStreamResource res = new InputStreamResource(new FileInputStream(export));


        return new ResponseEntity<InputStreamResource>(res, headers, HttpStatus.OK);
    }


}
