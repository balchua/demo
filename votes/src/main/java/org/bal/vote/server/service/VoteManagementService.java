package org.bal.vote.server.service;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.bal.quote.proto.internal.Quote;
import org.bal.vote.proto.internal.*;
import org.bal.vote.server.repository.VoteRepository;
import org.bal.vote.server.service.client.QuoteClient;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@GRpcService
@Component
@Slf4j
public class VoteManagementService extends VoteManagementGrpc.VoteManagementImplBase {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private QuoteClient quoteClient;

    @Override
    public void castVote(VoteRequest request, StreamObserver<VoteResponse> responseObserver) {
        try {
            failVoteCasting(request.getQuoteId());
        } catch (IllegalArgumentException iae) {
            log.error("Invalid Quote {} to vote.", request.getQuoteId(), iae);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(iae.getMessage())
                    .augmentDescription("illegal argument")
                    .withCause(iae)
                    .asRuntimeException());
            return;
        }
        voteRepository.castVote(request.getQuoteId());
        var q = quoteClient.getQuoteById(request.getQuoteId());
        var message = String.format("'%s'", q.getQuote());
        responseObserver.onNext(VoteResponse.newBuilder().setStatusMessage(message).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllVotes(Empty request, StreamObserver<VotesList> responseObserver) {
        var builder = VotesList.newBuilder();

        List<Quote> quotes = quoteClient.allQuotes();

        quotes.forEach(quote -> {
            Integer voteCount = voteRepository.getVote(quote.getId());
            log.debug("Quotes Id {}, quote: {} - with votes {}", quote.getId(), quote.getQuote(), voteCount);
            builder.addVotes(Vote.newBuilder().setId(quote.getId()).setQuoteId(quote.getId()).setQuote(quote.getQuote()).setCount(voteCount));

        });

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();


    }

    @Override
    public void getAllVotesWithMultiget(Empty request, StreamObserver<VotesList> responseObserver) {
        var builder = VotesList.newBuilder();

        List<Quote> quotes = quoteClient.allQuotes();

        List<Vote> votes = voteRepository.getAllVotes(quotes);

        votes.forEach(vote -> {
            log.debug("Quotes Id {}, quote: {} - with votes {}", vote.getQuoteId(), vote.getQuote(), vote.getCount());
            builder.addVotes(vote);

        });

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();

    }

    private void failVoteCasting(int quoteId) {
        if (quoteId == 2) {
            var ex = new IllegalArgumentException("Unable to cast vote to Hulk Smash.");
            log.error("Invalid Vote", ex);
            throw ex;
        }
    }
}
