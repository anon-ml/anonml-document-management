package ml.anon.documentmanagement.resource;

import ml.anon.anonymization.model.Replacement;
import ml.anon.exception.BadRequestException;
import ml.anon.resource.Create;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;

/**
 * API Endpoint class to use in different projects
 * Created by mirco on 13.06.17.
 */
public class ReplacementResource implements Create<Replacement> {

  private RestTemplate template = new RestTemplate();


  @Override
  public Replacement create(Replacement instance) throws BadRequestException {

    StringBuilder builder = new StringBuilder("http://127.0.0.1:9001/replacement");
    try {
      builder
          .append("?original=")
          .append(UriUtils.encode(instance.getOriginal(), "UTF-8")).append("&label=")
          .append(instance.getLabel().toString());
    } catch (UnsupportedEncodingException e) {
      throw new BadRequestException("Invalid parameters, e");
    }
    String url = builder.toString();

    ResponseEntity<Replacement> repl = template.getForEntity(url, Replacement.class);
    return repl.getBody();
  }
}
