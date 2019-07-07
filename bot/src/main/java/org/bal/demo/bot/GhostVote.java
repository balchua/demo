package org.bal.demo.bot;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Random;

@Component
@Slf4j
public class GhostVote {

    @Value("${server.host}")
    private String serverHost;

    @Value("${server.port}")
    private int serverPort;

    private final OkHttpClient client = new OkHttpClient();
    private String VOTE_BASE_URL;
    private String QUOTE_BASE_URL;
    private Random rn = new Random();

    @Scheduled(fixedRateString = "${spring.application.castVoteFixedRate}")
    public void castGhostVote() {
        int randomQuoteId = rn.nextInt(16);
        log.debug("Casting vote {}", randomQuoteId);

        RequestBody formBody = new FormBody.Builder()
                .add("quoteId", String.valueOf(randomQuoteId))
                .build();

        Request request = new Request.Builder()
                .url(VOTE_BASE_URL + "/castVote")
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
        } catch (IOException e) {
            log.error("Unable to cast vote", e);
        }
    }

    @Scheduled(fixedRateString = "${spring.application.listQuotesFixedRate}")
    public void listQuotes() {
        log.debug("listing quotes ... ");


        Request request = new Request.Builder()
                .url(QUOTE_BASE_URL + "/list")
                .get()
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
        } catch (IOException e) {
            log.error("Unable to list quotes", e);
        }
    }


    @Scheduled(fixedRateString = "${spring.application.tallyVotesFixedRate}")
    public void tallyVotes() {
        log.debug("talying votes ... ");


        Request request = new Request.Builder()
                .url(VOTE_BASE_URL + "/tally")
                .get()
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
        } catch (IOException e) {
            log.error("Unable to list quotes", e);
        }
    }

    @PostConstruct
    private void init() {
        VOTE_BASE_URL = "http://" + serverHost + ":" + serverPort + "/api/vote";
        QUOTE_BASE_URL = "http://" + serverHost + ":" + serverPort + "/api/quote";
    }
}