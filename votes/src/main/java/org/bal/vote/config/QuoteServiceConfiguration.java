package org.bal.vote.config;


import io.grpc.ManagedChannel;
import org.bal.quote.proto.internal.QuoteManagementGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.instrument.grpc.SpringAwareManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuoteServiceConfiguration {

    @Value("${quotes-server.host}")
    private String quoteServiceHost;

    @Value("${quotes-server.port}")
    private int quoteServicePort;

    @Autowired
    private SpringAwareManagedChannelBuilder clientManagedChannelBuilder;

    @Bean
    public ManagedChannel managedChannel() {
        return this.clientManagedChannelBuilder.forAddress(quoteServiceHost, quoteServicePort).usePlaintext().build();
    }

    @Bean("quoteManagementBlockingStub")
    public QuoteManagementGrpc.QuoteManagementBlockingStub quoteManagementBlockingStub() {
        return QuoteManagementGrpc.newBlockingStub(managedChannel());
    }


}
