package ml.anon.docmgmt.controller;

import lombok.extern.java.Log;
import ml.anon.docmgmt.service.TokenizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by mirco on 13.07.17.
 */
@Log
@RestController
public class TokenizeController {

    @Autowired
    private TokenizerService tokenizerService;


    @RequestMapping(value = "/document/tokenize/text", method = RequestMethod.POST)
    public ResponseEntity<List<String>> doTokenize(@RequestParam("text") String toTokenize) {
        log.info("tokenize " + toTokenize);
        return ResponseEntity.ok(tokenizerService.tokenize(toTokenize));
    }


}
