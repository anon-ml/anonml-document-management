package ml.anon.docmgmt.extraction;

import ml.anon.exception.DocumentManagementException;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;


public final class PlainTextExtractorFactory {

    private final static Logger LOGGER = LoggerFactory.getLogger(PlainTextExtractorFactory.class);


    @SuppressWarnings("deprecation")
    public static IPlainTextExtractor build(MultipartFile file) throws DocumentManagementException {
        IPlainTextExtractor extractor;
        try {
            if (DocumentFactoryHelper.hasOOXMLHeader(new BufferedInputStream(file.getInputStream()))) {
                extractor = new DOCXExtractor();
                LOGGER.info("Creating DOCXExtractor");
            } else {
                extractor = new PDFExtractor(); // PDF
                LOGGER.info("Creating PDFExtractor");
            }
        } catch (IOException e) {
            LOGGER.error("Not supported: " + file.getOriginalFilename());
            throw new DocumentManagementException("Not supported: " + file.getOriginalFilename(), e);
        }
        return extractor;
    }
}
