package ml.anon.docmgmt.importer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ml.anon.docmgmt.extraction.FileTypes;
import ml.anon.docmgmt.extraction.IPlainTextExtractor;
import ml.anon.docmgmt.extraction.PlainTextExtractorFactory;


public class DOCXExtractorTest {

  private final IPlainTextExtractor extractor = PlainTextExtractorFactory.build(FileTypes.DOCX);

  @Test
  public void simpleExtraction() {
    String expected = "Das ist ein Test.";
    String actual = extractor.extract(this.getClass().getResourceAsStream("/simple.docx"));
    assertEquals(expected, actual.trim());

  }
}
