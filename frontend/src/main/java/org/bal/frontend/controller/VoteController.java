package org.bal.frontend.controller;


import lombok.extern.slf4j.Slf4j;
import org.bal.frontend.dto.VoteDTO;
import org.bal.frontend.grpc.client.VoteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/api/vote/")
public class VoteController {

    @Autowired
    private VoteClient voteClient;


    @RequestMapping(value = "/castVote", method = {RequestMethod.POST},produces = "application/json")
    public String castVote(@RequestParam int quoteId) {
        log.debug("Casting votes");
        String message = voteClient.castVote(quoteId);
        return message;
    }

    @RequestMapping(value = "/tally", method = {RequestMethod.GET},produces = "application/json")
    public List<VoteDTO> tally() {
        List<VoteDTO> votes = new ArrayList<>();
        log.debug("Retrieving all the votes");
        voteClient.voteResult().forEach(v -> {
            VoteDTO vDTO = new VoteDTO();
            vDTO.setQuote(v.getQuote());
            vDTO.setCount(v.getCount());
            vDTO.setQuoteId(v.getQuoteId());
            votes.add(vDTO);
        });
        return votes;
    }

}
