package org.bal.quote.server.service;


import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.bal.quote.proto.internal.Quote;
import org.bal.quote.proto.internal.QuoteById;
import org.bal.quote.proto.internal.QuoteList;
import org.bal.quote.proto.internal.QuoteManagementGrpc;
import org.bal.quote.server.interceptor.ZipkinServerInterceptor;
import org.bal.quote.server.repository.QuoteRepository;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@GRpcService(interceptors = {ZipkinServerInterceptor.class})
@Component
@Slf4j
public class QuoteManagementService extends QuoteManagementGrpc.QuoteManagementImplBase {

    @Autowired
    private QuoteRepository quoteRepository;

    @Override
    public void getQuoteById(QuoteById request,
                             StreamObserver<Quote> responseObserver) {


        Quote reply = quoteRepository.getQuoteById(request.getId());
        log.info("Hi {}", reply.getName());

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void allQuotes(Empty request, StreamObserver<QuoteList> responseObserver) {

        List<Quote> quotes = quoteRepository.allQuotes();
        log.info ("allQuotes size: {}", quotes.size());

        QuoteList.Builder builder = QuoteList.newBuilder();
        for (Quote quote : quotes){
            builder.addQuotes(quote);
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }


}
