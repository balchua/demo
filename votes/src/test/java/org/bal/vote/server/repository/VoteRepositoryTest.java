package org.bal.vote.server.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VoteRepositoryTest {

    @Test
    @DisplayName("Must allow to vote")
    public void mustAllowToVote(){
        RedisTemplate<String, Integer> redisTemplate = mock(RedisTemplate.class);
        ValueOperations<String, Integer> valueOps = (ValueOperations<String, Integer>) mock(ValueOperations.class);
        when(valueOps.increment("quote:1")).thenReturn(Long.valueOf(1));

        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        VoteRepositoryImpl repo = new VoteRepositoryImpl(redisTemplate);
        repo.castVote(1);
    }

    @Test
    @DisplayName("Must allow to get the number of votes for a quote.")
    public void mustAllowToGetNumberOfVotes(){
        RedisTemplate<String, Integer> redisTemplate = mock(RedisTemplate.class);
        ValueOperations<String, Integer> valueOps = (ValueOperations<String, Integer>) mock(ValueOperations.class);
        when(valueOps.get("quote:1")).thenReturn(Integer.valueOf(10));
        when(valueOps.get("quote:2")).thenReturn(null);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        VoteRepositoryImpl repo = new VoteRepositoryImpl(redisTemplate);
        assertThat(repo.getVote(1)).isEqualTo(10);
        assertThat(repo.getVote(2)).isEqualTo(0);
    }
}
