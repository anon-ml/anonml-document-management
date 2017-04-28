package ml.anon.docmgmt.exceptions;

public class TextExtractionException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public TextExtractionException(Exception e) {
    initCause(e);
  }

}
