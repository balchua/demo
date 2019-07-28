package org.bal.vote.server.service;


import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.health.v1.HealthGrpc;
import lombok.extern.slf4j.Slf4j;
import org.bal.vote.server.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class HealthService extends HealthGrpc.HealthImplBase {

    @Autowired
    private VoteRepository voteRepository;


    @Override
    public void check(io.grpc.health.v1.HealthCheckRequest request,
                      io.grpc.stub.StreamObserver<HealthCheckResponse> responseObserver) {
        Integer voteId = voteRepository.getVote(0);
        HealthCheckResponse response = null;

        if (voteId == null) {
            response = HealthCheckResponse.newBuilder().setStatus(HealthCheckResponse.ServingStatus.NOT_SERVING).build();
        } else {
            response = HealthCheckResponse.newBuilder().setStatus(HealthCheckResponse.ServingStatus.SERVING).build();
        }
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }




}
