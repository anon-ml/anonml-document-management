package ml.anon.docmgmt.service;

import ml.anon.docmgmt.extraction.IPlainTextExtractor;
import ml.anon.docmgmt.extraction.PlainTextExtractorFactory;
import ml.anon.model.docmgmt.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
class DocumentImportService implements IDocumentImportService {


    private final static Logger LOGGER = LoggerFactory.getLogger(DocumentImportService.class);


    @Autowired
    private CrudRepository<Document, Long> repo;


    @Override
    public Document doImport(MultipartFile file) throws IOException {
        IPlainTextExtractor extractor = PlainTextExtractorFactory.build(file);
        Document doc = new Document();
        doc.setFile(file.getBytes());
        String text = extractor.extract(file.getInputStream());
        LOGGER.info("Extracted " + text);
        doc.setText(text);
        return repo.save(doc);
    }

}
