package ml.anon.docmgmt.extraction;

import java.io.BufferedInputStream;
import java.io.IOException;

import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.springframework.web.multipart.MultipartFile;

import ml.anon.docmgmt.exceptions.UnsupportedFileTypeException;

public final class PlainTextExtractorFactory {



  @SuppressWarnings("deprecation")
  public static IPlainTextExtractor build(MultipartFile file) throws UnsupportedFileTypeException {
    IPlainTextExtractor extractor;
    try {
      if (DocumentFactoryHelper.hasOOXMLHeader(new BufferedInputStream(file.getInputStream()))) {
        extractor = new DOCXExtractor();
      } else {
        extractor = null; // PDF
      }
    } catch (IOException e) {
      throw new UnsupportedFileTypeException(file.getContentType());
    }
    return extractor;
  }
}
