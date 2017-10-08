package ml.anon.docmgmt.extraction;


import com.google.common.base.MoreObjects;
import ml.anon.anonymization.model.Anonymization;
import ml.anon.anonymization.model.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Uses the saved replacements to replace the strings in the final document.
 * Created by mirco on 01.06.17.
 */
public class Anonymizer {

    private static final ArrayList<Anonymization> EMPTY = new ArrayList<>();

    /**
     * Replaces the words with their anonymizations
     *
     * @param document the document
     */
    public String anonymize(String document, List<Anonymization> anon) {
        String anonymized = document.replace("&#160;", " ").replace("  ", " ");

        List<Anonymization> anonymizations = MoreObjects.firstNonNull(anon, EMPTY);

        for (Anonymization anonymization : anonymizations.stream().filter(a -> a.getStatus().equals(Status.ACCEPTED)).collect(Collectors.toSet())) {

            anonymized = anonymized
                    .replaceAll(anonymization.getData().getOriginal(),
                            anonymization.getData().getReplacement());
        }
        return anonymized;
    }


}
