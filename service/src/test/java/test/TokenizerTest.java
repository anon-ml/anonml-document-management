package test;

import ml.anon.docmgmt.service.TokenizerService;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by mirco on 10.07.17.
 */
@Ignore
public class TokenizerTest {

  private String str = "Das ist der erste Satz. Dies ist der zweite Satz. Eben jener ist der dritte Satz. Im Namen des STGB. §555";

  @Test
  public void tokenize() {
    TokenizerService s = new TokenizerService();
    List<String> tokenize = s.tokenize(str);
    assertThat(tokenize.size(), is(18));
    assertThat(tokenize.get(3), is("erste"));

  }
}
