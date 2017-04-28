package ml.anon.docmgmt.extraction;

import org.springframework.web.multipart.MultipartFile;

import ml.anon.docmgmt.exceptions.UnsupportedFileTypeException;

public final class PlainTextExtractorFactory {



  public static IPlainTextExtractor build(MultipartFile file) throws UnsupportedFileTypeException {
    // TODO Auto-generated method stub
    return new DOCXExtractor();
  }
}
