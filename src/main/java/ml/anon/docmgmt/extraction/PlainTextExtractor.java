package ml.anon.docmgmt.extraction;

import com.google.common.base.Splitter;
import lombok.Builder;
import lombok.Data;
import lombok.extern.java.Log;
import ml.anon.exception.DocumentManagementException;
import ml.anon.model.docmgmt.Document;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Log
public abstract class PlainTextExtractor {
    protected static final String PAGE_END = "###+++ PAGE END +++###";

    @SuppressWarnings("deprecation")
    public static PlainTextExtractor build(MultipartFile file) throws DocumentManagementException {
        PlainTextExtractor extractor;
        try {
            if (DocumentFactoryHelper.hasOOXMLHeader(new BufferedInputStream(file.getInputStream()))) {
                extractor = new DOCXExtractor();
                log.info("Creating DOCXExtractor");
            } else {
                extractor = new PDFExtractor(); // PDF
                log.info("Creating PDFExtractor");
            }
        } catch (IOException e) {
            log.severe("Not supported: " + file.getOriginalFilename());
            throw new DocumentManagementException("Not supported: " + file.getOriginalFilename(), e);
        }
        return extractor;
    }

    /**
     * Returns the plain text for NER
     *
     * @param stream the original file
     * @return list of plain text pages
     * @throws DocumentManagementException if the text can not be extracted
     */
    public abstract ExtractionResult extract(InputStream stream) throws DocumentManagementException;

    protected final List<String> paginate(String text) {
        return Splitter.on(PAGE_END).splitToList(text);
    }

    @Data
    @Builder
    public static class ExtractionResult {

        private final List<String> paginated;
        private final String full;
        private Document.FileType type;

    }
}


