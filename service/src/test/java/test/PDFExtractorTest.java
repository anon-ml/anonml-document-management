package test;

import ml.anon.docmgmt.extraction.ExtractionResult;
import ml.anon.docmgmt.extraction.PDFExtractor;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by mirco on 20.05.17.
 */


public class PDFExtractorTest {


    @Test
    public void pdfExtraction() {
        InputStream resource = IntegrationTest.class.getResourceAsStream("vr_44_loesung.pdf");
        PDFExtractor pdfExtractor = new PDFExtractor();
        ExtractionResult extract = pdfExtractor.extract(resource);
        assertThat(extract, notNullValue());

        assertThat(extract.getFullText(), containsString("IM NAMEN DES VOLKES"));
        assertThat(extract.getFullText().length(), Matchers.greaterThan(2000));
        assertThat(extract.getFullText(), not(containsString("ヒラギノ明朝")));

        assertThat(extract.getPaginated(), notNullValue());
        assertThat(extract.getPaginated().size(), Matchers.is(21));
        assertThat(extract.getPaginated().get(0), containsString("IM NAMEN DES VOLKES"));

    }


}
