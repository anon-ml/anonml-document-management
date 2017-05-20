package ml.anon.docmgmt.service;

import lombok.SneakyThrows;
import ml.anon.annotation.Chunker;
import ml.anon.docmgmt.controller.DocumentRepository;
import ml.anon.docmgmt.extraction.PlainTextExtractor;
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
        PlainTextExtractor extractor = PlainTextExtractor.build(file);
        PlainTextExtractor.ExtractionResult extract = extractor.extract(file.getInputStream());

        List<String> text = extract.getPaginated();
        List<String> chunked = chunker.chunk(extract.getFull());
        return repo.save(Document.builder().file(file.getBytes()).fileName(file.getOriginalFilename()).text(text).chunks(chunked).originalFileType(extract.getType()).build());
    }


}
