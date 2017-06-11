package ml.anon.docmgmt.controller;

import lombok.extern.java.Log;
import ml.anon.docmgmt.export.Export;
import ml.anon.docmgmt.service.IDocumentImportService;
import ml.anon.model.docmgmt.Document;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
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

    @RequestMapping(value = "/document/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Document> update(@PathVariable String id, @RequestBody Document doc) {
        log.info("update id " + id);
        Document one = repo.findOne(id);
        one.setAnonymizations(doc.getAnonymizations());
        repo.save(one);
        return ResponseEntity.ok(one);
    }

    @GetMapping(value = "/document/{id}/")
    public Document getDocument(@PathVariable String id) {
        log.info("get by id " + id);
        Document one = repo.findOne(id);
        return one;
    }


    @GetMapping(value = "/document/{id}/export")
    public void export(HttpServletRequest request,
                       HttpServletResponse response, @PathVariable String id) throws IOException {
        log.info("Exporting document: " + id);
        Document one = repo.findOne(id);
        File export = Export.export(one);

        response.setContentType("text/html");
        response.addHeader("Content-Disposition", "attachment; filename=" + one.fileNameAs("html"));
        IOUtils.copy(new FileInputStream(export), response.getOutputStream());
        response.getOutputStream().flush();

    }


}
