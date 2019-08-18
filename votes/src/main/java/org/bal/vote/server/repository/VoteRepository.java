package org.bal.vote.server.repository;

import org.bal.quote.proto.internal.Quote;
import org.bal.vote.proto.internal.Vote;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface VoteRepository {

    void castVote(int quoteId);

    Integer getVote(int quoteId);

    List<Vote> getAllVotes(List<Quote> quotes);
}
