package org.bal.frontend.grpc.client;


import com.google.protobuf.Empty;
import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.health.v1.HealthGrpc;
import lombok.extern.slf4j.Slf4j;
import org.bal.frontend.dto.VoteDTO;
import org.bal.quote.proto.internal.Quote;
import org.bal.quote.proto.internal.QuoteById;
import org.bal.quote.proto.internal.QuoteList;
import org.bal.quote.proto.internal.QuoteManagementGrpc;
import org.bal.vote.proto.internal.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class VoteClient {

    @Autowired
    @Qualifier("voteManagementBlockingStub")
    private VoteManagementGrpc.VoteManagementBlockingStub blockingStub;

    @Autowired
    @Qualifier("voteHealthBlockingStub")
    private HealthGrpc.HealthBlockingStub healthBlockingStub;
    /**
     * @param quoteId, the quote id
     */
    public String castVote(int quoteId) {
        VoteResponse voteResponse = blockingStub.castVote(VoteRequest.newBuilder().setQuoteId(quoteId).build());

        log.debug("Voted for : {}" + voteResponse.getStatusMessage());

        return voteResponse.getStatusMessage();

    }


    public List<VoteDTO> voteResult() {
        List<VoteDTO> voteDTOList = new ArrayList<>();
        VotesList list = blockingStub.getAllVotes(Empty.newBuilder().build());
        list.getVotesList().forEach (vote -> {
            VoteDTO voteDTO = new VoteDTO();

            voteDTO.setCount(vote.getCount());
            voteDTO.setQuote(vote.getQuote());
            voteDTO.setQuoteId(vote.getQuoteId());

            voteDTOList.add(voteDTO);
        });

        return voteDTOList;
    }


    public HealthCheckResponse.ServingStatus health() {
        HealthCheckResponse response = healthBlockingStub.check(HealthCheckRequest.newBuilder().build());
        return response.getStatus();
    }
}
