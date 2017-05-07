package ml.anon.docmgmt.extraction;

import java.io.BufferedInputStream;
import java.io.IOException;

import ml.anon.exception.DocumentManagementException;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.springframework.web.multipart.MultipartFile;



public final class PlainTextExtractorFactory {



  @SuppressWarnings("deprecation")
  public static IPlainTextExtractor build(MultipartFile file) throws DocumentManagementException {
    IPlainTextExtractor extractor;
    try {
      if (DocumentFactoryHelper.hasOOXMLHeader(new BufferedInputStream(file.getInputStream()))) {
        extractor = new DOCXExtractor();
      } else {
        extractor = null; // PDF
      }
    } catch (IOException e) {
      throw new DocumentManagementException("Not supported: "+file.getOriginalFilename(), e);
    }
    return extractor;
  }
}
