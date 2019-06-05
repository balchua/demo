package org.bal.vote.server.repository;

import org.bal.vote.proto.internal.Vote;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface VoteRepository {

    void castVote(int quoteId);

    Map<Integer, AtomicInteger> getAllVotes();
}
