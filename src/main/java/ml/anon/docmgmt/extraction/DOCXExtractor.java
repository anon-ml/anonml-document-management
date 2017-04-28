package ml.anon.docmgmt.extraction;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import ml.anon.docmgmt.exceptions.TextExtractionException;

/**
 * Extracts plain text from DOCX files
 * 
 * @author mirco
 *
 */
class DOCXExtractor implements IPlainTextExtractor {

  @Override
  public String extract(InputStream fis) throws TextExtractionException {
    XWPFDocument doc;
    try {
      doc = new XWPFDocument(fis);
      XWPFWordExtractor ex = new XWPFWordExtractor(doc);
      String text = ex.getText();
      ex.close();
      return text;
    } catch (IOException e) {
      throw new TextExtractionException(e);
    }

  }



}
