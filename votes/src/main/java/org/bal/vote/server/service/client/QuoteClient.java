package org.bal.vote.server.service.client;

import org.bal.quote.proto.internal.Quote;


public interface QuoteClient {

    Quote getQuoteById(int quoteId);
}
