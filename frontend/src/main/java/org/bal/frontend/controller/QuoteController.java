package org.bal.frontend.controller;


import lombok.extern.slf4j.Slf4j;
import org.bal.frontend.dto.QuoteDTO;
import org.bal.frontend.grpc.client.QuoteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/api/quote/")
public class QuoteController {

    @Autowired
    private QuoteClient quoteClient;


    @RequestMapping(value = "/list", method = {RequestMethod.GET},produces = "application/json")
    public List<QuoteDTO> quotes() {
        List<QuoteDTO> quotes = new ArrayList<>();
        log.debug("Retrieving all the quotes");
        quoteClient.allQuotes().forEach(q -> {
            QuoteDTO qDTO = new QuoteDTO();
            qDTO.setQuote(q.getQuote());
            qDTO.setQuoteId(q.getId());
            quotes.add(qDTO);
        });
        return quotes;
    }


}
