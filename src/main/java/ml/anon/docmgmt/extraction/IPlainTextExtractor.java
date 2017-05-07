package ml.anon.docmgmt.extraction;

import ml.anon.exception.DocumentManagementException;

import java.io.InputStream;


public interface IPlainTextExtractor {

  /**
   * Returns the plain text for NER
   *
   * @param stream the original file
   * @return the plain text
   * @throws DocumentManagementException if the text can not be extracted
   */
  public String extract(InputStream stream) throws DocumentManagementException;

}
