package org.bal.vote.config;


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

    @Value("${quotes-server.host}")
    private String quoteServiceHost;

    @Value("${quotes-server.port}")
    private int quoteServicePort;

    @Autowired
    private GrpcTracing grpcTracing;

    @Bean
    public ManagedChannelBuilder managedChannelBuilder() {
        return ManagedChannelBuilder.forAddress(quoteServiceHost, quoteServicePort).intercept(grpcTracing.newClientInterceptor())
                .usePlaintext(true);
    }

    @Bean
    public ManagedChannel managedChannel() {
        return managedChannelBuilder().build();
    }

    @Bean("quoteManagementBlockingStub")
    public QuoteManagementGrpc.QuoteManagementBlockingStub quoteManagementBlockingStub() {
        return QuoteManagementGrpc.newBlockingStub(managedChannel());
    }


}
