package ml.anon.docmgmt.importer;

import com.google.common.collect.Lists;
import de.tudarmstadt.lt.seg.Segment;
import de.tudarmstadt.lt.seg.sentence.ISentenceSplitter;
import de.tudarmstadt.lt.seg.sentence.LineSplitter;
import de.tudarmstadt.lt.seg.sentence.RuleSplitter;
import de.tudarmstadt.lt.seg.token.DiffTokenizer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mirco on 15.06.17.
 */
@Service
public class TokenizerService {

    private DiffTokenizer tokenizer = new DiffTokenizer();
    private ISentenceSplitter splitter = new RuleSplitter();

    public List<String> tokenize(String string) {
        List<String> tokens = new ArrayList<>();
        List<String> segments = Lists.newArrayList(splitter.init(string).sentences());

        for (String sentence : segments) {
            Iterable<String> sentenceTokens = tokenizer.init(sentence).filteredAndNormalizedTokens(3,0,false,false);
            tokens.addAll(Lists.newArrayList(sentenceTokens));
            tokens.add("");
        }
        return tokens;
    }
}
