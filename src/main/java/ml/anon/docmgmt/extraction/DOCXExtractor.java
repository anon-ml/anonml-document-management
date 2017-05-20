package ml.anon.docmgmt.extraction;

import ml.anon.exception.DocumentManagementException;
import ml.anon.model.docmgmt.Document;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.io.InputStream;

/**
 * Extracts plain text from DOCX files
 *
 * @author mirco
 */
class DOCXExtractor extends PlainTextExtractor {

    @Override
    public ExtractionResult extract(InputStream fis) throws DocumentManagementException {
        XWPFDocument doc;
        try {
            doc = new XWPFDocument(fis);
            XWPFWordExtractor ex = new XWPFWordExtractor(doc);

            String text = ex.getText();
            ex.close();
            return ExtractionResult.builder().full(text).type(Document.FileType.DOCX).build();
        } catch (IOException e) {
            throw new DocumentManagementException(e);
        }

    }


}
