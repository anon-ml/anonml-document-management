package ml.anon.docmgmt.extraction;

import com.google.common.base.Splitter;
import lombok.extern.java.Log;
import ml.anon.exception.DocumentManagementException;
import ml.anon.model.docmgmt.FileType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by mirco on 11.05.17.
 */
@Log
public class PDFExtractor {

    protected static final String PAGE_END = "###+++ PAGE END +++###";

    public ExtractionResult extract(InputStream stream) throws DocumentManagementException {
        try {

            PDDocument pd = PDDocument.load(stream);
            final PDFTextStripper strip = new PDFTextStripper();

            String full = strip.getText(pd);
            strip.setAddMoreFormatting(true);
            strip.setPageEnd(PAGE_END);
            String paginatable = strip.getText(pd);
            pd.close();
            return ExtractionResult.builder().fullText(full).paginated(paginate(paginatable)).type(FileType.PDF).build();
        } catch (IOException e) {
            throw new DocumentManagementException(e);
        }

    }

    /**
     * Create a list with plain text pages
     *
     * @param text
     * @return
     */
    protected final List<String> paginate(String text) {
        return Splitter.on(PAGE_END).splitToList(text);
    }


}
