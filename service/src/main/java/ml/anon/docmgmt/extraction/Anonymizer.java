package ml.anon.docmgmt.extraction;


import com.google.common.base.MoreObjects;
import ml.anon.anonymization.model.Anonymization;

import java.util.ArrayList;
import java.util.List;

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

    for (Anonymization anonymization : MoreObjects.firstNonNull(anon, EMPTY)) {
      anonymized = anonymized
          .replaceAll(anonymization.getData().getOriginal(),
              anonymization.getData().getReplacement());
    }
    return anonymized;
  }


}
