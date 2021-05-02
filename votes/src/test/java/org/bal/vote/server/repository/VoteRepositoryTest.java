package org.bal.vote.server.repository;

import com.google.api.Quota;
import org.bal.quote.proto.internal.Quote;
import org.bal.vote.proto.internal.Vote;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
    public void shouldReturnTheNumberOfVotesForAQuote(){
        RedisTemplate<String, Integer> redisTemplate = mock(RedisTemplate.class);
        ValueOperations<String, Integer> valueOps = (ValueOperations<String, Integer>) mock(ValueOperations.class);
        when(valueOps.get("quote:1")).thenReturn(Integer.valueOf(10));
        when(valueOps.get("quote:2")).thenReturn(null);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        VoteRepositoryImpl repo = new VoteRepositoryImpl(redisTemplate);
        assertThat(repo.getVote(1)).isEqualTo(10);
        assertThat(repo.getVote(2)).isEqualTo(0);
    }

    @Test
    @DisplayName("Must allow to get all the votes for all quotes.")
    public void shouldReturnAllVotesOfAllQuotes(){
        RedisTemplate<String, Integer> redisTemplate = mock(RedisTemplate.class);
        ValueOperations<String, Integer> valueOps = (ValueOperations<String, Integer>) mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        List<Integer> keys = new ArrayList<>();
        keys.add(1);
        keys.add(2);

        Quote quote1 = Quote.newBuilder().setQuote("1").build();
        Quote quote2 = Quote.newBuilder().setQuote("2").build();
        List<Quote> quotes = new ArrayList<>();
        quotes.add(quote1);
        quotes.add(quote2);

        when(valueOps.multiGet(any())).thenReturn(keys);


        VoteRepositoryImpl repo = new VoteRepositoryImpl(redisTemplate);
        List<Vote> votes = repo.getAllVotes(quotes);
        assertThat(votes.size()).isEqualTo(2);
        assertThat(votes.get(0).getCount()).isEqualTo(1);
        assertThat(votes.get(1).getCount()).isEqualTo(2);
    }
}
