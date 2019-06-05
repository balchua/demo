package org.bal.quote.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.bal.quote.proto.internal.Quote;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class QuoteRepositoryImpl implements QuoteRepository {

    private List<Quote> quotes = new CopyOnWriteArrayList<>();

    @Override
    public Quote getQuoteById(int id) {
        Quote quote = Quote.newBuilder().build();
        for (Quote item: quotes) {
            if (item.getId() == id) {
                quote = item;
                break;
            }
        }

        return quote;
    }

    @Override
    public List<Quote> allQuotes() {
        return Collections.unmodifiableList(quotes);
    }

    @Override
    @PostConstruct
    public void init() {
        log.info("Initializing data");
        quotes.add(Quote.newBuilder().setName("Steve Rogers").setQuote("I can do this all day.").setId(0).build());
        quotes.add(Quote.newBuilder().setName("Tony Stark").setQuote("I am Iron Man.").setId(1).build());
        quotes.add(Quote.newBuilder().setName("Bruce Banner").setQuote("Hulk Smash!").setId(2).build());
        quotes.add(Quote.newBuilder().setName("James Rhodes").setQuote("Look, it’s me, I’m here, deal with it. Let’s move on.").setId(3).build());
        quotes.add(Quote.newBuilder().setName("Steve Rogers").setQuote("I don’t want to kill anyone. I don’t like bullies. I don’t care where they’re from.").setId(4).build());
    }


}
