package org.bal.frontend.controller;


import lombok.extern.slf4j.Slf4j;
import org.bal.frontend.dto.VoteDTO;
import org.bal.frontend.grpc.client.VoteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/api/vote/")
public class VoteController {

    @Autowired
    private VoteClient voteClient;


    @RequestMapping(value = "/castVote", method = {RequestMethod.POST},produces = "application/json")
    public String castVote(@RequestParam String quoteId) {
        log.info("Casting vote for Quote id {}", quoteId);
        String message = voteClient.castVote(Integer.parseInt(quoteId));
        log.info("Voted for the quote {}", message);
        return message;
    }

    @RequestMapping(value = "/tally", method = {RequestMethod.GET},produces = "application/json")
    public List<VoteDTO> tally() {
        List<VoteDTO> votes = new ArrayList<>();
        log.info("Retrieving all the votes");
        voteClient.voteResult().forEach(v -> {
            VoteDTO vDTO = new VoteDTO();
            vDTO.setQuote(v.getQuote());
            vDTO.setCount(v.getCount());
            vDTO.setQuoteId(v.getQuoteId());
            votes.add(vDTO);
        });
        log.info("Sorting votes from one with the highest vote to one with the lowest vote.");

        Collections.sort(votes);
        return votes;
    }


    @RequestMapping(value = "/tallyWithMultiget", method = {RequestMethod.GET},produces = "application/json")
    public List<VoteDTO> tallyWithPipeline() {
        List<VoteDTO> votes = new ArrayList<>();
        log.info("Retrieving all the votes");
        voteClient.voteResultWithMultiget().forEach(v -> {
            VoteDTO vDTO = new VoteDTO();
            vDTO.setQuote(v.getQuote());
            vDTO.setCount(v.getCount());
            vDTO.setQuoteId(v.getQuoteId());
            votes.add(vDTO);
        });
        log.info("Sorting votes from one with the highest vote to one with the lowest vote.");

        Collections.sort(votes);
        return votes;
    }

}
