package ml.anon.docmgmt.extraction;

import com.google.common.base.MoreObjects;
import ml.anon.model.anonymization.Anonymization;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mirco on 01.06.17.
 */
public class Anonymizer {

    private static final ArrayList<Anonymization> EMPTY = new ArrayList<>();

    /**
     * Replaces the words with their anonymizations
     *
     * @param document the document
     * @param anon
     * @return
     */
    public String anonymize(String document, List<Anonymization> anon) {
        String anonymized = document.replace("&#160;", " ").replace("  ", " ");

        for (Anonymization anonymization : MoreObjects.firstNonNull(anon, EMPTY)) {
            anonymized = anonymized.replaceAll(anonymization.getOriginal(), anonymization.getReplacement());
        }
        return anonymized;
    }


}
