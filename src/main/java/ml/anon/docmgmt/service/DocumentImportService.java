package ml.anon.docmgmt.service;

import lombok.SneakyThrows;
import ml.anon.annotation.Chunker;
import ml.anon.docmgmt.extraction.IPlainTextExtractor;
import ml.anon.docmgmt.extraction.PlainTextExtractorFactory;
import ml.anon.docmgmt.model.DocumentRepository;
import ml.anon.model.docmgmt.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
class DocumentImportService implements IDocumentImportService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DocumentImportService.class);
    private final Chunker chunker = new Chunker();
    @Autowired
    private DocumentRepository repo;

    @SneakyThrows
    @Override
    public Document doImport(MultipartFile file) {
        IPlainTextExtractor extractor = PlainTextExtractorFactory.build(file);
        String text = extractor.extract(file.getInputStream());
        List<String> chunked = chunker.chunk(text);
        return repo.save(Document.builder().file(file.getBytes()).fileName(file.getOriginalFilename()).text(text).chunks(chunked).build());
    }

}
