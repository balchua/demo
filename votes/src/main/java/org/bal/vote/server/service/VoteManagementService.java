package org.bal.vote.server.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.bal.quote.proto.internal.Quote;
import org.bal.vote.proto.internal.*;
import org.bal.vote.server.interceptor.ZipkinServerInterceptor;
import org.bal.vote.server.repository.VoteRepository;
import org.bal.vote.server.service.client.QuoteClient;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@GRpcService(interceptors = {ZipkinServerInterceptor.class})
@Component
@Slf4j
public class VoteManagementService extends VoteManagementGrpc.VoteManagementImplBase {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private QuoteClient quoteClient;

    @Override
    public void castVote(VoteRequest request, StreamObserver<VoteResponse> responseObserver) {
        voteRepository.castVote(request.getQuoteId());
        responseObserver.onNext(VoteResponse.newBuilder().setStatusMessage("OK").build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllVotes(Empty request, StreamObserver<VotesList> responseObserver) {
        Map<Integer, AtomicInteger> votes = voteRepository.getAllVotes();
        VotesList.Builder builder = VotesList.newBuilder();

        votes.forEach((k, v) -> {
            Quote quote = quoteClient.getQuoteById(k);
            log.debug("Quotes Id {}, quote: {}", quote.getId(), quote.getQuote());
            builder.addVotes(Vote.newBuilder().setId(0).setQuoteId(quote.getId()).setQuote(quote.getQuote()).setCount(v.intValue()));

        });

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();


    }
}
