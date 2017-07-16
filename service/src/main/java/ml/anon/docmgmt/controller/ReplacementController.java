package ml.anon.docmgmt.controller;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.extern.java.Log;
import ml.anon.anonymization.model.Label;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mirco on 12.07.17.
 */
@RestController
@Log
public class ReplacementController {


    private Map<String, String> replacements = new HashMap<>();


    @GetMapping(path = "/replacement", params = {"original", "label"})
    public ResponseEntity<String> getReplacement(@RequestParam String original, @RequestParam String label) throws UnsupportedEncodingException {
        String str = UriUtils.decode(original, "UTF-8");
        String lookupKey = str + "_" + label;

        String existing = replacements.get(lookupKey);
        String replacement = existing;

        while (replacement == null) {
            String generate = generate(str, Label.valueOf(label));
            if (!replacements.values().contains(generate)) {
                replacement = generate;
            }
        }
        replacements.put(lookupKey, replacement);

        return ResponseEntity.ok(replacement);
    }

    private String generate(String original, Label label) {

        if (label == Label.PERSON) {
            return doGenerate(original, 1, true);
        } else if (label == Label.LOCATION) {
            return doGenerate(original, 2, false);
        } else if (label == Label.ORGANIZATION) {
            return doGenerate(original, RandomUtils.nextInt(2, 4), true);
        } else {
            return doGenerate(original, 3, false);
        }


    }

    private String doGenerate(String original, int count, boolean split) {
        List<String> strings = split ? Splitter.on(" ").splitToList(original.trim()) : Lists.newArrayList(original.trim());
        List<String> parts = new ArrayList<>();
        for (String string : strings) {
            String re = RandomStringUtils.randomAlphabetic(count).toUpperCase();
            re = split ? re + "." : re;
            parts.add(re);
        }
        String join = Joiner.on(split ? " " : "").join(parts);
        return join;
    }


    @Scheduled(fixedRate = 120000, initialDelay = 10000)
    private void clearMap() {
        log.info("clearing replacements");
        replacements.clear();
    }

}
