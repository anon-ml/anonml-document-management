package ml.anon.docmgmt.extraction;

import ml.anon.exception.DocumentManagementException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mirco on 11.05.17.
 */
public class PDFExtractor implements IPlainTextExtractor {


    @Override
    public String extract(InputStream stream) throws DocumentManagementException {
        try {
            PDDocument pd = PDDocument.load(stream);
            final PDFTextStripper strip = new PDFTextStripper();
            return strip.getText(pd);
        } catch (IOException e) {
            throw new DocumentManagementException(e);
        }

    }
}
