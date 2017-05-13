package ml.anon.docmgmt.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import ml.anon.model.docmgmt.Document;

/**
 * Extracts the text and saves the file and text to the database
 *
 * @author mirco
 *
 */
public interface IDocumentImportService {

  Document doImport(MultipartFile file) throws IOException;

}
