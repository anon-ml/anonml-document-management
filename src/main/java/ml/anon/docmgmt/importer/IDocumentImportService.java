package ml.anon.docmgmt.importer;

import ml.anon.model.docmgmt.Document;

import java.io.IOException;

/**
 * Extracts the text and saves the file and text to the database
 *
 * @author mirco
 */
public interface IDocumentImportService {

    Document doImport(byte[] file, String fileName) throws IOException;

}
