package ml.anon.docmgmt.extraction;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.parser.Token;
import ml.anon.anonymization.model.Anonymization;
import ml.anon.docmgmt.service.TokenizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Compares the originals of the anonymizations and dismisses duplicates
 * Created by matthias on 20.08.2017
 */
@Component
public class ListPreparation {

    @Autowired
    private TokenizerService tokenizerService;

    /**
     * Removes the duplicates by looking at the original value of the anonymization objects
     *
     * @param anonymizations list of anonymizations from ml and rulebased approaches
     * @return the list of anonymizations without duplicates
     */
    private ArrayList<Anonymization> removeDuplicates(List<Anonymization> anonymizations) {
        ArrayList<Anonymization> noDuplicate = new ArrayList<Anonymization>();

        ObjectMapper mapper = new ObjectMapper();
        anonymizations = mapper.convertValue(anonymizations, new TypeReference<List<Anonymization>>() {
        });
        boolean contained = false;
        for (Anonymization anon1 : anonymizations) {
            for (Anonymization anon2 : noDuplicate) {
                if (anon1.getData().getOriginal().equals(anon2.getData().getOriginal())) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                noDuplicate.add(anon1);
            }
            contained = false;
        }
        return noDuplicate;
    }

    /**
     * Sorts the Anonymization list by the number of tokens the original holds, to cope with encapsulated
     * anonymizations.
     *
     * @param anonymizations list to sort by token number
     * @return the sorted list of {@link Anonymization}s
     */
    private List<Anonymization> sortByTokenNumber(List<Anonymization> anonymizations) {

        HashMap<Anonymization, Integer> anonymizationTokenNumber = new HashMap<>();
        anonymizations.forEach(anonymization ->
                anonymizationTokenNumber.put(anonymization, tokenizerService
                        .tokenize(anonymization.getData().getOriginal()).size())
        );

        Collections.sort(anonymizations,
                (Anonymization o1, Anonymization o2) ->
                        anonymizationTokenNumber.get(o2).compareTo(anonymizationTokenNumber.get(o1))
        );
        return anonymizations;
    }


    /**
     * Applies the duplicate removal and sort by number of tokens
     *
     * @param anonymizations list to apply operations on
     * @return sorted {@link Anonymization} list without duplicates
     */
    public List<Anonymization> prepareAnonymizationList(List<Anonymization> anonymizations) {

        anonymizations = this.removeDuplicates(anonymizations);
        anonymizations = this.sortByTokenNumber(anonymizations);

        return anonymizations;
    }
}
