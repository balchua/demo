package org.bal.quote.server.repository;

import org.bal.quote.proto.internal.Quote;

import java.util.List;

public interface QuoteRepository {
    Quote getQuoteById(int id);

    List<Quote> allQuotes();

    void init();
}
