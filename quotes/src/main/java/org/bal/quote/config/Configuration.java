package org.bal.quote.config;


import brave.Tracing;
import brave.grpc.GrpcTracing;
import io.grpc.ServerInterceptor;
import org.bal.quote.server.repository.QuoteRepository;
import org.bal.quote.server.repository.QuoteRepositoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

@ComponentScan("org.bal.quote.server")
@SpringBootConfiguration
public class Configuration {

    @Value("${zipkin.host}")
    private String zipkinHost;

    @Value("${zipkin.port:9411}")
    private int zipkinPort;


    @Bean
    public QuoteRepository quoteRepository() {
        return new QuoteRepositoryImpl();
    }


    /**
     * Configuration for how to buffer spans into messages for Zipkin
     */
    @Bean
    public Reporter<Span> reporter() {
        return AsyncReporter.builder(sender()).build();
    }

    @Bean
    public Tracing tracing() {
        return Tracing.newBuilder()
                .localServiceName("quote-service")
                .spanReporter(reporter()).build();
    }

    /**
     * Configuration for how to send spans to Zipkin
     */
    @Bean
    public Sender sender() {
        return OkHttpSender.create("http://" + zipkinHost + ":" + zipkinPort + "/api/v2/spans");
    }

    @Bean
    public GrpcTracing grpcTracing() {
        return GrpcTracing.create(tracing());
    }

    @Bean(name = "grpcServerInterceptor")
    public ServerInterceptor grpcServerInterceptor() {

        return grpcTracing().newServerInterceptor();
    }



}
