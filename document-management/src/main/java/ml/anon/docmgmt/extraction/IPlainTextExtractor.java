package ml.anon.docmgmt.extraction;

import java.io.InputStream;

import ml.anon.docmgmt.exceptions.TextExtractionException;

public interface IPlainTextExtractor {

  /**
   * Returns the plain text for NER
   *
   * @param stream the original file
   * @return the plain text
   * @throws TextExtractionException on IO or extraction error
   */
  public String extract(InputStream stream) throws TextExtractionException;

}
