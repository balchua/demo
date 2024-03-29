package org.bal.quote.server.service;


import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.health.v1.HealthGrpc;
import lombok.extern.slf4j.Slf4j;
import org.bal.quote.server.repository.QuoteEntity;
import org.bal.quote.server.repository.QuoteRepository;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.stereotype.Component;

import java.util.Optional;


@GRpcService
@Component
@Slf4j
public class HealthService extends HealthGrpc.HealthImplBase {

    private QuoteRepository quoteRepository;

    public HealthService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Override
    public void check(io.grpc.health.v1.HealthCheckRequest request,
                      io.grpc.stub.StreamObserver<io.grpc.health.v1.HealthCheckResponse> responseObserver) {
        Optional<QuoteEntity> quote = quoteRepository.findById(0);

        HealthCheckResponse response = null;

        if (quote.isEmpty()) {
            log.debug("NOT OK");
            response = HealthCheckResponse.newBuilder().setStatus(HealthCheckResponse.ServingStatus.NOT_SERVING).build();
        } else {
            log.debug("OK");
            response = HealthCheckResponse.newBuilder().setStatus(HealthCheckResponse.ServingStatus.SERVING).build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }




}
