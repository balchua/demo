package org.bal.vote.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.bal.quote.proto.internal.Quote;
import org.bal.vote.proto.internal.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class VoteRepositoryImpl implements VoteRepository {
    private RedisTemplate<String, Integer> redisTemplate;

    private ValueOperations<String, Integer> valueOps;

    private static final String KEY_PREFIX = "quote:";
    private static final String QUOTE_LOGS = "Quote id: {} has {} many votes";

    @Autowired
    public VoteRepositoryImpl(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
    }

    @Override
    public void castVote(int quoteId) {
        Long votesCasted = valueOps.increment(KEY_PREFIX + String.valueOf(quoteId), 1l);
        log.info(QUOTE_LOGS, String.valueOf(quoteId), votesCasted);
    }

    @Override
    public Integer getVote(int quoteId) {
        Integer votesCasted = valueOps.get(KEY_PREFIX + String.valueOf(quoteId));
        if (votesCasted == null) {
            votesCasted = Integer.valueOf(0);
        }
        log.info(QUOTE_LOGS, String.valueOf(quoteId), votesCasted);
        return votesCasted;
    }

    @Override
    public List<Vote> getAllVotes(List<Quote> quotes) {

        List<String> keys = new ArrayList<>();
        quotes.forEach(quote -> keys.add(KEY_PREFIX + quote.getId()));

        List<Integer> results = redisTemplate.opsForValue().multiGet(keys);
        if (results != null) {
            log.info("Size of votes: {}", results.size());
        }

        List<Vote> votes = new ArrayList<>();
        final var index = new AtomicInteger();
        quotes.forEach(quote -> {
            int count = index.getAndIncrement();
            Integer voteCount = 0;
            if (results.get(count) != null ){
                voteCount = results.get(count);
            }
            log.info(QUOTE_LOGS, String.valueOf(quote.getId()), voteCount);
            votes.add(Vote.newBuilder().setId(quote.getId()).setQuoteId(quote.getId()).setQuote(quote.getQuote()).setCount(voteCount).build());

        });

        return votes;


    }


}
