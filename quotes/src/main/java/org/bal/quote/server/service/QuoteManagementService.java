package org.bal.quote.server.service;


import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.bal.quote.proto.internal.Quote;
import org.bal.quote.proto.internal.QuoteById;
import org.bal.quote.proto.internal.QuoteList;
import org.bal.quote.proto.internal.QuoteManagementGrpc;
import org.bal.quote.server.repository.QuoteEntity;
import org.bal.quote.server.repository.QuoteRepository;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@GRpcService
@Component
@Slf4j
public class QuoteManagementService extends QuoteManagementGrpc.QuoteManagementImplBase {

    @Autowired
    private QuoteRepository quoteRepository;

    @Override
    public void getQuoteById(QuoteById request,
                             StreamObserver<Quote> responseObserver) {


        Optional<QuoteEntity> quoteResponse = quoteRepository.findById(request.getId());

        if (quoteResponse.isPresent()) {
            QuoteEntity reply = quoteResponse.get();
            log.info("Hi {}", reply.getName());
            var quote = Quote.newBuilder().setName(reply.getName()).setId(reply.getId()).setQuote(reply.getQuote()).build();
            responseObserver.onNext(quote);
        }

        responseObserver.onCompleted();
    }

    @Override
    public void allQuotes(Empty request, StreamObserver<QuoteList> responseObserver) {

        List<QuoteEntity> quotes = quoteRepository.findAll();
        log.info("allQuotes size: {}", quotes.size());

        var builder = QuoteList.newBuilder();

        for (QuoteEntity quote : quotes) {
            builder.addQuotes(Quote.newBuilder().setQuote(quote.getQuote()).setId(quote.getId()).setName(quote.getName()).build());
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }


}
