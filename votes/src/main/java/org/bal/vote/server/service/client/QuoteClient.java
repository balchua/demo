package org.bal.vote.server.service.client;

import org.bal.quote.proto.internal.Quote;

import java.util.List;


public interface QuoteClient {

    Quote getQuoteById(int quoteId);
    List<Quote> allQuotes();
}
