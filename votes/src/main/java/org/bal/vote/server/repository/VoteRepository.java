package org.bal.vote.server.repository;

import org.bal.quote.proto.internal.Quote;
import org.bal.vote.proto.internal.Vote;
import org.springframework.cloud.sleuth.annotation.NewSpan;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface VoteRepository {

    @NewSpan
    void castVote(int quoteId);

    @NewSpan
    Integer getVote(int quoteId);

    @NewSpan
    List<Vote> getAllVotes(List<Quote> quotes);
}
