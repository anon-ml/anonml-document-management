package ml.anon.docmgmt.extraction;

import lombok.extern.java.Log;
import ml.anon.exception.DocumentManagementException;
import ml.anon.model.docmgmt.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mirco on 11.05.17.
 */
@Log

public class PDFExtractor extends PlainTextExtractor {


    @Override
    public ExtractionResult extract(InputStream stream) throws DocumentManagementException {
        try {

            PDDocument pd = PDDocument.load(stream);
            final PDFTextStripper strip = new PDFTextStripper();

            String full = strip.getText(pd);
            strip.setAddMoreFormatting(true);
            strip.setPageEnd(PAGE_END);
            String paginatable = strip.getText(pd);
            pd.close();
            return ExtractionResult.builder().full(full).paginated(paginate(paginatable)).type(Document.FileType.PDF).build();
        } catch (IOException e) {
            throw new DocumentManagementException(e);
        }

    }


}
