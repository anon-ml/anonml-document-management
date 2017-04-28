package ml.anon.docmgmt.exceptions;

public class UnsupportedFileTypeException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 677660079421553743L;

  private final String type;

  public UnsupportedFileTypeException(String type) {
    this.type = type;
  }

  @Override
  public String getMessage() {
    return "File type " + type + " is not supported.";
  }
}
