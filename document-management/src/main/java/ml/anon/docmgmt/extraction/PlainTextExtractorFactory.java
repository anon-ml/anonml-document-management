package ml.anon.docmgmt.extraction;

import ml.anon.docmgmt.exceptions.UnsupportedFileTypeException;

public final class PlainTextExtractorFactory {

  public static IPlainTextExtractor build(FileTypes type) throws UnsupportedFileTypeException {
    if (type == FileTypes.DOCX) {
      return new DOCXExtractor();
    } else {
      throw new UnsupportedFileTypeException(type.toString());
    }

  }
}
