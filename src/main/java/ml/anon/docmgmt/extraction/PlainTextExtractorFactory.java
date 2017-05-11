package ml.anon.docmgmt.extraction;

import ml.anon.exception.DocumentManagementException;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;


public final class PlainTextExtractorFactory {


    @SuppressWarnings("deprecation")
    public static IPlainTextExtractor build(MultipartFile file) throws DocumentManagementException {
        IPlainTextExtractor extractor;
        try {
            if (DocumentFactoryHelper.hasOOXMLHeader(new BufferedInputStream(file.getInputStream()))) {
                extractor = new DOCXExtractor();
            } else {
                extractor = new PDFExtractor(); // PDF
            }
        } catch (IOException e) {
            throw new DocumentManagementException("Not supported: " + file.getOriginalFilename(), e);
        }
        return extractor;
    }
}
