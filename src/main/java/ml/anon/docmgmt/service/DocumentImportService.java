package ml.anon.docmgmt.service;

import lombok.SneakyThrows;
import ml.anon.annotation.Chunker;
import ml.anon.docmgmt.controller.DocumentRepository;
import ml.anon.docmgmt.extraction.ExtractionResult;
import ml.anon.docmgmt.extraction.PlainTextExtractor;
import ml.anon.model.docmgmt.Document;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@Service
class DocumentImportService implements IDocumentImportService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DocumentImportService.class);
    private final Chunker chunker = new Chunker();
    @Autowired
    private DocumentRepository repo;

    @SneakyThrows
    @Override
    public Document doImport(byte[] file, String fileName) {
        PlainTextExtractor extractor = PlainTextExtractor.build(new ByteArrayInputStream(file));

        File tempFile = File.createTempFile(fileName, null);
        FileUtils.writeByteArrayToFile(tempFile, file);
        ExtractionResult extract = extractor.extract(new FileInputStream(tempFile));

        List<String> text = extract.getPaginated();
        List<String> chunked = chunker.chunk(extract.getFullText());
        return repo.save(Document.builder().file(file).fileName(fileName).text(text).chunks(chunked).originalFileType(extract.getType()).build());
    }


}
