package org.bal.vote.server.service.client;

import lombok.extern.slf4j.Slf4j;
import org.bal.quote.proto.internal.Quote;
import org.bal.quote.proto.internal.QuoteById;
import org.bal.quote.proto.internal.QuoteManagementGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QuoteClientImpl implements QuoteClient {

    @Autowired
    @Qualifier("quoteManagementBlockingStub")
    private QuoteManagementGrpc.QuoteManagementBlockingStub blockingStub;


    /**
     * @param quoteId, the quote id
     */
    public Quote getQuoteById(int quoteId) {
        Quote quote = blockingStub.getQuoteById(QuoteById.newBuilder().setId(quoteId).build());

        log.debug("Quote id: {}, Quote: {}", quote.getId(), quote.getQuote());
        return quote;

    }
}
