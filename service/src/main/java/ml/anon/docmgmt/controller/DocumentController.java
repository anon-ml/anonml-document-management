package ml.anon.docmgmt.controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import lombok.extern.java.Log;
import ml.anon.docmgmt.extraction.ListPreparation;
import ml.anon.docmgmt.service.DocumentExportService;
import ml.anon.docmgmt.service.DocumentImportService;

import ml.anon.docmgmt.service.TokenizerService;
import ml.anon.documentmanagement.model.Document;
import ml.anon.documentmanagement.model.DocumentState;
import ml.anon.exception.OutdatedException;
import ml.anon.exception.NotFoundException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@Log
public class DocumentController {

    @Autowired
    public DocumentImportService service;

    @Autowired
    private DocumentRepository repo;

    @Autowired
    private EntityLinks links;

    @Autowired
    private ListPreparation listPreparation = new ListPreparation();

    @Autowired
    private TokenizerService tokenizerService;

    @Autowired
    private DocumentExportService documentExportService;

    @RequestMapping(value = "/document/import", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<Document> bulkUpload(@RequestParam("doc") String file,
                                               @RequestParam("title") String title) throws IOException {
        StopWatch stopWatch = new StopWatch("Import document " + title);
        stopWatch.start();
        byte[] bytes = Base64Utils.decodeFromString(file);
        Document body = service.doImport(bytes, title);

        log.info("Result: " + body);
        stopWatch.stop();
        log.info(stopWatch.shortSummary());
        return ResponseEntity.ok(body);
    }

    @RequestMapping(value = "/document/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Document> update(@PathVariable String id, @RequestBody Document doc) throws OutdatedException {
        log.info("update id " + id);

        Document one = repo.findOne(id);
        checkVersion(doc);
        one.setState(doc.getState());
        one.setAnonymizations(listPreparation.prepareAnonymizationList(doc.getAnonymizations()));
        repo.save(one);
        return ResponseEntity.ok(one);
    }

    @GetMapping(value = "/document/{id}")
    public Document getDocument(@PathVariable String id) throws NotFoundException {
        log.info("get by id " + id);
        Document one = repo.findOne(id);
        if (one == null) {
            throw new NotFoundException();
        }
        return one;
    }

    @GetMapping(value = "/document/count")
    public int getCount() {
        return repo.findAll().size();
    }


    @DeleteMapping(value = "/document/{id}")
    public void delete(@PathVariable String id) {
        log.info("delete  id " + id);
        repo.delete(id);

    }


    @GetMapping(value = "/document")
    public List<Document> getAll(@RequestParam(name = "page", defaultValue = "0") int page) {
        log.info("get all documents");
        if (page >= 0) {
            return repo.findAll(new PageRequest(page, 10)).getContent();
        } else {
            return repo.findAll();
        }
    }

    @GetMapping(value = "/document/{id}/original")
    public void exportOriginal(HttpServletRequest request,
                               HttpServletResponse response, @PathVariable String id) throws IOException {
        log.info("Exporting original for document: " + id);
        Document one = repo.findOne(id);

        response.setContentType("application/" + one.getOriginalFileType().extension);
        response.addHeader("Content-Disposition", "attachment; filename=" + one.fileNameAs(one.getOriginalFileType().extension));
        IOUtils.copy(new ByteArrayInputStream(one.getFile()), response.getOutputStream());
        response.getOutputStream().flush();

    }

    @GetMapping(value = "/document/{id}/export")
    public void export(HttpServletRequest request,
                       HttpServletResponse response, @PathVariable String id) throws IOException {
        log.info("Exporting document: " + id);
        Document one = repo.findOne(id);
        one.setState(DocumentState.EXPORTED);
        repo.save(one);
        File export = documentExportService.export(one);


        response.setContentType("application/zip");
        response.addHeader("Content-Disposition", "attachment; filename=" + "wusa.zip");
        IOUtils.copy(new FileInputStream(export), response.getOutputStream());
        response.getOutputStream().flush();

    }

    public void checkVersion(Document updated) throws OutdatedException {
        Document current = repo.findOne(updated.getId());
        log.info("Check lock; current version: " + current.getVersion() + " provided version: " + updated.getVersion());
        if (current.getVersion() > updated.getVersion()) {
            throw new OutdatedException();
        }
    }


}
