package ml.anon.docmgmt.controller;

import ml.anon.docmgmt.export.Export;
import ml.anon.docmgmt.misc.HtmlConvert;
import ml.anon.docmgmt.service.IDocumentImportService;
import ml.anon.model.docmgmt.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
        Document one = repo.findOne(id);
        return ResponseEntity.ok().body(Export.export(one));
    }


    @GetMapping(value = "/document/{id}/htmlView")
    public ResponseEntity<?> createHtmlView(@PathVariable String id) {
        Document one = repo.findOne(id);
        HtmlConvert htmlConvert = new HtmlConvert();
        String result = htmlConvert.toHtml(new ByteArrayInputStream(one.getFile()), one.getOriginalFileType());
        return ResponseEntity.ok(result);
    }


}
