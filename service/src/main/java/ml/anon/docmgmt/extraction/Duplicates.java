package ml.anon.docmgmt.extraction;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ml.anon.anonymization.model.Anonymization;

import java.util.ArrayList;
import java.util.List;

/**
 * Compares the originals of the anonymizations and dismisses duplicates
 * Created by matthias on 20.08.2017
 */
public class Duplicates {

    /**
     * Removes the duplicates by looking at the original value of the anonymization objects
     * @param anonymizations list of anonymizations from ml and rulebased approaches
     * @return the list of anonymizations without duplicates
     */
    public ArrayList<Anonymization> removeDuplicates(List<Anonymization> anonymizations) {
        ArrayList<Anonymization> noDuplicate = new ArrayList<Anonymization>();

        ObjectMapper mapper = new ObjectMapper();
        anonymizations = mapper.convertValue(anonymizations, new TypeReference<List<Anonymization>>(){});
        boolean contained = false;
        for (Anonymization anon1 : anonymizations) {
            for (Anonymization anon2 : noDuplicate) {
                if(anon1.getData().getOriginal().equals(anon2.getData().getOriginal())){
                    contained = true;
                    break;
                }
            }
            if(!contained){
                noDuplicate.add(anon1);
            }
            contained = false;
        }
        return noDuplicate;
    }
}
