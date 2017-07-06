package ml.anon.docmgmt.importer;

import de.tudarmstadt.lt.seg.token.DiffTokenizer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mirco on 15.06.17.
 */
@Service
public class TokenizerService {

    private DiffTokenizer tokenizer = new DiffTokenizer();

    public List<String> tokenize(String string) {
        return tokenizer.init(string).stream().map(t -> t.asString()).collect(Collectors.toList());
    }
}
