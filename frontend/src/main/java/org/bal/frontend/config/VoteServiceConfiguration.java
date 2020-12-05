package org.bal.frontend.config;

import io.grpc.ManagedChannel;
import io.grpc.health.v1.HealthGrpc;
import org.bal.vote.proto.internal.VoteManagementGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.instrument.grpc.SpringAwareManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VoteServiceConfiguration {

    @Value("${vote-server.host}")
    private String voteServiceHost;

    @Value("${vote-server.port}")
    private int voteServicePort;

    @Autowired
    private SpringAwareManagedChannelBuilder clientManagedChannelBuilder;

    @Bean
    public ManagedChannel voteServiceManagedChannel() {
        return this.clientManagedChannelBuilder.forAddress(voteServiceHost, voteServicePort).usePlaintext().build();
    }

    @Bean("voteManagementBlockingStub")
    public VoteManagementGrpc.VoteManagementBlockingStub voteManagementBlockingStub() {
        return VoteManagementGrpc.newBlockingStub(voteServiceManagedChannel());
    }

    @Bean("voteHealthBlockingStub")
    public HealthGrpc.HealthBlockingStub voteHealthBlockingStub() {
        return HealthGrpc.newBlockingStub(voteServiceManagedChannel());
    }

}
