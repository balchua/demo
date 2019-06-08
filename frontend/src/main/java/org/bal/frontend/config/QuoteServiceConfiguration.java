package org.bal.frontend.config;

import brave.grpc.GrpcTracing;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.bal.quote.proto.internal.QuoteManagementGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuoteServiceConfiguration {

    @Value("${quote-service.host:localhost}")
    private String quoteServiceHost;

    @Value("${quote-service.port:50051}")
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


}
