package ml.anon.docmgmt.importer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import ml.anon.docmgmt.extraction.IPlainTextExtractor;
import ml.anon.docmgmt.extraction.PlainTextExtractorFactory;


public class DOCXExtractorTest {

  private final IPlainTextExtractor extractor = PlainTextExtractorFactory.build(null);

  @Test
  public void simpleExtraction() throws IOException {
    String expected = "Das ist ein Test.\n";
    String actual = extractor.extract(this.getClass().getResourceAsStream("/2.docx"));
    assertEquals(expected, actual);

  }
}
