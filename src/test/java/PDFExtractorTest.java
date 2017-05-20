import ml.anon.docmgmt.extraction.PDFExtractor;
import ml.anon.docmgmt.extraction.PlainTextExtractor;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
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
        PlainTextExtractor.ExtractionResult extract = pdfExtractor.extract(resource);
        assertThat(extract, notNullValue());

        assertThat(extract.getFull(), containsString("IM NAMEN DES VOLKES"));
        assertThat(extract.getFull().length(), greaterThan(2000));
        assertThat(extract.getFull(), not(containsString("ヒラギノ明朝")));

        assertThat(extract.getPaginated(), notNullValue());
        assertThat(extract.getPaginated().size(), is(21));
        assertThat(extract.getPaginated().get(0), containsString("IM NAMEN DES VOLKES"));

    }


}
