package org.bal.vote.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.bal.vote.annotation.Traced;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class VoteRepositoryImpl implements VoteRepository {


    @Resource(name = "redisTemplate")
    private ValueOperations<String, Integer> valueOps;

    private static final String KEY_PREFIX = "quote:";

    @Override
    @Traced(serviceName = "redis-cast-vote")
    public void castVote(int quoteId) {
        Long votesCasted = valueOps.increment(KEY_PREFIX + String.valueOf(quoteId), 1l);
        log.info("Quote id: {} has {} many votes", String.valueOf(quoteId), votesCasted);
    }

    @Override
    @Traced(serviceName = "redis-get-vote")
    public Integer getVote(int quoteId) {
        Integer votesCasted = valueOps.get(KEY_PREFIX + String.valueOf(quoteId));
        if (votesCasted == null) {
            votesCasted = Integer.valueOf(0);
        }
        log.info("Quote id: {} has {} many votes", String.valueOf(quoteId), votesCasted);
        return votesCasted;
    }


}
