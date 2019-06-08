package org.bal.frontend.grpc.client;


import com.google.protobuf.Empty;
import lombok.extern.slf4j.Slf4j;
import org.bal.quote.proto.internal.Quote;
import org.bal.quote.proto.internal.QuoteById;
import org.bal.quote.proto.internal.QuoteList;
import org.bal.quote.proto.internal.QuoteManagementGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
public class QuoteClient {

    @Autowired
    @Qualifier("quoteManagementBlockingStub")
    private QuoteManagementGrpc.QuoteManagementBlockingStub blockingStub;

    /**
     * @param quoteId, the quote id
     */
    public Quote getQuoteById(int quoteId) {
        Quote quote = blockingStub.getQuoteById(QuoteById.newBuilder().setId(quoteId).build());

        log.debug("Quote: {}" + quote.getQuote());
        return quote;

    }


    public List<Quote> allQuotes() {
       QuoteList list = blockingStub.allQuotes(Empty.newBuilder().build());
       return list.getQuotesList();
    }


}
