import ml.anon.docmgmt.extraction.PDFExtractor;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by mirco on 20.05.17.
 */


public class PDFExtractorTest {


    @Test
    public void extract() {
        InputStream resource = IntegrationTest.class.getResourceAsStream("vr_44_loesung.pdf");
        PDFExtractor pdfExtractor = new PDFExtractor();
        String extract = pdfExtractor.extract(resource);
        assertThat(extract, notNullValue());
        assertThat(extract, containsString("IM NAMEN DES VOLKES"));
        assertThat(extract.length(), greaterThan(2000));
        assertThat(extract, not(containsString("ヒラギノ明朝")));

    }
}
