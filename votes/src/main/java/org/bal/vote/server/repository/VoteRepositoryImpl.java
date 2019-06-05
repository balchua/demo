package org.bal.vote.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class VoteRepositoryImpl implements VoteRepository {

    private ConcurrentHashMap<Integer, AtomicInteger> votes = new ConcurrentHashMap<>();


    @Override
    public void castVote(int quoteId) {
        log.info("Casting vote for Quote Id{} ", quoteId);

        AtomicInteger votesCasted = votes.get(quoteId);
        if (votesCasted == null) {
            votesCasted = new AtomicInteger(1);
        } else {
            votesCasted.incrementAndGet();
        }
        votes.put(quoteId, votesCasted);

    }

    @Override
    public Map<Integer, AtomicInteger> getAllVotes() {

        return Collections.unmodifiableMap(votes);
    }


}
