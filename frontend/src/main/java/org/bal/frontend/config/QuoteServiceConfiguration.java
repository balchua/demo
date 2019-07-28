package org.bal.frontend.config;

import brave.grpc.GrpcTracing;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.health.v1.HealthGrpc;
import org.bal.quote.proto.internal.QuoteManagementGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuoteServiceConfiguration {

    @Value("${quote-server.host}")
    private String quoteServiceHost;

    @Value("${quote-server.port}")
    private int quoteServicePort;

    @Autowired
    private GrpcTracing grpcTracing;

    @Bean
    public ManagedChannelBuilder quoteServiceManagedChannelBuilder() {
        return ManagedChannelBuilder.forAddress(quoteServiceHost, quoteServicePort).intercept(grpcTracing.newClientInterceptor())
                .usePlaintext(true);
    }

    @Bean
    public ManagedChannel quoteServiceManagedChannel() {
        return quoteServiceManagedChannelBuilder().build();
    }


    @Bean("quoteManagementBlockingStub")
    public QuoteManagementGrpc.QuoteManagementBlockingStub quoteManagementBlockingStub() {
        return QuoteManagementGrpc.newBlockingStub(quoteServiceManagedChannel());
    }

    @Bean("quoteHealthBlockingStub")
    public HealthGrpc.HealthBlockingStub quoteHealthBlockingStub() {
        return HealthGrpc.newBlockingStub(quoteServiceManagedChannel());
    }


}
