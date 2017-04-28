package ml.anon.docmgmt.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ml.anon.docmgmt.extraction.IPlainTextExtractor;
import ml.anon.docmgmt.extraction.PlainTextExtractorFactory;
import ml.anon.docmgmt.model.Document;

@Service
class DocumentImportService implements IDocumentImportService {

  @Autowired
  private CrudRepository<Document, Long> repo;


  @Override
  public Document doImport(MultipartFile file) throws IOException {
    IPlainTextExtractor extractor = PlainTextExtractorFactory.build(file);
    Document doc = new Document();
    doc.setOriginalFile(file.getBytes());
    String text = extractor.extract(file.getInputStream());
    doc.setPlainText(text);
    return repo.save(doc);
  }

}
