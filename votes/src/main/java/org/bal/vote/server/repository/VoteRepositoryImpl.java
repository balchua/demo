package org.bal.vote.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.bal.quote.proto.internal.Quote;
import org.bal.vote.proto.internal.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.SpanName;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class VoteRepositoryImpl implements VoteRepository {

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Integer> valueOps;

    private static final String KEY_PREFIX = "quote:";

    @Override
    public void castVote(int quoteId) {
        Long votesCasted = valueOps.increment(KEY_PREFIX + String.valueOf(quoteId), 1l);
        log.info("Quote id: {} has {} many votes", String.valueOf(quoteId), votesCasted);
    }

    @Override
    public Integer getVote(int quoteId) {
        Integer votesCasted = valueOps.get(KEY_PREFIX + String.valueOf(quoteId));
        if (votesCasted == null) {
            votesCasted = Integer.valueOf(0);
        }
        log.info("Quote id: {} has {} many votes", String.valueOf(quoteId), votesCasted);
        return votesCasted;
    }

    @Override
    public List<Vote> getAllVotes(List<Quote> quotes) {

        List<String> keys = new ArrayList<>();
        quotes.forEach(quote -> {
           keys.add(KEY_PREFIX + quote.getId());
        });

        List<Integer> results = redisTemplate.opsForValue().multiGet(keys);

        log.info("Size of votes: {}", results.size());

        List<Vote> votes = new ArrayList<>();
        final AtomicInteger index = new AtomicInteger();
        quotes.forEach(quote -> {
            int count = index.getAndIncrement();
            Integer voteCount = 0;
            if (results.get(count) != null ){
                voteCount = Integer.valueOf((Integer)results.get(count));
            }
            log.info("Quote id: {} has {} many votes", String.valueOf(quote.getId()), voteCount);
            votes.add(Vote.newBuilder().setId(quote.getId()).setQuoteId(quote.getId()).setQuote(quote.getQuote()).setCount(voteCount).build());

        });

        return votes;


    }


}
