package ml.anon.docmgmt.extraction;


import com.google.common.base.MoreObjects;
import ml.anon.anonymization.model.Anonymization;
import ml.anon.anonymization.model.Replacement;
import ml.anon.anonymization.model.Status;
import ml.anon.docmgmt.service.TokenizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Uses the saved replacements to replace the strings in the final document.
 * Created by mirco on 01.06.17.
 * Updated by matthias on 14.10.17.
 */
@Component
public class Anonymizer {

    private static final ArrayList<Anonymization> EMPTY = new ArrayList<>();

    @Autowired
    private ListPreparation listPreparation;

    @Autowired
    private TokenizerService tokenizerService;


    private String anonymized;
    private List<Anonymization> anonymizations;
    private List<Anonymization> newAnons;
    private List<String> foundParts;

    /**
     * Which token length is ignored (1 means simple chars are ignored)
     */
    private int treshold = 1;


    /**
     * Replaces the words with their anonymizations
     *
     * @param document the document
     */
    public String anonymize(String document, List<Anonymization> anon) {

        anonymizations = MoreObjects.firstNonNull(anon, EMPTY);
        anonymizations = anonymizations.stream().filter(a -> a.getStatus().equals(Status.ACCEPTED)).collect(Collectors.toList());
        anonymizations = listPreparation.prepareAnonymizationList(anonymizations);

        anonymized = document.replace("&#160;", " ").replace("  ", " ");
        newAnons = new ArrayList<>();
        foundParts = new ArrayList<>();

        for (Anonymization anonymization : anonymizations) {

            if (anonymization.getData().getOriginal().contains("\n")) {

                this.handleLineBreaks(anonymization);

            } else {
                this.replaceOnlyText(anonymization.getData().getOriginal(), anonymization.getData().getReplacement(), false);
            }
        }

        for (Anonymization newAnon : newAnons) {

            this.replaceOnlyText(newAnon.getData().getOriginal(), newAnon.getData().getReplacement(), true);
        }

        return anonymized;
    }

    /**
     * Searches just for the text regions in the area from > to the </p> tag to not mess up the format
     * @param original to replace
     * @param replacement replacement for the original
     */
    private void replaceOnlyText(String original, String replacement, boolean newAnon) {

        Pattern pattern = Pattern.compile("(?<=>)(.*)(?=</p>)");
        Matcher matcher = pattern.matcher(anonymized);

        StringBuffer newText = new StringBuffer();
        if(!newAnon){
            original = Pattern.quote(original);
        }
        while(matcher.find()){
            matcher.appendReplacement(newText, "");
            String temp = matcher.group(1);


            newText.append(temp.replaceAll(original, replacement));

        }
        matcher.appendTail(newText);
        anonymized = newText.toString();
    }


    /**
     * Checks if a original with containing line break can be found and replaced with replacing \n with <br/>. If
     * yes it is replaced if not it is necessary to split up the original at the line break.
     * @param anonymization the anonymization object which is looked at
     */
    private void handleLineBreaks(Anonymization anonymization) {

        String originalLBReplaced = anonymization.getData().getOriginal()
                .replaceAll("(\r\n|\n)", "<br/>");

        if (anonymized.contains(originalLBReplaced)) {

            this.replaceOnlyText(originalLBReplaced, anonymization.getData().getReplacement(), false);
        } else {

            this.handleLineBreakNoBr(anonymization);
        }
    }

    /**
     * Is called if the original can not be found with replacing \n with <br/>. Splits the original at the line break
     * and tries to replace the resulting parts separately. If they are found they are replaced with the replacement
     * but indexed to see which belong together.
     * @param anonymization the anonymization object
     */
    private void handleLineBreakNoBr(Anonymization anonymization) {

        String[] partsOfOriginal = anonymization.getData().getOriginal().split("\\n");

        int count = 0;
        for (String part : partsOfOriginal) {

            part = part.trim();
            if(part.equals("")){
                continue;
            }

            if (foundParts.contains(part)) {
                continue;
            }

            if (anonymized.contains(part) && (part.toCharArray().length > treshold)) {
                foundParts.add(part);

                this.replaceOnlyText(part, anonymization.getData().getReplacement() + "-(" + (++count) + ")", false);

            } else {

                count = this.findMatchSequence(anonymization, count, part);
            }
        }
    }


    /**
     * Finds the biggest matching token sequences and adds them to newAnons as Anonymization object to later on
     * replace it by the original replacement with index. Ignores tokens with length of treshold.
     * @param anonymization the anonymization object
     * @param count index of replacement
     * @param part seperated part of the original devided by \n
     * @return the highest index of replacement
     */
    private int findMatchSequence(Anonymization anonymization, int count, String part) {
        List<String> tokens = tokenizerService.tokenize(part);
        tokens.remove("");

        int nextStart = 0;
        for (int i = tokens.size(); i > nextStart; --i) {
            String match = "";

            for (int j = nextStart; j < i; ++j) {
                match += Pattern.quote(tokens.get(j));
                match += "\\s*";
            }

            Pattern pattern = Pattern.compile(match);
            Matcher matcher = pattern.matcher(anonymized);

            if (matcher.find()) {
                if ((anonymized.substring(matcher.start(), matcher.end()).trim().toCharArray().length) > treshold) {
                    newAnons.add(Anonymization.builder().data(Replacement.builder().original(match)
                            .replacement(anonymization.getData().getReplacement() + "-(" + (++count) + ")").build()).build());
                }
                nextStart = i;
                i = tokens.size() + 1;
                continue;

            }
        }
        return count;
    }


}
